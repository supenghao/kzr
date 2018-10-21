package com.dhk.api.service;

import com.dhk.api.entity.User;
import com.dhk.api.entity.Token;
import com.dhk.api.dto.AddUserCarDto;
import com.dhk.api.dto.ForgetPwdDto;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RegistDto;
import com.dhk.api.dto.UpdatePwdDto;
import com.dhk.api.dto.loginOutDto;

import javax.servlet.http.HttpServletRequest;

/**
 * t_s_user service 接口<br/>
 * 2016-12-30 10:32:56 qch
 */
public interface IUserService {

	QResponse login(String userName, String pwd);

	QResponse updatePwd(UpdatePwdDto dto);

	Token updatePwd(ForgetPwdDto dto);

	boolean loginOut(loginOutDto dto);

	QResponse txInsertUser(RegistDto dto);
	QResponse txInsertUserNew(RegistDto dto);
	/**
	 * 获取绑定信用卡时的基本信息
	 * 
	 * @param dto
	 * @return
	 */
	QResponse getBaseInfo(IdentityDto dto);

	/**
	 * 添加用户储蓄卡兼实名认证
	 * 
	 * @param dto
	 * @return
	 */
	QResponse addUserCard(AddUserCarDto dto, HttpServletRequest request);
	QResponse addUserCardNew(AddUserCarDto dto, HttpServletRequest request);
	/**
	 * 获取用户的手机号码
	 * 
	 * @param loginName
	 * @return
	 */
	String getUserPhoneNo(String loginName);

	/**
	 * 检查账户状态
	 * 
	 * @param userid
	 * @return
	 */
	boolean checkUserState(String userid);

	User getUserById(String userId);
}
