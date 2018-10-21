package com.dhk.api.dto;


public class RegistDto extends SuperDto {

	private String loginName, loginPwd, checkCode, invitationCode;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	@Override
	public String toString() {
		return "RegistDto [loginName=" + loginName + ", loginPwd=" + loginPwd
				+ ", checkCode=" + checkCode + ", invitationCode="
				+ invitationCode + "]";
	}
}
