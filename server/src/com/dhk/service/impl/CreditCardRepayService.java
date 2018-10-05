package com.dhk.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSONObject;
import com.dhk.CardType;
import com.dhk.FeeInfo;
import com.dhk.FeeParamCode;
import com.dhk.dao.IRepayRecordDao;
import com.dhk.entity.APPUser;
import com.dhk.entity.CreditCard;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.entity.RepayRecord;
import com.dhk.entity.TransWater;
import com.dhk.init.Constant;
import com.dhk.payment.PayRequest;
import com.dhk.payment.PayResult;
import com.dhk.service.IAPPUserService;
import com.dhk.service.ICallRemotePayService;
import com.dhk.service.ICheckPlatformAccountBalanceService;
import com.dhk.service.ICreditCardRepayService;
import com.dhk.service.ICreditCardService;
import com.dhk.service.IFeeParamService;
import com.dhk.service.IMessageService;
import com.dhk.service.IOperatorsAccoutService;
import com.dhk.service.IPayRequestService;
import com.dhk.service.IRepayCostService;
import com.dhk.service.IRepayPlanDetailService;
import com.dhk.service.IRepayRecordService;
import com.dhk.service.ITransWaterService;
import com.dhk.service.impl.PayRequestService.ProxyPayType;
import com.dhk.utils.DateTimeUtil;
import com.dhk.yunpain.YunPainSmsMsg;
import com.sunnada.kernel.util.StringUtil;

@Service("CreditCardRepayService")
public class CreditCardRepayService implements ICreditCardRepayService{
	
	@Resource(name = "APPUserService")
	private IAPPUserService appUserService;
	
	@Resource(name="PayRequestService")
	private  IPayRequestService payRequestService;
	
	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;

	@Resource(name = "FeeParamService")
	private IFeeParamService feeParamService;

	@Resource(name = "RepayPlanDetailService")
	private IRepayPlanDetailService repayPlanDetailService;

	@Resource(name = "CallRemotePayService")
	private ICallRemotePayService callRemotePayService;

	@Resource(name = "MessageService")
	private IMessageService messageService;

	@Resource(name = "repayRecordService")
	private IRepayRecordService repayRecordService;

	@Resource(name = "operatorsAccoutService")
	private IOperatorsAccoutService operatorsAccoutService;
	
	@Resource(name = "CheckPlatformAccountBalanceService")
	private  ICheckPlatformAccountBalanceService checkPlatformAccountBalanceService;

	@Resource(name = "repayRecordDao")
	private IRepayRecordDao repayRecordDao;
	@Resource(name = "CreditCardService")
	private ICreditCardService creditCardService;
	
	@Resource(name = "repayCostService")
	IRepayCostService repayCostService;

	@Autowired
	JedisPool jedisPool;
	@Autowired
	private RedissonClient redissonClient;
	
	private  final String Fast="F";
	private static final Logger log= LogManager.getLogger();

	/**
	* 信用卡还款
	*/
	public PayResult creditRepay(RepayPlanDetail rpd) {
		// TODO: 2017/9/11  可以在这里加上前面一条任务是否结束的判断  没有就等待10秒    防止服务器挂机 任务堆积运行 影响顺序

		RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+rpd.getRecordId());
		boolean res=false;
		try{

			RepayRecord repayRecord =  repayRecordDao.load(rpd.getRecordId());
			if("2".equals(repayRecord.getStatus())||"T".equals(repayRecord.getIsUnFreeze())){  //前面任务执行失败     //或者任务已经解冻
				log.info("repayrecord：{}执行失败，本条任务跳过",repayRecord.getId());
				return null;
			}

			 CreditCard card=creditCardService.findByCardNo(rpd.getCreditCardNo());
			if (card==null){
				log.error("repayPlan：{}查询不到信用卡，本条任务跳过",rpd.getId());
				repayRecordService.updateAllstatus(rpd.getRecordId(),"2",rpd.getId(),"2",null,null);
				return null;
			}



			res = fairLock.tryLock(60, 60, TimeUnit.SECONDS);
			long id=rpd.getUserId();
			String cardNo=rpd.getCreditCardNo();
			APPUser user=appUserService.findById(id);
			PayResult pr;
			if(canRepay(rpd,repayRecord)){
				//调用银行接口
				pr=platCreditProxyPay(rpd,user,card);

			}else{
				String repayDate=StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm");
				transWaterService.addTransls("repay-error-"+DateTimeUtil.getNowDateTime("yyyyMMddHHmmss"), user, cardNo, rpd.getRepayAmount(), new BigDecimal("0"), new BigDecimal("0"), rpd.getId(),null,"2","Fail","冻结金额不足不能进行还款");

				txRepayFailed(rpd, user.getId(), rpd.getRepayAmount(), "Fail", "冻结金额不足不能进行还款");//还款失败操作
				try {
					YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),"冻结金额不足不能进行还款", user.getUsername());    //发送短信
				} catch (Exception e) {
					// TODO: handle exception
				}

