package com.dhk.entity;
import com.dhk.utils.AESUtil;
import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_user_creditcard 实体类<br/>
    * 2017-02-10 11:29:19 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_user_creditcard")
public class CreditCard  extends Entity {

	private String aesKey="chenshuai1234567";

	@Column(name="USERID")
	private Long userid;

	@Column(name="CARD_NO")
	private String cardNo;

	@Column(name="realname")
	private String realname;

	@Column(name="cerdType")
	private String cerdType;

	@Column(name="cerdId")
	private String cerdId;

	@Column(name="cvn2")
	private String cvn2;

	@Column(name="phoneNo")
	private String phoneNo;

	@Column(name="expDate")
	private String expDate;

	@Column(name="maxmon")
	private String maxmon;

	@Column(name="BANK_NAME")
	private String bankName;

	@Column(name="Mail_ADDR")
	private String mailAddr;

	@Column(name="Mail_AUTH_CODE")
	private String mailAuthCode;

	@Column(name="BILL_DATE")
	private String billDate;

	@Column(name="REPAY_DATE")
	private String repayDate;

	@Column(name="MAIL_PARSE_DATE")
	private String mailParseDate;

	@Column(name="POLICY_ID")
	private Long policyId;

	@Column(name="REYPAY_STYLE")
	private String reypayStyle;

	@Column(name="CARD_STATUS")
	private String cardStatus;

	//附加字段
	@Column(name="ORG_ID")
	private Long orgId;
	
	@Column(name="RELATION_NO")
	private String relationNo;
	
	@Column(name="QRCODE_ID")
	private Long qrcodeId;
	
	@Column(name="BINDID")
	private String bindId;  //绑定卡id
	
	public Long getQrcodeId() {
		return qrcodeId;
	}

	public void setQrcodeId(Long qrcodeId) {
		this.qrcodeId = qrcodeId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getRelationNo() {
		return relationNo;
	}

	public void setRelationNo(String relationNo) {
		this.relationNo = relationNo;
	}

	

	public Long getOrgId() {
		return orgId;
	}
	
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public void setUserid(Long userid){
		this.userid=userid;
	}
	
	public Long getUserid(){
		return userid;
	}
	
	public void setCardNo(String cardNo){
		this.cardNo=cardNo;
	}
	public String getCardNo(){
		return cardNo;
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
	
	public void setCvn2(String cvn2){
		this.cvn2=cvn2;
	}
	
	public String getCvn2(){
		return cvn2;
	}
	
	public void setPhoneno(String phoneno){
		this.phoneNo=phoneno;
	}
	
	public String getPhoneno(){
		return phoneNo;
	}
	
	public void setExpdate(String expDate){
		this.expDate=expDate;
	}
	
	public String getExpdate(){
		return expDate;
	}
	
	public void setMaxmon(String maxmon){
		this.maxmon=maxmon;
	}
	
	public String getMaxmon(){
		return maxmon;
	}
	
	public void setBankName(String bankName){
		this.bankName=bankName;
	}
	
	public String getBankName(){
		return bankName;
	}
	
	public void setMailAddr(String mailAddr){
		this.mailAddr=mailAddr;
	}
	
	public String getMailAddr(){
		return mailAddr;
	}
	public void setMailAuthCode(String mailAuthCode){
		this.mailAuthCode=mailAuthCode;
	}
	
	public String getMailAuthCode(){
		return mailAuthCode;
	}
	
	public void setBillDate(String billDate){
		this.billDate=billDate;
	}
	public String getBillDate(){
		return billDate;
	}
	public void setRepayDate(String repayDate){
		this.repayDate=repayDate;
	}
	public String getRepayDate(){
		return repayDate;
	}
	public void setMailParseDate(String mailParseDate){
		this.mailParseDate=mailParseDate;
	}
	public String getMailParseDate(){
		return mailParseDate;
	}
	public void setPolicyId(Long policyId){
		this.policyId=policyId;
	}
	public Long getPolicyId(){
		return policyId;
	}
	public void setReypayStyle(String reypayStyle){
		this.reypayStyle=reypayStyle;
	}
	public String getReypayStyle(){
		return reypayStyle;
	}
	public void setCardStatus(String cardStatus){
		this.cardStatus=cardStatus;
	}
	public String getCardStatus(){
		return cardStatus;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}



}

