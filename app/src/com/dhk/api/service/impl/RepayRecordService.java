package com.dhk.api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.dhk.api.controller.JsonCreater;
import com.dhk.api.dao.IRepayRecordDao;
import com.dhk.api.service.ITokenService;
import com.dhk.api.entity.RepayRecord;
import com.dhk.api.service.IAccountService;
import com.dhk.api.service.IRepayRecordService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import static com.dhk.api.controller.JsonCreater.getJsonOk;

@Service("RepayRecordService")
public class RepayRecordService implements IRepayRecordService {

	@Resource(name = "RepayRecordDao")
	private IRepayRecordDao repayRecordDao;
	
	@Resource(name = "AccountService")
	private IAccountService accountService;

	@Resource(name = "TokenService")
	private ITokenService tokenService;
	
	@Autowired
	private RedissonClient redissonClient;

	@Resource(
			name = "jdbcTemplate"
	)
	private JdbcTemplate jdbcTemplate;

	/**
	 *
	 * @param user_id
	 * @param repay_month
	 * @param count
	 * @param card_no
	 * @param totalAmount 支付总金额
	 * @param fee          手续费
	 * @param startDate  开始日期
	 * @param endDate     结束日期
	 * @param orderNo
	 * @return
	 */
	public Long doReRepayRecode(String user_id, String repay_month,
			String count, String card_no, String totalAmount,String fee,String startDate,String endDate,String repay_money,String orderNo) {
		String sql = "insert into t_n_repay_record(repay_month,repay_count,card_no,user_id,amount,isUnFreeze,orderNo,status,create_dt,service_charge,service_charge_residue,caution_money,repay_money,plan_start,plan_end) " +
				" values(:repay_month,:repay_count,:card_no,:user_id,:amount,:isUnFreeze,:orderNo,0,NOW(),:service_charge,:service_charge_residue,:caution_money,:repay_money,:plan_start,:plan_end)";
		RepayRecord repayRecord = new RepayRecord();
		repayRecord.setAmount(totalAmount);
		repayRecord.setCard_no(card_no);
		repayRecord.setUser_id(user_id);
		repayRecord.setRepay_month(repay_month);
		repayRecord.setRepay_count(count);
		repayRecord.setOrderNo(orderNo);
		repayRecord.setIsUnFreeze("F");
		repayRecord.setService_charge(fee);
		repayRecord.setService_charge_residue(fee);
		repayRecord.setCaution_money((Double.parseDouble(totalAmount)-Double.parseDouble(fee))+"");
		repayRecord.setPlan_start(startDate);
		repayRecord.setPlan_end(endDate);
		repayRecord.setRepay_money(repay_money);
		return repayRecordDao.insert(sql, repayRecord);
	}
	


	@Override
	public String getUserRecodeValue(String userId) {
		String sql = "select ifnull(sum(amount),0) amount from t_n_repay_record where user_id=:user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		List<RepayRecord> li = repayRecordDao.find(sql, map);
		return li.get(0).getAmount();
	}

	@Transactional
	public JSONObject unfreeze(String userId, String cardNo, String token){
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			//取出冻结金额
			String sql = "select * from t_n_repay_record where user_id=:user_id and card_no=:card_no and isUnFreeze='F'";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", userId);
			map.put("card_no", cardNo);
			RepayRecord repayRecord = null;
			List<RepayRecord> repayRecordList = repayRecordDao.find(sql,map);
			if(repayRecordList!=null&&repayRecordList.size()>0){
				repayRecord=repayRecordList.get(0);
			}else{
				return JsonCreater.getJsonError(1001, "没有可结束的计划");
			}
			RLock fairLock = redissonClient.getFairLock("repayRecordLock_"+repayRecord.getId());
			boolean res =false;
			try{
				 res = fairLock.tryLock(0, 10, TimeUnit.SECONDS);
				 if(res){
					 boolean hasUndefined = hasUndefined(repayRecord.getId());
					 if(hasUndefined){
						 return JsonCreater.getJsonError(1001, "计划中含有未结算订单，请等待处理后再结束计划");
					 }
					 boolean hasCostSucess = hasCostSucess(repayRecord.getId());
					 if(hasCostSucess){
						 return JsonCreater.getJsonError(1002, "计划中含有已消费未还款订单，请等待处理后再结束计划");
					 }
					 BigDecimal amount = new BigDecimal(repayRecord.getAmount()).setScale(2, RoundingMode.HALF_UP);
					 Long id = repayRecord.getId();
					 //余额增加
					 accountService.addBalance(userId, amount.doubleValue());
					 //清0
					 sql = "update t_n_repay_record set amount=0.00,isUnFreeze='T',status=3 where id=:id";
					 Map<String, Object> map1 = new HashMap<String, Object>();
					 map1.put("id", id);
					 repayRecordDao.update(sql,map1);
					 return JsonCreater.getJsonOk();
				 }else{
				 	  return JsonCreater.getJsonError(1001, "当前计划正在执行,请稍后再试");
				 }
			} catch (InterruptedException e) {
				e.printStackTrace();
				return JsonCreater.getJsonError(1001, e.getMessage());
			} finally {
				if(res)fairLock.unlock();
			}
		}
		return JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
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
	 * 查找是否有已消费未还款计划
	 * @param repayRecordId
	 * @return
	 */
	private boolean hasCostSucess(Long repayRecordId){
		int count= repayRecordDao.count2Integer("select count(*) from t_n_repay_plan where status='3' and record_id="+repayRecordId,null);
		if(count>0){
			 return true;
		}
		return false;
	}


