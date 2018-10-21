package com.dhk.api.core.impl;

public interface FeeCalculator {

	/**
	 * 计划费用
	 * 
	 * @return
	 */
	double getfees();

	/**
	 * 总计划金额
	 * 
	 * @return
	 */
	double getTotalAmount();

	/**
	 * 需求金额
	 * 
	 * @return
	 */
	double getRequire();
	
	
	/**
	 * 计划费用(资金不过夜)
	 * 
	 * @return
	 */
	double getDatefees();
	
	/**
	 * 需求金额(资金不过夜)
	 * 
	 * @return
	 */
	double getDateRequire();

}
