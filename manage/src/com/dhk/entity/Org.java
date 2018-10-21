package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_org 实体类<br/>
    * 2017-01-14 09:50:52 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_org")
public class Org  extends Entity {
	

	@Column(name="ORG_NAME")
	private String orgName;

	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="MOBILE")
	private String mobile;

	@Column(name="FAX")
	private String fax;

	@Column(name="ADDRESS")
	private String address;

	@Column(name="BANK_NAME")
	private String bankName;

	@Column(name="ACCOUNT_NAME")
	private String accountName;

	@Column(name="ACCOUNT_NO")
	private String accountNo;

	@Column(name="ORG_RELATION_NO")
	private String orgRelationNo;

	@Column(name="D_RATE")
	private Double dRate;

	@Column(name="D_MIN")
	private Double dMin;

	@Column(name="T_RATE")
	private Double tRate;

	@Column(name="T_MIN")
	private Double tMin;

	@Column(name="STATUS")
	private String status;

	@Column(name="PARENT_ID")
	private Long parentId;
	
	@Column(name="BALANCE")
	private BigDecimal balance;
	
	@Column(name="CASH")
	private BigDecimal cash;
	
	@Column(name="ORG_TYPE")
	private String orgType;
	
	@Column(name="REALNAME")
	private String realName;
	
	@Column(name="IDNO")
	private String idNo;
	@Column(name="BIND_PHONE")
	private String bindPhone;
	private String orgTypeName;
	
	private Long userNums;
	
	
	
	
	private BigDecimal inAccount;
	
	public BigDecimal getCash() {
		return cash;
	}
	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	private String parentName;

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}
	
	public String getOrgName(){
		return orgName;
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
	
	public void setBankName(String bankName){
		this.bankName=bankName;
	}
	
	public String getBankName(){
		return bankName;
	}
	
	public void setAccountName(String accountName){
		this.accountName=accountName;
	}
	
	public String getAccountName(){
		return accountName;
	}
	
	public void setAccountNo(String accountNo){
		this.accountNo=accountNo;
	}
	
	public String getAccountNo(){
		return accountNo;
	}
	
	public void setOrgRelationNo(String orgRelationNo){
		this.orgRelationNo=orgRelationNo;
	}
	
	public String getOrgRelationNo(){
		return orgRelationNo;
	}
	
	public void setDRate(Double dRate){
		this.dRate=dRate;
	}
	
	public Double getDRate(){
		return dRate;
	}
	
	public void setDMin(Double dMin){
		this.dMin=dMin;
	}
	
	public Double getDMin(){
		return dMin;
	}
	
	public void setTRate(Double tRate){
		this.tRate=tRate;
	}
	
	public Double getTRate(){
		return tRate;
	}
	
	
	public void setTMin(Double tMin){
		this.tMin=tMin;
	}
	
	public Double getTMin(){
		return tMin;
	}
	
	public void setStatus(String status){
		this.status=status;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setParentId(Long parentId){
		this.parentId=parentId;
	}
	
	public Long getParentId(){
		return parentId;
	}
	
	public String getParentName() {
		return parentName;
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
	public String getOrgType() {
		return orgType;
	}
	
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	
	public String getOrgTypeName() {
		return orgTypeName;
	}
	
	
	public void setOrgTypeName(String orgTypeName) {
		this.orgTypeName = orgTypeName;
	}
	
	public Long getUserNums() {
		return userNums;
	}
	
	public void setUserNums(Long userNums) {
		this.userNums = userNums;
	}
	
	public String getBindPhone() {
		return bindPhone;
	}
	
	public void setBindPhone(String bindPhone) {
		this.bindPhone = bindPhone;
	}
	public BigDecimal getInAccount() {
		return inAccount;
	}
	public void setInAccount(BigDecimal inAccount) {
		this.inAccount = inAccount;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
}

