package com.dhk.api.entity;

import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
 * t_s_card_bill 实体类<br/>
 * 2016-12-25 10:04:08 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_card_bill")
public class CardBill extends Entity {
	
	private String creditcardId;
	private String billMonth;
	private Double billAmount;
	private String status;

	public void setCreditcardId(String creditcardId) {
		this.creditcardId = creditcardId;
	}

	public String getCreditcardId() {
		return creditcardId;
	}

	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}

	public String getBillMonth() {
		return billMonth;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
