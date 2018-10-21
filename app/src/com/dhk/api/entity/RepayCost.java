package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_n_repay_cost 实体类<br/>
 * 2017-03-02 10:53:08 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_n_repay_cost")
public class RepayCost extends Entity {
	private Long repay_plan_id;
	private Double cost_amount;
	private String status;
	private String exec_time;

	public void setRepay_plan_id(Long repay_plan_id) {
		this.repay_plan_id = repay_plan_id;
	}

	public Long getRepay_plan_id() {
		return repay_plan_id;
	}

	public String getExec_time() {
		return exec_time;
	}

	public void setExec_time(String exec_time) {
		this.exec_time = exec_time;
	}

	public void setCost_amount(Double cost_amount) {
		this.cost_amount = cost_amount;
	}

	public Double getCost_amount() {
		return cost_amount;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
