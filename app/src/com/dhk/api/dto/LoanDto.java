package com.dhk.api.dto;

public class LoanDto extends IdentityDto{

	private String userName;
	private String idNo;
	private String phone;
	private String creditItem;
	private String assetsItem;
	private String loanType;
	private String applyDate;
	private String applyTime;
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
	public String getApplyArea() {
		return applyArea;
	}
	public void setApplyArea(String applyArea) {
		this.applyArea = applyArea;
	}
	
}
