package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 银联确认支付
 * @author juxin-ecitic
 *
 */
public class UnionPortPayRequest extends BaseBeanRequest {
	
	public String extData;//银联支付凭证
	public String acctNo;//付款卡卡号
	public String phone;//付款卡预留手机号
	public String verifyCode;//短信验证码
	public String cvv2;//信用卡安全码
	public String expDate;//信用卡有效期
	
	public String getExtData() {
		return extData;
	}
	public void setExtData(String extData) {
		this.extData = extData;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getCvv2() {
		return cvv2;
	}
	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	
	

}
