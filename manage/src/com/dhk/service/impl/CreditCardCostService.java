package com.dhk.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.dhk.dao.IRepayRecordDao;
import com.dhk.entity.*;
import com.dhk.init.Constant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhk.FeeInfo;
import com.dhk.FeeParamCode;
import com.dhk.payment.PayRequest;
import com.dhk.payment.PayResult;
import com.dhk.service.IAPPUserService;
import com.dhk.service.ICallRemotePayService;
import com.dhk.service.ICostPlanDetailService;
import com.dhk.service.ICreditCardCostService;
import com.dhk.service.ICreditCardService;
import com.dhk.service.IFeeParamService;
import com.dhk.service.IMessageService;
import com.dhk.service.IOperatorsAccoutService;
import com.dhk.service.IPayRequestService;
import com.dhk.service.IRepayCostService;
import com.dhk.service.IRepayPlanDetailService;
import com.dhk.service.IRepayRecordService;
import com.dhk.service.ITransWaterService;
import com.dhk.service.IUserAccountService;
import com.dhk.utils.DateTimeUtil;
import com.dhk.yunpain.YunPainSmsMsg;
import com.sunnada.kernel.util.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("CreditCardCostService")
public class CreditCardCostService  implements ICreditCardCostService{
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

	@Resource(name = "repayCostService")
	IRepayCostService repayCostService;

	@Resource(name = "CreditCardService")
	ICreditCardService creditCardService;

	@Resource(name = "UserAccountService")
	IUserAccountService userAccountService;

	@Resource(name = "CostPlanDetailService")
	ICostPlanDetailService costPlanDetailService;
	@Autowired
	JedisPool jedisPool;
	@Autowired
	private RedissonClient redissonClient;

	@Resource(name = "repayRecordDao")
	private IRepayRecordDao repayRecordDao;


	private static final Logger log= LogManager.getLogger();
	
