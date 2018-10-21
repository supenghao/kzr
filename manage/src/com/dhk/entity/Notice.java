package com.dhk.entity;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_org_notice 实体类<br/>
    * 2017-02-15 10:46:37 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_org_notice")
public class Notice  extends Entity {
	
	private Long org_id;
	private String notice_title;
	private String notice_content;
	private String 	notice_create;
	private String notice_creater;
	private String relation_no;

	public Long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}

	public String getNotice_title() {
		return notice_title;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public String getNotice_content() {
		return notice_content;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}

	public String getNotice_create() {
		return notice_create;
	}

	public void setNotice_create(String notice_create) {
		this.notice_create = notice_create;
	}

	public String getNotice_creater() {
		return notice_creater;
	}

	public void setNotice_creater(String notice_creater) {
		this.notice_creater = notice_creater;
	}

	public String getRelation_no() {
		return relation_no;
	}

	public void setRelation_no(String relation_no) {
		this.relation_no = relation_no;
	}
	
	
	
}

