package com.dhk.api.dto;

public class UpdatePwdDto extends IdentityDto {
	
	private String loginPwd, newPwd, checkCode;

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
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

	/**
	 * 获取 newPwd 变量
	 * 
	 * @return 返回 newPwd 变量
	 */
	public String getNewPwd() {
		return newPwd;
	}

	/**
	 * 设置 newPwd 变量
	 * 
	 * @param newPwd
	 */
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	@Override
	public String toString() {
		return super.toString() + "UpdatePwdDto [loginPwd=" + loginPwd
				+ ", newPwd=" + newPwd + "]";
	}

}