	/*=====================================20170307=======================================*/
	/**
	 * 还款消费
	 * @param rpd
	 * @return
	 */
	public PayResult creditRepayCost( RepayPlanDetail rpd,RepayCost rc) {
		// TODO: 2017/9/11  可以在这里加上前面一条任务是否结束的判断  没有就等待10秒    防止服务器挂机 任务堆积运行 影响顺序
	     	RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+rpd.getRecordId());
	     	boolean res=false;
		    try{
				 res = fairLock.tryLock(10, 10, TimeUnit.SECONDS);
			 	 RepayRecord repayRecord =  repayRecordDao.load(rpd.getRecordId());
				 if("2".equals(repayRecord.getStatus())||"T".equals(repayRecord.getIsUnFreeze())){  //前面任务执行失败     //或者任务已经解冻
					log.info("repayrecord：{}执行失败，本条任务跳过",repayRecord.getId());
					return null;
				 }

				CreditCard card=creditCardService.findByCardNo(rpd.getCreditCardNo());
				if (card==null){
					log.error("RepayCost：{}查询不到信用卡，本条任务跳过",rc.getId());
					repayRecordService.updateAllstatus(rpd.getRecordId(),"2",rpd.getId(),"4",rc.getId(),"2");
					return null;
				}
				//1.查找消费计划对应的用户
				long id=rpd.getUserId();
				APPUser user=appUserService.findById(id);
				//2.查找消费计划对应的信用卡
				String cardNo=rpd.getCreditCardNo();
				//3.调用银行接口进行信用卡消费
				BigDecimal amount=rc.getCost_amount();
				if("u".equals(rpd.getStatus())){  //如果他对应的还款还是未还款状态（有可能是未知错误导致的）  就把整个任务停掉，
					transWaterService.addTransls("repayCost-error-"+DateTimeUtil.getNowDateTime("yyyyMMddHHmmss"), user, cardNo, rpd.getRepayAmount(), new BigDecimal("0"), new BigDecimal("0"), rpd.getId(),rc.getId(),"1","Fail","对应的还款未执行");
					log.error("RepayCost：{}对应的还款未执行",rc.getId());
					repayRecordService.updateAllstatus(rpd.getRecordId(),"2",null,null,rc.getId(),"2");
					return null;
				}



				//计算还款消费所需要的手续费
				FeeInfo feeInfo=feeParamService.computeFeeInfo(amount,FeeParamCode.PUSERCHASE,FeeParamCode.PUSERCHASE_COST);

				String orderNo = appUserService.getOrderNo(user.getId(), card.getPhoneNo());

				PayRequest cccp=payRequestService.getCreditCardCostRequest(user, amount,card,orderNo);
				//写流水
				long translsId = transWaterService.addTransls(orderNo, user, cardNo, amount, feeInfo.getFee(), feeInfo.getExternal(), rpd.getId(),rc.getId(),"1");

				PayResult pr;
				try {
					log.debug("调用银行接口请求参数:{}", cccp.toString());
					pr=callRemotePayService.creditPurchase(cccp);
					log.debug("调用银行接口返回code:{},message:{}", pr.getCode(),pr.getMessage());
				} catch (Exception e) {
					log.error("调用银行接口发生异常{}", e.toString());
					pr=PayResult.genCustomFailPayResult("还款消费失败");
					e.printStackTrace();
				}
				//pr.setCode("0000");
				String resCode=pr.getCode();
				String repayDate=StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm");
				String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
				String time = DateTimeUtil.getNowDateTime("HHmmss");

				if( "0000".equals(resCode) ||  "00".equals(resCode) ){
					transWaterService.modifyTransls(translsId, orderNo, "", "0000","交易成功" , date, time,pr.getTransactionType());
					//修改状态消费计划状态为消费成功
					//txRepayCostSuccess(cardNo,translsId,user.getId(),feeInfo,rpd,rc,pr,user.getUsername());
					txDateRepayCostSuccess(cardNo,translsId,user.getId(),feeInfo,rpd,rc,pr,user.getUsername()); //资金不过夜模式
				}else if( "9997".equals(resCode)){
					transWaterService.modifyTransls(translsId, orderNo, "", resCode,pr.getMessage() , date, time,pr.getTransactionType());
					txRepayCostUndefined(orderNo,rpd,rc,translsId);
				}else{      //消费失败
					transWaterService.modifyTransls(translsId, orderNo, "", resCode,pr.getMessage() , date, time,pr.getTransactionType());
					txRepayCostFailed(user.getId(),rpd,rc);
					try {     //发送短信提醒
						YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),pr.getMessage() , user.getUsername());
					} catch (Exception e) {
						e.printStackTrace();
						log.error("发送短信异常："+e.getMessage());
					}
					return pr;
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
	 * 处理还款消费成功
	 */
	protected  void txRepayCostSuccess(String cardNo,Long translsId,Long userId,
			FeeInfo feeInfo,RepayPlanDetail rpd ,RepayCost rc,PayResult pr,String phone){

		BigDecimal amount=rc.getCost_amount();
		BigDecimal inOA=amount.subtract(feeInfo.getCost()).setScale(2, RoundingMode.HALF_UP);
		log.debug("用户{}还款消费{}元，手续费{}元;\r\n"
				+ "运营商成本{}元，运营商信用卡账户收入{}元", 
				userId,
				amount,feeInfo.getFee().add(feeInfo.getExternal()),feeInfo.getCost(),inOA);

		long id=rpd.getId();
		long rcId=rc.getId();

		try {

			repayRecordService.reCalcAmountRepayRecord(amount.subtract(feeInfo.getFee()).subtract(feeInfo.getExternal()),feeInfo.getFee().add(feeInfo.getExternal()), rpd.getRecordId());
			//更新还款消费的状态
			repayCostService.doUpdateRepayCostStatus(rcId, "1");
			//更新运营商信用卡账户资金
			operatorsAccoutService.doUpdateCredit(inOA);


			if(!"u".equals(rpd.getStatus())){ //如果repayplan的状态是u 未知错误 就不给他更改状态
				//修改还款RepayPlan状态  ，判断依据是：是否所有的消费还款都成功
				int status = repayCostService.checkRepayPlanStatus(id);
				if(status==2){
					rpd.setStatus("4");
					repayPlanDetailService.doUpdateStatus(rpd);
				}else if(status==1){
					rpd.setStatus("3");
					repayPlanDetailService.doUpdateStatus(rpd);
				}
				if(status==1){    //如果单笔 t_n_repay_plan 成功了 才有必要去判断
					//检查t_n_repay_record 对应的t_n_repay_plan 是否全部成 ，如果全部成功则解冻用户余额
					if(repayRecordService.repayRecordIsSuccess(rpd.getRecordId()+"")){
						repayRecordService.unFreezePlanAmount(rpd.getUserId(),rpd.getRecordId(),rpd.getCreditCardNo(),rpd.getRepayMonth());
						repayRecordService.doUpdateStatus(rpd.getRecordId(),1);

						try {     //发送短信提醒
							YunPainSmsMsg.sendSuccess(cardNo.substring(cardNo.length()-4, cardNo.length()), StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm"), phone);
						} catch (Exception e) {
							e.printStackTrace();
							log.error("发送短信异常："+e.getMessage());
						}
					}
				}
			}


		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 处理还款消费成功 资金不过夜
	 */
	protected  void txDateRepayCostSuccess(String cardNo,Long translsId,Long userId,
			FeeInfo feeInfo,RepayPlanDetail rpd ,RepayCost rc,PayResult pr,String phone){

		BigDecimal amount=rc.getCost_amount();
		BigDecimal inOA=amount.subtract(feeInfo.getCost()).setScale(2, RoundingMode.HALF_UP);
		log.debug("用户{}还款消费{}元，手续费{}元;\r\n"
				+ "运营商成本{}元，运营商信用卡账户收入{}元", 
				userId,
				amount,feeInfo.getFee().add(feeInfo.getExternal()),feeInfo.getCost(),inOA);

		long id=rpd.getId();
		long rcId=rc.getId();

		try {

			repayRecordService.reCalcAmountRepayRecord(amount.subtract(feeInfo.getFee()).subtract(feeInfo.getExternal()),feeInfo.getFee().add(feeInfo.getExternal()), rpd.getRecordId());
			//更新还款消费的状态
			repayCostService.doUpdateRepayCostStatus(rcId, "1");
			//更新运营商信用卡账户资金
			operatorsAccoutService.doUpdateCredit(inOA);
			//修改还款RepayPlan状态  ，判断依据是：是否所有的消费还款都成功
			int status = repayCostService.checkRepayPlanStatus(id);
			if(status==2){
				rpd.setStatus("4");
				repayPlanDetailService.doUpdateStatus(rpd);
			}else if(status==1){
				rpd.setStatus("3");
				repayPlanDetailService.doUpdateStatus(rpd); //修改成已消费未还款
			}

			/*if(!"u".equals(rpd.getStatus())){ //如果repayplan的状态是u 未知错误 就不给他更改状态
				//修改还款RepayPlan状态  ，判断依据是：是否所有的消费还款都成功
				int status = repayCostService.checkRepayPlanStatus(id);
				if(status==2){
					rpd.setStatus("4");
					repayPlanDetailService.doUpdateStatus(rpd);
				}else if(status==1){
					rpd.setStatus("3");
					repayPlanDetailService.doUpdateStatus(rpd);
				}
				if(status==1){    //如果单笔 t_n_repay_plan 成功了 才有必要去判断
					//检查t_n_repay_record 对应的t_n_repay_plan 是否全部成 ，如果全部成功则解冻用户余额
					if(repayRecordService.repayRecordIsSuccess(rpd.getRecordId()+"")){
						repayRecordService.unFreezePlanAmount(rpd.getUserId(),rpd.getRecordId(),rpd.getCreditCardNo(),rpd.getRepayMonth());
						repayRecordService.doUpdateStatus(rpd.getRecordId(),1);

						try {     //发送短信提醒
							YunPainSmsMsg.sendSuccess(cardNo.substring(cardNo.length()-4, cardNo.length()), StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm"), phone);
						} catch (Exception e) {
							e.printStackTrace();
							log.error("发送短信异常："+e.getMessage());
						}
					}
				}
			}*/


		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	/**
	 * 处理还款消费失败
	 */
	private  void txRepayCostFailed(long userId,RepayPlanDetail rpd ,RepayCost rc){
		BigDecimal amount=rc.getCost_amount();

		repayRecordService.updateAllstatus(rpd.getRecordId(),"2",rpd.getId(),"4",rc.getId(),"2");
		//repayRecordService.unFreezePlanAmount(rpd.getUserId(),rpd.getRecordId(),rpd.getCreditCardNo(),rpd.getRepayMonth());
		messageService.writeMessage(userId,
				"您有一笔还款消费交易失败：金额" + amount + "（元） 时间："
						+ StringUtil.getCurrentTime() );
	}

	/**
	 * 处理还款消费 未知错误
	 */
	private  void txRepayCostUndefined(String orderNo,RepayPlanDetail rpd ,RepayCost rc,long translsId){
		Jedis jedis = null;
		try{
			repayCostService.doUpdateRepayCostStatus(rc.getId(), "u");
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
	 * @author 杨亮
	 * 对信用卡消费计划进行自动消费
	 */
	public PayResult creditCost( CostPlanDetail cpd) {
		//1.查找制定信用卡消费计划的用户ID
		long id=cpd.getUserId();
		APPUser user=appUserService.findById(id);
		//2.查找消费计划对应的信用卡
		String cardNo=cpd.getCardNo();
		CreditCard card=creditCardService.findByCardNo(cardNo);
		//3.调用银行接口进行信用卡消费
		BigDecimal amount=cpd.getCostAmount();
		//计算消费金额所需要的手续费
		FeeInfo feeInfo=feeParamService.computeFeeInfo(amount,FeeParamCode.PUSERCHASE,FeeParamCode.PUSERCHASE_COST);

		//写流水
		String orderNo = appUserService.getOrderNo(user.getId(), card.getPhoneNo());
		Long translsId = transWaterService.addTransls(orderNo, user, cardNo, amount, feeInfo.getFee(), feeInfo.getExternal(), cpd.getId(),null,"0");

		PayResult pr;
		PayRequest cccp=payRequestService.getCreditCardCostRequest(user, amount,card,orderNo);
		try {
			log.debug("调用银行接口请求参数:{}", cccp.toString());
			pr=callRemotePayService.creditPurchase(cccp);
			log.debug("调用银行接口返回code:{},message:{}", pr.getCode(),pr.getMessage());
		} catch (Exception e) {
			pr=PayResult.genCustomFailPayResult("消费失败");
			log.error("调用银行接口发生异常{}", e.toString());
			e.printStackTrace();
		}

		String resCode=pr.getCode();
		if( "0000".equals(resCode) ||  "00".equals(resCode) ){
			//修改状态:消费计划状态改为消费成功
			txDoCostSuccess(translsId,cccp.getOrderNo(),user,cardNo,amount,feeInfo,cpd,pr,false,pr.getTransactionType());
		}else if( "9997".equals(resCode)){
			//修改状态消费计划状态为消费发生未知错误
			//doCostFailed(user,cardNo,amount,feeInfo,cpd,pr);

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
			txCostFailed(translsId,cccp.getOrderNo(),user,cardNo,amount,cpd,pr,resCode,pr.getMessage(),pr.getTransactionType());
		}else{
			txCostFailed(translsId,cccp.getOrderNo(),user,cardNo,amount,cpd,pr,resCode,pr.getMessage(),pr.getTransactionType());
			return pr;
		}
		return pr;
	}


	/**
	 * 处理消费成功
	 */
	protected  void txDoCostSuccess(Long translsId,String orderNo,APPUser user,String cardNo,BigDecimal amount,
									FeeInfo feeInfo,CostPlanDetail cpd ,PayResult pr,boolean isBindCard,String transactionType){
		UserAccount userAccount = new UserAccount();
		BigDecimal totalFee=feeInfo.getFee().add(feeInfo.getExternal());
		BigDecimal inAccount = amount.subtract(totalFee).setScale(2, RoundingMode.HALF_UP);

		userAccount.setInAccount(inAccount);
		userAccount.setUserId(user.getId());
		userAccount.setUpdateDate(DateTimeUtil.getNowDateTime("yyyyMMdd"));
		userAccount.setUpdateTime(DateTimeUtil.getNowDateTime("HHmmss"));
		//更新用户当前余额
		userAccountService.seriUpdateBalance(userAccount);

		BigDecimal inOA=amount.subtract(feeInfo.getCost()).setScale(2, RoundingMode.HALF_UP);
		//更新运营商信用卡账户资金
		operatorsAccoutService.doUpdateCredit(inOA);
		//记录消费流水
		if(cpd!=null){
			cpd.setStatus("1");
			costPlanDetailService.updateStatus(cpd);
			log.debug("用户{}成功消费{}元，手续费{}元，用户账户收入{}元;\r\n"
							+ "运营商成本{}元，运营商信用卡账户收入{}元",
					user.getId(),
					amount,feeInfo.getFee(),inAccount,feeInfo.getCost(),inOA);
		}else{
			//transWaterService.writeCostTransWater(orderNo,user, cardNo, amount, feeInfo.getFee(), feeInfo.getExternal(),null, pr);
			log.debug("用户{}绑卡{}消费{}元，手续费{}元;\r\n"
							+ "运营商成本{}元，运营商信用卡账户收入{}元",
					user.getId(),cardNo,
					amount,feeInfo.getFee(),inAccount,feeInfo.getCost(),inOA);
		}


		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");


		transWaterService.modifyTransls(translsId, orderNo, "", "0000","交易成功" , date, time,transactionType);

	}

	/**
	 * 处理消费失败
	 */
	private  void txCostFailed(Long translsId,String orderNo,APPUser user,String cardNo,BigDecimal amount,
							   CostPlanDetail cpd ,PayResult pr,String respCode,String respRes,String transactionType){
		//记录消费流水
		//long id=cpd.getId();
		Long id = null;
		if(cpd!=null){
			id = cpd.getId();
		}

		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");


		transWaterService.modifyTransls(translsId, orderNo, "", respCode,respRes , date, time,transactionType);
		//更新消费计划状态为消费失败
		if(cpd!=null){
			cpd.setStatus("-1");
			costPlanDetailService.updateStatus(cpd);
		}
		messageService.writeMessage(user.getId(),
				"您有一笔消费交易失败：金额" + amount + "（元） 时间："
						+ StringUtil.getCurrentTime() );
		if(cpd!=null){
			log.info("用户{}的{}元消费失败，对应消费计划Id为{}，失败代码{}，失败描述：{}",
					user.getId(),amount,cpd.getId(),pr.getCode(),pr.getMessage());
		}else{
			log.info("用户{}的{}元消费失，失败代码{}，失败描述：{}",
					user.getId(),amount,pr.getCode(),pr.getMessage());
		}
	}

	@Override
	public PayResult creditDateRepayCost(RepayPlanDetail rpd, RepayCost rc) {
		// TODO: 2017/9/11  可以在这里加上前面一条任务是否结束的判断  没有就等待10秒    防止服务器挂机 任务堆积运行 影响顺序
     	RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+rpd.getRecordId());
     	boolean res=false;
	    try{
			 res = fairLock.tryLock(10, 10, TimeUnit.SECONDS);
		 	 RepayRecord repayRecord =  repayRecordDao.load(rpd.getRecordId());
			 if("2".equals(repayRecord.getStatus())||"T".equals(repayRecord.getIsUnFreeze())||"3".equals(repayRecord.getIsUnFreeze())){  //前面任务执行失败     //或者任务已经解冻
				log.info("repayrecord：{}执行失败，本条任务跳过",repayRecord.getId());
				return null;
			 }

			CreditCard card=creditCardService.findByCardNo(rpd.getCreditCardNo());
			if (card==null){
				log.error("RepayCost：{}查询不到信用卡，本条任务跳过",rc.getId());
				repayRecordService.updateAllstatus(rpd.getRecordId(),"2",rpd.getId(),"4",rc.getId(),"2");
				return null;
			}
			//1.查找消费计划对应的用户
			long id=rpd.getUserId();
			APPUser user=appUserService.findById(id);
			//2.查找消费计划对应的信用卡
			String cardNo=rpd.getCreditCardNo();
			//3.调用银行接口进行信用卡消费
			BigDecimal amount=rc.getCost_amount();
			/*if("u".equals(rpd.getStatus())){  //如果他对应的还款还是未还款状态（有可能是未知错误导致的）  就把整个任务停掉，
				transWaterService.addTransls("repayCost-error-"+DateTimeUtil.getNowDateTime("yyyyMMddHHmmss"), user, cardNo, rpd.getRepayAmount(), new BigDecimal("0"), new BigDecimal("0"), rpd.getId(),rc.getId(),"1","Fail","对应的还款未执行");
				log.error("RepayCost：{}对应的还款未执行",rc.getId());
				repayRecordService.updateAllstatus(rpd.getRecordId(),"2",null,null,rc.getId(),"2");
				return null;
			}
			 */


			//计算还款消费所需要的手续费
			FeeInfo feeInfo=feeParamService.computeFeeInfo(amount,FeeParamCode.PUSERCHASE,FeeParamCode.PUSERCHASE_COST);

			String orderNo = appUserService.getOrderNo(user.getId(), card.getPhoneNo());

			PayRequest cccp=payRequestService.getCreditCardCostRequest(user, amount,card,orderNo);
			//写流水
			long translsId = transWaterService.addTransls(orderNo, user, cardNo, amount, feeInfo.getFee(), feeInfo.getExternal(), rpd.getId(),rc.getId(),"1");

			PayResult pr;
//			if(1==1){
//				pr=callRemotePayService.kjCreditPurchase(cccp);
//				return null;
//			}
			try {
				log.debug("调用银行接口请求参数:{}", cccp.toString());
//				pr=callRemotePayService.creditPurchase(cccp);
				//pr=callRemotePayService.hxtcCreditPurchase(cccp);
				
				if("hx".equals(Constant.costtype)){
					//汇享天成支付
					pr=callRemotePayService.hxtcCreditPurchase(cccp);
				}else if("ybl".equals(Constant.costtype)){
					pr=callRemotePayService.yblCreditPurchase(cccp);
				}else if("kj".equals(Constant.costtype)){
					if(StringUtil.isEmpty(cccp.getKjmerno()) || StringUtil.isEmpty(cccp.getKjkey())){
						pr=PayResult.genCustomFailPayResult("用户未进行报户!");
						return pr;
					}
					pr=callRemotePayService.kjCreditPurchase(cccp);
				}else{
					pr=callRemotePayService.yblCreditPurchase(cccp);
				}
				
				log.debug("调用银行接口返回code:{},message:{}", pr.getCode(),pr.getMessage());
			} catch (Exception e) {
				log.error("调用银行接口发生异常{}", e.toString());
				pr=PayResult.genCustomFailPayResult("还款消费失败");
				e.printStackTrace();
			}
			//pr.setCode("0000");
			String resCode=pr.getCode();
			String repayDate=StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm");
			String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
			String time = DateTimeUtil.getNowDateTime("HHmmss");
			
			if( "0000".equals(resCode) ||  "00".equals(resCode) ){
				transWaterService.modifyTransls(translsId, orderNo, "", "0000","交易成功" , date, time,pr.getTransactionType());
				//修改状态消费计划状态为消费成功
				//txRepayCostSuccess(cardNo,translsId,user.getId(),feeInfo,rpd,rc,pr,user.getUsername());
				txDateRepayCostSuccess(cardNo,translsId,user.getId(),feeInfo,rpd,rc,pr,user.getUsername());  //资金不过夜模式
			}else if( "9997".equals(resCode)){
				transWaterService.modifyTransls(translsId, orderNo, "", resCode,pr.getMessage() , date, time,pr.getTransactionType());
				if("ybl".equals(Constant.costtype)){
					txRepayCostUndefined(orderNo,rpd,rc,translsId);
				}
				//txRepayCostUndefined(orderNo,rpd,rc,translsId);
			}else{      //消费失败
				transWaterService.modifyTransls(translsId, orderNo, "", resCode,"h-交易失败:"+pr.getMessage() , date, time,pr.getTransactionType());
				txRepayCostFailed(user.getId(),rpd,rc);
				try {     //发送短信提醒
					YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),pr.getMessage(), user.getUsername());
				} catch (Exception e) {
					e.printStackTrace();
					log.error("发送短信异常："+e.getMessage());
				}
				return pr;
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
