package com.dhk.api.dto;

public class LoginDto extends SuperDto {

	private String loginName, loginPwd;

	/**
	 * 获取 loginName 变量
	 * 
	 * @return 返回 loginName 变量
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * 设置 loginName 变量
	 * 
	 * @param loginName
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 获取 loginPwd 变量
	 * 
	 * @return 返回 loginPwd 变量
	 */
	public String getLoginPwd() {
		return loginPwd;
	}

	/**
	 * 设置 loginPwd 变量
	 * 
	 * @param loginPwd
	 */
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

}
