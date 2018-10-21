package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_settle 实体类<br/>
    * 2017-02-11 10:44:04 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_settle")
public class Settle  extends Entity {

	@Column(name="SETTLEDATE")
	private String settledate;

	@Column(name="ORGID")
	private Long orgid;

	@Column(name="ORG_RELATION_NO")
	private String orgRelationNo;

	@Column(name="BALANCE")
	private BigDecimal balance;

	@Column(name="TRANS_COUNT")
	private Integer transCount;

	@Column(name="STATUS")
	private String status;
	
	private String orgName;

	private String statusText;
	
	public void setSettledate(String settledate){
		this.settledate=settledate;
	}
	public String getSettledate(){
		return settledate;
	}
	public void setOrgid(Long orgid){
		this.orgid=orgid;
	}
	public Long getOrgid(){
		return orgid;
	}
	public void setOrgRelationNo(String orgRelationNo){
		this.orgRelationNo=orgRelationNo;
	}
	public String getOrgRelationNo(){
		return orgRelationNo;
	}
	public void setBalance(BigDecimal balance){
		this.balance=balance;
	}
	public BigDecimal getBalance(){
		return balance;
	}
	public void setTransCount(Integer transCount){
		this.transCount=transCount;
	}
	public Integer getTransCount(){
		return transCount;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
}

