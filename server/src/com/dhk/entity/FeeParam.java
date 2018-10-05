package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_param_fee 实体类<br/>
    * 2017-02-10 09:31:05 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_param_fee")
public class FeeParam  extends Entity {
	

	@Column(name="CODE")
	private String code;

	@Column(name="CODE_DESC")
	private String codeDesc;

	@Column(name="FEE")
	private BigDecimal fee;

	@Column(name="EXTERNAL")
	private BigDecimal external;

	@Column(name="STATUS")
	private String status;

	@Column(name="FEETYPE")
	private String feeType;
	
	@Column(name="LOWERLIMIT")
	private BigDecimal lowerlimit;
	
	@Column(name="UPLIMIT")
	private BigDecimal uplimit;
	
	public void setCode(String code){
		this.code=code;
	}
	public String getCode(){
		return code;
	}
	public void setCodeDesc(String codeDesc){
		this.codeDesc=codeDesc;
	}
	public String getCodeDesc(){
		return codeDesc;
	}
	public void setFee(BigDecimal fee){
		this.fee=fee;
	}
	public BigDecimal getFee(){
		return fee;
	}
	public void setExternal(BigDecimal external){
		this.external=external;
	}
	public BigDecimal getExternal(){
		return external;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public String getFeeType() {
		return feeType;
	}
	
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public BigDecimal getLowerlimit() {
		return lowerlimit;
	}
	public void setLowerlimit(BigDecimal lowerlimit) {
		this.lowerlimit = lowerlimit;
	}
	public BigDecimal getUplimit() {
		return uplimit;
	}
	public void setUplimit(BigDecimal uplimit) {
		this.uplimit = uplimit;
	}
	
	
}

