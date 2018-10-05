package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

public class MerchantCredentialRequest extends BaseBeanRequest {
	
	public String time;//时间戳

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}

