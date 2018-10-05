package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 代付请求
 * @author juxin-ecitic
 *
 */
public class WithdrawPayRequest extends BaseBeanRequest {
	private String batchId;//批次号
	private String totalNumber;//总笔数
	private String totalAmount;//总金额
	private String remark;
	private String list;
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	

}
