package com.dhk.api.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.impl.PlanTool;
import com.dhk.api.dto.QResponse;
import com.dhk.api.entity.RepayCost;
import com.dhk.api.service.IRepayCostService;
import com.dhk.api.service.ISystemParamService;
import com.dhk.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhk.api.dao.IRepayCostDao;

@Service("RepayCostService")
public class RepayCostService implements IRepayCostService {

	@Resource(name = "RepayCostDao")
	private IRepayCostDao repayCostDao;

	@Resource(name = "systemParamService")
	private ISystemParamService systemParamService;


	@Autowired
	RedisUtils redisUtils;

	private String insertSql = "insert into t_n_repay_cost(repay_plan_id,cost_amount,status,exec_time) values(:repay_plan_id,:cost_amount,'0',:exec_time)";
	
	private static Map<Integer,Double> moneyMap = new HashMap<Integer,Double>();
	
	static{
		moneyMap.put(1, 0.3);
		moneyMap.put(2, 0.4);
	}
	private static double[] calcAmount(double amount,double amount1){
		BigDecimal amt = new BigDecimal(amount);
		BigDecimal amt1 = new BigDecimal(amount1);
		BigDecimal temp = amt.multiply(amt1).setScale(0,BigDecimal.ROUND_UP);//.setScale(0, BigDecimal.ROUND_UP);
		double result1 = temp.doubleValue();
		double result2 = amount - result1;
		double[] result = new double[2];
		result[0] = result1;
		result[1] = result2;
		return result;
	}

	/**
	 *
	 * @param repay_id
	 * @param repayExecTine   还款任务执行的时间  生成消费时间的时候需要考虑到这个时间（推迟10分钟）
	 * @param nextRepayExecTine  当天下一笔还款任务的时间 生成消费时间的时候需要考虑到这个时间（提前10分钟）  如果没有下一笔， 这个时间就默认成24:00:00
	 * @param amount   还款金额  超过 api_repay_slip_money的金额就要拆分成2比
	 * @throws Exception
	 */
	@Override
	public void insertRepayCost(long repay_id, String repayExecTine,String nextRepayExecTine,double amount,String repayDate) throws Exception{
	    long beginTimeStamp = PlanTool.getTimeStamp(repayExecTine,"HH:mm:ss",600000);   //1000*60*10  10分钟
	    long endTimeStamp;
	    if(nextRepayExecTine==null){
		   endTimeStamp =  PlanTool.getTimeStamp("22:50:00","HH:mm:ss",0);
	    }else{
		   endTimeStamp =  PlanTool.getTimeStamp(nextRepayExecTine,"HH:mm:ss",-600000);  //1000*60*10  10分钟
	    }

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		RepayCost cost = new RepayCost();
		cost.setRepay_plan_id(repay_id);
		//拆分金额
		Integer caiMon = Integer.parseInt(systemParamService.findParam("api_repay_slip_money"));
		if (amount > caiMon) {  //要拆分成2笔    2笔之间要间隔半小时
			 //获取开始时间 和结束时间的中间值
			long middle = beginTimeStamp+(endTimeStamp- beginTimeStamp)/2;  //比如 beginTimeStamp=11：00：00   endTimeStamp=14：00：00    middle就等于12：30
			//前后加上15分钟，就是间隔半小时了
			String firstExecTime = sdf.format(ThreadLocalRandom.current().nextLong(beginTimeStamp,middle-900000));      //1000*60*15
			String secondExecTime = sdf.format(ThreadLocalRandom.current().nextLong(middle+900000,endTimeStamp));      //1000*60*15

			int temp = (int)(repay_id%2) + 1;
			double dd = moneyMap.get(temp);
			double[] mon = calcAmount(amount, dd);
			insertRepayCost(mon[0], firstExecTime, repay_id,repayDate);
			insertRepayCost(mon[1], secondExecTime, repay_id,repayDate);
		}else{
			String execTime = sdf.format(ThreadLocalRandom.current().nextLong(beginTimeStamp,endTimeStamp));
			insertRepayCost(amount, execTime, repay_id,repayDate);
		}
	}

	public void insertRepayCost(double amount, String exetime,long  repayPlanId,String repayDate) {
		RepayCost cost = new RepayCost();
		cost.setRepay_plan_id(repayPlanId);
		cost.setExec_time(exetime);// 设置执行时间
		cost.setStatus("0");
		cost.setCost_amount(amount);
		Long id =  repayCostDao.insert(insertSql, cost);
		cost.setId(id);
		redisUtils.zadd("repayCostZset_"+repayDate, Double.parseDouble(exetime.replaceAll(":","")), (JSONObject) JSONObject.toJSON(cost));
		
	}
	
	public List<RepayCost> findByRecordId(Long recordId){
		String sql = "select c.* from t_n_repay_cost c LEFT JOIN t_n_repay_plan p on c.repay_plan_id=p.id   where p.RECORD_ID=:recordId order by c.id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recordId", recordId);
		
		List<RepayCost> l = repayCostDao.find(sql, map);
		return l;
	}

}
