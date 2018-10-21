package com.dhk.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhk.FeeInfo;
import com.dhk.FeeParamCode;
import com.dhk.entity.*;
import com.dhk.payment.PayRequest;
import com.dhk.payment.PayResult;
import com.dhk.service.*;
import com.dhk.service.impl.PayRequestService.ProxyPayType;
import com.dhk.utils.DateTimeUtil;
import com.sunnada.kernel.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service("ReviewApplyCashService")
public class ReviewApplyCashService implements IReviewApplyCashService {

	@Resource(name = "APPUserService")
	private IAPPUserService appUserService;

	@Resource(name="PayRequestService")
	private IPayRequestService payRequestService;

	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;

	@Resource(name = "UserAccountService")
	private IUserAccountService userAccountService;

	@Resource(name = "FeeParamService")
	private IFeeParamService feeParamService;


	@Resource(name = "CallRemotePayService")
	private ICallRemotePayService callRemotePayService;


	@Resource(name = "operatorsAccoutService")
	private IOperatorsAccoutService operatorsAccoutService;

	@Resource(name = "CheckPlatformAccountBalanceService")
	private ICheckPlatformAccountBalanceService checkPlatformAccountBalanceService;

	@Resource(name = "OrgService")
	private IOrgService orgService;

	@Resource(name = "operatorsInfoService")
	private IOperatorsInfoService operatorsInfoService;

	@Resource(name = "OrgAgentApplyCashService")
	private IOrgAgentApplyCashService orgAgentApplyCashService;

	@Resource(name = "APPUserApplyCashService")
	private IAPPUserApplyCashService appUserApplyCashService;



	@Autowired
	JedisPool jedisPool;

	private static final Logger log= LogManager.getLogger();


	public PayResult applyCash(String applyId, boolean isPass){
		PayResult payResult=new PayResult();
		APPUserApplyCash aac = appUserApplyCashService.findById(Long.parseLong(applyId));

		if(aac==null){
			payResult.setCode("error");
			payResult.setMessage("找不到该提现申请："+applyId);
			return payResult;
		}

		if(!"0".equals(aac.getStatus())){
			payResult.setCode("error");
			payResult.setMessage("该申请已审核："+applyId);
			return payResult;
		}

		APPUser user = appUserService.findById(aac.getUser_id());
		aac.setAmount(aac.getAmount().setScale(2, RoundingMode.HALF_UP));

		UserAccount ua=userAccountService.findByUserId(user.getId());
		if(!isPass){
			ua.setInAccount(aac.getAmount().negate());
			userAccountService.seriUpdateBalanceAndRecashFreeze(ua);
			appUserApplyCashService.updateStatus(Long.parseLong(applyId),"2");
			payResult.setCode("success");
			payResult.setMessage("操作成功");
			return payResult;
		}
		PayResult pr=platCreditProxyPay(user, aac.getAmount(), applyId);

		return pr;
	}



	/**
	 * 使用平台信用卡代付给用户
	 * @param user
	 * @param transAmount
	 * @return
	 */
	private PayResult platCreditProxyPay(APPUser user, BigDecimal transAmount, String applyId){
		UserAccount ua=userAccountService.findByUserId(user.getId());
		boolean cpcb=checkPlatformAccountBalanceService.checkPlatformCreditBalance();
		if(cpcb){
			FeeInfo feeInfo;
			BigDecimal trueAmt;
			feeInfo=feeParamService.computeFeeInfo(transAmount, FeeParamCode.RECASH, FeeParamCode.CREDIT_QUICK_PROXY_PAY_COST);
			//调用银行接口发生的实际提现额
			trueAmt=transAmount.subtract(feeInfo.getFee()).subtract(feeInfo.getExternal())
					.setScale(2, RoundingMode.HALF_UP);
			//记录流水
			String orderNo = appUserService.getOrderNo(user.getId(), user.getMobilephone());
			PayRequest request= payRequestService.getDebitCarRecashRequest(orderNo,user,  trueAmt,user.getCardNo());
			long translsId = transWaterService.addTransls(orderNo, user, user.getCardNo(), transAmount, feeInfo.getFee(), feeInfo.getExternal(), Long.parseLong(applyId), "5", "0", "0"); //planId字段给提现id用

			PayResult result = this.txUserProxypay( request, translsId, orderNo, transAmount, trueAmt, feeInfo, ua, user,applyId);
			return result;
		}else{
			//平台账户资金错误
			PayResult result= PayResult.genCustomFailPayResult("内部系统错误");
			transWaterService.writeRecashTransWater(appUserService.getOrderNo(user.getId(), user.getMobilephone()),user, user.getCardNo(),transAmount, new BigDecimal("0"), new BigDecimal("0"),null,null, result);
			ua.setInAccount(transAmount.negate());
			userAccountService.seriUpdateBalanceAndRecashFreeze(ua);
				return result;
		}
	}

