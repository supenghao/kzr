package com.dhk.service;

import com.dhk.entity.APPUser;

   /**
    * t_s_user service 接口<br/>
    * 2017-01-17 10:05:35 Gnaily
    */ 
public interface IAPPUserService {
	/**
	 * 根据用户ID查询用户
	 * @param id
	 * @return
	 */
	public APPUser findById(long id);
	
	/**
	 * 根据用户ID和手机号产生订单号
	 * @param userId
	 * @param phoneNum
	 * @return
	 */
	public String getOrderNo(long userId, String phoneNum);
}

