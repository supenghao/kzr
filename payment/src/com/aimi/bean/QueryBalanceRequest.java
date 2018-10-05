package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 查询商户余额
 * @author juxin-ecitic
 *
 */
public class QueryBalanceRequest extends BaseBeanRequest {
	private String accessId;//接入商id

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}
	

}
