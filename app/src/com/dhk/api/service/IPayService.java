package com.dhk.api.service;

import com.dhk.api.core.impl.PayResult;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.core.impl.PayRequest;

public interface IPayService {

	/**
	 * 信用卡消费
	 * 
	 * @param payRequest
	 * @return
	 */
	public PayResult creditPurchase(PayRequest payRequest) throws Exception;

	/**
	 * 借记卡消费
	 * 
	 * @param payRequest
	 * @return
	 */
	public PayResult debitPurchase(PayRequest payRequest) throws Exception;

	/**
	 * 借记卡充值
	 * 
	 * @param re
	 * @return
	 */
	public PayResult debitPurchase_recharge(PayRequest re);

	// public PayResult creditProxyPay(PayRequest payRequest) throws Exception;

	// public PayResult debitProxyPay(PayRequest payRequest) throws Exception;
	public PayResult fudeIssue(PayRequest req);
	/**
	 * 
		* @Title: XrPayQuickzxEPay 
		* @Description: 消费
		* @param @param amt
		* @param @param dto
		* @param @param cardNo
		* @param @return    设定文件 
		* @return PayResult    返回类型 
		* @throws
	 */
	public PayResult XrPayQuickzxEPay(String amt,IdentityDto dto,String cardNo);
	public PayResult YtPayQuickzxEPay(String amt,IdentityDto dto,String cardNo);
    /**
     * 精选优选
     * @param amt
     * @param dto
     * @param cardNo
     * @return
     */
	public PayResult YtyxPayQuickzxEPay(String amt,IdentityDto dto,String cardNo);
	public PayResult YtjxPayQuickzxEPay(String amt,IdentityDto dto,String cardNo);
	
	/**
	 * 快捷（多店宝）
	 */
	public PayResult KjPayQuickzxEPay(String amt,IdentityDto dto,String cardNo,String payCode);
}