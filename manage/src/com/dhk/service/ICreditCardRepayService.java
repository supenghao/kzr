package com.dhk.service;

import java.math.BigDecimal;

import com.dhk.entity.RepayPlanDetail;
import com.dhk.payment.PayResult;

public interface ICreditCardRepayService {
	/**
	 * 信用卡还款
	 * @param rpd
	 * @return
	 */
	public PayResult creditRepay( RepayPlanDetail rpd);
	
	/**
	 * 信用卡还款 资金不过夜模式
	 * @param rpd
	 * @return
	 */
	public PayResult creditRepayDate( RepayPlanDetail rpd);
	
	
	public void txBalanceException(Long userId,RepayPlanDetail rpd,BigDecimal transAmount);
	
	public PayResult repayPlanAgain(String orderNo,String transno);
	
	public PayResult reCreatOrder(RepayPlanDetail rpd);
}
