package com.dhk.api.service;

import java.util.Map;

import com.dhk.api.entity.param.HXPayBindConfirmParam;
import com.dhk.api.entity.param.HXPayBindParam;
import com.dhk.api.entity.param.HXPayParam;
import com.dhk.api.entity.param.HXPayQueryParam;
import com.dhk.api.entity.param.HXPayWithdrawParam;
import com.dhk.api.entity.param.HXPayWithdrawQueryParam;
import com.dhk.api.entity.param.HXRegisterParam;

public interface IHXPayService {

	/**
	 * 
		* @Title: HXRegister 
		* @Description: 入网接口
		* @param @param hxregisterParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return Map    返回类型 
		* @throws
	 */
	Map HXRegister(HXRegisterParam hxregisterParam) throws Exception;

	
	public Map HXBind(HXPayBindParam bindParam) throws Exception ;
	
	public Map HXBindConfirm(HXPayBindConfirmParam bindConfirmParam) throws Exception;
	/**
	 * 
		* @Title: HXPay 
		* @Description: 消费接口
		* @param @param hxpayParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return Map    返回类型 
		* @throws
	 */
	Map HXPay(HXPayParam hxpayParam) throws Exception;

	/**
	 * 
		* @Title: HXPayQuery 
		* @Description: 消费查询
		* @param @param hxpayParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return Map    返回类型 
		* @throws
	 */
	Map HXPayQuery(HXPayQueryParam hxpayQueryParam) throws Exception;

	/**
	 * 
		* @Title: HXPayWithdraw 
		* @Description: 提现接口
		* @param @param hxpayQueryParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return Map    返回类型 
		* @throws
	 */
	Map HXPayWithdraw(HXPayWithdrawParam hxpayWithdrawParam) throws Exception;

	/**
	 * 
		* @Title: HXPayWithdrawQuery 
		* @Description: 提现查询
		* @param @param hxpayWithdrawQueryParam
		* @param @return
		* @param @throws Exception    设定文件 
		* @return Map    返回类型 
		* @throws
	 */
	Map HXPayWithdrawQuery(HXPayWithdrawQueryParam hxpayWithdrawQueryParam) throws Exception;

}