package com.dhk.payment.yilian;

public class QuickPay {
	
	private String Amount;//交易金额
	private String CommodityName;//商品名称
	private String CustomerInfo;//异步通知地址
	private String MerchantId;//机构商户号
	private String NotifyCallBackUrl;
	private String OrderCode;//商户订单号
	private String ProductId;//产品类型
	private String RequestNo;//交易请求流水号
	private String RespCode;//应答码
	private String RespDesc;//应答码描述
	private String ShopNo;//机构子商户号
	private String Signature;//验签字段
	private String Version;//版本号
	private String WebCallBackUrl;//页面通知地址
	
	
	private String IdentityId;//证件号
	private String AccountName;//代付账户名
	private String MobileNo;//代付银行手机号
	private String BankName;//收款账户开户行名称
	private String Note;//代付摘要
	private String AccType;//对公对私标识
	private String NotifyUrl;//代付异步通知地址
	private String CardNo;//银行卡号
	
	private String Balance;//总余额
	private String FrozenBalance;//总冻结金额
	private String CanBalance;//可用余额
	
	
	public String getBalance() {
		return Balance;
	}
	public void setBalance(String balance) {
		Balance = balance;
	}
	public String getFrozenBalance() {
		return FrozenBalance;
	}
	public void setFrozenBalance(String frozenBalance) {
		FrozenBalance = frozenBalance;
	}
	public String getCanBalance() {
		return CanBalance;
	}
	public void setCanBalance(String canBalance) {
		CanBalance = canBalance;
	}
	public String getCardNo() {
		return CardNo;
	}
	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}
	public String getNotifyUrl() {
		return NotifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		NotifyUrl = notifyUrl;
	}
	public String getIdentityId() {
		return IdentityId;
	}
	public void setIdentityId(String identityId) {
		IdentityId = identityId;
	}
	public String getAccountName() {
		return AccountName;
	}
	public void setAccountName(String accountName) {
		AccountName = accountName;
	}
	public String getMobileNo() {
		return MobileNo;
	}
	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}
	public String getBankName() {
		return BankName;
	}
	public void setBankName(String bankName) {
		BankName = bankName;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	public String getAccType() {
		return AccType;
	}
	public void setAccType(String accType) {
		AccType = accType;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getCommodityName() {
		return CommodityName;
	}
	public void setCommodityName(String commodityName) {
		CommodityName = commodityName;
	}
	public String getCustomerInfo() {
		return CustomerInfo;
	}
	public void setCustomerInfo(String customerInfo) {
		CustomerInfo = customerInfo;
	}
	public String getMerchantId() {
		return MerchantId;
	}
	public void setMerchantId(String merchantId) {
		MerchantId = merchantId;
	}
	public String getNotifyCallBackUrl() {
		return NotifyCallBackUrl;
	}
	public void setNotifyCallBackUrl(String notifyCallBackUrl) {
		NotifyCallBackUrl = notifyCallBackUrl;
	}
	public String getOrderCode() {
		return OrderCode;
	}
	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getRequestNo() {
		return RequestNo;
	}
	public void setRequestNo(String requestNo) {
		RequestNo = requestNo;
	}
	public String getRespCode() {
		return RespCode;
	}
	public void setRespCode(String respCode) {
		RespCode = respCode;
	}
	public String getRespDesc() {
		return RespDesc;
	}
	public void setRespDesc(String respDesc) {
		RespDesc = respDesc;
	}
	public String getShopNo() {
		return ShopNo;
	}
	public void setShopNo(String shopNo) {
		ShopNo = shopNo;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getWebCallBackUrl() {
		return WebCallBackUrl;
	}
	public void setWebCallBackUrl(String webCallBackUrl) {
		WebCallBackUrl = webCallBackUrl;
	}

}
