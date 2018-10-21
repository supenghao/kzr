package com.dhk.api.core;

import java.text.ParseException;
import java.util.List;

import com.dhk.api.entity.RepayPlan;

public interface IRepayPolicy extends Policy {

	/**
	 * 检测是否是还款时间
	 * 
	 * @return
	 */
	public boolean testRepaytime();

	/**
	 * 根据消费策略生成详细消费计划
	 * 
	 * @return
	 * @throws ParseException
	 */
	List<RepayPlan> genDetailPolicy();

	/**
	 * 生成计划所需要的手续费
	 * 
	 * @return
	 * @throws ParseException
	 */
	//double genExpense();

	/**
	 * 还款计划是否能够还完 大于0能够还完,小于0差额
	 * 
	 * @return
	 * @throws ParseException
	 */
	//double repayEnough();
}
