package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_cost_plan 实体类<br/>
    * 2017-01-09 08:53:23 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_cost_plan")
public class CostPlanDetail  extends Entity {
	
	@Column(name="COST_POLICY_ID")
	private Long costPolicyId;

	@Column(name="CARD_NO")
	private String cardNo;

	@Column(name="COST_AMOUNT")
	private BigDecimal costAmount;

	@Column(name="COST_DATETIME")
	private String costDatetime;

	@Column(name="STATUS")
	private String status;
	
	@Column(name="EXEC_TIME")
	private String execTime;
	//附加字段
	@Column(name="USER_ID")
	private long userId;
	
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public void setCostPolicyId(Long costPolicyId){
		this.costPolicyId=costPolicyId;
	}
	public Long getCostPolicyId(){
		return costPolicyId;
	}
	public void setCardNo(String cardNo){
		this.cardNo=cardNo;
	}
	public String getCardNo(){
		return cardNo;
	}
	public void setCostAmount(BigDecimal costAmount){
		this.costAmount=costAmount;
	}
	public BigDecimal getCostAmount(){
		return costAmount;
	}
	public void setCostDatetime(String costDatetime){
		this.costDatetime=costDatetime;
	}
	public String getCostDatetime(){
		return costDatetime;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	
}

