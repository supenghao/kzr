package com.dhk.entity;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_headbut 实体类<br/>
    * 2017-02-16 09:09:58 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_headbut")
public class Headbut  extends Entity {

	private String bt_name;
	private Integer bt_no;
	private String bt_img_code;
	private String bt_img_url;
	private String bt_show;
	
	public String getBt_name() {
		return bt_name;
	}
	public void setBt_name(String bt_name) {
		this.bt_name = bt_name;
	}
	public Integer getBt_no() {
		return bt_no;
	}
	public void setBt_no(Integer bt_no) {
		this.bt_no = bt_no;
	}
	public String getBt_img_code() {
		return bt_img_code;
	}
	public void setBt_img_code(String bt_img_code) {
		this.bt_img_code = bt_img_code;
	}
	public String getBt_img_url() {
		return bt_img_url;
	}
	public void setBt_img_url(String bt_img_url) {
		this.bt_img_url = bt_img_url;
	}
	public String getBt_show() {
		return bt_show;
	}
	public void setBt_show(String bt_show) {
		this.bt_show = bt_show;
	}
	
	
}

