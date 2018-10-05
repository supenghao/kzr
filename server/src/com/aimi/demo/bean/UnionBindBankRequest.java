package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanRequest;

/**
 * 绑定支付卡
 * @author juxin-ecitic
 *
 */
public class UnionBindBankRequest extends BaseBeanRequest {
	
	public String acctNo;//付款银行卡卡号
	public String phone;//付款卡预留手机号
	public String cvv2;//信用卡安全码
	public String expDate;//信用卡背后有效期
	
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
