package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import com.dhk.api.dao.ISystemParamDao;
import com.dhk.api.entity.SystemParam;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dhk.api.service.ISystemParamService;
import com.xdream.kernel.sql.SQLConf;

@Service("systemParamService")
public class SystemParamService implements ISystemParamService{

	@Resource(name = "systemParamDao")
	private ISystemParamDao systemParamDao;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	



	/**
	 * 根据变量名取参数值
	 * @param paramName
	 * @return
	 */
	@Cacheable(value="t_system_param", key="#paramName")
	public String findParam(String paramName){
		SystemParam systemParam = null;

		String sql = SQLConf.getSql("system_param", "findByParamName");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("param_name", paramName);
		try{
			systemParam = systemParamDao.findBy(sql, map);
		}catch(Exception e){
			systemParam = new SystemParam();
		}
		return systemParam.getParam_text();
	}

	public int update(SystemParam param){
		String sql = SQLConf.getSql("system_param", "update");
		int i= systemParamDao.updateBy(sql, param);
		return i;
	}



	/**
	 * @return
	 */
	@Cacheable(value="t_region", key="")
	public JSONArray getRegion(){
		List<Map<String, Object>> list =jdbcTemplate.queryForList("select *from t_region");

		return (JSONArray)JSONArray.toJSON(list);
	}
}
