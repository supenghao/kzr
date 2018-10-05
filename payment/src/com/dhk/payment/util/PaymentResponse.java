package com.dhk.payment.util;

public class PaymentResponse {

	private String orderDate;
	private String orderNo;
	private String origRespCode;
	private String origRespDesc;
	private String transAmt;
	private String refundAmt;
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrigRespCode() {
		return origRespCode;
	}
	public void setOrigRespCode(String origRespCode) {
		this.origRespCode = origRespCode;
	}
	public String getOrigRespDesc() {
		return origRespDesc;
	}
	public void setOrigRespDesc(String origRespDesc) {
		this.origRespDesc = origRespDesc;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getRefundAmt() {
		return refundAmt;
	}
	public void setRefundAmt(String refundAmt) {
		this.refundAmt = refundAmt;
	}
	
}
