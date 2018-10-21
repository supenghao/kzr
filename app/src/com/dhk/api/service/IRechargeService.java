package com.dhk.api.service;

import com.dhk.api.dto.IdentityDto;

/**
 * t_s_recharge service 接口<br/>
 * 2017-02-15 10:45:22 qch
 */
public interface IRechargeService {

	/**
	 * 更新recharge表
	 * 
	 * @param dto
	 * @param amount
	 *            变化差值
	 * @return
	 */
	boolean updateRecharge(IdentityDto dto, double amount);

	/**
	 * 获得数据
	 * 
	 * @param dto
	 * @return
	 */
	double getRecharge(IdentityDto dto);

	/**
	 * 插入一条记录
	 * 
	 * @param dto
	 */
	void insertRecharge(int id);
}
