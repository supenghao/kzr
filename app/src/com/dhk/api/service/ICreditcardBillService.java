package com.dhk.api.service;

import java.util.List;

import com.dhk.api.entity.CreditcardBill;

/**
 * t_s_creditcard_bill service 接口<br/>
 * 2017-01-14 04:04:17 qch
 */
public interface ICreditcardBillService {

	List<CreditcardBill> getBillList(String creditcardId);

	/**
	 * 获取最新的信用卡账单,不在有效时间返回null
	 * 
	 * @param CREDITCARD_ID
	 * @return
	 */
	CreditcardBill getLastestBill(String CREDITCARD_ID);
}
