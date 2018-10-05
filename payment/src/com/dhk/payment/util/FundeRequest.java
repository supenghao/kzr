package com.dhk.payment.util;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

//XML文件中的根标识  
@XmlRootElement(name = "REQUEST")  
public class FundeRequest implements Serializable{

	private String PARTNER_ID;
	private String TRAN_CODE;
	private String UserCode;
	private String PassWord;
	private String insuredAmount;
	private String productCode;
	private String premium;
	private String quoteSchemeId;
	private String insuranceEndDate;
	private String insuranceBeginDate;
	private String provinceCode;
	private String cityCode;
	private String areaCode;
	private String addressInDetail;
	private String kindCountMap; 
	private ApplyRelationParamDomain uwApplyRelationParamDomain;	
	private List<ApplyBeneficiary> listUwApplyBeneficiary;
	private List<Kind> kindList;
	private List<ApplyInsured> listUwApplyInsured;
	private List<ApplyBidPropertyDomain> listUwApplyBidPropertyDomain;

	@XmlElement(name = "PARTNER_ID") 
	public String getPARTNER_ID() {
		return PARTNER_ID;
	}
	public void setPARTNER_ID(String pARTNER_ID) {
		PARTNER_ID = pARTNER_ID;
	}
	@XmlElement(name = "TRAN_CODE") 
	public String getTRAN_CODE() {
		return TRAN_CODE;
	}
	public void setTRAN_CODE(String tRAN_CODE) {
		TRAN_CODE = tRAN_CODE;
	}
	@XmlElement(name = "UserCode")  
	public String getUserCode() {
		return UserCode;
	}
	public void setUserCode(String userCode) {
		UserCode = userCode;
	}
	@XmlElement(name = "PassWord") 
	public String getPassWord() {
		return PassWord;
	}
	public void setPassWord(String passWord) {
		PassWord = passWord;
	}
	public String getInsuredAmount() {
		return insuredAmount;
	}
	public void setInsuredAmount(String insuredAmount) {
		this.insuredAmount = insuredAmount;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getQuoteSchemeId() {
		return quoteSchemeId;
	}
	public void setQuoteSchemeId(String quoteSchemeId) {
		this.quoteSchemeId = quoteSchemeId;
	}
	public String getInsuranceEndDate() {
		return insuranceEndDate;
	}
	public void setInsuranceEndDate(String insuranceEndDate) {
		this.insuranceEndDate = insuranceEndDate;
	}
	public String getInsuranceBeginDate() {
		return insuranceBeginDate;
	}
	public void setInsuranceBeginDate(String insuranceBeginDate) {
		this.insuranceBeginDate = insuranceBeginDate;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAddressInDetail() {
		return addressInDetail;
	}
	public void setAddressInDetail(String addressInDetail) {
		this.addressInDetail = addressInDetail;
	}
	public String getKindCountMap() {
		return kindCountMap;
	}
	public void setKindCountMap(String kindCountMap) {
		this.kindCountMap = kindCountMap;
	}
	public ApplyRelationParamDomain getUwApplyRelationParamDomain() {
		return uwApplyRelationParamDomain;
	}
	public void setUwApplyRelationParamDomain(
			ApplyRelationParamDomain uwApplyRelationParamDomain) {
		this.uwApplyRelationParamDomain = uwApplyRelationParamDomain;
	}
	@XmlElementWrapper(name = "listUwApplyBeneficiary")  
    @XmlElement(name = "UwApplyBeneficiary") 
	public List<ApplyBeneficiary> getListUwApplyBeneficiary() {
		return listUwApplyBeneficiary;
	}
	public void setListUwApplyBeneficiary(
			List<ApplyBeneficiary> listUwApplyBeneficiary) {
		this.listUwApplyBeneficiary = listUwApplyBeneficiary;
	}
	@XmlElementWrapper(name = "kindList")  
    @XmlElement(name = "kind")
	public List<Kind> getKindList() {
		return kindList;
	}
	public void setKindList(List<Kind> kindList) {
		this.kindList = kindList;
	}
	@XmlElementWrapper(name = "listUwApplyInsured")  
    @XmlElement(name = "UwApplyInsured") 
	public List<ApplyInsured> getListUwApplyInsured() {
		return listUwApplyInsured;
	}
	public void setListUwApplyInsured(List<ApplyInsured> listUwApplyInsured) {
		this.listUwApplyInsured = listUwApplyInsured;
	}
	@XmlElementWrapper(name = "listUwApplyBidPropertyDomain")  
    @XmlElement(name = "UwApplyBidPropertyDomain")
	public List<ApplyBidPropertyDomain> getListUwApplyBidPropertyDomain() {
		return listUwApplyBidPropertyDomain;
	}
	public void setListUwApplyBidPropertyDomain(
			List<ApplyBidPropertyDomain> listUwApplyBidPropertyDomain) {
		this.listUwApplyBidPropertyDomain = listUwApplyBidPropertyDomain;
	}
	
}
