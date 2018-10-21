package com.dhk.api.third;


public class BankCard4VerifyObj {

	private String reason;
	private int error_code;
	private BankCard4VerifyData result;
	private String ordersign;
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	public BankCard4VerifyData getResult() {
		return result;
	}
	public void setResult(BankCard4VerifyData result) {
		this.result = result;
	}
	public String getOrdersign() {
		return ordersign;
	}
	public void setOrdersign(String ordersign) {
		this.ordersign = ordersign;
	}
	
	
	
}
