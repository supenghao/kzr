package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_user_apply_cash 实体类<br/>
 * 2017-02-14 10:48:52 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_user_apply_cash")
public class ApplyCash extends Entity {

	private String user_id;
	private String apply_date;
	private String amount;
	private String status;
	private String oper_id;
	private String refuse;

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}

	public String getApply_date() {
		return apply_date;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmount() {
		return amount;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setOper_id(String oper_id) {
		this.oper_id = oper_id;
	}

	public String getOper_id() {
		return oper_id;
	}

	public void setRefuse(String refuse) {
		this.refuse = refuse;
	}

	public String getRefuse() {
		return refuse;
	}
}
