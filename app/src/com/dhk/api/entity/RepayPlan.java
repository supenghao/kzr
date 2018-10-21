package com.dhk.api.entity;

import java.math.BigDecimal;
import java.util.List;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_n_repay_plan 实体类<br/>
 * 2017-02-19 02:20:10 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_n_repay_plan")
public class RepayPlan extends Entity {

	private String credit_card_no;
	private String status;
	private String user_id;
	private Double repay_amount;
	private String policy_type;
	private String repay_day;
	private String repay_month;
	private Long repaysuccess_time;
	private String orderNo;
	private String exec_time;
	private Long record_id;

	private List<RepayCost> repayCostList;

	public List<RepayCost> getRepayCostList() {
		return repayCostList;
	}

	public void setRepayCostList(List<RepayCost> repayCostList) {
		this.repayCostList = repayCostList;
	}

	public Long getRecord_id() {
		return record_id;
	}

	public void setRecord_id(Long record_id) {
		this.record_id = record_id;
	}

	public RepayPlan() {
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setCredit_card_no(String credit_card_no) {
		this.credit_card_no = credit_card_no;
	}

	public String getCredit_card_no() {
		return credit_card_no;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExec_time() {
		return exec_time;
	}

	public void setExec_time(String exec_time) {
		this.exec_time = exec_time;
	}

	public String getStatus() {
		return status;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setRepay_amount(Double repay_amount) {
		this.repay_amount = repay_amount;
	}

	public Double getRepay_amount() {
		BigDecimal b = new BigDecimal(repay_amount);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
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

	public void setRepay_month(String repay_month) {
		this.repay_month = repay_month;
	}

	public String getRepay_month() {
		return repay_month;
	}

	public void setRepaysuccess_time(Long repaysuccess_time) {
		this.repaysuccess_time = repaysuccess_time;
	}

	public Long getRepaysuccess_time() {
		return repaysuccess_time;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RepayPlan [credit_card_no=" + credit_card_no
				+ ", repay_amount=" + repay_amount + ", policy_type="
				+ policy_type + ", repay_day=" + repay_day + "]";
	}

	

}
