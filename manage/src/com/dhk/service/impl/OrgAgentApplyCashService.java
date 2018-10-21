package com.dhk.service.impl;

import com.dhk.dao.IOrgAgentApplyCashDao;
import com.dhk.entity.OrgAgentApplyCash;
import com.dhk.service.IOrgAgentApplyCashService;
import com.dhk.service.IOrgService;
import com.sunnada.kernel.sql.SQLConf;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("OrgAgentApplyCashService")
public class OrgAgentApplyCashService implements IOrgAgentApplyCashService {
	@Resource(name = "OrgAgentApplyCashDao") 
	private IOrgAgentApplyCashDao orgAgentApplyCashDao;
	
	@Resource(name = "OrgService")
	private IOrgService orgService;

	public boolean updateStatus(String status,String id) {
		String sql = SQLConf.getSql("orgagent_apply_cash", "updateStatus");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("status", status);
		paramMap.put("id", id);

		try {
			if( orgAgentApplyCashDao.update(sql,paramMap)>0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Integer orgByIdUpdateStatus(Long applyId,String status,String date ,String time) {
		String sql = SQLConf.getSql("orgagent_apply_cash", "orgByIdUpdateStatus");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", applyId);
		paramMap.put("status", status);
		paramMap.put("auth_date", date);
		paramMap.put("auth_time", time);
		return orgAgentApplyCashDao.update(sql, paramMap);
	}

	//查询ApplyId
	public OrgAgentApplyCash findApplyId(Long orgId, String status, BigDecimal money) {
		String sql = SQLConf.getSql("orgagent_apply_cash", "findApplyId");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ORG_ID", orgId);
		paramMap.put("STATUS", status);
		paramMap.put("AMOUNT", money);
		List<OrgAgentApplyCash> user=orgAgentApplyCashDao.find(sql, paramMap);
		if(user!=null && !user.isEmpty() && user.size()>0){
			return user.get(0);
		}
		return null;
	}

	//查询ApplyId
	public OrgAgentApplyCash findApplyId(String applyId) {
		String sql = "select *from t_s_org_apply_cash where id=:id";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id",applyId);
		List<OrgAgentApplyCash> user=orgAgentApplyCashDao.find(sql, paramMap);
		if(user!=null && !user.isEmpty() && user.size()>0){
			return user.get(0);
		}
		return null;
	}
	
}

