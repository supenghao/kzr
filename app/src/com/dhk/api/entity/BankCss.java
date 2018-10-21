package com.dhk.api.entity;
import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
    * t_s_bank_css 实体类<br/>
    * 2017-02-16 07:01:40 qch
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_bank_css")
public class BankCss  extends Entity {
	private String bankcode;
	private String bankname;
	private String baccolor;
	private String logoimgname;
	public void setBankcode(String bankcode){
		this.bankcode=bankcode;
	}
	public String getBankcode(){
		return bankcode;
	}
	public void setBankname(String bankname){
		this.bankname=bankname;
	}
	public String getBankname(){
		return bankname;
	}
	public void setBaccolor(String baccolor){
		this.baccolor=baccolor;
	}
	public String getBaccolor(){
		return baccolor;
	}
	public void setLogoimgname(String logoimgname){
		this.logoimgname=logoimgname;
	}
	public String getLogoimgname(){
		return logoimgname;
	}
}

