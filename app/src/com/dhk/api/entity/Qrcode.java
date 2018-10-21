package com.dhk.api.entity;
import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
    * t_s_org_qrcode 实体类<br/>
    * 2017-02-16 10:54:24 qch
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_org_qrcode")
public class Qrcode  extends Entity {
	private Long org_id;
	private String isuse;
	private String invitation_code;
	private String channel_id;
	private String relation_no;
	public void setOrg_id(Long org_id){
		this.org_id=org_id;
	}
	public Long getOrg_id(){
		return org_id;
	}
	public void setIsuse(String isuse){
		this.isuse=isuse;
	}
	public String getIsuse(){
		return isuse;
	}
	public void setInvitation_code(String invitation_code){
		this.invitation_code=invitation_code;
	}
	public String getInvitation_code(){
		return invitation_code;
	}
	public void setChannel_id(String channel_id){
		this.channel_id=channel_id;
	}
	public String getChannel_id(){
		return channel_id;
	}
	public void setRelation_no(String relation_no){
		this.relation_no=relation_no;
	}
	public String getRelation_no(){
		return relation_no;
	}
}

