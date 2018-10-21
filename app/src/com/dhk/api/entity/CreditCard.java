package com.dhk.api.entity;

import com.dhk.api.dto.EditCreditCarDto;
import com.dhk.api.dto.AddCreditCarDto;
import com.dhk.api.dto.RechargeDto;
import com.dhk.api.tool.AESUtil;
import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_user_creditcard 实体类<br/>
 * 2017-02-08 09:31:12 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_user_creditcard")
public class CreditCard extends Entity {

	private String userid;
	private String card_no;
	private String realname;
	private String cerdtype;
	private String cerdid;
	private String cvn2;
	private String phoneno;
	private String expdate;
	private String maxmon;// 信用卡额度
	private String bank_name;
	private String mail_addr;
	private String mail_auth_code;
	private String bill_date;
	private String repay_date;
	private String mail_parse_date;
	private Long policy_id;
	private String reypay_style;
	private String card_status;
	private String bindId; //绑定卡id

	public CreditCard() {
	}

	public CreditCard(AddCreditCarDto dto) {
		realname = dto.getRealname();
		cerdtype = dto.getCerdType();
		cerdid = dto.getCerdId();
		cvn2 = dto.getCvn2();
		phoneno = dto.getPhoneNo();
		expdate = dto.getExpDate();
		maxmon = dto.getMaxmon();
		userid = dto.getUserId();
		card_no = dto.getCardNo();
		bank_name = dto.getBank_name();
		bill_date = dto.getBill_date();
		repay_date = dto.getRepay_date();
		mail_addr = dto.getMail_addr();
		mail_auth_code = dto.getMail_passwd();
	}

	public CreditCard(RechargeDto dto) {
		realname = dto.getRealname();
		cerdtype = "01";
		cerdid = dto.getCerdId();
		cvn2 = dto.getCvn2();
		phoneno = dto.getPhoneNo();
		expdate = dto.getExpdate();
		maxmon = "";
		userid = dto.getUserId();
		card_no = dto.getCreditCardNo();
		bank_name = dto.getBankName();
		bill_date ="";
		repay_date ="";
		mail_addr = "";
		mail_auth_code = "";
	}



	public CreditCard(EditCreditCarDto dto) {
		userid = dto.getUserId();
		card_no = dto.getCardNo();
		bill_date = dto.getBill_date();
		mail_addr = dto.getMail_addr();
		mail_auth_code = dto.getMail_passwd();
		repay_date = dto.getRepay_date();
		cvn2 = dto.getCvn2();
		expdate = dto.getExpDate();
		maxmon = dto.getMaxmon();
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRealname() {
		return realname;
	}

	public void setCerdtype(String cerdtype) {
		this.cerdtype = cerdtype;
	}

	public String getCerdtype() {
		return cerdtype;
	}

	public void setCerdid(String cerdid) {
		this.cerdid = cerdid;
	}

	public String getCerdid() {
		return cerdid;
	}

	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}

	public String getCvn2() {
		return cvn2;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setExpdate(String expdate) {
		this.expdate = expdate;
	}

	public String getExpdate() {
		return expdate;
	}

	public void setMaxmon(String maxmon) {
		this.maxmon = maxmon;
	}

	public String getMaxmon() {
		return maxmon;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setMail_addr(String mail_addr) {
		this.mail_addr = mail_addr;
	}

	public String getMail_addr() {
		return mail_addr;
	}

	public void setMail_auth_code(String mail_auth_code) {
		this.mail_auth_code = mail_auth_code;
	}

	public String getMail_auth_code() {
		return mail_auth_code;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}

	public String getBill_date() {
		return bill_date;
	}

	public void setRepay_date(String repay_date) {
		this.repay_date = repay_date;
	}

	public String getRepay_date() {
		return repay_date;
	}

	public void setMail_parse_date(String mail_parse_date) {
		this.mail_parse_date = mail_parse_date;
	}

	public String getMail_parse_date() {
		return mail_parse_date;
	}

	public void setPolicy_id(Long policy_id) {
		this.policy_id = policy_id;
	}

	public Long getPolicy_id() {
		return policy_id;
	}

	public void setReypay_style(String reypay_style) {
		this.reypay_style = reypay_style;
	}

	public String getReypay_style() {
		return reypay_style;
	}

	public void setCard_status(String card_status) {
		this.card_status = card_status;
	}

	public String getCard_status() {
		return card_status;
	}


	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public void encrypt() throws Exception{
		card_no= AESUtil.Encrypt(card_no);
		realname=AESUtil.Encrypt(realname);
		cerdid=AESUtil.Encrypt(cerdid);
		cvn2=AESUtil.Encrypt(cvn2);
		phoneno=AESUtil.Encrypt(phoneno);
		expdate=AESUtil.Encrypt(expdate);
	}

	public void decrypt() throws Exception{
		card_no=AESUtil.Decrypt(card_no);
		realname=AESUtil.Decrypt(realname);
		cerdid=AESUtil.Decrypt(cerdid);
		cvn2=AESUtil.Decrypt(cvn2);
		phoneno=AESUtil.Decrypt(phoneno);
		expdate=AESUtil.Decrypt(expdate);
	}


	@Override
	public String toString() {
		return "CreditCard [userid=" + userid + ", card_no=" + card_no
				+ ", realname=" + realname + ", cerdtype=" + cerdtype
				+ ", cerdid=" + cerdid + ", cvn2=" + cvn2 + ", phoneno="
				+ phoneno + ", expdate=" + expdate + ", maxmon=" + maxmon
				+ ", bank_name=" + bank_name + ", mail_addr=" + mail_addr
				+ ", mail_auth_code=" + mail_auth_code + ", bill_date="
				+ bill_date + ", repay_date=" + repay_date
				+ ", mail_parse_date=" + mail_parse_date + ", policy_id="
				+ policy_id + ", reypay_style=" + reypay_style
				+ ", card_status=" + card_status + "]";
	}

}