	public void updateStatus(String status,String id){
		String sql = "update t_n_repay_record set status="+status+" where id="+id;
		repayRecordDao.update(sql,null);
	}


	/**
	 * 查询用户已还多少钱 未还多少钱
	 * @param userId
	 * @return
	 */
	public RepayRecord queryRepayResult(String userId,String cardNo){
		String sql="SELECT t.*, ( SELECT IFNULL(sum(repay_amount), 0) FROM t_n_repay_plan WHERE STATUS NOT IN ('0', '2', 'u') AND record_id = t.id ) hasRepay, ( SELECT IFNULL(sum(repay_amount), 0) " +
				"FROM t_n_repay_plan WHERE STATUS IN ('0', '2', 'u') AND record_id = t.id ) unRepay FROM t_n_repay_record t " +
				"WHERE user_id = :user_id AND  card_no=:card_no AND isUnFreeze='F' ORDER BY id DESC LIMIT 1";


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("card_no", cardNo);
		List<RepayRecord> list = repayRecordDao.find(sql,map);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return new RepayRecord();
		}
	}
	public List<RepayRecord> getRepayRecord(String userId, String cardNo){
		String sql="select " +
					" (select BANK_NAME from t_s_user_creditcard c where c.CARD_STATUS=1 and  c.CARD_NO=t.card_no ) bank_name," +
					" (select realname from t_s_user s where s.id=t.user_id) realname," +
					" t.* " +
					" from t_n_repay_record t WHERE t.user_id = :user_id AND  t.card_no=:card_no  ORDER BY id DESC ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("card_no", cardNo);
		List<RepayRecord> list = repayRecordDao.find(sql,map);
		return list;
	}


	/**
	 * 查询用户已还多少钱 未还多少钱
	 * @param userId
	 * @return
	 */
	public String hasRepayCurMonth(String userId,String cardNo){
		SimpleDateFormat format = new SimpleDateFormat("yyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String firstday = format.format(calendar.getTime());

		String sql="select  IFNULL(sum(a.repay_amount), 0) " +
				"  from `t_n_repay_plan` a" +
				"  left join `t_n_repay_record` b on a.`RECORD_ID`= b.`id`" +
				" where b.`user_id`=  "+userId+" " +
				"   and b.`card_no`= '"+cardNo+"' and a.`REPAY_DAY` >="+firstday+"  and a.STATUS NOT IN ('0', '2', 'u') ";

		return jdbcTemplate.queryForObject(sql,String.class);
//	   return "500";
	}

	/**
	 * 是否还有冻结余额的订单
	 * @return
	 */
	public boolean hasFreeze(String userId,String cardNo){
		String sql = "select * from t_n_repay_record where user_id=:user_id and card_no=:card_no  and isUnFreeze='F' and amount>0";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("card_no", cardNo);
		RepayRecord repayRecord = null;
		List<RepayRecord> repayRecordList = repayRecordDao.find(sql,map);
		if(repayRecordList!=null&&repayRecordList.size()>0){
			return true;
		}else{
			return false;
		}
	}



	@Override
	public boolean hasExecRepayRecord(String userId, String cardNo) {
		String sql = "select * from t_n_repay_record where user_id=:user_id and card_no=:card_no  and status='0' ";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("card_no", cardNo);
		RepayRecord repayRecord = null;
		List<RepayRecord> repayRecordList = repayRecordDao.find(sql,map);
		if(repayRecordList!=null&&repayRecordList.size()>0){
			return true;
		}else{
			return false;
		}
	}

}
