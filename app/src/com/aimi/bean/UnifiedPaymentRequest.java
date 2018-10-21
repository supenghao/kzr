package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 统一支付bean
 * @author juxin-ecitic
 *
 */
public class UnifiedPaymentRequest extends BaseBeanRequest {
	
	public String tradeNo;//商户订单号
	public String notifyUrl;//支付成功通知地址
	public String callbackUrl;//打款成功通知地址
	public String frontbackUrl;//页面跳转地址
	public String tradeAmt;//支付金额
	public String tradeType;//交易类型
	public String authCode;//付款码
	public String openId;//微信用户id
	public String buyerId;//支付宝用户id
	public String manualSettle;//是否清分
	public String goodsName;//商品名称
	public String settleType;//交易方式 0：T0，1：T1 
	public String attach;//附加字段 支付成功后,原样返回
	public String chlcode;//支付通道
	
	
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getFrontbackUrl() {
		return frontbackUrl;
	}
	public void setFrontbackUrl(String frontbackUrl) {
		this.frontbackUrl = frontbackUrl;
	}
	public String getTradeAmt() {
		return tradeAmt;
	}
	public void setTradeAmt(String tradeAmt) {
		this.tradeAmt = tradeAmt;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getManualSettle() {
		return manualSettle;
	}
	public void setManualSettle(String manualSettle) {
		this.manualSettle = manualSettle;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getChlcode() {
		return chlcode;
	}
	public void setChlcode(String chlcode) {
		this.chlcode = chlcode;
	}
	@Override
	public String toString() {
		return "UnifiedPaymentRequest [tradeNo=" + tradeNo + ", notifyUrl=" + notifyUrl + ", callbackUrl=" + callbackUrl
				+ ", frontbackUrl=" + frontbackUrl + ", tradeAmt=" + tradeAmt + ", tradeType=" + tradeType
				+ ", authCode=" + authCode + ", openId=" + openId + ", buyerId=" + buyerId + ", manualSettle="
				+ manualSettle + ", goodsName=" + goodsName + ", settleType=" + settleType + ", attach=" + attach+ ", chlcode=" + chlcode + "]";
	}
	
	
}
