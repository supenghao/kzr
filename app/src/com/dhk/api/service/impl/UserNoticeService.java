package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.IUserNoticeDao;
import com.dhk.api.entity.UserNotice;
import com.dhk.api.service.IUserNoticeService;
import org.springframework.stereotype.Service;

@Service("userNoticeService")
public class UserNoticeService implements IUserNoticeService {

	@Resource(name = "userNoticeDao")
	private IUserNoticeDao userNoticeDao;
	
    public UserNotice findByUserId(Long userId){
    	String sql = "select * from t_s_user_notice where userId=:userId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		List<UserNotice> uns = userNoticeDao.find(sql, map);
		
		UserNotice un = null;
		if (uns!=null && !uns.isEmpty() && uns.size()>0){
			un = uns.get(0);
		}
		return un;
    }
	
	public int updateMaxNoticeId(Long userId,Long maxNoticeId){
		UserNotice un = findByUserId(userId);
		String sql = "";
		if (un==null){
			sql = "insert into t_s_user_notice (userId,maxNoticeId) values (:userId,:maxNoticeId)";
		}else{
			sql = "update t_s_user_notice set maxNoticeId=:maxNoticeId where userId=:userId";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("maxNoticeId", maxNoticeId);
		
		return userNoticeDao.update(sql, map);
		
	}
	
	
}
