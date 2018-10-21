package com.dhk.api.dto;

public class IdentityDto extends SuperDto {

	// 获取用户数据接口参数
	private String token, userId;

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
	 * 获取 userId 变量
	 * 
	 * @return 返回 userId 变量
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置 userId 变量
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ",token=" + token + ", userId=" + userId + super.toString();
	}

}
