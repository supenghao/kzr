package com.dhk.api.core.impl;

public class PayRequest {
	
	private String userId;
	private Long transAmt;
	private String phoneNo;
	private String customerName;
	private String cerdType;
	private String cerdId;
	private String acctNo;
	private String cvn2;
	private String expDate;
	private String accBankNo;
	private String accBankName;

	public Long getTransAmt() {
		return transAmt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setTransAmt(Long transAmt) {
		this.transAmt = transAmt;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCerdType() {
		return cerdType;
	}

	public void setCerdType(String cerdType) {
		this.cerdType = cerdType;
	}

	public String getCerdId() {
		return cerdId;
	}

	public void setCerdId(String cerdId) {
		this.cerdId = cerdId;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getCvn2() {
		return cvn2;
	}

	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getAccBankNo() {
		return accBankNo;
	}

	public void setAccBankNo(String accBankNo) {
		this.accBankNo = accBankNo;
	}

	public String getAccBankName() {
		return accBankName;
	}

	public void setAccBankName(String accBankName) {
		this.accBankName = accBankName;
	}
}
