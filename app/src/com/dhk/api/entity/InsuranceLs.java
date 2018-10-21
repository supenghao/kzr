package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_insurance_ls 实体类<br/>
 * 2017-03-08 06:07:58 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_insurance_ls")
public class InsuranceLs extends Entity {

	protected String lsDate;
	protected String lsTime;
	protected String applyNo;
	protected String policyNo;
	protected String userid;
	protected String status;
	protected String resultCode;
	protected String resultText;
	protected String quoteSchemeId;
	protected String insuranceBeginDate;
	protected String insuranceEndDate;
	protected String insuredAmount;
	protected Double premium;
	public String getLsDate() {
		return lsDate;
	}
	public void setLsDate(String lsDate) {
		this.lsDate = lsDate;
	}
	public String getLsTime() {
		return lsTime;
	}
	public void setLsTime(String lsTime) {
		this.lsTime = lsTime;
	}
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
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultText() {
		return resultText;
	}
	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
	public String getQuoteSchemeId() {
		return quoteSchemeId;
	}
	public void setQuoteSchemeId(String quoteSchemeId) {
		this.quoteSchemeId = quoteSchemeId;
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
	public Double getPremium() {
		return premium;
	}
	public void setPremium(Double premium) {
		this.premium = premium;
	}
	
	
}
