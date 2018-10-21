package com.dhk.api.service;

import com.dhk.api.dto.IdentityDto;
import com.dhk.api.entity.Token;

/**
 * t_s_token service 接口<br/>
 * 2016-12-20 11:46:56 qch
 */
public interface ITokenService {
	void updateToken(Token t);

	/**
	 * 通过用户id查找token
	 * 
	 * @param user_id
	 * @return
	 */
	Token findTokenById(Object userId);

	/**
	 * 通过用户名查找token
	 * 
	 * @param userName
	 * @return
	 */
	Token findTokenByName(String userName);

	void insertToken(Token t);

	boolean checkToken(String userid, String token);

	boolean checkToken(IdentityDto dto);
}
