package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 子商户注册接口
 * @author juxin-ecitic
 *
 */
public class MerchantRegisterRequest extends BaseBeanRequest {
	public String requestId;//商户注册请求号 
	public String customerType;//商户类型    PERSON ：个人；ENTERPRISE：企业
	public String signedName;//签约名
	public String bindMobile;//联系人手机号
	public String linkman;//商户姓名
	public String idcard;//身份证号
	public String minSettleAmount;//起始结算金额  默认填0
	public String bankAccountType;//银行卡类型,
	public String bankAccountNumber;//银行卡号
	public String bankHeadOfficeName;//银行卡开户行总行名称
	public String bankName;//银行卡开户所在支行
	public String accountName;//银行卡开户人姓名
	public String bankProvince;//开户省
	public String bankCity;//开户市
	public String bankArea;//开户区/县
	public String deposit;//保证金
	public String callbackUrl;//审核成功 后台通知地址
	public String businessLicence;//营业执照号 
	public String legalPerson;//企业的法人姓名
	public String legalPersonIdcard;//法人身份证号
	public String partner;//第三方商户号, ps:需要开通公众号支付,注册子商户前提交公众号appid给我方,由我方下发第三方商户号
	public String companyName;//公司全称  商户类型为企业时必填
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getSignedName() {
		return signedName;
	}
	public void setSignedName(String signedName) {
		this.signedName = signedName;
	}
	public String getBindMobile() {
		return bindMobile;
	}
	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getMinSettleAmount() {
		return minSettleAmount;
	}
	public void setMinSettleAmount(String minSettleAmount) {
		this.minSettleAmount = minSettleAmount;
	}
	public String getBankAccountType() {
		return bankAccountType;
	}
	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getBankHeadOfficeName() {
		return bankHeadOfficeName;
	}
	public void setBankHeadOfficeName(String bankHeadOfficeName) {
		this.bankHeadOfficeName = bankHeadOfficeName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBankProvince() {
		return bankProvince;
	}
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	public String getBankArea() {
		return bankArea;
	}
	public void setBankArea(String bankArea) {
		this.bankArea = bankArea;
	}
	public String getDeposit() {
		return deposit;
	}
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getBusinessLicence() {
		return businessLicence;
	}
	public void setBusinessLicence(String businessLicence) {
		this.businessLicence = businessLicence;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	
	public String getLegalPersonIdcard() {
		return legalPersonIdcard;
	}
	public void setLegalPersonIdcard(String legalPersonIdcard) {
		this.legalPersonIdcard = legalPersonIdcard;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	
}
