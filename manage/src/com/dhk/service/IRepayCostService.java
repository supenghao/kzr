package com.dhk.service;

import com.dhk.entity.RepayCost;

/**
    * t_n_repay_cost service 接口<br/>
    * 2017-03-07 04:53:33 bianzk
    */ 
public interface IRepayCostService {

	/**
	 * 更新还款消费的状态
	 * @param id 还款消费ID
	 * @param status 状态
	 * @return
	 */
	public boolean  doUpdateRepayCostStatus(Long id,String status);
	

   /**
	* 检查还款计划状态    0|未全部执行完；1|全部成；2|失败(部分失败)
	* @param repayPlanId
	* @return
	*/
	public int checkRepayPlanStatus(Long repayPlanId);

	   public RepayCost findRepayCostById(long id);


}

