package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanResponse;

/**
 * 银联确认支付
 * @author juxin-ecitic
 *
 */
public class UnionPortPayResponse extends BaseBeanResponse {
	
	public String transactionId;//平台订单号
	public String outTradeNo;//商户订单号
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	
	
	

}