		private PayResult txUserProxypay( PayRequest request, Long translsId, String orderNo, BigDecimal transAmount, BigDecimal trueAmt, FeeInfo feeInfo, UserAccount ua, APPUser user,String applyId){
			PayResult result;
			try {
				log.debug("调用银行接口请求参数:{}", request.toString());
				result=callRemotePayService.creditProxyPay(request);
				log.debug("银行接口返回code:{},message:{}", result.getCode(),result.getMessage());
			} catch (Exception e1) {
				result= PayResult.genCustomFailPayResult("调用信用卡代付接口发生异常");
				log.error("调用信用卡代付接口发生异常{}", e1.toString());
				e1.printStackTrace();
			}
			String code=result.getCode();
			if("0000".equals(code)){

				String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
				String time = DateTimeUtil.getNowDateTime("HHmmss");

				transWaterService.modifyTransls(translsId, orderNo, "", "0000", "交易成功", date, time,result.getTransactionType());

				BigDecimal inOA=trueAmt.add(feeInfo.getCost()).setScale(2, RoundingMode.HALF_UP).negate();
				//更新运营商信用卡账户资金
				operatorsAccoutService.doUpdateCredit(inOA);
				ua.setInAccount(transAmount.negate());
				//更新用户账户提现冻结
				userAccountService.seriUpdateRecashFreeze(ua);
				log.debug("用户{}提现{}元，提现手续费{}元；运营商信用卡{}代付成本{}元，运营商信用卡账户收入{}元",
						user.getId(), trueAmt,feeInfo.getFee().add(feeInfo.getExternal()),"",
						feeInfo.getCost(),inOA);

				appUserApplyCashService.updateStatus(Long.parseLong(applyId),"1");
				return result;
			}else if ("9997".equals(code)){
				Jedis jedis = null;
				try{
					jedis = jedisPool.getResource();
//					String key="repayUndefined_"+ StringUtil.getCurrentDateTime("yyyyMMdd");
//					jedis.hset(key,orderNo,translsId+"");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("orderNo",orderNo);
					jsonObject.put("translsId",translsId);
					jsonObject.put("time",System.currentTimeMillis()+30000);
					jsonObject.put("count","0");
					jedis.lpush("UndefinedTask",jsonObject.toJSONString());
				}catch (Exception e){
					e.printStackTrace();
				}finally {
					if (jedis!=null){
						jedis.close();
					}
				}
				appUserApplyCashService.updateStatus(Long.parseLong(applyId),"1");
				transWaterService.modifyTransls(translsId, orderNo, "", code, result.getMessage(), DateTimeUtil.getNowDateTime("yyyyMMdd"), DateTimeUtil.getNowDateTime("HHmmss"),result.getTransactionType());
				return result;
			}else {
				ua.setInAccount(transAmount.negate());
				userAccountService.seriUpdateBalanceAndRecashFreeze(ua);
				appUserApplyCashService.updateStatus(Long.parseLong(applyId),"1");
				transWaterService.modifyTransls(translsId, orderNo, "", code, result.getMessage(), DateTimeUtil.getNowDateTime("yyyyMMdd"), DateTimeUtil.getNowDateTime("HHmmss"),result.getTransactionType());
				return result;
			}

	}



	/**
	 * 判断是否能够进行还款，冻结金额必须大于或等于还款金额才能进行执行还款
	 * @return
	 */
	private boolean canRecash(BigDecimal transAmt,BigDecimal freeze){

		if(transAmt.compareTo(freeze)>0){
			return false;
		}
		return true;
	}

    public PayResult operatorsCash(BigDecimal amount) {
    	amount=amount.setScale(2, RoundingMode.HALF_UP);

    	OperatorsInfo info = operatorsInfoService.findInfo();

    	PayResult pr;
    	pr=platCreditProxyPay(amount, info.getRealName(),info.getIdNo(),info.getCardNo(),info.getPhone(), ProxyPayType.D0);
		if(!"0000".equals(pr.getCode()) && !"00".equals(pr.getCode())
				&& !"P000".equals(pr.getCode()) && !"9999".equals(pr.getCode()) && !"9997".equals(pr.getCode())){
			//运营商信用卡T1付款
			pr=platCreditProxyPay(amount, info.getRealName(),info.getIdNo(),info.getCardNo(),info.getPhone(), ProxyPayType.T1);
		}

		return pr;
    }

