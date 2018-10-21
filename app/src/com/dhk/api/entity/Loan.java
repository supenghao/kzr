package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

@SuppressWarnings("serial")
@Table(name = "t_s_loan")
public class Loan extends Entity{

	private Long userId;
	private String userName;
	private String idNo;
	private String phone;
	private String creditItem;
	private String assetsItem;
	private String loanType;
	private String applyDate;
	private String applyTime;
	private String cstatus;
	private String applyArea;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCreditItem() {
		return creditItem;
	}
	public void setCreditItem(String creditItem) {
		this.creditItem = creditItem;
	}
	public String getAssetsItem() {
		return assetsItem;
	}
	public void setAssetsItem(String assetsItem) {
		this.assetsItem = assetsItem;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCstatus() {
		return cstatus;
	}
	public void setCstatus(String cstatus) {
		this.cstatus = cstatus;
	}
	public String getApplyArea() {
		return applyArea;
	}
	public void setApplyArea(String applyArea) {
		this.applyArea = applyArea;
	}
	
}
