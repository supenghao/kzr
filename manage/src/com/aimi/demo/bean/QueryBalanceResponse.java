package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanResponse;

/**
 * 查询账户余额
 * @author juxin-ecitic
 *
 */
public class QueryBalanceResponse extends BaseBeanResponse {
	private String accessId;//接入商编号
	private String accessName;//商户名称
	private String balance;//余额
	private String forzenAmt;//冻结金额
	public String getAccessId() {
		return accessId;
	}
	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}
	public String getAccessName() {
		return accessName;
	}
	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getForzenAmt() {
		return forzenAmt;
	}
	public void setForzenAmt(String forzenAmt) {
		this.forzenAmt = forzenAmt;
	}
	

}
