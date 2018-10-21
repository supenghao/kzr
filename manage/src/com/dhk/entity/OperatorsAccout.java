package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * operators_accout 实体类<br/>
    * 2017-02-19 09:02:11 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="operators_accout")
public class OperatorsAccout  extends Entity {
	private BigDecimal creditcard_balance;
	private BigDecimal debitecard_balance;
	
	public void setCreditcard_balance(BigDecimal creditcard_balance){
		this.creditcard_balance=creditcard_balance;
	}
	
	public BigDecimal getCreditcard_balance(){
		return creditcard_balance;
	}
	
	public void setDebitecard_balance(BigDecimal debitecard_balance){
		this.debitecard_balance=debitecard_balance;
	}
	
	public BigDecimal getDebitecard_balance(){
		return debitecard_balance;
	}
}

