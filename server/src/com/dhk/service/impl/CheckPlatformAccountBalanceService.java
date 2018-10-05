package com.dhk.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.entity.OperatorsAccout;
import com.dhk.payment.PayResult;
import com.dhk.service.ICallRemotePayService;
import com.dhk.service.ICheckPlatformAccountBalanceService;
import com.dhk.service.IOperatorsAccoutService;
import com.sunnada.uaas.service.ISystemParamService;
@Service("CheckPlatformAccountBalanceService")
public class CheckPlatformAccountBalanceService implements ICheckPlatformAccountBalanceService{
	
	@Resource(name = "operatorsAccoutService")
	private IOperatorsAccoutService operatorsAccoutService;
	
	@Resource(name = "systemParamService")
	ISystemParamService systemParamService;
	
	@Resource(name = "CallRemotePayService")
	private ICallRemotePayService callRemotePayService;
	/**
	 * 检查运营商信用卡账户金额是否正确
	 */
	public boolean checkPlatformCreditBalance() {
		String isCheck=systemParamService.findByParamName("is_check_balance").getParam_text();
		if ("F".equals(isCheck)) {//F则不进行检查
			return true;
		}
		
		OperatorsAccout operAccount = operatorsAccoutService.find();
		BigDecimal credit = operAccount.getCreditcard_balance();
		try {
			PayResult payResult = callRemotePayService.queryBankCardBalance("credit");
			if ("0000".equals(payResult.getCode())) {
				return checkBalance(new BigDecimal(payResult.getData().getAvaBal()).setScale(2, RoundingMode.HALF_UP),
						credit.setScale(2, RoundingMode.HALF_UP));
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * 检查运营商借记账户金额是否正确
	 */
	public boolean checkPlatformDebiteBalance() {
		String isCheck=systemParamService.findByParamName("is_check_balance").getParam_text();
		if ("F".equals(isCheck)) {//F则不进行检查
			return true;
		}
		
		OperatorsAccout operAccount = operatorsAccoutService.find();
		BigDecimal debit = operAccount.getDebitecard_balance();
		try {
			PayResult payResult = callRemotePayService.queryBankCardBalance("debit");

			if ("0000".equals(payResult.getCode())) {
				System.out.println(">>>>>>>>>>>>>查询借记金额：" + payResult.getData().getAvaBal());
				return checkBalance(
						new BigDecimal(payResult.getData().getAvaBal()).setScale(2, RoundingMode.HALF_UP), 
						debit.setScale(2, RoundingMode.HALF_UP));
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	/**
	 * 检查账户金额
	 * @param bankBalance 银行账户金额
	 * @param balance 平台账户金额
	 * @return
	 */
	private boolean checkBalance(BigDecimal bankBalance, BigDecimal balance) {
		
		BigDecimal min = new BigDecimal("0");
		BigDecimal max = new BigDecimal("100");
		BigDecimal diff = bankBalance.subtract(balance.multiply(new BigDecimal("100")));
		if (diff.compareTo(min) == 0 || diff.compareTo(max) < 0) {
			return true;
		}
		return false;

	}

}
