package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanResponse;

/**
 * 查询账户交易明细返回
 * @author juxin-ecitic
 *
 */
public class QueryBalanceDetailsResponse extends BaseBeanResponse {
	private String accessId;
	private String accessName;//商户名称
	private String totalRecords;//总记录条数
	private String returnRecords;//返回记录条数
	private String list;
	public String getAccessId() {
		return accessId;
	}
	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}
	public String getAccessName() {
		return accessName;
	}
	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getReturnRecords() {
		return returnRecords;
	}
	public void setReturnRecords(String returnRecords) {
		this.returnRecords = returnRecords;
	}
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	
	

}
