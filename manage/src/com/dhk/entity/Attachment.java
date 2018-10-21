package com.dhk.entity;
import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_org_attachment 实体类<br/>
    * 2016-12-22 04:00:43 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_org_attachment")
public class Attachment  extends Entity {
	

	@Column(name="ORG_ID")
	private String orgId;

	@Column(name="SAVE_PATH")
	private String savePath;

	@Column(name="ATTATCH_TYPE")
	private String attatchType;

	
	public void setOrgId(String orgId){
		this.orgId=orgId;
	}
	public String getOrgId(){
		return orgId;
	}
	public void setSavePath(String savePath){
		this.savePath=savePath;
	}
	public String getSavePath(){
		return savePath;
	}
	public void setAttatchType(String attatchType){
		this.attatchType=attatchType;
	}
	public String getAttatchType(){
		return attatchType;
	}
}

