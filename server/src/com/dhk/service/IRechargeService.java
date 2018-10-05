package com.dhk.service;

import com.dhk.entity.APPUser;
import com.dhk.payment.PayRequest;
import com.dhk.payment.PayResult;

/**
 * 充值接口
 * @author y1iag
 *
 */
public interface IRechargeService {
	/**
	 * app用户充值
	 * @param payRequest
	 * @param user
	 * @return
	 */
	public  PayResult userRecharge(PayRequest payRequest, APPUser user);
}
