package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_cost_policy 实体类<br/>
 * 2017-01-10 03:49:11 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_cost_policy")
public class CostPolicy extends Entity {
	private Long id;

	private String card_no;

	private String user_id;

	private String repeat_detail;

	private String trans_amount;

	private String policy_type; 

	private String repeat_bengin_date;

	private String repeat_end_date;

	private String trans_count;

	private String repeat_mode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRepeat_detail() {
		return repeat_detail;
	}

	public void setRepeat_detail(String repeat_detail) {
		this.repeat_detail = repeat_detail;
	}

	public String getTrans_amount() {
		return trans_amount;
	}

	public void setTrans_amount(String trans_amount) {
		this.trans_amount = trans_amount;
	}

	public String getPolicy_type() {
		return policy_type;
	}

	public void setPolicy_type(String policy_type) {
		this.policy_type = policy_type;
	}

	public String getRepeat_bengin_date() {
		return repeat_bengin_date;
	}

	public void setRepeat_bengin_date(String repeat_bengin_date) {
		this.repeat_bengin_date = repeat_bengin_date;
	}

	public String getRepeat_end_date() {
		return repeat_end_date;
	}

	public void setRepeat_end_date(String repeat_end_date) {
		this.repeat_end_date = repeat_end_date;
	}

	public String getTrans_count() {
		return trans_count;
	}

	public void setTrans_count(String trans_count) {
		this.trans_count = trans_count;
	}

	public String getRepeat_mode() {
		return repeat_mode;
	}

	public void setRepeat_mode(String repeat_mode) {
		this.repeat_mode = repeat_mode;
	}

}
