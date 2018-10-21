package com.dhk.api.core.impl;

public class PayResultData {

	private String transDate;
	private String transTime;
	private String transNo;

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	@Override
	public String toString() {
		return "PayResultData [transDate=" + transDate + ", transTime="
				+ transTime + ", transNo=" + transNo + "]";
	}

}
