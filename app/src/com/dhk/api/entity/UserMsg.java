package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_message 实体类<br/>
 * 2017-02-15 11:31:56 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_message")
public class UserMsg extends Entity {
	private Long user_id;
	private Long transwater_id;
	private String createtime;
	private String status;
	private String message_content;

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setTranswater_id(Long transwater_id) {
		this.transwater_id = transwater_id;
	}

	public Long getTranswater_id() {
		return transwater_id;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}

	public String getMessage_content() {
		return message_content;
	}
}
