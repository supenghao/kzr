package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dhk.api.dao.IParamFeeDao;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.service.IParamFeeService;

@Service("ParamFeeService")
public class ParamFeeService implements IParamFeeService {

	@Resource(name = "ParamFeeDao")
	private IParamFeeDao paramFeeDao;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	@Cacheable(value="t_param_fee", key="'getAllUseParam_'+#bank4code")
	public List<ParamFee> getAllUseParam(String status) {
		String sql = "select * from t_param_fee where status=:status";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		return paramFeeDao.find(sql, map);
	}

	@Override
	@Cacheable(value="t_param_fee", key="'getUseParamByCode_'+#bank4code")
	public ParamFee getUseParamByCode(String code) {
		String sql = "select * from t_param_fee where status=:status and code=:code;";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "0");
		map.put("code", code);
		return paramFeeDao.find(sql, map).get(0);
	}
	
	@Cacheable(value="t_param_fee", key="'findByParamFee_'+#code")
	public ParamFee findBy(String code) {	
		String sql = "select * from t_param_fee where status=:status and code=:code;";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "0");
		map.put("code", code);
		return paramFeeDao.find(sql, map).get(0);
	}

	@Override
	@Cacheable(value="getRepayFeeStr", key="")
	public String getRepayFeeStr() {
		Map map =jdbcTemplate.queryForMap("select (select fee from t_param_fee where code ='purchase') purchase,(select fee from t_param_fee where code ='quickl_proxy_pay') proxypay  from dual");
		return "交易"+map.get("purchase")+"%，还款"+map.get("proxypay")+"元/笔";
	}
}
