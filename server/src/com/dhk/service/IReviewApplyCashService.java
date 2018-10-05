package com.dhk.service;

import java.math.BigDecimal;

import com.dhk.payment.PayResult;
/**
 * 提现审核
 * @author y1iag
 *
 */
public interface IReviewApplyCashService {
	public PayResult applyCash( String applyId,boolean isPass);
	public PayResult applyCashForOrg(String applyId,boolean isPass);

	public PayResult operatorsCash(BigDecimal amount);
}
