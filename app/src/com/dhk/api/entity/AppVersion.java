package com.dhk.api.entity;

import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
 * t_s_appVersion 实体类<br/>
 * 2017-01-13 05:04:36 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_appVersion")
public class AppVersion extends Entity {

	private String cur_version;
	private String title;
	private String version_type;
	private String update_type;
	private String version_des;
	private Integer file_size;
	private String url;
	
	private Integer version_num;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCur_version(String cur_version) {
		this.cur_version = cur_version;
	}

	public String getCur_version() {
		return cur_version;
	}

	public void setVersion_type(String version_type) {
		this.version_type = version_type;
	}

	public String getVersion_type() {
		return version_type;
	}

	public void setUpdate_type(String update_type) {
		this.update_type = update_type;
	}

	public String getUpdate_type() {
		return update_type;
	}

	public void setVersion_des(String version_des) {
		this.version_des = version_des;
	}

	public String getVersion_des() {
		return version_des;
	}

	public void setFile_size(Integer file_size) {
		this.file_size = file_size;
	}

	public Integer getFile_size() {
		return file_size;
	}

	public Integer getVersion_num() {
		return version_num;
	}

	public void setVersion_num(Integer version_num) {
		this.version_num = version_num;
	}
	
	
}
