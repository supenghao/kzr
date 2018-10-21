package com.dhk.api.service;

import com.dhk.api.entity.BankCss;

/**
 * t_s_bank_css service 接口<br/>
 * 2017-02-16 07:01:40 qch
 */
public interface IBankCssService {

	/**
	 * 根据Cardbin里的BankCode前4位获取css信息
	 * 
	 * @param bankname
	 * @return
	 */
	BankCss getBankcssByBank4code(String bankname);
}
