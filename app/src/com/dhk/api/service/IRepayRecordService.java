package com.dhk.api.service;

import com.alibaba.fastjson.JSONObject;
import com.dhk.api.entity.RepayRecord;

import java.util.List;

/**
 * t_n_repay_record service 接口<br/>
 * 2017-02-19 03:00:57 qch
 */
public interface IRepayRecordService {

	Long doReRepayRecode(String user_id, String repay_month,
						 String count, String card_no, String totalAmount,String fee,String startDate,String endDate,String repay_money,String orderNo);

	String getUserRecodeValue(String userId);

	JSONObject unfreeze(String userId, String cardNo, String token);

	void updateStatus(String status,String id);



	/**
	 * 查询用户已还多少钱 未还多少钱
	 * @param userId
	 * @return
	 */
	public RepayRecord queryRepayResult(String userId, String cardNo);
	public List<RepayRecord> getRepayRecord(String userId, String cardNo);

	public String  hasRepayCurMonth(String userId,String cardNo);

	/**
	 * 是否还有冻结余额的订单
	 * @param userId
	 * @param cardNo
	 * @return
	 */
	public boolean hasFreeze(String userId,String cardNo);
	
	/**
	 * 是否有为还款计划在执行
	 * @param userId
	 * @param cardNo
	 * @return
	 */
	public boolean hasExecRepayRecord(String userId,String cardNo);
	
	
}
