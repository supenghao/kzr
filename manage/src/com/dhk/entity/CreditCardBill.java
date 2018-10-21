package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_creditcard_bill 实体类<br/>
    * 2017-01-09 08:56:58 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_creditcard_bill")
public class CreditCardBill  extends Entity {
	
	@Column(name="CREDITCARD_ID")
	private Long creditcardId;

	@Column(name="BILL_AMOUNT")
	private BigDecimal billAmount;
	
	@Column(name="BILL_DAY")
	private String billDay;

	@Column(name="REPAY_DAY")
	private String repayDay;

	@Column(name="STATUS")
	private String status;

	@Column(name="VALUEABLE")
	private String valueable;
	
	@Column(name="CREATE_DAY")
	private String createDay;

	public Long getCreditcardId() {
		return creditcardId;
	}
	public void setCreditcardId(Long creditcardId) {
		this.creditcardId = creditcardId;
	}
	
	public String getCreateDay() {
		return createDay;
	}
	public void setCreateDay(String createDay) {
		this.createDay = createDay;
	}
	public void setBillDay(String billDay){
		this.billDay=billDay;
	}
	public String getBillDay(){
		return billDay;
	}
	public void setBillAmount(BigDecimal billAmount){
		this.billAmount=billAmount;
	}
	public BigDecimal getBillAmount(){
		return billAmount;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setRepayDay(String repayDay){
		this.repayDay=repayDay;
	}
	public String getRepayDay(){
		return repayDay;
	}
	public String getValueable() {
		return valueable;
	}
	public void setValueable(String valueable) {
		this.valueable = valueable;
	}
	
}

