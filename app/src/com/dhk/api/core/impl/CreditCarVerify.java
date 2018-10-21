package com.dhk.api.core.impl;

import com.dhk.api.core.Verify;
import com.dhk.api.tool.M;
import com.dhk.api.dto.AddCreditCarDto;

/**
 * 验证信用卡是否有效
 * 
 */
public class CreditCarVerify implements Verify {

	private String error;
	private Long transAmt;
	private String phoneNo;
	private String customerName;
	private String cerdType;
	private String cerdId;
	private String acctNo;
	private String cvn2;
	private String expDate;

	public CreditCarVerify(AddCreditCarDto dto, String url) {
		transAmt = 1l;
		phoneNo = dto.getPhoneNo();
		customerName = dto.getRealname();
		cerdType = "01";
		cerdId = dto.getCerdId();
		acctNo = dto.getCardNo();
		cvn2 = dto.getCvn2();
		expDate = dto.getExpDate();
	}

	@Override
	public boolean isValueable() {
		
		if (M.debug) {
			return true;
		}
		// 待实现具体逻辑
		return true;
	}

	@Override
	public String errorMsg() {
		return error;
	}

}
