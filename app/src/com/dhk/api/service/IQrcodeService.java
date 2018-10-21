package com.dhk.api.service;

import com.dhk.api.entity.Qrcode;

/**
 * t_s_org_qrcode service 接口<br/>
 * 2017-02-09 03:34:34 qch
 */
public interface IQrcodeService {

	/**
	 * 通过邀请码获取一条记录
	 * 
	 * @param invitationCode
	 * @return
	 */
	Qrcode getQrcodeByInvitation(String invitationCode);

	/**
	 * 修改二维码的使用状态
	 * 
	 * @param string
	 */
	void changeQrcodeState(String id, String state);
}
