package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanResponse;

/**
 * 代付状态查询返回
 * @author juxin-ecitic
 *
 */
public class WithdrawQueryResponse extends BaseBeanResponse{
	private String succAmount;//成功总金额
	private String succNum;//成功总笔数
	private String failNum;//失败总笔数
	private String failAmount;//失败总金额
	private String remark;//摘要
	private String list;//该字段为jsonArrayString  包含参数详细见文档
	public String getSuccAmount() {
		return succAmount;
	}
	public void setSuccAmount(String succAmount) {
		this.succAmount = succAmount;
	}
	public String getSuccNum() {
		return succNum;
	}
	public void setSuccNum(String succNum) {
		this.succNum = succNum;
	}
	public String getFailNum() {
		return failNum;
	}
	public void setFailNum(String failNum) {
		this.failNum = failNum;
	}
	public String getFailAmount() {
		return failAmount;
	}
	public void setFailAmount(String failAmount) {
		this.failAmount = failAmount;
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
