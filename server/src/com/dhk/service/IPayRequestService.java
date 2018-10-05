package com.dhk.service;

import java.math.BigDecimal;

import com.dhk.entity.APPUser;
import com.dhk.entity.CreditCard;
import com.dhk.entity.Org;
import com.dhk.payment.PayRequest;


public interface IPayRequestService {
	/**
	 * 用户信用卡 交易参数
	 * @param user
	 * @param transAmount
	 * @param cardNo
	 * @param expDate
	 * @return
	 */
	public PayRequest getCreditCardCostRequest(APPUser user, BigDecimal transAmount, CreditCard card,String orderNo);
	/**
	 * 信用卡还款请求参数
	 * @param user
	 * @param transAmount
	 * @param card
	 * @param payType
	 * @return
	 */
	public PayRequest getCreditCardRepayRequest(APPUser user, BigDecimal transAmount,String orderNo, CreditCard card);

	/**
	 * 用户借记卡提现请求参数
	 * @param user
	 * @param transAmount
	 * @param cardNo
	 * @param payType
	 * @return
	 */
	public PayRequest getDebitCarRecashRequest(String orderNo,APPUser user, BigDecimal transAmount, String cardNo);
	public PayRequest getDebitCarRecashRequest(String orderNo, Org org, BigDecimal transAmount, String cardNo);
	
	
}
