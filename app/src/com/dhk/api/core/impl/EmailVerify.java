package com.dhk.api.core.impl;

import com.dhk.api.core.Verify;
import com.dhk.api.dto.AddCreditCarDto;

/**
 * 验证邮箱是否有效
 * 
 */
public class EmailVerify implements Verify {

	private String error = null;

	public EmailVerify(AddCreditCarDto dto, String string) {

	}

	@Override
	public boolean isValueable() {
		// 待实现具体逻辑
		return true;
	}

	@Override
	public String errorMsg() {
		return error;
	}
}
