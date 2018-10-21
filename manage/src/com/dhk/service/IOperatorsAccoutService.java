package com.dhk.service;
import java.math.BigDecimal;

import com.dhk.entity.OperatorsAccout;

   /**
    * operators_accout service 接口<br/>
    * 2017-02-19 09:02:11 bianzk
    */ 
public interface IOperatorsAccoutService {
	/**
	 * 更新运营商信用卡资金
	 * 目前没什么用，只是记录金额20170824
	 * @param cost
	 */
	public void doUpdateCredit(BigDecimal cost);

	/**
	 * 查詢运营商账户信息
	 * @return
	 */
	public OperatorsAccout find();

}

