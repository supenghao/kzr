package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanRequest;

/**
 * 银联发送短信
 * @author juxin-ecitic
 *
 */
public class UnionSendSmsRequest extends BaseBeanRequest {
	
	public String extData;//银联支付凭证
	public String acctNo;//付款银行卡卡号
	public String phone;//付款银行卡绑定的手机号
	
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
	
}
