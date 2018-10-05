package com.sunnada.uaas.entity;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

/**
 * 角色
 * @author bian
 *
 */
@SuppressWarnings("serial")
@Table(name="t_role")
public class Role extends Entity{

	private String role_name;
	private Long sid;
	private String role_relation_no;
	//扩展字段
	private String s_name;
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public String getRole_relation_no() {
		return role_relation_no;
	}
	public void setRole_relation_no(String role_relation_no) {
		this.role_relation_no = role_relation_no;
	}
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	
	
}