	public PayResult applyCashForOrg(String applyId,boolean isPass) {
		PayResult payResult=new PayResult();
		OrgAgentApplyCash orgAgentApplyCash = orgAgentApplyCashService.findApplyId(applyId);

		if(orgAgentApplyCash==null){
			payResult.setCode("error");
			payResult.setMessage("找不到该提现申请："+applyId);
			return payResult;
		}

		if(!"0".equals(orgAgentApplyCash.getStatus())){
			payResult.setCode("error");
			payResult.setMessage("该申请已审核："+applyId);
			return payResult;
		}
		Org org = orgService.findById(orgAgentApplyCash.getOrg_id());
		if(!isPass){
			org.setInAccount(orgAgentApplyCash.getAmount().negate());
			orgService.seriUpdateBalanceAndRecashFreeze(org);
			log.info("返还客户账户:"+org.getRealName()+" : "+orgAgentApplyCash.getAmount().negate());
			orgAgentApplyCashService.updateStatus("2",applyId);
			payResult.setCode("success");
			payResult.setMessage("操作成功");
			return payResult;
		}
		PayResult pr=platCreditProxyPay(org, orgAgentApplyCash.getAmount(),applyId);
		return pr;

	}

	private PayResult

	platCreditProxyPay(BigDecimal transAmount, String realName, String idNo, String cardNo, String phone, String payType){
		String orderNo = appUserService.getOrderNo(0, phone);
		PayRequest payRequest=new PayRequest();
    	payRequest.setOrderDate(DateTimeUtil.getNowDateTime("yyyyMMdd"));

    	payRequest.setTransAmt(transAmount.multiply(new BigDecimal("100")).longValue());

    	payRequest.setRealName(realName);
    	payRequest.setCustomerName(realName);
    	payRequest.setCerdId(idNo);
    	payRequest.setCerdType("01");//证件类型为身份证
    	payRequest.setAcctNo(cardNo);
		payRequest.setOrderNo(orderNo);
		payRequest.setPayType(payType);
		payRequest.setPhoneNo(phone);

		PayResult result;
		//记录流水
		Org org = orgService.findById(1l);
		long translsId = transWaterService.addTransls(orderNo, org, cardNo, transAmount, new BigDecimal("0"), new BigDecimal("0"), null, "5", "0");

		return this.txOperatorProxyPay(translsId, orderNo, org, payType, payRequest, transAmount);
	}


	private PayResult txOperatorProxyPay(Long translsId, String orderNo, Org org, String payType, PayRequest payRequest, BigDecimal transAmount){
		PayResult result;
		try {
			log.debug("{}方式调用银行接口请求参数:{}",payType, payRequest.toString());
			//result=callRemotePayService.creditProxyPay(payRequest);
			result = new PayResult();
			result.setCode("0000");
			result.setMessage("没有调用接口成功");
			log.debug("银行接口返回code:{},message:{}", result.getCode(),result.getMessage());
		} catch (Exception e1) {
			result= PayResult.genCustomFailPayResult("调用信用卡代付接口发生异常");
			log.error("{}方式调用信用卡代付接口发生异常{}", payType, e1.toString());
			e1.printStackTrace();
		}
		String code=result.getCode();
		if("0000".equals(code) || "00".equals(code)){
			//更新运营商信用卡账户资金
			operatorsAccoutService.doUpdateCredit(transAmount.setScale(2, RoundingMode.HALF_UP).negate());

			//记录流水
			String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
			String time = DateTimeUtil.getNowDateTime("HHmmss");

			transWaterService.modifyTransls(translsId, orderNo, payType, "0000", "交易成功", date, time,result.getTransactionType());
//			transWaterService.writeRecashTransWater(payRequest.getOrderNo(),org, cardNo, transAmount, new BigDecimal("0"),
//					new BigDecimal("0"),""+CardType.CREDIT ,payType,result);
			log.debug("运营商{}提现{}元，提现手续费{}元；运营商信用卡{}代付成本{}元，运营商信用卡账户收入{}元",
					org.getId(), transAmount,new BigDecimal("0"),payType,
					new BigDecimal("0"),transAmount.negate());

		}else{
			String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
			String time = DateTimeUtil.getNowDateTime("HHmmss");

			transWaterService.modifyTransls(translsId, orderNo, payType, code, "h-失败-"+result.getMessage(), date, time,result.getTransactionType());
		}

		return result;
	}

