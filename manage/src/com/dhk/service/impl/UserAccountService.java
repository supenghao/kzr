package com.dhk.service.impl;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.IUserAccountDao;
import com.dhk.entity.UserAccount;
import com.dhk.service.IRepayRecordService;
import com.dhk.service.IUserAccountService;
import com.sunnada.kernel.sql.SQLConf;
@Service("UserAccountService")
public class UserAccountService implements IUserAccountService {
	@Resource(name = "UserAccountDao") 
	private IUserAccountDao userAccountDao;
	
	@Resource(name = "repayRecordService") 
	private IRepayRecordService repayRecordService;

	
	
	
	public synchronized boolean  seriUpdateBalance(UserAccount ua) {
		String sql=SQLConf.getSql("useraccount", "updateBalance");
		if(userAccountDao.updateBy(sql,ua)>0){
    		return true;
    	}else{
    		return false;
    	}
	}
	
	public synchronized boolean  seriUpdateRecashFreeze(UserAccount ua) {
		String sql=SQLConf.getSql("useraccount", "updateRecashFreeze");
		if(userAccountDao.updateBy(sql,ua)>0){
    		return true;
    	}else{
    		return false;
    	}
		
	}
	
	
	public synchronized boolean seriUpdateBalanceAndRecashFreeze(UserAccount ua) {
		String sql=SQLConf.getSql("useraccount", "updateBalanceAndRecashFreeze");
			if(userAccountDao.updateBy(sql,ua)>0){
	    		return true;
	    	}else{
	    		return false;
	    	}
	}

	
	

	public UserAccount findByUserId(Long userId) {
		String sql=SQLConf.getSql("useraccount", "findByUserId");
		Map<String , Object> map=new HashMap<String, Object>();
		
		map.put("userId", userId);
		try {
			return userAccountDao.findBy(sql, map);
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	//添加用户金额
	public void upUserAccount(BigDecimal freeze, String date, String time, Long userId) {
		String sql="update t_s_user_account set CUR_BALANCE=CUR_BALANCE+:freeze,UPDATE_DATE=:UPDATE_DATE,UPDATE_TIME=:UPDATE_TIME where USER_ID=:userId";
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("freeze", freeze);
		map.put("userId", userId);
		map.put("UPDATE_DATE", date);
		map.put("UPDATE_TIME", time);
		userAccountDao.update(sql, map);
	}

	public void doDuctionRecashFreezeMoney(BigDecimal freeze, String date,String time, Long userId) {
		String sql=SQLConf.getSql("useraccount", "deductionRecashFreezeMoney");
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("freeze", freeze);
		map.put("userId", userId);
		map.put("UPDATE_DATE", date);
		map.put("UPDATE_TIME", time);
		userAccountDao.update(sql, map);
	}

	public void disposeUserMoney(BigDecimal freeze, String date, String time,Long userId) {
		String sql=SQLConf.getSql("useraccount", "updateUserMoney");
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("freeze", freeze);
		map.put("userId", userId);
		map.put("UPDATE_DATE", date);
		map.put("UPDATE_TIME", time);
		userAccountDao.update(sql, map);
	}

}

