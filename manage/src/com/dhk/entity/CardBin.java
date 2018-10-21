package com.dhk.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_card_bin 实体类<br/>
 * 2017-02-07 09:22:44 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_card_bin")
public class CardBin extends Entity {

	private String bankCode;
	private String bankName;
	private String cardName;
	private String cardbin;
	private String cardType;
	private String lhh;
	private String lhmc;

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardbin() {
		return cardbin;
	}

	public void setCardbin(String cardbin) {
		this.cardbin = cardbin;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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
	
	
}
