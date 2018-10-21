package com.dhk.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhk.FeeInfo;
import com.dhk.FeeParamCode;
import com.dhk.dao.ITransWaterDao;
import com.dhk.entity.APPUser;
import com.dhk.entity.APPUserApplyCash;
import com.dhk.entity.CostPlanDetail;
import com.dhk.entity.Org;
import com.dhk.entity.OrgAgentApplyCash;
import com.dhk.entity.RepayCost;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.entity.TransWater;
import com.dhk.entity.UserAccount;
import com.dhk.service.IAPPUserApplyCashService;
import com.dhk.service.IAPPUserService;
import com.dhk.service.ICallBackService;
import com.dhk.service.ICostPlanDetailService;
import com.dhk.service.IFeeParamService;
import com.dhk.service.IOperatorsAccoutService;
import com.dhk.service.IOrgAgentApplyCashService;
import com.dhk.service.IOrgService;
import com.dhk.service.IRepayCostService;
import com.dhk.service.IRepayPlanDetailService;
import com.dhk.service.IRepayRecordService;
import com.dhk.service.ITransWaterService;
import com.dhk.service.IUserAccountService;
import com.dhk.utils.Tool;
import com.dhk.yunpain.YunPainSmsMsg;
import com.sunnada.kernel.util.StringUtil;

@Service("callBackService")
public class CallBackService implements ICallBackService {


	@Resource(name = "FeeParamService")
	private IFeeParamService feeParamService;

	@Resource(name = "TransWaterDao")
	private ITransWaterDao transWaterDao;
	@Resource(name = "UserAccountService")
	private IUserAccountService userAccountService;

	@Resource(name = "operatorsAccoutService")
	private IOperatorsAccoutService operatorsAccoutService;

	@Resource(name="APPUserApplyCashService")
	private IAPPUserApplyCashService APPUserApplyCashService;

	@Resource(name = "RepayPlanDetailService")
	private IRepayPlanDetailService repayPlanDetailService;

	@Resource(name = "repayCostService")
	IRepayCostService repayCostService;

	@Resource(name = "repayRecordService")
	private IRepayRecordService repayRecordService;

	@Resource(name = "CostPlanDetailService")
	private ICostPlanDetailService costPlanDetailService;

	@Resource(name="OrgAgentApplyCashService")
	private IOrgAgentApplyCashService orgAgentApplyCashService;

	@Resource(name = "OrgService")
	private IOrgService orgService;

	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;
	

	@Autowired
	private RedissonClient redissonClient;

	@Resource(name = "APPUserService")
	private IAPPUserService appUserService;


	private static final Logger log= LogManager.getLogger();
	/**
	 * 充值
	 * @param respCode
	 * @param transWater
	 */
	@Transactional
	public void czhd(String respCode, String RespDesc, TransWater transWater) {
		FeeInfo moneyFee=feeParamService.computeFeeInfo(transWater.getTransAmount(), FeeParamCode.RECHARGE,
				FeeParamCode.RECHARGE_COST);
		if("0000".equals(respCode)){
			//充值
			//获取用户手续费

			//计算金额
			BigDecimal userFee=moneyFee.getFee().add(moneyFee.getExternal());
			BigDecimal userSurplusMoney=transWater.getTransAmount().subtract(userFee);
			BigDecimal operAtorsAccountMoney=transWater.getTransAmount().subtract(moneyFee.getCost());

			//修改流水
			upTransls(transWater.getId(), "0000", "h-"+RespDesc);

			//获取时间及日期
			String data=StringUtil.getCurrentDateTime("yyyyMMdd");
			String time=StringUtil.getCurrentDateTime("HHmmss");

			//添加用户金额
			userAccountService.upUserAccount(userSurplusMoney, data, time, transWater.getUserId());

			//更新运营商信用卡账户资金
			operatorsAccoutService.doUpdateCredit(operAtorsAccountMoney);
		}else if(!"9997".equals(respCode)){//失败
			//修改流水
			upTransls(transWater.getId(), "Fail", "h-"+RespDesc);
		}
	}



