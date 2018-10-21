package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanRequest;

/**
 * 查询支付订单
 * @author juxin-ecitic
 *
 */
public class PayOrderQueryRequest extends BaseBeanRequest {
	
	public String transactionId;//平台订单号

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	
	
	

}
