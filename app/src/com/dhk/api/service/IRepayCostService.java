package com.dhk.api.service;

import com.dhk.api.dto.QResponse;
import com.dhk.api.entity.RepayCost;

import java.util.List;

/**
 * t_n_repay_cost service 接口<br/>
 * 2017-03-02 10:53:08 qch
 */
public interface IRepayCostService {
	
	/**
	 * 插入还款消费表,大于100块拆分成三笔
	 * @param repay_id
	 * @param amount
	 */
	public void insertRepayCost(long repay_id, String repayExecTine,String nextRepayExecTine,double amount,String repayDate) throws Exception;

	public void insertRepayCost(double amount, String exetime,long  repayPlanId,String repayDate);
	
	public List<RepayCost> findByRecordId(Long repayId);
}