	@Transactional
	public void txhd(String respCode, String RespDesc, TransWater transWater) {

		if("0000".equals(respCode)){
			//计算金额
			BigDecimal userFee=transWater.getFee().add(transWater.getExternal());
			BigDecimal userSurplusMoney=transWater.getTransAmount().subtract(userFee);
			if ("0".equals(transWater.getIsOrgRecah())) {
				//用户提现
				APPUserApplyCash apply =APPUserApplyCashService.findById(transWater.getPlanId());
				if (apply == null) {
					//未找到改用户的异常提现申请
					log.error("未找到改用户的异常提现申请:"+transWater.getId());
					return;
				}
				UserAccount userAccount = userAccountService.findByUserId(transWater.getUserId());
				if(userAccount.getRecashFreeze().compareTo(transWater.getTransAmount()) == 0 || userAccount.getRecashFreeze().compareTo(transWater.getTransAmount()) == 1){
					//扣除用户冻结金额
					userAccountService.doDuctionRecashFreezeMoney(userSurplusMoney.negate(), StringUtil.getCurrentDateTime("yyyyMMdd"), StringUtil.getCurrentDateTime("HHmmss"), transWater.getUserId());
					//修改提现申请为成功
					APPUserApplyCashService.renewStatus(apply.getId(), "1", StringUtil.getCurrentDateTime("yyyyMMdd"), StringUtil.getCurrentDateTime("HHmmss"));
					//更新流水信息
					upTransls(transWater.getId(), "0000", "h-"+RespDesc);

					return;
				}else {
					//余额不足
					log.error("余额不足:"+transWater.getId());
					return;
				}
			}else if("1".equals(transWater.getIsOrgRecah())){

				//代理商提现
				OrgAgentApplyCash orgApply = orgAgentApplyCashService.findApplyId(transWater.getPlanId()+"");//提現id放在planid里面
				if (orgApply == null) {
					//未找到改用户的异常提现申请
					log.error("未找到该代理商的异常提现申请:"+transWater.getId());
					return;
				}
				//判断金额是否足够
				Org orgAccount=orgService.findById(transWater.getOrgId());
				if(orgAccount.getCash().compareTo(transWater.getTransAmount()) == 0 || orgAccount.getCash().compareTo(transWater.getTransAmount()) == 1){

					//更改代理商冻结余额
					orgService.seriUpdateRecashFreeze(transWater.getOrgId(), userSurplusMoney.negate());

					//更新流水信息
					upTransls(transWater.getId(), "0000", "h-"+RespDesc);
					//执行成功
					return;
				}else {
					log.error("余额不足:"+transWater.getId());
					return;
				}
			}
		}else if(!"9997".equals(respCode)) {//失败
			if ("0".equals(transWater.getIsOrgRecah())) {
				//用户提现
				APPUserApplyCash apply =APPUserApplyCashService.findById(transWater.getPlanId());
				if (apply == null) {
					//未找到改用户的异常提现申请
					log.error("未找到改用户的异常提现申请:"+transWater.getId());
					return;
				}
				UserAccount userAccount = userAccountService.findByUserId(transWater.getUserId());
				if(userAccount.getRecashFreeze().compareTo(transWater.getTransAmount()) == 0 || userAccount.getRecashFreeze().compareTo(transWater.getTransAmount()) == 1){
					userAccountService.disposeUserMoney(transWater.getTransAmount(), StringUtil.getCurrentDateTime("yyyyMMdd"), StringUtil.getCurrentDateTime("HHmmss"), transWater.getUserId());
					//更新流水信息
					upTransls(transWater.getId(), "Fail", "h-"+RespDesc);
					//修改提现申请为失败
					APPUserApplyCashService.renewStatus(apply.getId(), "2", StringUtil.getCurrentDateTime("yyyyMMdd"), StringUtil.getCurrentDateTime("HHmmss"));
					//执行成功
					return;
				}else {
					log.error("余额不足:"+transWater.getId());
					return;//冻结金额不足
				}
			}else if("1".equals(transWater.getIsOrgRecah())){

				//代理商提现
				OrgAgentApplyCash orgApply =  orgAgentApplyCashService.findApplyId(transWater.getPlanId()+"");//提現id放在planid里面
				if (orgApply == null) {
				
					return;
				}
				orgAgentApplyCashService.updateStatus("3",transWater.getPlanId()+"");//失败状态
				Org orgAccount=orgService.findById(transWater.getOrgId());

				if(orgAccount.getCash().compareTo(transWater.getTransAmount()) == 0 || orgAccount.getCash().compareTo(transWater.getTransAmount()) == 1){
					 Org org=new Org();
				     org.setId(transWater.getOrgId());
					 org.setInAccount(transWater.getTransAmount().negate());
					 orgService.seriUpdateBalanceAndRecashFreeze(org);
						log.info("返还客户账户:"+transWater.getId()+" : "+transWater.getTransAmount().negate());
					//更新流水信息
					upTransls(transWater.getId(), "Fail", "h-"+RespDesc);

					return;
				}else {
					log.error("余额不足:"+transWater.getId());
					return;
				}


			}
		}
	}

