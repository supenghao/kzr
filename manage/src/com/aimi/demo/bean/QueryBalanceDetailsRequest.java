package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanRequest;

/**
 * 查询账户交易明细
 * @author juxin-ecitic
 *
 */
public class QueryBalanceDetailsRequest extends BaseBeanRequest {
	private String batchId;//批次号
	private String accessId;
	private String startDate;//起始日期 格式YYYYMMDD
	private String endDate;//终止日期 格式YYYYMMDD
	private String pageNumber;//请求记录条数，最大为20
	private String startRecord;//起始记录号
	public String getAccessId() {
		return accessId;
	}
	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getStartRecord() {
		return startRecord;
	}
	public void setStartRecord(String startRecord) {
		this.startRecord = startRecord;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	

	
	

}
