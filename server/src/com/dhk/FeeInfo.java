package com.dhk;

import java.math.BigDecimal;

public class FeeInfo {
	private  BigDecimal fee;
	private  BigDecimal external;
	private  BigDecimal cost;
	public BigDecimal getFee() {
		return fee;
	}
	
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public BigDecimal getExternal() {
		return external;
	}
	public void setExternal(BigDecimal external) {
		this.external = external;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	

}
