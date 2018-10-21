package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_recharge 实体类<br/>
 * 2017-02-15 10:45:22 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_recharge")
public class Recharge extends Entity {
	private Double amount;
	private String user_id;

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return user_id;
	}
}
