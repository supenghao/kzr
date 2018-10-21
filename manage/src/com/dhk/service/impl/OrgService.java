package com.dhk.service.impl;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.IOrgDao;
import com.dhk.entity.Org;
import com.dhk.service.IOrgService;
import com.sunnada.kernel.sql.SQLConf;
@Service("OrgService")
public class OrgService implements IOrgService {
	@Resource(name = "OrgDao") 
	private IOrgDao orgDao;

	public Org findById(Long id){
		Org org = null;
		String sql = SQLConf.getSql("org", "findById");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		try{
			org = orgDao.findBy(sql, map);
		}catch(Exception e){
			//e.printStackTrace();
			return null;
		}
		return org;
	}

	public synchronized int  seriUpdateBalance(Long orgId,BigDecimal balance){
		String sql = SQLConf.getSql("org", "updateBalance");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("balance", balance);
		map.put("orgId", orgId);
		return orgDao.update(sql, map);
	}
	public synchronized int  seriUpdateRecashFreeze(Long orgId,BigDecimal cash){
		String sql = SQLConf.getSql("org", "updateRecashFreeze");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("inAccount", cash);
		map.put("id", orgId);
		return orgDao.update(sql,map);
	}

	public boolean seriUpdateBalanceAndRecashFreeze(Org org) {

		String sql = SQLConf.getSql("org", "updateBalanceAndRecashFreeze");

		if( orgDao.updateBy(sql,org)>0){
			return true;
		}else{
			return false;
		}
	}

}

