package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_org_notice 实体类<br/>
 * 2017-02-15 08:49:29 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_org_notice")
public class Notices extends Entity {

	private Long org_id;
	private String notice_title;
	private String notice_content;
	private String notice_create;
	private String notice_creater;

	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}

	public Long getOrg_id() {
		return org_id;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public String getNotice_title() {
		return notice_title;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}

	public String getNotice_content() {
		return notice_content;
	}

	public void setNotice_create(String notice_create) {
		this.notice_create = notice_create;
	}

	public String getNotice_create() {
		return notice_create;
	}

	public void setNotice_creater(String notice_creater) {
		this.notice_creater = notice_creater;
	}

	public String getNotice_creater() {
		return notice_creater;
	}
}
