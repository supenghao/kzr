package com.aimi.bean;


import com.aimi.bean.base.BaseBeanResponse;

/**
 * 代付请求返回
 * @author juxin-ecitic
 *
 */
public class WithdrawPayResponse extends BaseBeanResponse {
	
	private String batchId;//批次号

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	
}
