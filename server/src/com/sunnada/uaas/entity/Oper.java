package com.sunnada.uaas.entity;

import com.sunnada.kernel.entity.Entity;

/**
 * 系统操作员
 * @author bian
 *
 */
@SuppressWarnings("serial")
@com.sunnada.kernel.dao.jdbc.Table(name="t_oper")
public class Oper extends Entity{

	private String oper_code;
	private String oper_name;
	private String login_name;
	private Long org_id;
	private Long role_id;
	private String org_relation_no;
	private String pwd;
	private String sstatus;
	private String status_text;
	//扩展字段
	private String org_name;
	private String role_name;
	private String role_relation_no;
	
	public String getOper_code() {
		return oper_code;
	}
	public void setOper_code(String oper_code) {
		this.oper_code = oper_code;
	}
	public String getOper_name() {
		return oper_name;
	}
	public void setOper_name(String oper_name) {
		this.oper_name = oper_name;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public Long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}
	public Long getRole_id() {
		return role_id;
	}
	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}
	public String getOrg_relation_no() {
		return org_relation_no;
	}
	public void setOrg_relation_no(String org_relation_no) {
		this.org_relation_no = org_relation_no;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getSstatus() {
		return sstatus;
	}
	public void setSstatus(String sstatus) {
		this.sstatus = sstatus;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getStatus_text() {
		return status_text;
	}
	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}
	public String getRole_relation_no() {
		return role_relation_no;
	}
	public void setRole_relation_no(String role_relation_no) {
		this.role_relation_no = role_relation_no;
	}
	
}
