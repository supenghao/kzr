package com.aimi.bean.base;

public class BaseBeanResponse {
	public String code;//状态码
	public String msg;//返回信息描述
	public String sign;//签名
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	

}
