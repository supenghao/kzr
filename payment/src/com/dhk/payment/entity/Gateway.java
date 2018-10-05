package com.dhk.payment.entity;
import com.dhk.kernel.entity.Entity;
import com.dhk.kernel.dao.jdbc.Table;

/**
    * t_gateway 实体类<br/>
    * 2017-01-05 10:36:23 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_gateway")
public class Gateway  extends Entity {
	private String gatewayCode;
	private String gatewayName;
	private String gatewayType;
	private String gatewayUrl;
	private String gatewayIp;
	private Integer gatewayPort;
	private String execClass;
	private String status;
	//扩展
	private String gatewayTypeText;
	private String statusText;
	public void setGatewayCode(String gatewayCode){
		this.gatewayCode=gatewayCode;
	}
	public String getGatewayCode(){
		return gatewayCode;
	}
	public void setGatewayName(String gatewayName){
		this.gatewayName=gatewayName;
	}
	public String getGatewayName(){
		return gatewayName;
	}
	public void setGatewayType(String gatewayType){
		this.gatewayType=gatewayType;
	}
	public String getGatewayType(){
		return gatewayType;
	}
	public void setGatewayUrl(String gatewayUrl){
		this.gatewayUrl=gatewayUrl;
	}
	public String getGatewayUrl(){
		return gatewayUrl;
	}
	public void setGatewayIp(String gatewayIp){
		this.gatewayIp=gatewayIp;
	}
	public String getGatewayIp(){
		return gatewayIp;
	}
	public void setGatewayPort(Integer gatewayPort){
		this.gatewayPort=gatewayPort;
	}
	public Integer getGatewayPort(){
		return gatewayPort;
	}
	public void setExecClass(String execClass){
		this.execClass=execClass;
	}
	public String getExecClass(){
		return execClass;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public String getGatewayTypeText() {
		return gatewayTypeText;
	}
	public void setGatewayTypeText(String gatewayTypeText) {
		this.gatewayTypeText = gatewayTypeText;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
}

