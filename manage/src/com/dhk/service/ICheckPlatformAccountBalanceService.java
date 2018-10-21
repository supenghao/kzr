package com.dhk.service;

public interface ICheckPlatformAccountBalanceService {
	/**
	 * 检查平台信用卡账户余额与所在银行余额是否一致
	 * @return
	 */
	public boolean checkPlatformCreditBalance() ;
	/**
	 * 检查平台借记卡账户余额与所在银行余额是否一致
	 * @return
	 */
	public boolean checkPlatformDebiteBalance();
}
