package com.aimi.bean;


import com.aimi.bean.base.BaseBeanResponse;

/**
 * 银联发送短信返回
 * @author juxin-ecitic
 *
 */
public class UnionSendSmsResponse extends BaseBeanResponse {
	
	public String extData;
	public String acctNo;
	public String transactionId;
	public String outTradeNo;
	
	public String getExtData() {
		return extData;
	}
	public void setExtData(String extData) {
		this.extData = extData;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
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
