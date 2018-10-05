package com.dhk.payment.entity;

import com.dhk.kernel.dao.jdbc.Table;
import com.dhk.kernel.entity.Entity;

/**
 * t_param_fee 实体类<br/>
 * 2017-02-20 10:40:06 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_param_fee")
public class ParamFee extends Entity {
	
	private String code;
	private String code_desc;
	private Double fee;
	private Double external;
	private String status;
	private String feetype;
	private Double uplimit;
	private Double lowerlimit;

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode_desc(String code_desc) {
		this.code_desc = code_desc;
	}

	public String getCode_desc() {
		return code_desc;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getFee() {
		return fee;
	}

	public void setExternal(Double external) {
		this.external = external;
	}

	public Double getExternal() {
		return external;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}

	public String getFeetype() {
		return feetype;
	}

	public void setUplimit(Double uplimit) {
		this.uplimit = uplimit;
	}

	public Double getUplimit() {
		return uplimit;
	}

	public void setLowerlimit(Double lowerlimit) {
		this.lowerlimit = lowerlimit;
	}

	public Double getLowerlimit() {
		return lowerlimit;
	}
}