	/**
	 * 使用平台信用卡代付给用户
	 * @param transAmount
	 * @param applyId
	 * @return
	 */
	private PayResult platCreditProxyPay(Org org, BigDecimal transAmount, String applyId){


		boolean cpcb=checkPlatformAccountBalanceService.checkPlatformCreditBalance();

		if(cpcb){
			FeeInfo feeInfo;
			BigDecimal trueAmt;

			feeInfo=feeParamService.computeFeeInfo(transAmount, FeeParamCode.RECASH, FeeParamCode.CREDIT_QUICK_PROXY_PAY_COST);
			//调用银行接口发生的实际提现额
			trueAmt=transAmount.subtract(feeInfo.getFee()).subtract(feeInfo.getExternal())
					.setScale(2, RoundingMode.HALF_UP);

			//记录流水
			String orderNo = appUserService.getOrderNo(org.getId(), org.getBindPhone());
			long translsId = transWaterService.addTransls(orderNo, org, org.getAccountNo(), trueAmt, feeInfo.getFee(), feeInfo.getExternal(), Long.parseLong(applyId), "5", "0");

			PayResult result = this.txOrgProxyPay(translsId, orderNo, org, transAmount, trueAmt, org.getAccountNo(), feeInfo,applyId);
			return result;
		}else{
			//平台账户资金错误
			PayResult result= PayResult.genCustomFailPayResult("内部系统错误");
			transWaterService.writeRecashTransWater(appUserService.getOrderNo(org.getId(), org.getBindPhone()),org, org.getAccountNo(),transAmount, new BigDecimal("0"), new BigDecimal("0"),null,null, result);
			org.setInAccount(transAmount.negate());
			orgService.seriUpdateBalanceAndRecashFreeze(org);

			return result;
		}
	}

	private PayResult txOrgProxyPay(Long translsId, String orderNo, Org org, BigDecimal transAmount, BigDecimal trueAmt, String cardNo,  FeeInfo feeInfo,String applyId){
		PayRequest request= payRequestService.getDebitCarRecashRequest(orderNo,org,  trueAmt,cardNo);
		PayResult result;
		try {
			log.debug("调用银行接口请求参数:{}", request.toString());
			result=callRemotePayService.creditProxyPay(request);
//			result=PayResult.genCustomSuccessPayResult();
			log.debug("银行接口返回code:{},message:{}", result.getCode(),result.getMessage());
		} catch (Exception e1) {
			result= PayResult.genCustomFailPayResult("调用信用卡代付接口发生异常");
			log.error("调用信用卡代付接口发生异常{}", e1.toString());
			e1.printStackTrace();
		}
		String code=result.getCode();

		orgAgentApplyCashService.updateStatus("1",applyId);
		if("0000".equals(code) || "00".equals(code)){
			//修改流水

			String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
			String time = DateTimeUtil.getNowDateTime("HHmmss");

			transWaterService.modifyTransls(translsId, orderNo, "", "0000", "交易成功", date, time,result.getTransactionType());

			BigDecimal inOA=trueAmt.add(feeInfo.getCost()).setScale(2, RoundingMode.HALF_UP).negate();
			//更新运营商信用卡账户资金
			operatorsAccoutService.doUpdateCredit(inOA);
			orgService.seriUpdateRecashFreeze(org.getId(),trueAmt.negate());
			log.debug("代理商{}提现{}元，提现手续费{}元；运营商信用卡{}代付成本{}元，运营商信用卡账户收入{}元",
					org.getId(), trueAmt,feeInfo.getFee().add(feeInfo.getExternal()),"",
					feeInfo.getCost(),inOA);
			return result;
		}else if( "P000".equals(code)
				|| "9997".equals(code)
				|| "9999".equals(code)
				|| "trans".equals(code)
				){
			String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
			String time = DateTimeUtil.getNowDateTime("HHmmss");
			transWaterService.modifyTransls(translsId, orderNo, "", code,result.getMessage(), date, time,result.getTransactionType());
//			orgAgentApplyCashService.updateStatus("3",applyId);
			Jedis jedis = null;
			try{
				jedis = jedisPool.getResource();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orderNo",orderNo);
				jsonObject.put("translsId",translsId);
				jsonObject.put("time",System.currentTimeMillis()+30000);
				jsonObject.put("count","0");
				jedis.lpush("UndefinedTask",jsonObject.toJSONString());
			}catch (Exception e){
				e.printStackTrace();
			}finally {
				if (jedis!=null){
					jedis.close();
				}
			}

			return result;
		} else {
			String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
			String time = DateTimeUtil.getNowDateTime("HHmmss");

			transWaterService.modifyTransls(translsId, orderNo, "", code,"失败-"+result.getMessage(), date, time,result.getTransactionType());
			orgAgentApplyCashService.updateStatus("3",applyId);
			org.setInAccount(transAmount.negate());
			orgService.seriUpdateBalanceAndRecashFreeze(org);

			return result;
		}
	}



}
