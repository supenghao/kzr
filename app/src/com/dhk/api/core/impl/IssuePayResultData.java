package com.dhk.api.core.impl;

public class IssuePayResultData extends PayResultData {
	// 保险接口
	private String applyNo;// 投保单号
	private String policyNo;// 保单号
	private String insuranceBeginDate;// 起保日期
	private String insuranceEndDate;// 终保日期
	private String insuredAmount;// 保额
	private String premium;// 保费

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsuranceBeginDate() {
		return insuranceBeginDate;
	}

	public void setInsuranceBeginDate(String insuranceBeginDate) {
		this.insuranceBeginDate = insuranceBeginDate;
	}

	public String getInsuranceEndDate() {
		return insuranceEndDate;
	}

	public void setInsuranceEndDate(String insuranceEndDate) {
		this.insuranceEndDate = insuranceEndDate;
	}

	public String getInsuredAmount() {
		return insuredAmount;
	}

	public void setInsuredAmount(String insuredAmount) {
		this.insuredAmount = insuredAmount;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IssuePayResultData [applyNo=" + applyNo + ", policyNo="
				+ policyNo + ", insuranceBeginDate=" + insuranceBeginDate
				+ ", insuranceEndDate=" + insuranceEndDate + ", insuredAmount="
				+ insuredAmount + ", premium=" + premium + "]";
	}

}
