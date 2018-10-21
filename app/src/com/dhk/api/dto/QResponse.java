package com.dhk.api.dto;

public class QResponse {

	public final static QResponse OK = new QResponse();
	public final static QResponse ERROR = new QResponse(false, "系统错误");
	public final static QResponse ERROR_SECURITY = new QResponse(false, "登录状态异常,请重新登录");
	public final static String EMPTY = "";
	/**
	 * 相应的消息
	 */
	public String msg;
	public String code;


	/**
	 * 相应的状态
	 */
	public boolean state;
	/**
	 * 相应的数据
	 */
	public Object data;

	public QResponse(boolean state, String msg) {
		this.msg = msg;
		this.state = state;
	}

	/**
	 * state = true,data
	 * 
	 * @param data
	 */
	public QResponse(Object data) {
		this.data = data;
		this.state = true;
	}

	public QResponse() {
		this.state = true;
	}

	public static QResponse newInstance(boolean state, String msg) {
		return new QResponse(state, msg);
	}
}
