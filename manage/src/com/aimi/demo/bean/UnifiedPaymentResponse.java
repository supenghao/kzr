package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanResponse;

/**
 * 支付请求返回
 * @author juxin-ecitic
 *
 */
public class UnifiedPaymentResponse extends BaseBeanResponse{
	public String outTradeNo;//商户订单号
	public String transactionId;//平台订单号
	public String tradeAmt;//订单金额 单位：分
	public String actualAmt;//实际到账金额 单位：分，扣除手续费后金额
	public String extData;//银联支付凭证
	public String code_url;//二维码链接
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTradeAmt() {
		return tradeAmt;
	}
	public void setTradeAmt(String tradeAmt) {
		this.tradeAmt = tradeAmt;
	}
	public String getActualAmt() {
		return actualAmt;
	}
	public void setActualAmt(String actualAmt) {
		this.actualAmt = actualAmt;
	}
	public String getExtData() {
		return extData;
	}
	public void setExtData(String extData) {
		this.extData = extData;
	}
	public String getCode_url() {
		return code_url;
	}
	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}
	
	
	
	

}
