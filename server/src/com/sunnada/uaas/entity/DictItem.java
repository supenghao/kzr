package com.sunnada.uaas.entity;

import com.sunnada.kernel.entity.Entity;
/**
 * 字典小类
 * @author bian
 *
 */
@SuppressWarnings("serial")
@com.sunnada.kernel.dao.jdbc.Table(name="t_dict_item")
public class DictItem extends Entity{

	private String dict_code;
	private String item_code;
	private String item_text;
	//扩展
	private String dict_text;
	public String getDict_code() {
		return dict_code;
	}
	public void setDict_code(String dict_code) {
		this.dict_code = dict_code;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_text() {
		return item_text;
	}
	public void setItem_text(String item_text) {
		this.item_text = item_text;
	}
	public String getDict_text() {
		return dict_text;
	}
	public void setDict_text(String dict_text) {
		this.dict_text = dict_text;
	}
	
}
