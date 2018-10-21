package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_memberinfo 实体类<br/>
 * 2016-12-21 05:04:13 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_user_notice")
public class UserNotice extends Entity{

	private Long userId;
	private Long maxNoticeId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getMaxNoticeId() {
		return maxNoticeId;
	}
	public void setMaxNoticeId(Long maxNoticeId) {
		this.maxNoticeId = maxNoticeId;
	}
	
	
}
