package com.dhk.payment.entity.result;

public class HXPayDataReult {
	private String third_merchant_code;
	private String resp_code;
	private String resp_msg;
	private String status;
	private String message;
	private String err_code;
	private String err_msg;
	private String withdrawals_status;
	private String withdrawals_msg;
	public String getThird_merchant_code() {
		return third_merchant_code;
	}
	public void setThird_merchant_code(String third_merchant_code) {
		this.third_merchant_code = third_merchant_code;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	public String getWithdrawals_status() {
		return withdrawals_status;
	}
	public void setWithdrawals_status(String withdrawals_status) {
		this.withdrawals_status = withdrawals_status;
	}
	public String getWithdrawals_msg() {
		return withdrawals_msg;
	}
	public void setWithdrawals_msg(String withdrawals_msg) {
		this.withdrawals_msg = withdrawals_msg;
	}
	
	
}
