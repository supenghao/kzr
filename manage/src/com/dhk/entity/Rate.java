package com.dhk.entity;
import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_org_rate 实体类<br/>
    * 2017-01-09 09:01:45 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_org_rate")
public class Rate  extends Entity {
	
	@Column(name="QRCODE_ID")
	private Long qrcodeId;

	@Column(name="ORG_ID")
	private String orgId;

	@Column(name="RATE")
	private Double rate;

	@Column(name="DIFF_RATE")
	private Double diffRate;

	
	public void setQrcodeId(Long qrcodeId){
		this.qrcodeId=qrcodeId;
	}
	public Long getQrcodeId(){
		return qrcodeId;
	}
	public void setOrgId(String orgId){
		this.orgId=orgId;
	}
	public String getOrgId(){
		return orgId;
	}
	public void setRate(Double rate){
		this.rate=rate;
	}
	public Double getRate(){
		return rate;
	}
	public void setDiffRate(Double diffRate){
		this.diffRate=diffRate;
	}
	public Double getDiffRate(){
		return diffRate;
	}
}

