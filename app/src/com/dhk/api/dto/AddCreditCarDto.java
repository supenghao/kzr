package com.dhk.api.dto;

public class AddCreditCarDto extends EditCreditCarDto {

	private String checkCode, bank_name, realname, cerdType, cerdId, phoneNo,
			cvn2, expDate,orderId;

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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



	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	
	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AddCreditCarDto [bank_name=" + bank_name + "]";
	}

}
