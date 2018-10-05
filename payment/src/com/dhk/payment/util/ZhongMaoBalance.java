package com.dhk.payment.util;

public class ZhongMaoBalance {

	private String avaBal;//总余额（已入账金额+未入账金额） 分为单位
	private String avaFreezeBal;//总冻结金额（已入账冻结+未入账冻结）
	private String cwcBal;//已入账金额 沉淀资金，由未入账金额在次日清结算后转入到已入账金额中
	private String cwcFreezeBal;//已入账冻结金额
	public String getAvaBal() {
		return avaBal;
	}
	public void setAvaBal(String avaBal) {
		this.avaBal = avaBal;
	}
	public String getAvaFreezeBal() {
		return avaFreezeBal;
	}
	public void setAvaFreezeBal(String avaFreezeBal) {
		this.avaFreezeBal = avaFreezeBal;
	}
	public String getCwcBal() {
		return cwcBal;
	}
	public void setCwcBal(String cwcBal) {
		this.cwcBal = cwcBal;
	}
	public String getCwcFreezeBal() {
		return cwcFreezeBal;
	}
	public void setCwcFreezeBal(String cwcFreezeBal) {
		this.cwcFreezeBal = cwcFreezeBal;
	}
	
}
