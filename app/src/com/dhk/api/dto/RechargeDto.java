package com.dhk.api.dto;

public class RechargeDto extends IdentityDto {

	private String amount;
	
	private String creditCardNo;
	private String cvn2;
	private String expdate;
	private String creditCardPhone;
	private String checkCode;

	private String realname;
	private String bankName;
	private String cerdId;
	private String phoneNo;
	private String needBind;

	public String getNeedBind() {
		return needBind;
	}

	public void setNeedBind(String needBind) {
		this.needBind = needBind;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreditCardNo() {
		return creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public String getCvn2() {
		return cvn2;
	}

	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}

	public String getExpdate() {
		return expdate;
	}

	public void setExpdate(String expdate) {
		this.expdate = expdate;
	}

	public String getCreditCardPhone() {
		return creditCardPhone;
	}

	public void setCreditCardPhone(String creditCardPhone) {
		this.creditCardPhone = creditCardPhone;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	@Override
	public String toString() {
		return "RechargeDto [amount=" + amount + ", toString()="
				+ super.toString() + "]";
	}

}
