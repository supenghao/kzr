package com.dhk.service;

import com.dhk.payment.PayRequest;
import com.dhk.payment.PayResult;

public interface ICallRemotePayService {
	/**
	 * 信用卡消费
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public PayResult creditPurchase(PayRequest payRequest) throws Exception;
	/**
	 * 汇享天成信用卡消费
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public PayResult hxtcCreditPurchase(PayRequest payRequest) throws Exception;
	
	/**
	 * 易佰联信用卡消费
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	 public PayResult yblCreditPurchase(PayRequest payRequest) throws Exception;
	 
	 /**
		 * kj信用卡消费
		 * @param payRequest
		 * @return
		 * @throws Exception
		 */
		 public PayResult kjCreditPurchase(PayRequest payRequest) throws Exception;
	 /**
		 * 易佰联信用卡还款
		 * @param payRequest
		 * @return
		 * @throws Exception
		 */
	 public PayResult yblCreditProxyPay(PayRequest payRequest) throws Exception;
	/**
	 * 汇享天成借记卡还款代付
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public PayResult hxtcCreditProxyPay(PayRequest payRequest) throws Exception;
	
	/**
	 * 快捷支付还款代付
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public PayResult kjCreditProxyPay(PayRequest payRequest) throws Exception;
	/**
	 * 借记卡还款代付
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public PayResult creditProxyPay(PayRequest payRequest) throws Exception;
	

	
	public PayResult queryBankCardBalance(String cardType) throws Exception;
}
