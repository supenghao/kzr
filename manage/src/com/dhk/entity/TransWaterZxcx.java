package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

 /**
 * t_s_trans_water 实体类<br/>
 * 2017-01-19 01:21:54 Gnaily
 */ 
@SuppressWarnings("serial")
@Table(name="t_s_trans_water_zxcx")
public class TransWaterZxcx  extends Entity {
	
	@Column(name="TRANS_DATE")
	private String transDate;

	@Column(name="TRANS_TIME")
	private String transTime;

	 
	@Column(name="TRANS_NO")
	private String transNo;

	 

	@Column(name="TRANS_AMOUNT")
	private BigDecimal transAmount;

	@Column(name="TRANS_TYPE")
	private String transType;

	@Column(name="RESP_CODE")
	private String respCode;

	@Column(name="FEE")
	private BigDecimal fee;

	 

	@Column(name="RESP_RES")
	private String respRes;
	
	@Column(name="STATUS")
	private String status;
	
	 
	@Column(name="EXTERNAL")
	private BigDecimal external;
	
	 
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="MOBILE")
	private String mobile;

 

	 
	public BigDecimal getExternal() {
		return external;
	}
	public void setExternal(BigDecimal external) {
		this.external = external;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	 
	public String getRespRes() {
		return respRes;
	}
	public void setRespRes(String respRes) {
		this.respRes = respRes;
	}
	
	public void setTransDate(String transDate){
		this.transDate=transDate;
	}
	public String getTransDate(){
		return transDate;
	}
	
	public void setTransTime(String transTime){
		this.transTime=transTime;
	}
	public String getTransTime(){
		return transTime;
	}
	 
	public void setTransNo(String transNo){
		this.transNo=transNo;
	}
	public String getTransNo(){
		return transNo;
	}
 
	public void setTransAmount(BigDecimal transAmount){
		this.transAmount=transAmount;
	}
	public BigDecimal getTransAmount(){
		return transAmount;
	}
	public void setTransType(String transType){
		this.transType=transType;
	}
	public String getTransType(){
		return transType;
	}
	public void setRespCode(String respCode){
		this.respCode=respCode;
	}
	public String getRespCode(){
		return respCode;
	}
	public void setFee(BigDecimal fee){
		this.fee=fee;
	}
	public BigDecimal getFee(){
		return fee;
	}
	 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	 
	 
	 
 }

