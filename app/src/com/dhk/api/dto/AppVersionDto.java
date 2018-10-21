package com.dhk.api.dto;

public class AppVersionDto extends SuperDto {

	private String versionType;

	/**
	 * 获取 versionType 变量
	 * 
	 * @return 返回 versionType 变量
	 */
	public String getVersionType() {
		return versionType;
	}

	/**
	 * 设置 versionType 变量
	 * 
	 * @param versionType
	 */
	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	@Override
	public String toString() {
		return "AppVersionDto [versionType=" + versionType + super.toString()
				+ "]";
	}

}
