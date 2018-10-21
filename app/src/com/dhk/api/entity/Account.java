package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_user_account 实体类<br/>
 * 2017-02-19 11:12:53 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_user_account")
public class Account extends Entity {
	private int user_id;
	private String cur_balance;
	private String recash_freeze;
	private String update_date;
	private String update_time;

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setCur_balance(String cur_balance) {
		this.cur_balance = cur_balance;
	}

	public String getCur_balance() {
		return cur_balance;
	}

	public void setRecash_freeze(String recash_freeze) {
		this.recash_freeze = recash_freeze;
	}

	public String getRecash_freeze() {
		return recash_freeze;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUpdate_time() {
		return update_time;
	}
}
