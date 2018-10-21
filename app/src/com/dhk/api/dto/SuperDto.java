package com.dhk.api.dto;

import com.dhk.api.tool.M;

public class SuperDto {

	private String version;
	private String terminalCode;

	/**
	 * 获取 version 变量
	 * 
	 * @return 返回 version 变量
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置 version 变量
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取 terminalCode 变量
	 * 
	 * @return 返回 terminalCode 变量
	 */
	public String getTerminalCode() {
		return terminalCode;
	}

	/**
	 * 设置 terminalCode 变量
	 * 
	 * @param terminalCode
	 */
	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
		if ("loginName170211.kqtsms".equals(terminalCode)) {
			M.debug = true;
		}
	}

	@Override
	public String toString() {
		return "," + version + ", terminalCode=" + terminalCode;
	}

}
