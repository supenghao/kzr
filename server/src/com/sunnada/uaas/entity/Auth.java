package com.sunnada.uaas.entity;

import com.sunnada.kernel.entity.Entity;

/**
 * 权限
 * @author bian
 *
 */
@SuppressWarnings("serial")
@com.sunnada.kernel.dao.jdbc.Table(name="t_auth")
public class Auth extends Entity{

	private Long role_id;
	private Long menu_id;
	//扩展字段
	private String menu_code;
	private String menu_name;
	private String menu_url;
	private Long   sid;
	private String istop;
	private String menu_icon;
	private String relation_no;
	private String sstatus;
	private String remarks;
	public Long getRole_id() {
		return role_id;
	}
	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}
	public Long getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(Long menu_id) {
		this.menu_id = menu_id;
	}
	public String getMenu_code() {
		return menu_code;
	}
	public void setMenu_code(String menu_code) {
		this.menu_code = menu_code;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public String getMenu_url() {
		return menu_url;
	}
	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public String getIstop() {
		return istop;
	}
	public void setIstop(String istop) {
		this.istop = istop;
	}
	public String getMenu_icon() {
		return menu_icon;
	}
	public void setMenu_icon(String menu_icon) {
		this.menu_icon = menu_icon;
	}
	public String getRelation_no() {
		return relation_no;
	}
	public void setRelation_no(String relation_no) {
		this.relation_no = relation_no;
	}
	public String getSstatus() {
		return sstatus;
	}
	public void setSstatus(String sstatus) {
		this.sstatus = sstatus;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
