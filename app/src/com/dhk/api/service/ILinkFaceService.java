package com.dhk.api.service;

import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.QResponse;

/**
 * 验证身份证和银行卡接口
 * @author supenghao
 *
 */
public interface ILinkFaceService {
	
	/**
	 * 验证身份证
	 * @return
	 */
	QResponse validateIdCard(IdentityDto dto,String imgPath);
	
	/**
	 * 验证银行卡
	 * @return
	 */

	QResponse validateBankCard(IdentityDto dto, String imgPath);

}
