package com.dhk.service;

import com.dhk.entity.CostPlanDetail;
import com.dhk.entity.RepayCost;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.payment.PayResult;
/**
 * 用户信用卡还款消费
 * @author y1iag
 *
 */
public interface ICreditCardCostService {
	

	/**
	 * 绑定信用卡

	 * @return
	 */
	/*========20170307 新增还款还款消费==============*/
	
	public PayResult creditRepayCost( RepayPlanDetail rpd,RepayCost rc);

	public PayResult creditCost(CostPlanDetail cpd) ;
	
	/**
	 * 资金不过夜模式
	 * @param rpd
	 * @param rc
	 * @return
	 */
	public PayResult creditDateRepayCost( RepayPlanDetail rpd,RepayCost rc);
}
