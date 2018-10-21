package com.dhk.api.service;

import com.dhk.api.entity.UserNotice;

public interface IUserNoticeService {

	public UserNotice findByUserId(Long userId);
	
	public int updateMaxNoticeId(Long userId,Long maxNoticeId);
	
	
}
