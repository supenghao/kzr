package com.dhk.api.service;

import com.dhk.api.dto.MemberInfoDto;

/**
 * t_s_memberinfo service 接口<br/>
 * 2016-12-21 05:04:13 qch
 */
public interface IMemberinfoService {


	/**
	 * 修改用户的个人信息
	 * 
	 * @param dto
	 * @return
	 */
	Boolean editMemberInfo(MemberInfoDto dto);


}
