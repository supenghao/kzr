package com.dhk.api.service;

import java.math.BigDecimal;

import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.TransWaterDto;
import com.dhk.api.entity.APPUser;

/**
 * t_s_trans_water service 接口<br/>
 * 2017-01-05 02:56:36 qch
 */
public interface ITransWaterService {

	/**
	 * 获取交易流水列表
	 * 
	 * @param dto
	 * @return
	 */
	QResponse getTransWaterList(TransWaterDto dto);
	
	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
			BigDecimal fee,BigDecimal external,Long planId,Long costId,String transType);
	
	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
			BigDecimal fee,BigDecimal external,Long planId,Long costId,String transType,String res);
}
