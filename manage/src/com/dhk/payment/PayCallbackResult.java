package com.dhk.payment;

public class PayCallbackResult {

	private String client_trans_id;
	
	private String resp_code;
	
	private String resp_msg;
	
	private String err_code;
	
	private String err_msg;
	
	private String sign;

	public String getClient_trans_id() {
		return client_trans_id;
	}

	public void setClient_trans_id(String client_trans_id) {
		this.client_trans_id = client_trans_id;
	}

	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	
	
}
