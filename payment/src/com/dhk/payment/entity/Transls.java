package com.dhk.payment.entity;
import com.dhk.kernel.entity.Entity;
import com.dhk.kernel.dao.jdbc.Table;

/**
    * t_transls 实体类<br/>
    * 2017-01-06 11:37:45 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_transls")
public class Transls  extends Entity {
	private String transType;
	private String transDate;
	private String transTime;
	private String transNo;
	private String requestNo;
	private String productId;
	private String transId;
	private String merNo;
	private String orderDate;
	private String orderNo;
	private Double transAmt;
	private String commodityName;
	private String phoneNo;
	private String customerName;
	private String cerdType;
	private String cerdId;
	private String acctNo;
	private String respCode;
	private String respDesc;
	private String isCompay;
	private String status;
	private String gateway;
	public void setTransType(String transType){
		this.transType=transType;
	}
	public String getTransType(){
		return transType;
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
	public void setRequestNo(String requestNo){
		this.requestNo=requestNo;
	}
	public String getRequestNo(){
		return requestNo;
	}
	public void setProductId(String productId){
		this.productId=productId;
	}
	public String getProductId(){
		return productId;
	}
	public void setTransId(String transId){
		this.transId=transId;
	}
	public String getTransId(){
		return transId;
	}
	public void setMerNo(String merNo){
		this.merNo=merNo;
	}
	public String getMerNo(){
		return merNo;
	}
	public void setOrderDate(String orderDate){
		this.orderDate=orderDate;
	}
	public String getOrderDate(){
		return orderDate;
	}
	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}
	public String getOrderNo(){
		return orderNo;
	}
	public void setTransAmt(Double transAmt){
		this.transAmt=transAmt;
	}
	public Double getTransAmt(){
		return transAmt;
	}
	public void setCommodityName(String commodityName){
		this.commodityName=commodityName;
	}
	public String getCommodityName(){
		return commodityName;
	}
	public void setPhoneNo(String phoneNo){
		this.phoneNo=phoneNo;
	}
	public String getPhoneNo(){
		return phoneNo;
	}
	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}
	public String getCustomerName(){
		return customerName;
	}
	public void setCerdType(String cerdType){
		this.cerdType=cerdType;
	}
	public String getCerdType(){
		return cerdType;
	}
	public void setCerdId(String cerdId){
		this.cerdId=cerdId;
	}
	public String getCerdId(){
		return cerdId;
	}
	public void setAcctNo(String acctNo){
		this.acctNo=acctNo;
	}
	public String getAcctNo(){
		return acctNo;
	}
	public void setRespCode(String respCode){
		this.respCode=respCode;
	}
	public String getRespCode(){
		return respCode;
	}
	public void setRespDesc(String respDesc){
		this.respDesc=respDesc;
	}
	public String getRespDesc(){
		return respDesc;
	}
	public void setIsCompay(String isCompay){
		this.isCompay=isCompay;
	}
	public String getIsCompay(){
		return isCompay;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	
}

