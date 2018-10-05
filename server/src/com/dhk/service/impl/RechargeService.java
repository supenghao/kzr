package com.dhk.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhk.FeeInfo;
import com.dhk.FeeParamCode;
import com.dhk.entity.APPUser;
import com.dhk.entity.UserAccount;
import com.dhk.payment.PayRequest;
import com.dhk.payment.PayResult;
import com.dhk.service.*;
import com.dhk.utils.DateTimeUtil;
import com.sunnada.kernel.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service("RechargeService")
public class RechargeService implements IRechargeService {

	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;

	@Resource(name = "UserAccountService")
	private IUserAccountService userAccountService;

	@Resource(name = "FeeParamService")
	private IFeeParamService feeParamService;

	@Resource(name = "CallRemotePayService")
	private ICallRemotePayService callRemotePayService;

	@Resource(name = "MessageService")
	private IMessageService messageService;

	@Resource(name = "operatorsAccoutService")
	private IOperatorsAccoutService operatorsAccoutService;

	@Autowired
	JedisPool jedisPool;


	private static final Logger log= LogManager.getLogger();
	
	public PayResult userRecharge(PayRequest payRequest, APPUser user){
		//充值金额
		BigDecimal amount=new BigDecimal(payRequest.getTransAmt())
				.divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
		String debiteCardNo=payRequest.getAcctNo();
		//计算充值手续费、运营商充值成本

		FeeInfo feeInfo=feeParamService.computeFeeInfo(amount, FeeParamCode.RECHARGE,
				FeeParamCode.RECHARGE_COST);

		long translsId = transWaterService.addTransls(payRequest.getOrderNo(), user,payRequest.getAcctNo(), amount, feeInfo.getFee(), feeInfo.getExternal(), null, "4", "0", "0"); //planId字段给提现id用

		PayResult pr;
		try {
			 log.debug("调用银行接口请求参数:{}", payRequest.toString());
			// pr=callRemotePayService.debitPurchase(payRequest);
			 pr=callRemotePayService.creditPurchase(payRequest);
			 log.debug("调用银行接口返回code:{},message:{}", pr.getCode(),pr.getMessage());
		} catch (Exception e) {
			pr= PayResult.genCustomFailPayResult("充值失败");
			log.error("调用银行接口发生异常{}", e.toString());
			e.printStackTrace();
		}

		String resCode=pr.getCode();
		Jedis jedis = null;
		try{

			if( "0000".equals(resCode) || "00".equals(resCode)){
				txDoRechargeSuccess(payRequest.getOrderNo(),user,debiteCardNo,amount,feeInfo,pr);
				transWaterService.modifyTransls(translsId, payRequest.getOrderNo(), "","0000", "交易成功", DateTimeUtil.getNowDateTime("yyyyMMdd"), DateTimeUtil.getNowDateTime("HHmmss"),pr.getTransactionType());
			}else if( "9997".equals(resCode)){
				jedis = jedisPool.getResource();
//				String key="repayUndefined_"+ StringUtil.getCurrentDateTime("yyyyMMdd");
//				jedis.hset(key,payRequest.getOrderNo(),translsId+"");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orderNo",payRequest.getOrderNo());
				jsonObject.put("translsId",translsId);
				jsonObject.put("time",System.currentTimeMillis()+30000);
				jsonObject.put("count","0");
				jedis.lpush("UndefinedTask",jsonObject.toJSONString());

				transWaterService.modifyTransls(translsId, payRequest.getOrderNo(), "",resCode, pr.getMessage(), DateTimeUtil.getNowDateTime("yyyyMMdd"), DateTimeUtil.getNowDateTime("HHmmss"),pr.getTransactionType());
			}else{
				doDoRechargeFailed(payRequest.getOrderNo(),user,debiteCardNo,amount,pr,feeInfo);
			    	transWaterService.modifyTransls(translsId, payRequest.getOrderNo(), "",resCode, pr.getMessage(), DateTimeUtil.getNowDateTime("yyyyMMdd"), DateTimeUtil.getNowDateTime("HHmmss"),pr.getTransactionType());
				return pr;
			}
		}catch (Exception e){
			log.error("充值失败：amount"+amount);
			e.printStackTrace();
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}
		return pr;
		
	};
	
	private void doDoRechargeFailed(String orderNo, APPUser user, String debiteCardNo, BigDecimal amount, PayResult pr, FeeInfo feeInfo) {

		messageService.writeMessage(user.getId(),
				"您有一笔充值失败：金额" + amount + "（元） 时间："
						+ StringUtil.getCurrentTime() );
		log.info("用户{}充值{}元失败，失败代码{}，失败描述：{}", 
				user.getId(),amount,pr.getCode(),pr.getMessage());
	}
	@Transactional
	private void txDoRechargeSuccess(String orderNo, APPUser user, String debiteCardNo, BigDecimal amount, FeeInfo feeInfo,
                                     PayResult pr) {
		UserAccount userAccount = new UserAccount();
		BigDecimal totalFee=feeInfo.getFee().add(feeInfo.getExternal());
		BigDecimal inAccount = amount.subtract(totalFee)
					.setScale(2, RoundingMode.HALF_UP);
		userAccount.setInAccount(inAccount);
		userAccount.setUserId(user.getId());
		userAccount.setUpdateDate(DateTimeUtil.getNowDateTime("yyyyMMdd"));
		userAccount.setUpdateTime(DateTimeUtil.getNowDateTime("HHmmss"));
		//更新用户当前余额
		userAccountService.seriUpdateBalance(userAccount);
		BigDecimal inOA=amount.subtract(feeInfo.getCost()).setScale(2, RoundingMode.HALF_UP);
		//更新运营商信用卡账户资金
		operatorsAccoutService.doUpdateCredit(inOA);

		log.debug("用户{}成功充值{}元，手续费{}元，用户账户收入{}元;"
				+ "运营商成本{}元，运营商借记卡账户收入{}元", 
				user.getId(),
				amount,feeInfo.getFee(),inAccount,feeInfo.getCost(),inOA);
	}
	

}
