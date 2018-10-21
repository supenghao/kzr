package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.IOrgrateDao;
import com.dhk.api.service.IOrgrateService;
import org.springframework.stereotype.Service;

@Service("OrgrateService")
public class OrgrateService implements IOrgrateService {
	@Resource(name = "OrgrateDao")
	private IOrgrateDao orgrateDao;

	@Override
	public boolean updateDiffRateInfo(String orgid, String qrcodeid,
			Double max_rate) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "update t_s_org_rate set diff_rate =(:max_rate-rate) where org_id =:org_id and qrcode_id=:qrcode_id";
		map = new HashMap<String, Object>();
		map.put("org_id", orgid);
		map.put("qrcode_id", qrcodeid);
		map.put("max_rate", max_rate);
		orgrateDao.update(sql, map);
		return true;
	}
}
