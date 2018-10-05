package com.sunnada.uaas.entity;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;
/**
 * 快捷方式
 * @author bian
 *
 */
@SuppressWarnings("serial")
@Table(name="t_shortcut")
public class Shortcut extends Entity{

	private String operno;
	private Long menu_id;
	//扩展
	private String menu_name;
	private String menu_url;
	private String menu_icon;
	private String remarks;
	public String getOperno() {
		return operno;
	}
	public void setOperno(String operno) {
		this.operno = operno;
	}
	public Long getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(Long menu_id) {
		this.menu_id = menu_id;
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
	public String getMenu_icon() {
		return menu_icon;
	}
	public void setMenu_icon(String menu_icon) {
		this.menu_icon = menu_icon;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
