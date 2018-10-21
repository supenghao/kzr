package com.dhk.api.entity;
import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
    * t_s_org_rate 实体类<br/>
    * 2017-02-11 06:36:13 qch
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_org_rate")
public class Orgrate  extends Entity {
	private Long qrcode_id;
	private String org_id;
	private Double rate;
	private Double diff_rate;
	public void setQrcode_id(Long qrcode_id){
		this.qrcode_id=qrcode_id;
	}
	public Long getQrcode_id(){
		return qrcode_id;
	}
	public void setOrg_id(String org_id){
		this.org_id=org_id;
	}
	public String getOrg_id(){
		return org_id;
	}
	public void setRate(Double rate){
		this.rate=rate;
	}
	public Double getRate(){
		return rate;
	}
	public void setDiff_rate(Double diff_rate){
		this.diff_rate=diff_rate;
	}
	public Double getDiff_rate(){
		return diff_rate;
	}
}