				log.error(rpd.getId()+"|冻结金额不足不能进行还款");
				pr=PayResult.genCustomFailPayResult("冻结金额不足不能进行还款");
			}

			return pr;
		}catch (Exception ex){
			ex.printStackTrace();
			return null;
		}finally {
			if(res)fairLock.unlock();
		}

	}
	
	
	/**
	* 信用卡还款
	*/
	public PayResult creditRepayDate(RepayPlanDetail rpd) {
		// TODO: 2017/9/11  可以在这里加上前面一条任务是否结束的判断  没有就等待10秒    防止服务器挂机 任务堆积运行 影响顺序

		RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+rpd.getRecordId());
		boolean res=false;
		try{
//			RepayRecord repayRecord=new RepayRecord();
			RepayRecord repayRecord =  repayRecordDao.load(rpd.getRecordId());
			if("2".equals(repayRecord.getStatus())||"T".equals(repayRecord.getIsUnFreeze())){  //前面任务执行失败     //或者任务已经解冻
				log.info("repayrecord：{}执行失败，本条任务跳过",repayRecord.getId());
				return null;
			}
 
			 CreditCard card=creditCardService.findByCardNo(rpd.getCreditCardNo());
			if (card==null){
				log.error("repayPlan:{}查询不到信用卡，本条任务跳过",rpd.getId());
				repayRecordService.updateAllstatus(rpd.getRecordId(),"2",rpd.getId(),"2",null,null);
				return null;
			}



			res = fairLock.tryLock(60, 60, TimeUnit.SECONDS);
			long id=rpd.getUserId();
			String cardNo=rpd.getCreditCardNo();
			APPUser user=appUserService.findById(id);
			PayResult pr;
			
//			if(1==1){
//				pr=platCreditProxyPay(rpd,user,card);
//				return null;
//			}
			if(canRepayDate(rpd,repayRecord)){
				//调用银行接口
				pr=platCreditProxyPay(rpd,user,card);

			}else{
				String repayDate=StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm");
				transWaterService.addTransls("repay-error-"+DateTimeUtil.getNowDateTime("yyyyMMddHHmmss"), user, cardNo, rpd.getRepayAmount(), new BigDecimal("0"), new BigDecimal("0"), rpd.getId(),null,"2","Fail","消费未完成不足不能进行还款");

				txRepayFailed(rpd, user.getId(), rpd.getRepayAmount(), "Fail", "消费未完成不足不能进行还款");//还款失败操作
				try {
					YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),"消费未完成不足不能进行还款", user.getUsername());    //发送短信
				} catch (Exception e) {
					// TODO: handle exception
				}

				log.error(rpd.getId()+"|消费未完成不足不能进行还款");
				pr=PayResult.genCustomFailPayResult("消费未完成不足不能进行还款");
			}

			return pr;
		}catch (Exception ex){
			ex.printStackTrace();
			return null;
		}finally {
			if(res)fairLock.unlock();
		}

	}
	
	private FeeInfo getFeeInfo(BigDecimal amount,String repayType,int cardType, String payType){
		String feeCode;//费率code
		String costCode;//成本code
		if(Fast.equals(repayType)){
			feeCode=FeeParamCode.QUICK_PROXY_PAY;
		}else{
			feeCode=FeeParamCode.NORMAL_PROXY_PAY;
		}
		if( CardType.CREDIT==cardType && ProxyPayType.D0.equals(payType) ){
			costCode=FeeParamCode.CREDIT_QUICK_PROXY_PAY_COST;
		}else if( CardType.CREDIT==cardType && ProxyPayType.T1.equals(payType) ){
				costCode=FeeParamCode.CREDIT_NORMAL_PROXY_PAY_COST;
		}else if( CardType.DEBIT==cardType && ProxyPayType.T1.equals(payType) ){
			costCode=FeeParamCode.DEBIT_NORMAL_PROXY_PAY_COST;
		}else if( CardType.DEBIT==cardType && ProxyPayType.D0.equals(payType) ){
			costCode=FeeParamCode.DEBIT_QUICK_PROXY_PAY_COST;
		}else{
			costCode=FeeParamCode.CREDIT_QUICK_PROXY_PAY_COST;
		}
		return  feeParamService.computeFeeInfo(amount,feeCode,costCode);
		
	}
	
	/**
	 * 还款成功操作
	 * @param amount
	 * @param feeInfo
	 * @param rpd
	 */
	private void txRepaySuccess(RepayPlanDetail rpd,Long userId,BigDecimal amount,FeeInfo feeInfo) {
		BigDecimal inOA=amount.add(feeInfo.getCost()).setScale(2, RoundingMode.HALF_UP).negate();
		log.debug("用户{}成功还款{}元，手续费{}元，"
				+ "运营商成本{}元，运营商信用卡账户收入{}元", 
				userId,
				amount,feeInfo.getFee().add(feeInfo.getExternal()),feeInfo.getCost(),inOA);

		//更新还款计划状态为还款成功未消费状态
		rpd.setStatus("1");
		repayPlanDetailService.doUpdateStatus(rpd);
		repayRecordService.reCalcAmountRepayRecord(amount.add(feeInfo.getFee()).add(feeInfo.getExternal()).negate(),feeInfo.getFee().add(feeInfo.getExternal()),rpd.getRecordId() );//修改计划冻结金额
		operatorsAccoutService.doUpdateCredit(inOA);
	}
	
	/**
	 * 还款成功操作
	 * @param amount
	 * @param feeInfo
	 * @param rpd
	 */
	private void txDateRepaySuccess(RepayPlanDetail rpd,Long userId,BigDecimal amount,FeeInfo feeInfo) {
		BigDecimal inOA=amount.add(feeInfo.getCost()).setScale(2, RoundingMode.HALF_UP).negate();
		log.debug("用户{}成功还款{}元，手续费{}元，"
				+ "运营商成本{}元，运营商信用卡账户收入{}元", 
				userId,
				amount,feeInfo.getFee().add(feeInfo.getExternal()),feeInfo.getCost(),inOA);

		//更新还款计划状态为还款成功未消费状态
		rpd.setStatus("5");
		repayPlanDetailService.doUpdateStatus(rpd);
		if(repayPlanDetailService.isLastRepay(rpd.getRecordId(),rpd.getId())) { //判断是否是最后一笔还款   把还款计划流水更新有成功
			repayRecordService.doUpdateStatus(rpd.getRecordId(),1); //还款计划修改成功还款成功
		}
		repayRecordService.reCalcAmountRepayRecord(amount.add(feeInfo.getFee()).add(feeInfo.getExternal()).negate(),feeInfo.getFee().add(feeInfo.getExternal()),rpd.getRecordId() );//修改计划冻结金额
//		operatorsAccoutService.doUpdateCredit(inOA);说
	}
	
	
	/**
	 * 还款失败操作
	 * @param amount 还款金额元为单位
	 * @param rpd    还款计划
	 */
	private void txRepayFailed(RepayPlanDetail rpd,long userId, BigDecimal amount, String respCode,String respRes) {
		repayRecordService.updateAllstatus(rpd.getRecordId(),"2",rpd.getId(),"2",null,null);
		//repayRecordService.unFreezePlanAmount(rpd.getUserId(),rpd.getRecordId(),rpd.getCreditCardNo(),rpd.getRepayMonth());
		messageService.writeMessage(userId,
				"您有一笔还款交易失败：金额" + amount + "（元） 时间："
						+ StringUtil.getCurrentTime() );
		log.info("用户{}的{}元还款失败，对应还款计划Id为{}，失败代码{}，失败描述：{}",
				userId,amount,rpd.getId(),respCode,respRes);
	}

	/**
	 * 处理还款消费 未知错误
	 */
	private  void txRepayHxtcUndefined(RepayPlanDetail rpd,String orderNo,BigDecimal amount,FeeInfo feeInfo,long translsId){
		rpd.setStatus("u");
		repayPlanDetailService.doUpdateStatus(rpd);
		repayRecordService.reCalcAmountRepayRecord(amount.add(feeInfo.getFee()).add(feeInfo.getExternal()).negate(),feeInfo.getFee().add(feeInfo.getExternal()),rpd.getRecordId() );//未知错误需要给他先减去冻结余额，  手动处理的时候如果成功不减，失败加上去
	}
	
	/**
	 * 处理还款消费 未知错误
	 */
	private  void txRepayUndefined(RepayPlanDetail rpd,String orderNo,BigDecimal amount,FeeInfo feeInfo,long translsId){
		rpd.setStatus("u");
		repayPlanDetailService.doUpdateStatus(rpd);
		repayRecordService.reCalcAmountRepayRecord(amount.add(feeInfo.getFee()).add(feeInfo.getExternal()).negate(),feeInfo.getFee().add(feeInfo.getExternal()),rpd.getRecordId() );//未知错误需要给他先减去冻结余额，  手动处理的时候如果成功不减，失败加上去
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
	}

	/**
	 * 使用平台信用卡代付给用户
	 * @param user
	 * @return
	 */
	private PayResult platCreditProxyPay(RepayPlanDetail rpd,APPUser user,CreditCard card){
		
		//2.查找还款计划对应的信用卡
		String cardNo=rpd.getCreditCardNo();
		BigDecimal amount=rpd.getRepayAmount();

		FeeInfo feeInfo=feeParamService.computeFeeInfo(amount,FeeParamCode.QUICK_PROXY_PAY,FeeParamCode.CREDIT_QUICK_PROXY_PAY_COST);
		String orderNo = appUserService.getOrderNo(user.getId(), card.getPhoneNo());
		boolean cpcb=checkPlatformAccountBalanceService.checkPlatformCreditBalance();  //检查平台信用卡账户余额与所在银行余额是否一致
		PayResult result = null;
		if(cpcb){
			//先记流水
			long translsId  = transWaterService.addTransls(orderNo, user, cardNo, amount, feeInfo.getFee(), feeInfo.getExternal(), rpd.getId(),null,"2");//添加流水

			log.debug("调用银行接口请求参数:{}",user.toString());
			PayRequest request= payRequestService.getCreditCardRepayRequest(user,amount,orderNo,card);
			
			try {
				log.debug("调用银行接口请求参数:{}",request.toString());
//				result=callRemotePayService.creditProxyPay(request);
				if("hx".equals(Constant.tdtype)){
					//汇享天成支付
					result=callRemotePayService.hxtcCreditProxyPay(request);
				}else if("ybl".equals(Constant.tdtype)){
					result=callRemotePayService.yblCreditProxyPay(request);
				}else if("kj".equals(Constant.tdtype)){
					if(StringUtil.isEmpty(request.getKjmerno()) || StringUtil.isEmpty(request.getKjkey())){
						result=PayResult.genCustomFailPayResult("用户未进行报户!");
						return result;
					}
					result=callRemotePayService.kjCreditProxyPay(request);
				}
				
				//
				
				log.debug("银行接口返回code:{},message:{}", result.getCode(),result.getMessage());
			} catch (Exception e1) {
				result=PayResult.genCustomFailPayResult("调用信用卡代付接口发生异常");
				log.error("用信用卡代付接口发生异常{}", e1.toString());
				e1.printStackTrace();
			}
			//result.setCode("0000");//模拟
			String code=result.getCode();
			String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
			String time = DateTimeUtil.getNowDateTime("HHmmss");
//            if(1==1){
//            	return null;
//            }
			if("0000".equals(code) ){

				//记流水
				transWaterService.modifyTransls(translsId, orderNo, "", "0000","h-还款成功" , date, time,result.getTransactionType());  //修改流水
				//修改状态：还款计划状态为还款成功值为5
				txDateRepaySuccess(rpd, user.getId(), amount, feeInfo);
		//		txRepayHxtcUndefined(rpd,orderNo, amount, feeInfo,translsId);
			}else if( "9997".equals(code)){  //networkError 自己定义的网络错误
				transWaterService.modifyTransls(translsId, orderNo, "", code,result.getMessage() , date, time,result.getTransactionType());
				txRepayHxtcUndefined(rpd,orderNo, amount, feeInfo,translsId);
			}else{
//				String repayDate=StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm");
				transWaterService.modifyTransls(translsId, orderNo, "", code,"h-还款失败:"+result.getMessage() , date, time,result.getTransactionType());    //修改流水
				txRepayFailed(rpd, user.getId(), amount, code, result.getMessage());//还款失败操作
				try {
					YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),result.getMessage(), user.getUsername());    //发送短信
				} catch (Exception e) {
					// TODO: handle exception
				}
				result.setTranslsId(translsId);
			}
			return result;
		}else{
			//平台账户资金错误
			result=PayResult.genCustomFailPayResult("系统错误-余额对不上");
			transWaterService.addTransls("balance-error-"+DateTimeUtil.getNowDateTime("yyyyMMddHHmmss"), user, cardNo, amount, new BigDecimal("0"), new BigDecimal("0"), rpd.getId(),null,"2");
			return result;
		}
	}
	
	
	/**
	 * 判断是否能够进行还款，冻结金额必须大于或等于还款金额才能进行执行还款
	 * @param rpd 还款计划
	 * @return 
	 */
	private boolean canRepay(RepayPlanDetail rpd,RepayRecord record ){

		if (record==null){
			return false;
		}
		//冻结金额
		BigDecimal freeze=record.getAmount().setScale(2, RoundingMode.HALF_UP);
		//还款金额
		BigDecimal repay=rpd.getRepayAmount().setScale(2, RoundingMode.HALF_UP);
		if(repay.compareTo(freeze)>0){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否能够进行还款，冻结金额必须大于或等于还款金额才能进行执行还款
	 * @param rpd 还款计划
	 * @return 
	 */
	private boolean canRepayDate(RepayPlanDetail rpd,RepayRecord record ){

		if (record==null){
			return false;
		}
		int ret = repayCostService.checkRepayPlanStatus(rpd.getId());  //0|未全部执行完；1|全部成；2|失败(部分失败)
		if(ret !=1)
			return false; //说明hi阿尤为消费的
		String status = rpd.getStatus();
		if("0".equals(status)){
			return true;
		}
		return false;
	}
	
	public void txBalanceException(Long userId,RepayPlanDetail rpd,BigDecimal transAmount){
		messageService.writeMessage(userId, "您有一笔还款交易失败：金额"
				+ rpd.getRepayAmount().setScale(2, RoundingMode.HALF_UP) + "（元） 时间："
				+  StringUtil.getCurrentTime() );
		rpd.setStatus("2");
		APPUser user = appUserService.findById(userId);
		TransWater tw=getTransWater(user,rpd.getCreditCardNo(),transAmount);
		tw.setRespCode("fail");
		tw.setRespRes("冻结金额不足");
		transWaterService.doInsert(tw);
		repayPlanDetailService.doUpdateStatus(rpd);
	}
	private TransWater getTransWater(APPUser user,String cardNo,BigDecimal transAmount){
		TransWater transWater = new TransWater();
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");
		transWater.setTransDate(date);
		transWater.setTransTime(time);
		transWater.setTransNo(appUserService.getOrderNo(user.getId(), user.getMobilephone()));
		transWater.setCardNo(cardNo);
		transWater.setOrgId(user.getOrgId());
		transWater.setUserId(user.getId());
		transWater.setTransAmount(transAmount.setScale(2, RoundingMode.HALF_UP));
		transWater.setTrans_type("2");
		transWater.setCardType("1");
		transWater.setFee(new BigDecimal("0"));
		transWater.setExternal(new BigDecimal("0"));
		transWater.setQrcodeId(user.getQrcodeId());
		transWater.setRelationNo(user.getRelationNo());
		return transWater;
	}
	
	
	public PayResult repayPlanAgain(String orderNo,String transno){
		PayResult result = null;
		TransWater transWater = transWaterService.findByTransNo(orderNo);
		 CreditCard card=creditCardService.findByCardNo(transWater.getCardNo());
			if (card==null){
				log.error("repayPlanAgain:{}查询不到信用卡",transWater.getCardNo());
				return null;
			}
			BigDecimal amount = transWater.getTransAmount();
			APPUser user=  appUserService.findById(transWater.getUserId());
			if (user==null){
				log.error("repayPlanAgain:{}查询不到用户",transWater.getUserId());
				return null;
			}
		PayRequest request= payRequestService.getCreditCardRepayRequest(user,amount,transno,card);
		try {
			log.debug("repayPlanAgain调用银行接口请求参数:{}",request.toString());
			
			
			if("hx".equals(Constant.tdtype)){
				//汇享天成支付
				result=callRemotePayService.hxtcCreditProxyPay(request);
			}else if("ybl".equals(Constant.tdtype)){
				result=callRemotePayService.yblCreditProxyPay(request);
			}else if("kj".equals(Constant.tdtype)){
				if(StringUtil.isEmpty(request.getKjmerno()) || StringUtil.isEmpty(request.getKjkey())){
					result=PayResult.genCustomFailPayResult("用户未进行报户!");
					return result;
				}
				result=callRemotePayService.kjCreditProxyPay(request);
			}
			
			//汇享天成支付
			//result=callRemotePayService.hxtcCreditProxyPay(request);
			log.debug("repayPlanAgain银行接口返回code:{},message:{}", result.getCode(),result.getMessage());
		} catch (Exception e1) {
			result=PayResult.genCustomFailPayResult("repayPlanAgain调用信用卡代付接口发生异常");
			log.error("repayPlanAgain用信用卡代付接口发生异常{}", e1.toString());
			e1.printStackTrace();
		}
		return result;
	}
	
	/**
	* 信用卡还款
	*/
	public PayResult reCreatOrder(RepayPlanDetail rpd) {
		// TODO: 2017/9/11  可以在这里加上前面一条任务是否结束的判断  没有就等待10秒    防止服务器挂机 任务堆积运行 影响顺序

		RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+rpd.getRecordId());
		boolean res=false;
		try{
			RepayRecord repayRecord =  repayRecordDao.load(rpd.getRecordId());
			if(repayRecord==null){  //前面任务执行失败     //或者任务已经解冻
				log.info("计划不存在：{}",rpd.getRecordId());
				return null;
			}

			 CreditCard card=creditCardService.findByCardNo(rpd.getCreditCardNo());
			if (card==null){
				log.error("repayPlan:{}查询不到信用卡，本条任务跳过",rpd.getId());
				return null;
			}

			res = fairLock.tryLock(60, 60, TimeUnit.SECONDS);
			long id=rpd.getUserId();
			String cardNo=rpd.getCreditCardNo();
			APPUser user=appUserService.findById(id);
			PayResult pr = null;
				BigDecimal amount=rpd.getRepayAmount();
				FeeInfo feeInfo=feeParamService.computeFeeInfo(amount,FeeParamCode.QUICK_PROXY_PAY,FeeParamCode.CREDIT_QUICK_PROXY_PAY_COST);
				String orderNo = appUserService.getOrderNo(user.getId(), card.getPhoneNo());
				boolean cpcb=checkPlatformAccountBalanceService.checkPlatformCreditBalance();  //检查平台信用卡账户余额与所在银行余额是否一致
				if(cpcb){
					//先记流水
					long translsId  = transWaterService.addTransls(orderNo, user, cardNo, amount, feeInfo.getFee(), feeInfo.getExternal(), rpd.getId(),null,"2");//添加流水
					pr=PayResult.genCustomFailPayResult("重新生成订单成功"+translsId);
				}
			return pr;
		}catch (Exception ex){
			ex.printStackTrace();
			return null;
		}finally {
			if(res)fairLock.unlock();
		}

	}
}
