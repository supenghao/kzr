package com.dhk.api.core;

import java.text.ParseException;
import java.util.List;

import com.dhk.api.entity.CostPlan;

public interface ICostPolicy extends Policy {
	/**
	 * 根据消费策略生成详细消费计划
	 * 
	 * @return
	 * @throws ParseException
	 */
	List<CostPlan> genDetailPolicy() throws ParseException;

	/**
	 * 生成计划所需要的手续费
	 * 
	 * @return
	 * @throws ParseException
	 */
	double genExpense() throws ParseException;

	double genExpense(List<CostPlan> ps);
}
