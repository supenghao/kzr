package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_n_repay_plan 实体类<br/>
    * 2017-02-10 11:15:55 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_n_repay_plan")
public class RepayPlanDetail  extends Entity {
	
	@Column(name="CREDIT_CARD_No")
	private String creditCardNo;

	@Column(name="STATUS")
	private String status;

	@Column(name="USER_ID")
	private Long userId;

	@Column(name="REPAY_AMOUNT")
	private BigDecimal repayAmount;

	@Column(name="POLICY_TYPE")
	private String policyType;

	@Column(name="REPAY_DAY")
	private String repayDay;
	
	@Column(name="REPAY_MONTH")
	private String repayMonth;
	
	@Column(name="EXEC_TIME")
	private String execTime;
	
	@Column(name="REPAYSUCCESS_TIME")
	private long repaySuccessTime;
	
	@Column(name="ORDERNO")
	private String orderNo;//订单号

   @Column(name="RECORD_ID")
    private Long recordId;//订单Id
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	private Integer execNums;
	
	public void setCreditCardNo(String creditCardNo){
		this.creditCardNo=creditCardNo;
	}
	public String getCreditCardNo(){
		return creditCardNo;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public Long getUserId(){
		return userId;
	}
	public void setRepayAmount(BigDecimal repayAmount){
		this.repayAmount=repayAmount;
	}
	public BigDecimal getRepayAmount(){
		return repayAmount;
	}
	public void setPolicyType(String policyType){
		this.policyType=policyType;
	}
	public String getPolicyType(){
		return policyType;
	}
	public void setRepayDay(String repayDay){
		this.repayDay=repayDay;
	}
	public String getRepayDay(){
		return repayDay;
	}
	public long getRepaySuccessTime() {
		return repaySuccessTime;
	}
	public void setRepaySuccessTime(long repaySuccessTime) {
		this.repaySuccessTime = repaySuccessTime;
	}
	public String getRepayMonth() {
		return repayMonth;
	}
	public void setRepayMonth(String repayMonth) {
		this.repayMonth = repayMonth;
	}
	
	public Integer getExecNums() {
		return execNums;
	}
	
	public void setExecNums(Integer execNums) {
		this.execNums = execNums;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	   public Long getRecordId() {
		   return recordId;
	   }

	   public void setRecordId(Long recordId) {
		   this.recordId = recordId;
	   }
   }

