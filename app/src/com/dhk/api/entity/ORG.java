package com.dhk.api.entity;
import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;

/**
    * t_org 实体类<br/>
    * 2017-01-13 04:03:14 qch
    */ 
@SuppressWarnings("serial")
@Table(name="t_org")
public class ORG  extends Entity {
	private String org_name;
	private String description;
	private String mobile;
	private String fax;
	private String address;
	private String bank_name;
	private String account_name;
	private String account_no;
	private String org_relation_no;
	private Double d_rate;
	private Double d_min;
	private Double t_rate;
	private Double t_min;
	private String status;
	private Long parent_id;
	public void setOrg_name(String org_name){
		this.org_name=org_name;
	}
	public String getOrg_name(){
		return org_name;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public String getDescription(){
		return description;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public String getMobile(){
		return mobile;
	}
	public void setFax(String fax){
		this.fax=fax;
	}
	public String getFax(){
		return fax;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public String getAddress(){
		return address;
	}
	public void setBank_name(String bank_name){
		this.bank_name=bank_name;
	}
	public String getBank_name(){
		return bank_name;
	}
	public void setAccount_name(String account_name){
		this.account_name=account_name;
	}
	public String getAccount_name(){
		return account_name;
	}
	public void setAccount_no(String account_no){
		this.account_no=account_no;
	}
	public String getAccount_no(){
		return account_no;
	}
	public void setOrg_relation_no(String org_relation_no){
		this.org_relation_no=org_relation_no;
	}
	public String getOrg_relation_no(){
		return org_relation_no;
	}
	public void setD_rate(Double d_rate){
		this.d_rate=d_rate;
	}
	public Double getD_rate(){
		return d_rate;
	}
	public void setD_min(Double d_min){
		this.d_min=d_min;
	}
	public Double getD_min(){
		return d_min;
	}
	public void setT_rate(Double t_rate){
		this.t_rate=t_rate;
	}
	public Double getT_rate(){
		return t_rate;
	}
	public void setT_min(Double t_min){
		this.t_min=t_min;
	}
	public Double getT_min(){
		return t_min;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setParent_id(Long parent_id){
		this.parent_id=parent_id;
	}
	public Long getParent_id(){
		return parent_id;
	}
}

