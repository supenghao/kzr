package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 代付状态查询
 * @author juxin-ecitic
 *
 */
public class WithdrawQueryRequest extends BaseBeanRequest {
	public String batchId;//批次号

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	

}
