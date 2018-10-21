package com.dhk.api.third;

public class JdBankCardVerifyObj {

	private String code;
	private String charge;
	private String msg;
	private JdBankCardVerifyData result;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public JdBankCardVerifyData getResult() {
		return result;
	}
	public void setResult(JdBankCardVerifyData result) {
		this.result = result;
	}
	
}
