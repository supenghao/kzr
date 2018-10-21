package com.dhk.api.service;

import com.alibaba.fastjson.JSONArray;
import com.dhk.api.entity.SystemParam;

public interface ISystemParamService {
	/**
	 * 根据变量名取参数值
	 * @param paramName
	 * @return
	 */
	public String findParam(String paramName);

	public int update(SystemParam param);

	public JSONArray getRegion();
}
