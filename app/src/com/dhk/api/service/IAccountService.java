package com.dhk.api.service;

import com.alibaba.fastjson.JSONObject;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.entity.Account;
import com.dhk.api.dto.RechargeDto;

import javax.servlet.http.HttpServletRequest;

/**
 * t_s_user_account service 接口<br/>
 * 2017-02-09 03:49:52 qch
 */
public interface IAccountService {

	/**
	 * 插入账户
	 * 
	 * @param userid
	 */
	void insertAaccount(int userid);

	/**
	 * 获取用户账户
	 * 
	 * @param dto
	 * @return
	 */
	QResponse getUserAccount(IdentityDto dto);

	Account getUserAccount(String user_id);

	/**
	 * 充值
	 * 
	 * @param dto
	 * @return
	 */
	QResponse txRecharge(RechargeDto dto);

	/**
	 * 充值 +绑定
	 *
	 * @param dto
	 * @return
	 */
	JSONObject rechargeWithBindCard(RechargeDto dto, HttpServletRequest request);

	/**
	 * 提现申请
	 * 
	 * @param dto
	 * @return
	 */
	QResponse seriWithdrawCash(RechargeDto dto,HttpServletRequest request);

	QResponse getWithdrawCash(IdentityDto dto);

	boolean subBalance(String userId, double am);
	
	boolean addBalance(String userId, double am);

}
