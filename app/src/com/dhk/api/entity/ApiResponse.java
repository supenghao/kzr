package com.dhk.api.entity;

import java.util.List;

public class ApiResponse {

	private String result;
	private String userid;
	private String token;
	private String errorCode;
	private String errorMsg;
	private List<Object> messageList;

	/**
	 * 获取 result 变量
	 * 
	 * @return 返回 result 变量
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 设置 result 变量
	 * 
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 获取 userid 变量
	 * 
	 * @return 返回 userid 变量
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * 设置 userid 变量
	 * 
	 * @param userid
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * 获取 token 变量
	 * 
	 * @return 返回 token 变量
	 */
	public String getToken() {
		return token;
	}

	/**
	 * 设置 token 变量
	 * 
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 获取 errorCode 变量
	 * 
	 * @return 返回 errorCode 变量
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 设置 errorCode 变量
	 * 
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 获取 errorMsg 变量
	 * 
	 * @return 返回 errorMsg 变量
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * 设置 errorMsg 变量
	 * 
	 * @param errorMsg
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * 获取 messageList 变量
	 * 
	 * @return 返回 messageList 变量
	 */
	public List<Object> getMessageList() {
		return messageList;
	}

	/**
	 * 设置 messageList 变量
	 * 
	 * @param messageList
	 */
	public void setMessageList(List<Object> messageList) {
		this.messageList = messageList;
	}
}
