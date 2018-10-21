package com.dhk.service.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.dhk.dao.IRepayPlanDetailDao;
import com.dhk.dao.IUnfreezeLsDao;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.entity.UnfreezeLs;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.dhk.dao.IRepayRecordDao;
import com.dhk.entity.RepayRecord;
import com.dhk.entity.UserAccount;
import com.dhk.service.IRepayRecordService;
import com.dhk.service.IUserAccountService;
import com.dhk.utils.DateTimeUtil;
import com.sunnada.kernel.sql.SQLConf;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("repayRecordService")
public class RepayRecordService implements IRepayRecordService {
	@Resource(name = "repayRecordDao") 
	private IRepayRecordDao repayRecordDao;
	@Resource(name = "RepayPlanDetailDao")
	private IRepayPlanDetailDao  repayPlanDetailDao;

	@Resource(name = "UserAccountService")
	IUserAccountService userAccountService;

	@Resource(name = "unfreezeLsDao")
	private IUnfreezeLsDao unfreezeLsDao;
	Logger logger = Logger.getLogger(this.getClass());

	public boolean reCalcAmountRepayRecord(BigDecimal bd,BigDecimal fee,long recordId) {
		String sql="update t_n_repay_record set amount=amount+:inAccount, service_charge_residue=service_charge_residue-:fee where id=:id";
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("inAccount", bd);
		map.put("fee", fee);
		map.put("id", recordId);

		try {
			if(repayRecordDao.update(sql,map)>0){
				return true;
			}else{
				logger.error("余额加点减错 bd："+bd+",recordId:"+recordId);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public RepayRecord findByCardNoAndRepayMonth(String cardNo, String repayMonth,Long userId) {
		String sql=SQLConf.getSql("repayrecord", "findByCardNoAndRepayMonth");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("repay_month", repayMonth);
		map.put("card_no", cardNo);
		map.put("userId", userId);
		try {
			List<RepayRecord> rps = repayRecordDao.find(sql, map);
			if (rps!=null && !rps.isEmpty() && rps.size()>0){
				return rps.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}




	public RepayRecord findByCardNo(Long userId,String cardNo,String repay_month) {
		String sql=SQLConf.getSql("repayrecord", "findByCarNo");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("card_no", cardNo);
		map.put("repay_month", repay_month);
		map.put("userId", userId);
		RepayRecord rp = null;
		List<RepayRecord> rps = repayRecordDao.find(sql, map);
		if (rps!=null && !rps.isEmpty() && rps.size()>0){
			rp = rps.get(0);
		}
		return rp;

	}

	/**
	 * 查询未解冻的记录
	 * @return
	 * @throws Exception
	 */
	public List<RepayRecord> findUnFreezeRecord() throws Exception {
		String sql = SQLConf.getSql("repayrecord", "findUnFreezeRecord");
		return repayRecordDao.find(sql, null);
	}

	/**
	 * 给计划添加订单号
	 * @param orderNo 订单号
	 * @return
	 * @throws Exception
	 * @date 2017年7月14日
	 */
	public int updateRecordOrderNo(String orderNo, Long id) throws Exception {
		String sql = SQLConf.getSql("repayrecord", "updateRecordOrderNo");
        Map<String,Object> map=new HashMap<String, Object>();
		map.put("orderNo", orderNo);
		map.put("id", id);
		int i= repayRecordDao.update(sql, map);
		return i;
	}

	/**
	 * 给根据订单号查询还款是否全部成功
	 * @param orderNo 订单号
	 * @return
	 * @throws Exception
	 * @date 2017年7月14日
	 */
	public boolean repayRecordIsSuccess(String recordId){
		Map paramMap = new HashMap(){};
		paramMap.put("record_id",recordId);
		List<RepayPlanDetail> repayPlanDetailList = repayPlanDetailDao.find(paramMap);
		if(repayPlanDetailList==null||repayPlanDetailList.size()==0)return false;
		for(int i=0;i<repayPlanDetailList.size();i++){
			RepayPlanDetail repayPlanDetail =  repayPlanDetailList.get(i);
			 if(!"3".equals(repayPlanDetail.getStatus())){
				return false;
			 }
		}
		return true;
	}

	@Transactional
	public void unFreezePlanAmount(Long userId,Long recordId,String cardNo,String repayMonth){
		 //20170921 计划解冻改成用户手动执行

		if(hasUndefined(recordId)){   //如果还有未知错误的订单  就不给他结算。等待手工处理
			return;
		}
		try{
			//取出冻结金额

			RepayRecord repayRecord = repayRecordDao.findBy("select * from t_n_repay_record where id="+recordId,null);
			BigDecimal amount = repayRecord.getAmount().setScale(2, RoundingMode.HALF_UP);

			UserAccount userAccount=userAccountService.findByUserId(userId);
			BigDecimal preAmount = userAccount.getCurBalance();
			BigDecimal curBalance = preAmount.add(amount).setScale(2, RoundingMode.HALF_UP);

			String  nowDate = DateTimeUtil.getNowDateTime("yyyyMMdd");
			String  nowTime = DateTimeUtil.getNowDateTime("HHmmss");

			UnfreezeLs ls = new UnfreezeLs();  //新增解冻流水
			ls.setAmount(amount);
			ls.setCardNo(cardNo);
			ls.setCurAmount(curBalance);
			ls.setPreAmount(preAmount);
			ls.setRepayMonth(repayMonth);
			ls.setReplanRecordid(recordId);
			ls.setTransDate(nowDate);
			ls.setTransTime(nowTime);
			ls.setUserId(userId);
			String sql = SQLConf.getSql("t_s_unfreeze_ls", "insert");
			unfreezeLsDao.insert(sql, ls);

			userAccount.setUserId(userId);
			userAccount.setInAccount(amount.setScale(2, RoundingMode.HALF_UP));
			userAccount.setUpdateDate(DateTimeUtil.getNowDateTime("yyyyMMdd"));
			userAccount.setUpdateTime(DateTimeUtil.getNowDateTime("HHmmss"));
			userAccountService.seriUpdateBalance(userAccount);
			//清0
			sql = "update t_n_repay_record set amount=0.00,isUnFreeze='T' where id=:id";
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("id", repayRecord.getId());
			repayRecordDao.update(sql,map1);
		}catch (Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}



	public int doUpdateStatus(Long id,int status){
		String sql = SQLConf.getSql("repayrecord", "doUpdateStatus");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		int i= repayRecordDao.update(sql, map);
		return i;
	}

	public void updateAllstatus(Long recordId,String recordStatus,Long repayPlanId,String repayPlanStatus,Long repayCostId,String repayCostStatus){
		Map<String, Object> paramMap=new HashMap<String, Object>();
		if(!StringUtils.isEmpty(repayCostId)&&!StringUtils.isEmpty(repayCostStatus)){
			paramMap.put("id", repayCostId);
			paramMap.put("status", repayCostStatus);
			repayRecordDao.update("update t_n_repay_cost set status=:status where id=:id", paramMap);
		}
		if(!StringUtils.isEmpty(repayPlanId)&&!StringUtils.isEmpty(repayPlanStatus)){
			paramMap.put("id", repayPlanId);
			paramMap.put("status", repayPlanStatus);
			repayRecordDao.update("update t_n_repay_plan set status=:status where id=:id", paramMap);
		}
		if(!StringUtils.isEmpty(recordId)&&!StringUtils.isEmpty(recordStatus)){
			paramMap.put("id", recordId);
			paramMap.put("status", recordStatus);
			repayRecordDao.update("update t_n_repay_record set status = :status where id = :id", paramMap);
		}
	}


	/**
	 * 查找是否有未知错误的订单 如果有就不允许解冻
	 * @param repayRecordId
	 * @return
	 */
	private boolean hasUndefined(Long repayRecordId){
		int count= repayRecordDao.count2Integer("select count(*) from t_n_repay_plan where status='u' and record_id="+repayRecordId,null);
		if(count>0){
			return true;
		}
		count= repayRecordDao.count2Integer("select count(*) from t_n_repay_plan a left join t_n_repay_cost b on a.id=b.repay_plan_id where b.status='u' and a.record_id="+repayRecordId,null);
		if(count>0){
			return true;
		}
		return false;
	}


	/**
	 * 是否全部成功   如果全部成功 手工处理的时候要马上结算
	 * @param repayRecordId
	 * @return
	 */
	public boolean isAllSuccess(Long repayRecordId){
		int count= repayRecordDao.count2Integer("select count(*) from t_n_repay_plan where status!=3 and record_id="+repayRecordId,null);
		if(count>0){
			return false;
		}
		count= repayRecordDao.count2Integer("select count(*) from t_n_repay_plan a left join t_n_repay_cost b on a.id=b.repay_plan_id where b.status!=1 and a.record_id="+repayRecordId,null);
		if(count>0){
			return false;
		}
		return true;
	}

}

