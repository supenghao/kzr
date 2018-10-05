package com.dhk.payment.wpay;

public class SendData {
	private String bizName;
	private BaseData data;
	public String getBizName() {
		return bizName;
	}
	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
	public BaseData getData() {
		return data;
	}
	public void setData(BaseData data) {
		this.data = data;
	}
}
