package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_cost_plan 实体类<br/>
 * 2017-01-14 04:55:47 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_cost_plan")
public class CostPlan extends Entity {
	private String cost_policy_id;
	private String user_id;
	private String card_no;
	private String cost_amount;
	private String cost_datetime;
	private String status;
	private String exec_time;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getExec_time() {
		return exec_time;
	}

	public void setExec_time(String exec_time) {
		this.exec_time = exec_time;
	}

	public void setCost_policy_id(String cost_policy_id) {
		this.cost_policy_id = cost_policy_id;
	}

	public String getCost_policy_id() {
		return cost_policy_id;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCost_amount(String cost_amount) {
		this.cost_amount = cost_amount;
	}

	public String getCost_amount() {
		return cost_amount;
	}

	public void setCost_datetime(String cost_datetime) {
		this.cost_datetime = cost_datetime;
	}

	public String getCost_datetime() {
		return cost_datetime;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CostPlan [cost_policy_id=" + cost_policy_id + ", card_no="
				+ card_no + ", cost_amount=" + cost_amount + ", cost_datetime="
				+ cost_datetime + ", status=" + status + "]";
	}

}
