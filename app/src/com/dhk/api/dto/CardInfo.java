package com.dhk.api.dto;

public class CardInfo {
	
	private String cardNum;
	private String bankName;
	private String bankCode;
	private String cardName;
	private String cardType;
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	@Override
	public String toString() {
		return "CardInfo [cardNum=" + cardNum + ", bankName=" + bankName
				+ ", bankCode=" + bankCode + ", cardName=" + cardName
				+ ", cardType=" + cardType + "]";
	}
	
	
	
}
