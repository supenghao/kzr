package com.dhk.payment.wpay;

public class TransferData extends BaseData {
	private String orderNo;//订单号
	private String accountName;//账户姓名
	private String accountNo;//账户
	private String remark;//说明
	private String transAmount;//代付金额
	private String bankFullName;//银行开户行*对公代付填写
	private String accountType;//private对私 public对公 不填默认对私
	private String cnaps;//联行号*对公代付填写
	private String callbackUrl;//回调地址，中间状态的交易将异步通知结果
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getBankFullName() {
		return bankFullName;
	}
	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getCnaps() {
		return cnaps;
	}
	public void setCnaps(String cnaps) {
		this.cnaps = cnaps;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	
}
