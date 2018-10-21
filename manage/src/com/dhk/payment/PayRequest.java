package com.dhk.payment;

public class PayRequest {

	private String orderDate;
	private String orderTime;
	private String orderNo;
	private Long transAmt;
	private String commodityName;
	private String phoneNo;
	private String customerName;
	private String cerdType;
	private String cerdId;
	private String acctNo;
	private String cvn2;
	private String expDate;
	private String accBankNo;
	private String accBankName;
	private String note;
	private String payType;
	private String  realname;
	private String  merchantId;
	private String lhh; //总行联行号
	private String lhmc; //总行联行名称
	private String bindId; //卡绑定id
	private String uerId; //用户id
	private String purchase_cost;
	private String re_cash;
	private String byl_merchantId;
	
	private String kjkey;
	private String kjmerno;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getRealname() {
		return realname;
	}
	
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public Long getTransAmt() {
		return transAmt;
	}
	
	public void setTransAmt(Long transAmt) {
		this.transAmt = transAmt;
	}
	
	public String getCommodityName() {
		return commodityName;
	}
	
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCerdType() {
		return cerdType;
	}
	
	public void setCerdType(String cerdType) {
		this.cerdType = cerdType;
	}
	
	public String getCerdId() {
		return cerdId;
	}
	
	public void setCerdId(String cerdId) {
		this.cerdId = cerdId;
	}
	
	public String getAcctNo() {
		return acctNo;
	}
	
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	
	public String getCvn2() {
		return cvn2;
	}
	
	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}
	
	public String getExpDate() {
		return expDate;
	}
	
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	
	public String getAccBankNo() {
		return accBankNo;
	}
	
	public void setAccBankNo(String accBankNo) {
		this.accBankNo = accBankNo;
	}
	
	public String getAccBankName() {
		return accBankName;
	}
	
	public void setAccBankName(String accBankName) {
		this.accBankName = accBankName;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getPayType() {
		return payType;
	}
	
	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	public void setRealName(String realname) {
		this.realname=realname;
	}
	
	
	
	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	
	

	public String getLhh() {
		return lhh;
	}

	public void setLhh(String lhh) {
		this.lhh = lhh;
	}

	public String getLhmc() {
		return lhmc;
	}

	public void setLhmc(String lhmc) {
		this.lhmc = lhmc;
	}
	
	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	
	public String getUerId() {
		return uerId;
	}

	public void setUerId(String uerId) {
		this.uerId = uerId;
	}


	public String getRe_cash() {
		return re_cash;
	}

	public void setRe_cash(String re_cash) {
		this.re_cash = re_cash;
	}

	public String getPurchase_cost() {
		return purchase_cost;
	}

	public void setPurchase_cost(String purchase_cost) {
		this.purchase_cost = purchase_cost;
	}

	public String getByl_merchantId() {
		return byl_merchantId;
	}

	public void setByl_merchantId(String byl_merchantId) {
		this.byl_merchantId = byl_merchantId;
	}

	public String getKjkey() {
		return kjkey;
	}

	public void setKjkey(String kjkey) {
		this.kjkey = kjkey;
	}

	public String getKjmerno() {
		return kjmerno;
	}

	public void setKjmerno(String kjmerno) {
		this.kjmerno = kjmerno;
	}

	@Override
	public String toString() {
		return "PayRequest [orderDate=" + orderDate + ", orderTime="
				+ orderTime + ", orderNo=" + orderNo + ", transAmt=" + transAmt
				+ ", commodityName=" + commodityName + ", phoneNo=" + phoneNo
				+ ", customerName=" + customerName + ", cerdType=" + cerdType
				+ ", cerdId=" + cerdId + ", acctNo=" + acctNo + ", cvn2="
				+ cvn2 + ", expDate=" + expDate + ", accBankNo=" + accBankNo
				+ ", accBankName=" + accBankName + ", note=" + note
				+ ", payType=" + payType + ", realname=" + realname
				+ ", merchantId=" + merchantId + ", lhh=" + lhh + ", lhmc="
				+ lhmc + ", bindId=" + bindId + ", uerId=" + uerId
				+ ", purchase_cost=" + purchase_cost + ", re_cash=" + re_cash
				+ ", byl_merchantId=" + byl_merchantId + ", kjkey=" + kjkey
				+ ", kjmerno=" + kjmerno + "]";
	}

	 
	
	
}
