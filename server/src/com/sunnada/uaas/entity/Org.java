package com.sunnada.uaas.entity;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

/**
 * 组织机构
 * @author bian
 *
 */
@SuppressWarnings("serial")
@Table(name="t_org")
public class Org extends Entity{

	private String org_code;
	private String org_name;
	private Long sid;
	private String istop;
	private String org_icon;
	private String relation_no;
	private String sstatus;
	//扩展字段
	private String s_name;
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
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
	public String getOrg_icon() {
		return org_icon;
	}
	public void setOrg_icon(String org_icon) {
		this.org_icon = org_icon;
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
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	
}