	/**
	 * 还款
	 * @param respCode
	 * @param transWater
	 */
	@Transactional
	public void repay( String respCode,String RespDesc, TransWater transWater) {
		RepayPlanDetail repayPlanDetail = repayPlanDetailService.findRepayPlanById(transWater.getPlanId());
		if (repayPlanDetail == null){
			log.error("没有找到 repayPlan："+transWater.getPlanId());
			return;
		}
		//判断是否为未知状态
		if (!"u".equals(repayPlanDetail.getStatus())) {
			log.error("repayPlan已经被执行："+transWater.getPlanId());
			return;
		}

		if("0000".equals(respCode)){
			RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+repayPlanDetail.getRecordId());
			boolean res=false;
			try{
				res = fairLock.tryLock(10, 10, TimeUnit.SECONDS);
				int status = repayCostService.checkRepayPlanStatus(repayPlanDetail.getId());  //0|未全部执行完；1|全部成；2|失败(部分失败)
				if(status==0){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "1");
				}else if(status==1){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "3");
				}else if(status==2){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "4");
				}

				//更新流水信息
				upTransls(transWater.getId(), "0000", "h-"+RespDesc);
				//这里不减冻结余额是因为执行的时候就给他扣掉了

				if(repayRecordService.isAllSuccess(repayPlanDetail.getRecordId())){     //如果订单全部成功 就马上结算
					repayRecordService.unFreezePlanAmount(repayPlanDetail.getUserId(),repayPlanDetail.getRecordId(),repayPlanDetail.getCreditCardNo(),repayPlanDetail.getRepayMonth());

					APPUser user=appUserService.findById(transWater.getUserId());
					String cardNo=transWater.getCardNo();
					try {     //发送短信提醒
						YunPainSmsMsg.sendSuccess(cardNo.substring(cardNo.length()-4, cardNo.length()), StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm"), user.getUsername());
					} catch (Exception e) {
						e.printStackTrace();
						log.error("发送短信异常："+e.getMessage());
					}
				}

			}catch (Exception ex){
				ex.printStackTrace();

			}finally {
				if(res)fairLock.unlock();
			}

		}else if(!"9997".equals(respCode)){//失败
			repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "2");
			repayRecordService.doUpdateStatus(repayPlanDetail.getRecordId(),2);
			repayRecordService.reCalcAmountRepayRecord(transWater.getTransAmount().add(transWater.getFee()).add(transWater.getExternal()),transWater.getFee().add(transWater.getExternal()),repayPlanDetail.getRecordId() );//修改计划冻结金额
			//更新流水信息
			upTransls(transWater.getId(), "Fail", "h-"+RespDesc);
			try {     //发送短信提醒
				APPUser user=appUserService.findById(transWater.getUserId());
//				String cardNo=transWater.getCardNo();
				YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),RespDesc, user.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("发送短信异常："+e.getMessage());
			}

		}
		 return;
	}

	/**
	 * 还款消费
	 * @param respCode
	 * @param transWater
	 */
	@Transactional
	public void repayCost( String respCode,String RespDesc, TransWater transWater) {

		RepayPlanDetail repayPlanDetail = repayPlanDetailService.findRepayPlanById(transWater.getPlanId());
		if (repayPlanDetail == null){
			log.error("没有找到 repayPlan："+transWater.getPlanId());
			return;
		}

		//修改还款状态
		RepayCost cost = repayCostService.findRepayCostById(transWater.getCostId());
		if (cost == null) {
			//未找到这笔还款消费
			log.error("未找到这笔还款消费："+transWater.getCostId());
			return ;
		}

		if("0000".equals(respCode)){
			//修改还款消费状态为成功
			repayCostService.doUpdateRepayCostStatus(cost.getId(),"1");
			if(repayPlanDetail.getStatus().equals("1")) {//   已还款未消费才用得着給他改狀態  防止他本來是还款失败了 又给他改成消费失败
				int status = repayCostService.checkRepayPlanStatus(repayPlanDetail.getId());
				if(status==2){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "4");    //消费失败   todo
				}else if(status==1){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "3");
				}
			}
			//修改流水
			upTransls(transWater.getId(), "0000", "h-"+RespDesc);
			//修改金额
			//消费    要交易额 减去手续费
			repayRecordService.reCalcAmountRepayRecord(transWater.getTransAmount().subtract(transWater.getFee()).subtract(transWater.getExternal()),transWater.getFee().add(transWater.getExternal()),repayPlanDetail.getRecordId());//修改计划冻结金额

			if(repayRecordService.isAllSuccess(repayPlanDetail.getRecordId())){     //如果订单全部成功 就马上结算
				repayRecordService.unFreezePlanAmount(repayPlanDetail.getUserId(),repayPlanDetail.getRecordId(),repayPlanDetail.getCreditCardNo(),repayPlanDetail.getRepayMonth());

				APPUser user=appUserService.findById(transWater.getUserId());
				String cardNo=transWater.getCardNo();
				try {     //发送短信提醒
					YunPainSmsMsg.sendSuccess(cardNo.substring(cardNo.length()-4, cardNo.length()), StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm"), user.getUsername());
				} catch (Exception e) {
					e.printStackTrace();
					log.error("发送短信异常："+e.getMessage());
				}
			}
		}else if(!"9997".equals(respCode)) {//失败
			repayRecordService.doUpdateStatus(repayPlanDetail.getRecordId(),2);
			repayCostService.doUpdateRepayCostStatus(cost.getId(),"2");
			if(repayPlanDetail.getStatus().equals("1")) {//   已还款未消费才用得着給他改狀態  防止他本來是还款失败了 又给他改成消费失败
				repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "4");
			}

			upTransls(transWater.getId(), "Fail", "h-"+RespDesc);

			try {     //发送短信提醒
				APPUser user=appUserService.findById(transWater.getUserId());
				String cardNo=transWater.getCardNo();
				YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),RespDesc, user.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("发送短信异常："+e.getMessage());
			}
		}
	}

	public void cost(String respCode,String RespDesc, TransWater transWater) {
		if("0000".equals(respCode)){
			//获取用户手续费
			FeeInfo moneyFee=feeParamService.computeFeeInfo(transWater.getTransAmount(), "purchase", "purchase_cost");
			//计算金额
			BigDecimal userFee=moneyFee.getFee().add(moneyFee.getExternal());
			BigDecimal userSurplusMoney=transWater.getTransAmount().subtract(userFee);
			BigDecimal operAtorsAccountMoney=transWater.getTransAmount().subtract(moneyFee.getCost());

			//获取时间及日期
			String data=StringUtil.getCurrentDateTime("yyyyMMdd");
			String time=StringUtil.getCurrentDateTime("HHmmss");

			//修改流水
			upTransls(transWater.getId(), "0000", "h-"+RespDesc, moneyFee.getFee(), moneyFee.getExternal());

			//修改纯消费计划
			CostPlanDetail cpd = new CostPlanDetail();
			cpd.setId(transWater.getCostId());
			cpd.setStatus("1");
			costPlanDetailService.updateStatus(cpd);
			//添加用户金额
			userAccountService.upUserAccount(userSurplusMoney, data, time, transWater.getUserId());
			//增加信用卡金额
			operatorsAccoutService.doUpdateCredit(operAtorsAccountMoney);
		}else if(!"9997".equals(respCode)) {//失败
			//获取用户手续费
			FeeInfo moneyFee=feeParamService.computeFeeInfo(transWater.getTransAmount(), "purchase", "purchase_cost");
			//计算金额
			//修改纯消费计划
			CostPlanDetail cpd = new CostPlanDetail();
			cpd.setId(transWater.getCostId());
			cpd.setStatus("-1");
			costPlanDetailService.updateStatus(cpd);
			//修改流水
			upTransls(transWater.getId(), "Fail", "h-"+RespDesc, moneyFee.getFee(), moneyFee.getExternal());
		}
	}

	public void costNew(String respCode,String RespDesc, TransWater transWater) {
		if("0000".equals(respCode)){
/*			//获取用户手续费
			FeeInfo moneyFee=feeParamService.computeFeeInfo(transWater.getTransAmount(), "purchase", "purchase_cost");
			//计算金额
			BigDecimal userFee=moneyFee.getFee().add(moneyFee.getExternal());
			BigDecimal userSurplusMoney=transWater.getTransAmount().subtract(userFee);
			BigDecimal operAtorsAccountMoney=transWater.getTransAmount().subtract(moneyFee.getCost());*/

			//获取时间及日期
			String data=StringUtil.getCurrentDateTime("yyyyMMdd");
			String time=StringUtil.getCurrentDateTime("HHmmss");

			//修改流水
			upTransls(transWater.getId(), "0000", "h-"+RespDesc);

			//修改纯消费计划
			CostPlanDetail cpd = new CostPlanDetail();
			cpd.setId(transWater.getCostId());
			cpd.setStatus("1");
			costPlanDetailService.updateStatus(cpd);
			//添加用户金额
			userAccountService.upUserAccount(transWater.getTransAmount(), data, time, transWater.getUserId());
			//增加信用卡金额
//			operatorsAccoutService.doUpdateCredit(operAtorsAccountMoney);
		}else if(!"9997".equals(respCode)) {//失败
/*			//获取用户手续费
			FeeInfo moneyFee=feeParamService.computeFeeInfo(transWater.getTransAmount(), "purchase", "purchase_cost");
			//计算金额
			//修改纯消费计划
			CostPlanDetail cpd = new CostPlanDetail();
			cpd.setId(transWater.getCostId());
			cpd.setStatus("-1");
			costPlanDetailService.updateStatus(cpd);*/
			//修改流水
			upTransls(transWater.getId(), "Fail", "h-"+RespDesc);
		}
	}

	
	public void jwpay(String respCode,String RespDesc, TransWater transWater) {
		if("0000".equals(respCode)){
 
 

			//修改流水
			upTransls(transWater.getId(), "0000", "h-"+RespDesc,transWater.getTransAmount()+"",transWater.getTransTime());
 
		}else {//失败
 
			//修改流水
			upTransls(transWater.getId(), "Fail", "h-"+RespDesc,transWater.getTransAmount()+"",transWater.getTransTime());
		}
	}
	public Integer upTransls(Long translsId, String code, String msg, BigDecimal fee, BigDecimal external) {//充值失败的时候 是续费没写到表里面，所以这里需要写上
		String sql= "update t_s_trans_water set RESP_CODE=:respCode,RESP_RES=:respRes,FEE=:fee,EXTERNAL=:external where id=:id";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", translsId);
		map.put("respCode", code);
		map.put("respRes", msg);
		map.put("fee", fee);
		map.put("external", external);
		return transWaterDao.update(sql, map);
	}
	
	public Integer upTransls(Long translsId, String code, String msg, String PAY_AMOUNT,String PAYCH_TIME) {//境外交易
		String sql= "update t_s_trans_water set RESP_CODE=:respCode,RESP_RES=:respRes,TRANS_AMOUNT=:trans_amount where id=:id";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", translsId);
		map.put("respCode", code);
		map.put("respRes", msg);
		map.put("trans_amount", PAY_AMOUNT);
 
		return transWaterDao.update(sql, map);
	}

	public Integer upTransls(Long translsId, String code, String msg) {
		String sql= "update t_s_trans_water set RESP_CODE=:respCode,RESP_RES=:respRes where id=:id";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", translsId);
		map.put("respCode", code);
		map.put("respRes", msg);

		return transWaterDao.update(sql, map);
	}



	public void backCallProxyPay(String transId,String code,String msg) {
		try{
			TransWater transWater =transWaterService.findById(Long.parseLong(transId));
			if (!"9997".equals(transWater.getRespCode())){
				log.info("该订单已处理："+transId);
				return;
			}
			String transType = transWater.getTrans_type();
			//0:纯消费，1：还款消费，2：快速还款，3：普通还款,4:充值，5,：提现
			if("4".equals(transType)){
				czhd(code,msg,transWater);
			}else if("5".equals(transType)){//提现     有区代理商提现，用户提现
				txhd(code,msg,transWater);
			}else if("2".equals(transType)){
				repay(code,msg,transWater);
			}else if("1".equals(transType)){
				repayCost(code,msg,transWater);
			}else if("0".equals(transType)){
				cost(code,msg,transWater);
			}
		}catch (Exception e){
			e.printStackTrace();
			log.error(Tool.getTrace(e));
		}
		return;
	}



	@Override
	public Boolean repayDate(String respCode, String RespDesc, TransWater transWater) {
		RepayPlanDetail repayPlanDetail = repayPlanDetailService.findRepayPlanById(transWater.getPlanId());
		if (repayPlanDetail == null){
			log.error("没有找到 repayPlan："+transWater.getPlanId());
			return false;
		}
		//判断是否为未知状态
		if (!"u".equals(repayPlanDetail.getStatus())) {
			log.error("repayPlan已经被执行："+transWater.getPlanId());
			return false;
		}

		if("0000".equals(respCode)){
			RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+repayPlanDetail.getRecordId());
			boolean res=false;
			try{
				res = fairLock.tryLock(10, 10, TimeUnit.SECONDS);
				/*int status = repayCostService.checkRepayPlanStatus(repayPlanDetail.getId());  //0|未全部执行完；1|全部成；2|失败(部分失败)
				if(status==0){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "1");
				}else if(status==1){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "3");
				}else if(status==2){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "4");
				}*/
				repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "5"); //更新为还款成功

				//更新流水信息
				upTransls(transWater.getId(), "0000", "h-"+RespDesc);
				//repayRecordService.reCalcAmountRepayRecord(transWater.getTransAmount().add(transWater.getFee()).add(transWater.getExternal()),transWater.getFee().add(transWater.getExternal()),repayPlanDetail.getRecordId() );//修改计划冻结金额
				if(repayPlanDetailService.isLastRepay(repayPlanDetail.getRecordId(),repayPlanDetail.getId())) { //判断是否是最后一笔还款   把还款计划流水更新有成功
					repayRecordService.doUpdateStatus(repayPlanDetail.getRecordId(),1); //还款计划修改成功还款成功

				}
				//这里不减冻结余额是因为执行的时候就给他扣掉了
				/*if(repayRecordService.isAllSuccess(repayPlanDetail.getRecordId())){     //如果订单全部成功 就马上结算
					repayRecordService.unFreezePlanAmount(repayPlanDetail.getUserId(),repayPlanDetail.getRecordId(),repayPlanDetail.getCreditCardNo(),repayPlanDetail.getRepayMonth());

					APPUser user=appUserService.findById(transWater.getUserId());
					String cardNo=transWater.getCardNo();
					try {     //发送短信提醒
						YunPainSmsMsg.sendSuccess(cardNo.substring(cardNo.length()-4, cardNo.length()), StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm"), user.getUsername());
					} catch (Exception e) {
						e.printStackTrace();
						log.error("发送短信异常："+e.getMessage());
					}
				}*/
              return true;
			}catch (Exception ex){
				ex.printStackTrace();
				 return false;
			}finally {
				if(res)fairLock.unlock();
			}

		}else if(!"9997".equals(respCode)){//失败
			repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "2");
			repayRecordService.doUpdateStatus(repayPlanDetail.getRecordId(),2);
			repayRecordService.reCalcAmountRepayRecord(transWater.getTransAmount().add(transWater.getFee()).add(transWater.getExternal()),transWater.getFee().add(transWater.getExternal()),repayPlanDetail.getRecordId() );//修改计划冻结金额
			//更新流水信息
			upTransls(transWater.getId(), "Fail", "h-"+RespDesc);
			try {     //发送短信提醒
				APPUser user=appUserService.findById(transWater.getUserId());
//				String cardNo=transWater.getCardNo();
				YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),RespDesc, user.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("发送短信异常："+e.getMessage());
			}
			return false;
		}
		 return false;
	}

	public void repayDateAgain(String respCode, String RespDesc, TransWater transWater) {
		RepayPlanDetail repayPlanDetail = repayPlanDetailService.findRepayPlanById(transWater.getPlanId());
		if (repayPlanDetail == null){
			log.error("没有找到 repayPlan："+transWater.getPlanId());
			return;
		}
		//判断是否为未知状态
		if (!"u".equals(repayPlanDetail.getStatus())) {
			log.error("repayPlan已经被执行："+transWater.getPlanId());
			return;
		}

		if("0000".equals(respCode)){
			RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+repayPlanDetail.getRecordId());
			boolean res=false;
			try{
				res = fairLock.tryLock(10, 10, TimeUnit.SECONDS);
				/*int status = repayCostService.checkRepayPlanStatus(repayPlanDetail.getId());  //0|未全部执行完；1|全部成；2|失败(部分失败)
				if(status==0){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "1");
				}else if(status==1){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "3");
				}else if(status==2){
					repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "4");
				}*/
				repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "5"); //更新为还款成功

				//更新流水信息
				upTransls(transWater.getId(), "0000", "h-"+RespDesc);
				//repayRecordService.reCalcAmountRepayRecord(transWater.getTransAmount().add(transWater.getFee()).add(transWater.getExternal()),transWater.getFee().add(transWater.getExternal()),repayPlanDetail.getRecordId() );//修改计划冻结金额
				if(repayPlanDetailService.isLastRepay(repayPlanDetail.getRecordId(),repayPlanDetail.getId())) { //判断是否是最后一笔还款   把还款计划流水更新有成功
					repayRecordService.doUpdateStatus(repayPlanDetail.getRecordId(),1); //还款计划修改成功还款成功

				}
				//这里不减冻结余额是因为执行的时候就给他扣掉了
				/*if(repayRecordService.isAllSuccess(repayPlanDetail.getRecordId())){     //如果订单全部成功 就马上结算
					repayRecordService.unFreezePlanAmount(repayPlanDetail.getUserId(),repayPlanDetail.getRecordId(),repayPlanDetail.getCreditCardNo(),repayPlanDetail.getRepayMonth());

					APPUser user=appUserService.findById(transWater.getUserId());
					String cardNo=transWater.getCardNo();
					try {     //发送短信提醒
						YunPainSmsMsg.sendSuccess(cardNo.substring(cardNo.length()-4, cardNo.length()), StringUtil.getCurrentDateTime("yyyy-MM-dd HH:mm"), user.getUsername());
					} catch (Exception e) {
						e.printStackTrace();
						log.error("发送短信异常："+e.getMessage());
					}
				}*/

			}catch (Exception ex){
				ex.printStackTrace();

			}finally {
				if(res)fairLock.unlock();
			}

		}else if(!"9997".equals(respCode)){//失败
			repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "2");
			repayRecordService.doUpdateStatus(repayPlanDetail.getRecordId(),2);
			repayRecordService.reCalcAmountRepayRecord(transWater.getTransAmount().add(transWater.getFee()).add(transWater.getExternal()),transWater.getFee().add(transWater.getExternal()),repayPlanDetail.getRecordId() );//修改计划冻结金额
			//更新流水信息
			upTransls(transWater.getId(), "Fail", "h-"+RespDesc);
			try {     //发送短信提醒
				APPUser user=appUserService.findById(transWater.getUserId());
//				String cardNo=transWater.getCardNo();
				YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),RespDesc, user.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("发送短信异常："+e.getMessage());
			}

		}
		 return;
	}

	@Override
	public Boolean repayCostDate(String respCode, String RespDesc, TransWater transWater) {

		RepayPlanDetail repayPlanDetail = repayPlanDetailService.findRepayPlanById(transWater.getPlanId());
		if (repayPlanDetail == null){
			log.error("没有找到 repayPlan："+transWater.getPlanId());
			return false;
		}

		//修改还款状态
		RepayCost cost = repayCostService.findRepayCostById(transWater.getCostId());
		if (cost == null) {
			//未找到这笔还款消费
			log.error("未找到这笔还款消费："+transWater.getCostId());
			return false;
		}

		if("0000".equals(respCode)){
			//修改还款消费状态为成功
			repayCostService.doUpdateRepayCostStatus(cost.getId(),"1");
			
			int status = repayCostService.checkRepayPlanStatus(cost.getRepay_plan_id());
			if(status==2){
				repayPlanDetail.setStatus("4");
				repayPlanDetailService.doUpdateStatus(repayPlanDetail);
			}else if(status==1){
				repayPlanDetail.setStatus("3");
				repayPlanDetailService.doUpdateStatus(repayPlanDetail); //修改成已消费未还款
			}
			//修改流水
			upTransls(transWater.getId(), "0000", "h-"+RespDesc);
			//消费    要交易额 减去手续费
			repayRecordService.reCalcAmountRepayRecord(transWater.getTransAmount().subtract(transWater.getFee()).subtract(transWater.getExternal()),transWater.getFee().add(transWater.getExternal()),repayPlanDetail.getRecordId());//修改计划冻结金额
          return true;
		}else if(!"9997".equals(respCode)) {//失败
			repayRecordService.doUpdateStatus(repayPlanDetail.getRecordId(),2);
			repayCostService.doUpdateRepayCostStatus(cost.getId(),"2");
			if(repayPlanDetail.getStatus().equals("1")) {//   已还款未消费才用得着給他改狀態  防止他本來是还款失败了 又给他改成消费失败
				repayPlanDetailService.doUpdateStatus(repayPlanDetail.getId(), "4");
			}

			upTransls(transWater.getId(), "Fail", "h-"+RespDesc);

			try {     //发送短信提醒
				APPUser user=appUserService.findById(transWater.getUserId());
//				String cardNo=transWater.getCardNo();
				YunPainSmsMsg.sendNotice("***"+user.getUsername().substring(user.getUsername().length()-4, user.getUsername().length()),RespDesc, user.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("发送短信异常："+e.getMessage());
			}
			return false;
		}
		return false;
	}
}
