package com.dhk.api.dto;

public class CostPlanDto extends IdentityDto {
	private String type, cardNo, repeat_mode, repeat_detail, trans_count,
			trans_amount, policy_type;
	private String repeat_begindate, repeat_enddate;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getRepeat_mode() {
		return repeat_mode;
	}

	public void setRepeat_mode(String repeat_mode) {
		this.repeat_mode = repeat_mode;
	}

	public String getRepeat_detail() {
		return repeat_detail;
	}

	public void setRepeat_detail(String repeat_detail) {
		this.repeat_detail = repeat_detail;
	}

	public String getTrans_count() {
		return trans_count;
	}

	public void setTrans_count(String trans_count) {
		this.trans_count = trans_count;
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

	public String getRepeat_begindate() {
		return repeat_begindate;
	}

	public void setRepeat_begindate(String repeat_begindate) {
		this.repeat_begindate = repeat_begindate;
	}

	public String getRepeat_enddate() {
		return repeat_enddate;
	}

	public void setRepeat_enddate(String repeat_enddate) {
		this.repeat_enddate = repeat_enddate;
	}

}
