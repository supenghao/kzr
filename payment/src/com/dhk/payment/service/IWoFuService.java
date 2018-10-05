package com.dhk.payment.service;

import java.util.Map;

import com.dhk.payment.yilian.QuickPay;


public interface IWoFuService {
	/**
	 * 快捷支付
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public QuickPay creditPurchase(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 代付
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public QuickPay proxyPay(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 交易查询
	 * @param bizName 交易名称
	 * @param orderNo 订单号
	 * @return
	 * @throws Exception
	 */
	public QuickPay search(String bizName, String orderNo) throws Exception;
}
