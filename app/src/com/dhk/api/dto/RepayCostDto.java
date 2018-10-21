package com.dhk.api.dto;

public class RepayCostDto extends IdentityDto{

	private String repayId;

	public String getRepayId() {
		return repayId;
	}

	public void setRepayId(String repayId) {
		this.repayId = repayId;
	}
	@Override
	public String toString() {
		return "RepayCostDto [repayId=" + repayId + ", toString()="
				+ super.toString() + "]";
	}
}
