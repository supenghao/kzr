package com.dhk.api.entity;

import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
 * t_s_headbut 实体类<br/>
 * 2017-01-14 09:42:53 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_headbut")
public class HomeBut extends Entity {

	private String bt_id;
	private String bt_name;
	private String bt_no;
	private String bt_link_url;
	private String bt_img_url;
	private String bt_show;

	public void setBt_id(String bt_id) {
		this.bt_id = bt_id;
	}

	public String getBt_id() {
		return bt_id;
	}

	public String getBt_name() {
		return bt_name;
	}

	public void setBt_name(String bt_name) {
		this.bt_name = bt_name;
	}

	public void setBt_no(String bt_no) {
		this.bt_no = bt_no;
	}

	public String getBt_no() {
		return bt_no;
	}

	public void setBt_link_url(String bt_img_url) {
		this.bt_link_url = bt_img_url;
	}

	public String getBt_link_url() {
		return bt_link_url;
	}

	public String getBt_img_url() {
		return bt_img_url;
	}

	public void setBt_img_url(String bt_img_url) {
		this.bt_img_url = bt_img_url;
	}

	public void setBt_show(String bt_show) {
		this.bt_show = bt_show;
	}

	public String getBt_show() {
		return bt_show;
	}
}
