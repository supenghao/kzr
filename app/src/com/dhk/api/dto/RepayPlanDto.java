package com.dhk.api.dto;

public class RepayPlanDto extends DelCreditCarDto {

	private String type, repeat_detail, trans_amount, policy_type, trans_count;
	private String repaytemid;
	private String repayType;
	private String creditCardNo;
	private String cvn2;
	private String expdate;
	private String creditCardPhone;
	private String checkCode;

	private String total_amount;
	private String pay_amount;

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getRepaytemid() {
		return repaytemid;
	}

	public void setRepaytemid(String repaytemid) {
		this.repaytemid = repaytemid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRepeat_detail() {
		return repeat_detail;
	}

	public void setRepeat_detail(String repeat_detail) {
		this.repeat_detail = repeat_detail;
	}

	public String getTrans_amount() {
		return trans_amount;
	}

	public void setTrans_amount(String trans_amount) {
		this.trans_amount = trans_amount;
	}

	public String getPolicy_type() {
		return policy_type;
	}

	public void setPolicy_type(String policy_type) {
		this.policy_type = policy_type;
	}

	public String getTrans_count() {
		return trans_count;
	}

	public void setTrans_count(String trans_count) {
		this.trans_count = trans_count;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RepayPlanDto [type=" + type + ", repeat_detail="
				+ repeat_detail + ", trans_amount=" + trans_amount
				+ ", policy_type=" + policy_type + ", trans_count="
				+ trans_count + ", repaytemid=" + repaytemid + ", repayType="
				+ repayType + ", toString()=" + super.toString() + "]";
	}

}
