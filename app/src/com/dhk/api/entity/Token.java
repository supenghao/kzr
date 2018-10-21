package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_token 实体类<br/>
 * 2016-12-20 11:46:56 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_token")
public class Token extends Entity {

	protected String token;
	protected String userid;
	protected String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}
}
