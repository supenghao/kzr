package com.dhk.entity;
import java.math.BigDecimal;
import java.util.Date;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

 /**
 * t_org_profit 实体类<br/>
 * 2017-01-24 01:21:54 zzl
 */ 
@SuppressWarnings("serial")
@Table(name="t_org_profit")
public class OrgProfit  extends Entity {
	
	@Column(name="add_time")
	private Date addTime;

	@Column(name="user_org_id")
	private String userOrgId;
	
	@Column(name="user_org_name")
	private String userOrgName;
	
	@Column(name="user_type")
	private Integer userType;
	
	@Column(name="parent_user_org_id")
	private String parentUserOrgId;
	
	@Column(name="parent_user_org_name")
	private String parentUserOrgName;
	
	@Column(name="order_no")
	private String orderNo;
	
	@Column(name="trans_date")
	private String transDate;
	
	@Column(name="trans_time")
	private String transTime;
	
	@Column(name="card_no")
	private String cardNo;
	
	@Column(name="trans_amount")
	private BigDecimal transAmount;
	 
	@Column(name="fee")
	private String fee;
	
	@Column(name="profit")
	private BigDecimal profit;
	
	@Column(name="trans_type")
	private String transType;
	@Column(name="relation_no")
	private String relationNo;
	@Column(name="channel_id")
	private String channelId;

	private String transTypeText;
	
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getUserOrgId() {
		return userOrgId;
	}

	public void setUserOrgId(String userOrgId) {
		this.userOrgId = userOrgId;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}


	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getParentUserOrgId() {
		return parentUserOrgId;
	}

	public void setParentUserOrgId(String parentUserOrgId) {
		this.parentUserOrgId = parentUserOrgId;
	}

	public String getParentUserOrgName() {
		return parentUserOrgName;
	}

	public void setParentUserOrgName(String parentUserOrgName) {
		this.parentUserOrgName = parentUserOrgName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTransTypeText() {
		return transTypeText;
	}

	public void setTransTypeText(String transTypeText) {
		this.transTypeText = transTypeText;
	}

	public String getRelationNo() {
		return relationNo;
	}

	public void setRelationNo(String relationNo) {
		this.relationNo = relationNo;
	}

	
	
	
 }

