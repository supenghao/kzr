package com.dhk.api.service;

import java.util.List;

import com.dhk.api.entity.ParamFee;

/**
 * t_param_fee service 接口<br/>
 * 2017-02-11 08:45:05 qch
 */
public interface IParamFeeService {

	/**
	 * 获取系统费率信息
	 * 
	 * @return
	 */
	List<ParamFee> getAllUseParam(String status);

	/**
	 * 获取系统费率信息
	 * 
	 * @return
	 */
	ParamFee getUseParamByCode(String code);
	
	
	ParamFee findBy(String code);


	String  getRepayFeeStr();
}
