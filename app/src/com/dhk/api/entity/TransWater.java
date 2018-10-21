package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_trans_water 实体类<br/>
 * 2017-02-14 10:44:14 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_trans_water")
public class TransWater extends Entity {
	private String trans_date;
	private String trans_time;
	private String host_trans_date;
	private String host_trans_time;
	private String trans_no;
	private String host_trans_no;
	private String card_no;
	private Long org_id;
	private Long user_id;
	private String trans_amount;
	private String trans_type;
	private String resp_code;
	private String fee;
	private String card_type;
	private String resp_res;
	private String relatioin_no;
	private Long qrcode_id;
	private String external;
	private String is_org_recah;
	private Long plan_id;
	 private Long cost_id;
	 private String proxy_pay_channel;
	 private String proxy_pay_type;
	 private String transactionType;
	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}

	public String getTrans_date() {
		return trans_date;
	}

	public void setTrans_time(String trans_time) {
		this.trans_time = trans_time;
	}

	public String getTrans_time() {
		return trans_time;
	}

	public void setHost_trans_date(String host_trans_date) {
		this.host_trans_date = host_trans_date;
	}

	public String getHost_trans_date() {
		return host_trans_date;
	}

	public void setHost_trans_time(String host_trans_time) {
		this.host_trans_time = host_trans_time;
	}

	public String getHost_trans_time() {
		return host_trans_time;
	}

	public void setTrans_no(String trans_no) {
		this.trans_no = trans_no;
	}

	public String getTrans_no() {
		return trans_no;
	}

	public void setHost_trans_no(String host_trans_no) {
		this.host_trans_no = host_trans_no;
	}

	public String getHost_trans_no() {
		return host_trans_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}

	public Long getOrg_id() {
		return org_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setTrans_amount(String trans_amount) {
		this.trans_amount = trans_amount;
	}

	public String getTrans_amount() {
		float t = Float.parseFloat(trans_amount);
		return String.format("%.2f", t);
	}

	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}

	public String getTrans_type() {
		return trans_type;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_code() {
		return resp_code;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getFee() {
		return fee;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setResp_res(String resp_res) {
		this.resp_res = resp_res;
	}

	public String getResp_res() {
		return resp_res;
	}

	public void setRelatioin_no(String relatioin_no) {
		this.relatioin_no = relatioin_no;
	}

	public String getRelatioin_no() {
		return relatioin_no;
	}

	public void setQrcode_id(Long qrcode_id) {
		this.qrcode_id = qrcode_id;
	}

	public Long getQrcode_id() {
		return qrcode_id;
	}

	public void setExternal(String external) {
		this.external = external;
	}

	public String getExternal() {
		return external;
	}

	public String getIs_org_recah() {
		return is_org_recah;
	}

	public void setIs_org_recah(String is_org_recah) {
		this.is_org_recah = is_org_recah;
	}

	public Long getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(Long plan_id) {
		this.plan_id = plan_id;
	}

	public Long getCost_id() {
		return cost_id;
	}

	public void setCost_id(Long cost_id) {
		this.cost_id = cost_id;
	}

	public String getProxy_pay_channel() {
		return proxy_pay_channel;
	}

	public void setProxy_pay_channel(String proxy_pay_channel) {
		this.proxy_pay_channel = proxy_pay_channel;
	}

	public String getProxy_pay_type() {
		return proxy_pay_type;
	}

	public void setProxy_pay_type(String proxy_pay_type) {
		this.proxy_pay_type = proxy_pay_type;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
}
