package com.dhk.payment.entity;
import com.dhk.kernel.entity.Entity;
import com.dhk.kernel.dao.jdbc.Table;

/**
    * t_gateway_detail 实体类<br/>
    * 2017-01-05 10:37:00 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_gateway_detail")
public class GatewayDetail  extends Entity {
	private Long gatewayId;
	private String merchantCode;
	private String publicKeyPath;
	private String privateKeyPath;
	private String privateKeyPfxPath;
	private String privateKeyPwd;
	public void setGatewayId(Long gatewayId){
		this.gatewayId=gatewayId;
	}
	public Long getGatewayId(){
		return gatewayId;
	}
	public void setMerchantCode(String merchantCode){
		this.merchantCode=merchantCode;
	}
	public String getMerchantCode(){
		return merchantCode;
	}
	public void setPublicKeyPath(String publicKeyPath){
		this.publicKeyPath=publicKeyPath;
	}
	public String getPublicKeyPath(){
		return publicKeyPath;
	}
	public void setPrivateKeyPath(String privateKeyPath){
		this.privateKeyPath=privateKeyPath;
	}
	public String getPrivateKeyPath(){
		return privateKeyPath;
	}
	public void setPrivateKeyPfxPath(String privateKeyPfxPath){
		this.privateKeyPfxPath=privateKeyPfxPath;
	}
	public String getPrivateKeyPfxPath(){
		return privateKeyPfxPath;
	}
	public void setPrivateKeyPwd(String privateKeyPwd){
		this.privateKeyPwd=privateKeyPwd;
	}
	public String getPrivateKeyPwd(){
		return privateKeyPwd;
	}
}

