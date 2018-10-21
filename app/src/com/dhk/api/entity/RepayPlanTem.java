package com.dhk.api.entity;

import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
 * t_s_repayplan_tem 实体类<br/>
 * 2017-02-19 11:11:18 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_repayplan_tem")
public class RepayPlanTem extends Entity {
	private String user_id;
	private String card_no;
	private Double repay_amount;
	private String policy_type;
	private String repay_day;

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setRepay_amount(Double repay_amount) {
		this.repay_amount = repay_amount;
	}

	public Double getRepay_amount() {
		return repay_amount;
	}

	public void setPolicy_type(String policy_type) {
		this.policy_type = policy_type;
	}

	public String getPolicy_type() {
		return policy_type;
	}

	public void setRepay_day(String repay_day) {
		this.repay_day = repay_day;
	}

	public String getRepay_day() {
		return repay_day;
	}
}
