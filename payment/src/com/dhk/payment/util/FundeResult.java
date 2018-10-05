package com.dhk.payment.util;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

//XML文件中的根标识  
@XmlRootElement(name = "RESPONSE") 
public class FundeResult implements Serializable{

	private String policyNo;
	private String applyNo;
	private String insuranceBeginDate;
	private String insuranceEndDate;
	private String SL_RSLT_CODE;
	private String SL_RSLT_MESG;
	private String insuredAmount;
	private String premium;
	
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
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
	public String getSL_RSLT_CODE() {
		return SL_RSLT_CODE;
	}
	public void setSL_RSLT_CODE(String sL_RSLT_CODE) {
		SL_RSLT_CODE = sL_RSLT_CODE;
	}
	public String getSL_RSLT_MESG() {
		return SL_RSLT_MESG;
	}
	public void setSL_RSLT_MESG(String sL_RSLT_MESG) {
		SL_RSLT_MESG = sL_RSLT_MESG;
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
	
	
}
