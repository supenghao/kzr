package com.dhk.payment.service;

import com.dhk.payment.entity.param.HXPayParam;
import com.dhk.payment.entity.param.HXPayQueryParam;
import com.dhk.payment.entity.param.HXPayWithdrawParam;
import com.dhk.payment.entity.param.HXPayWithdrawQueryParam;
import com.dhk.payment.entity.param.HXRegisterParam;
import com.dhk.payment.yilian.QuickPay;

public interface IHXPayService {

	/**
	 * 
		* @Title: HXRegister 
		* @Description: 入网接口
		* @param @param hxregisterParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return QuickPay    返回类型 
		* @throws
	 */
	QuickPay HXRegister(HXRegisterParam hxregisterParam) throws Exception;

	/**
	 * 
		* @Title: HXPay 
		* @Description: 消费接口
		* @param @param hxpayParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return QuickPay    返回类型 
		* @throws
	 */
	QuickPay HXPay(HXPayParam hxpayParam) throws Exception;

	/**
	 * 
		* @Title: HXPayQuery 
		* @Description: 消费查询
		* @param @param hxpayParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return QuickPay    返回类型 
		* @throws
	 */
	QuickPay HXPayQuery(HXPayQueryParam hxpayQueryParam) throws Exception;

	/**
	 * 
		* @Title: HXPayWithdraw 
		* @Description: 提现接口
		* @param @param hxpayQueryParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return QuickPay    返回类型 
		* @throws
	 */
	QuickPay HXPayWithdraw(HXPayWithdrawParam hxpayWithdrawParam) throws Exception;

	/**
	 * 
		* @Title: HXPayWithdrawQuery 
		* @Description: 提现查询
		* @param @param hxpayWithdrawQueryParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return QuickPay    返回类型 
		* @throws
	 */
	QuickPay HXPayWithdrawQuery(HXPayWithdrawQueryParam hxpayWithdrawQueryParam) throws Exception;

}