package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_org_qrcode 实体类<br/>
    * 2017-01-09 08:48:56 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_org_qrcode")
public class QRCode  extends Entity {
	
	@Column(name="ORG_ID")
	private Long orgId;

	@Column(name="QR_CODE")
	private String qrCode;

	@Column(name="ISUSE")
	private String isUse;
	
	@Column(name="RELATION_NO")
	private String relationNo;
	
	public String getRelationNo() {
		return relationNo;
	}
	public void setRelationNo(String relationNo) {
		this.relationNo = relationNo;
	}
	@Column(name="INVITATION_CODE")
	private String invitationCode;

	@Column(name="CHANNEL_ID")
	private String channelId;
	
	@Column(name="CREATE_DATE")
	private String createDate;

	
	//二维码使用状态描述
	private String isUseDes;
	
	private String orgName;
	
	private BigDecimal rate;
	
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getIsUseDes() {
		return isUseDes;
	}
	public void setIsUseDes(String isUseDes) {
		this.isUseDes = isUseDes;
	}
	
	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}
	public Long getOrgId(){
		return orgId;
	}
	public void setQrCode(String qrCode){
		this.qrCode=qrCode;
	}
	public String getQrCode(){
		return qrCode;
	}
	public void setIsUse(String isuse){
		this.isUse=isuse;
	}
	public String getIsUse(){
		return isUse;
	}
	public void setInvitationCode(String invitationCode){
		this.invitationCode=invitationCode;
	}
	public String getInvitationCode(){
		return invitationCode;
	}
	public void setChannelId(String channelId){
		this.channelId=channelId;
	}
	public String getChannelId(){
		return channelId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}

