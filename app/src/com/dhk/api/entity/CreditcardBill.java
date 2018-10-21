package com.dhk.api.entity;

import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
 * t_s_creditcard_bill 实体类<br/>    20170906废除
 * 2017-02-19 09:53:17 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_creditcard_bill")
public class CreditcardBill extends Entity {
	private Long creditcard_id;
	private String bill_amount;
	private String bill_day;
	private String repay_day;
	private String status;
	private String valueable;
	private String create_day;

	public void setCreditcard_id(Long creditcard_id) {
		this.creditcard_id = creditcard_id;
	}

	public Long getCreditcard_id() {
		return creditcard_id;
	}

	public void setBill_amount(String bill_amount) {
		this.bill_amount = bill_amount;
	}

	public String getBill_amount() {
		return bill_amount;
	}

	public void setBill_day(String bill_day) {
		this.bill_day = bill_day;
	}

	public String getBill_day() {
		return bill_day;
	}

	public void setRepay_day(String repay_day) {
		this.repay_day = repay_day;
	}

	public String getRepay_day() {
		return repay_day;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setValueable(String valueable) {
		this.valueable = valueable;
	}

	public String getValueable() {
		return valueable;
	}

	public void setCreate_day(String create_day) {
		this.create_day = create_day;
	}

	public String getCreate_day() {
		return create_day;
	}
}
