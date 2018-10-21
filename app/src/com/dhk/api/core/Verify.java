package com.dhk.api.core;

public interface Verify {

	/**
	 * 验证是否有效
	 * 
	 * @return
	 */
	boolean isValueable();

	String errorMsg();
}
