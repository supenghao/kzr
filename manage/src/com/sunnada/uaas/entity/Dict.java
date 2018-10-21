package com.sunnada.uaas.entity;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;
/**
 * 字典大类
 * @author bian
 *
 */
@SuppressWarnings("serial")
@Table(name="t_dict")
public class Dict extends Entity{

	private String dict_code;
	private String dict_text;
	public String getDict_code() {
		return dict_code;
	}
	public void setDict_code(String dict_code) {
		this.dict_code = dict_code;
	}
	public String getDict_text() {
		return dict_text;
	}
	public void setDict_text(String dict_text) {
		this.dict_text = dict_text;
	}
	
}
