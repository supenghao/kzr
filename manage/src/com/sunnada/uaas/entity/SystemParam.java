package com.sunnada.uaas.entity;

/**
 * 系统参数
 * @author bian
 *
 */
@SuppressWarnings("serial")
@com.sunnada.kernel.dao.jdbc.Table(name="t_system_param")
public class SystemParam extends com.sunnada.kernel.entity.Entity {

	private String param_name;
	private String param_text;
	private String remark;
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	public String getParam_text() {
		return param_text;
	}
	public void setParam_text(String param_text) {
		this.param_text = param_text;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
