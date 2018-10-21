package com.dhk.service.impl;

import com.dhk.dao.IAPPUserApplyCashDao;
import com.dhk.entity.APPUserApplyCash;
import com.dhk.service.IAPPUserApplyCashService;
import com.dhk.service.IUserAccountService;
import com.sunnada.kernel.dao.Pager;
import com.sunnada.kernel.sql.SQLConf;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("APPUserApplyCashService")
public class APPUserApplyCashService implements IAPPUserApplyCashService {
	@Resource(name = "APPUserApplyCashDao") 
	private IAPPUserApplyCashDao appUserApplyCashDao;
	
	@Resource(name = "UserAccountService")
	private IUserAccountService UserAccountService;

	public Pager<APPUserApplyCash> findByPager(int curPage, int pages, String param, Map<String,Object> paramMap) throws Exception{
		Pager<APPUserApplyCash> pager = Pager.getPager();
		pager.setCurPageNum(curPage);
		pager.setPageSize(pages);
		
		String sql = SQLConf.getSql("appuser_apply_cash", "findByPager");
		
		if (!StringUtils.isBlank(param)){
			sql = sql + param;
		}
		/*else{
			paramMap = null;
		}*/
		pager = appUserApplyCashDao.findForPage2OrderBy(sql, paramMap, pager,null);
		
		return pager;
	}

	public APPUserApplyCash findById(Long id) {
		String sql = SQLConf.getSql("appuser_apply_cash", "findById");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		try {
			return  appUserApplyCashDao.findBy(sql, map);
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean updateStatus(APPUserApplyCash appUserApplyCash){
		String sql = SQLConf.getSql("appuser_apply_cash", "updateStatus");
		try {
			if( appUserApplyCashDao.updateBy(sql,appUserApplyCash)>0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateStatus(Long id, String status ){
		String sql = "update t_s_user_apply_cash  set  STATUS=:status where id=:id";
		try {
			Map map = new HashMap();
			map.put("id",id);
			map.put("status",status);
			if( appUserApplyCashDao.update(sql,map)>0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//统计当前提现成功总额
		public APPUserApplyCash countAgentApplyCash(Long orgId,String status) {
			String sql = SQLConf.getSql("appuser_apply_cash", "countAgentApplyCash");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("USER_ID", orgId);
			paramMap.put("STATUS", status);
			List<APPUserApplyCash> user=appUserApplyCashDao.find(sql, paramMap);
			if(user!=null && !user.isEmpty() && user.size()>0){
				return user.get(0);
			}
			return null;
		}


		public void renewStatus(Long cashId, String Status,String date,String time,Long operId) {
			String sql = SQLConf.getSql("appuser_apply_cash", "byIdUpdateStatus");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("id", cashId);
			paramMap.put("STATUS", Status);
			paramMap.put("auth_date", date);
			paramMap.put("auth_time", time);
			paramMap.put("OPER_ID", operId);
			appUserApplyCashDao.update(sql, paramMap);
		}

		public void renewStatus(Long cashId, String Status,String date,String time) {
			String sql = "update t_s_user_apply_cash set STATUS=:STATUS,AUTH_DATE=:auth_date,AUTH_TIME=:auth_time where id=:id";
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("id", cashId);
			paramMap.put("STATUS", Status);
			paramMap.put("auth_date", date);
			paramMap.put("auth_time", time);
			appUserApplyCashDao.update(sql, paramMap);
		}

		public APPUserApplyCash findApplyId(Long userId, String status,BigDecimal money) {
			
			String sql = SQLConf.getSql("appuser_apply_cash", "findApplyId");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("USER_ID", userId);
			paramMap.put("STATUS", status);
			paramMap.put("AMOUNT", money);
			List<APPUserApplyCash> user=appUserApplyCashDao.find(sql, paramMap);
			if(user!=null && !user.isEmpty() && user.size()>0){
				return user.get(0);
			}
			return null;
		}
	
}

