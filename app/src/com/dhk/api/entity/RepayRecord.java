package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_n_repay_record 实体类<br/>
 * 2017-02-19 03:00:57 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_n_repay_record")
public class RepayRecord extends Entity {
	private String repay_month;
	private String repay_count;
	private String card_no;
	private String amount;
	private String user_id;
	private String orderNo;
	private String status;
	private String service_charge;
	private String service_charge_residue;
	private String caution_money;
	private String repay_money;
	private String plan_start;
	private String plan_end;


	private String bank_name;
	private String realname;

	private String hasRepay;
	private String unRepay;
	private String create_dt;

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getService_charge_residue() {
		return service_charge_residue;
	}

	public void setService_charge_residue(String service_charge_residue) {
		this.service_charge_residue = service_charge_residue;
	}

	public String getRepay_money() {
		return repay_money;
	}

	public void setRepay_money(String repay_money) {
		this.repay_money = repay_money;
	}

	public String getService_charge() {
		return service_charge;
	}

	public void setService_charge(String service_charge) {
		this.service_charge = service_charge;
	}

	public String getCaution_money() {
		return caution_money;
	}

	public void setCaution_money(String caution_money) {
		this.caution_money = caution_money;
	}

	public String getPlan_start() {
		return plan_start;
	}

	public void setPlan_start(String plan_start) {
		this.plan_start = plan_start;
	}

	public String getPlan_end() {
		return plan_end;
	}

	public void setPlan_end(String plan_end) {
		this.plan_end = plan_end;
	}

	public String getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(String create_dt) {
		this.create_dt = create_dt;
	}

	public String getHasRepay() {
		return hasRepay;
	}

	public void setHasRepay(String hasRepay) {
		this.hasRepay = hasRepay;
	}

	public String getUnRepay() {
		return unRepay;
	}

	public void setUnRepay(String unRepay) {
		this.unRepay = unRepay;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String isUnFreeze;
	public String getUser_id() {
		return user_id;
	}
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setRepay_month(String repay_month) {
		this.repay_month = repay_month;
	}

	public String getRepay_month() {
		return repay_month;
	}
	
	public String getRepay_count() {
		return repay_count;
	}

	public void setRepay_count(String repay_count) {
		this.repay_count = repay_count;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmount() {
		return amount;
	}

	public String getIsUnFreeze() {
		return isUnFreeze;
	}

	public void setIsUnFreeze(String isUnFreeze) {
		this.isUnFreeze = isUnFreeze;
	}
	
}
