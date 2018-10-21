package com.dhk.api.service;

import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.UserMsgDto;

/**
 * t_s_message service 接口<br/>
 * 2017-02-11 12:00:36 qch
 */
public interface IUserMsgService {

	/**
	 * 获取用户的短消息
	 * 
	 * @param dto
	 * @return
	 */
	QResponse getShortMsg(UserMsgDto dto);
}
