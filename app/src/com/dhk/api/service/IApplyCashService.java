package com.dhk.api.service;

import java.util.List;

import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RechargeDto;
import com.dhk.api.entity.ApplyCash;

/**
 * t_s_user_apply_cash service 接口<br/>
 * 2017-02-14 10:48:52 qch
 */
public interface IApplyCashService {

	/**
	 * 提现申请
	 * 
	 * @param dto
	 * @return
	 */
	QResponse insertApplyCash(RechargeDto dto);

	List<ApplyCash> getApplyCashByUserid(String userid);

	/**
	 * 获得正在提现的总金额
	 * 
	 * @param userId
	 * @return
	 */
	double getCurrentApplyCash(String userId);

	void insertApplyCash(String userId, String amount);
}
