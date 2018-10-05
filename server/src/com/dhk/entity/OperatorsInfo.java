package com.dhk.entity;
import com.sunnada.kernel.entity.Entity;
import com.sunnada.kernel.dao.jdbc.Table;

/**
    * t_s_operators_info 实体类<br/>
    * 2017-04-27 05:28:06 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_operators_info")
public class OperatorsInfo  extends Entity {
	private String realName;
	private String cardNo;
	private String phone;
	private String idNo;
	private String bankName;
	public void setRealName(String realName){
		this.realName=realName;
	}
	public String getRealName(){
		return realName;
	}
	public void setCardNo(String cardNo){
		this.cardNo=cardNo;
	}
	public String getCardNo(){
		return cardNo;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setIdNo(String idNo){
		this.idNo=idNo;
	}
	public String getIdNo(){
		return idNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}

