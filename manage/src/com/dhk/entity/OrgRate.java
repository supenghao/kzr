package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_org_rate 实体类<br/>
    * 2017-02-09 11:04:21 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_org_rate")
public class OrgRate  extends Entity {
	

	@Column(name="QRCODE_ID")
	private Long qrcodeId;

	@Column(name="ORG_ID")
	private long orgId;

	@Column(name="RATE")
	private BigDecimal rate;

	@Column(name="DIFF_RATE")
	private BigDecimal diffRate;
	
	private String invitationCode;
	private Long qrcode_org_id;
	
	private String org_name;
	private String channelId;
	
	private String parent_org_name;
	private String parent_org_id;
	
	private String relation_no;

	public void setQrcodeId(Long qrcodeId){
		this.qrcodeId=qrcodeId;
	}
	public Long getQrcodeId(){
		return qrcodeId;
	}
	public void setOrgId(long orgId){
		this.orgId=orgId;
	}
	public long getOrgId(){
		return orgId;
	}
	public void setRate(BigDecimal rate){
		this.rate=rate;
	}
	public BigDecimal getRate(){
		return rate;
	}
	public void setDiffRate(BigDecimal diffRate){
		this.diffRate=diffRate;
	}
	public BigDecimal getDiffRate(){
		return diffRate;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

   public Long getQrcode_org_id() {
	   return qrcode_org_id;
   }

   public void setQrcode_org_id(Long qrcode_org_id) {
	   this.qrcode_org_id = qrcode_org_id;
   }
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getParent_org_name() {
		return parent_org_name;
	}
	public void setParent_org_name(String parent_org_name) {
		this.parent_org_name = parent_org_name;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getParent_org_id() {
		return parent_org_id;
	}
	public void setParent_org_id(String parent_org_id) {
		this.parent_org_id = parent_org_id;
	}
	public String getRelation_no() {
		return relation_no;
	}
	public void setRelation_no(String relation_no) {
		this.relation_no = relation_no;
	}
   
   
   }

