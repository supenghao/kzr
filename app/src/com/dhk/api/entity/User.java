package com.dhk.api.entity;

import com.dhk.api.dto.MemberInfoDto;
import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_user 实体类<br/>
 * 2017-02-16 10:55:34 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_user")
public class User extends Entity {
	private String org_id;
	private String mobilephone;
	private String id_number;
	private String password;
	private String realname;
	private String signature;
	private String status;
	private String username;
	private String id_front_url;
	private String id_opposite_url;
	private String bank_pic_url;
	private String user_level;
	private String card_no;
	private String bank_name;
	private String user_email;
	private String terminal_code;
	private String sex;
	private String province_code;
	private String city_code;
	private String district_code;
	private String user_street;
	private String head_pic_url;
	private String nick_name;
	private String is_auth;
	private Long qrcode_id;
	private String relation_no;
	private String upfileurl;
	private String merchantid;
	private String reg_date;
	private String kj_key;
	private String kj_merno;
	public User() {
	}

	public User(MemberInfoDto dto) {
		// dto.getCityCode();
		// dto.getDistrictCode();
		// dto.getHeadPic();
		this.nick_name = dto.getNickName();
		this.sex = dto.getSex();
	}

	public String getUpfileurl() {
		return upfileurl;
	}

	public void setUpfileurl(String upfileurl) {
		this.upfileurl = upfileurl;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getId_number() {
		return id_number;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRealname() {
		return realname;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setId_front_url(String id_front_url) {
		this.id_front_url = id_front_url;
	}

	public String getId_front_url() {
		return id_front_url;
	}

	public void setId_opposite_url(String id_opposite_url) {
		this.id_opposite_url = id_opposite_url;
	}

	public String getId_opposite_url() {
		return id_opposite_url;
	}

	public void setBank_pic_url(String bank_pic_url) {
		this.bank_pic_url = bank_pic_url;
	}

	public String getBank_pic_url() {
		return bank_pic_url;
	}

	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}

	public String getUser_level() {
		return user_level;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}

	public String getTerminal_code() {
		return terminal_code;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return sex;
	}

	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}

	public String getProvince_code() {
		return province_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	public String getDistrict_code() {
		return district_code;
	}

	public void setUser_street(String user_street) {
		this.user_street = user_street;
	}

	public String getUser_street() {
		return user_street;
	}

	public void setHead_pic_url(String head_pic_url) {
		this.head_pic_url = head_pic_url;
	}

	public String getHead_pic_url() {
		return head_pic_url;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setIs_auth(String is_auth) {
		this.is_auth = is_auth;
	}

	public String getIs_auth() {
		return is_auth;
	}

	public void setQrcode_id(Long qrcode_id) {
		this.qrcode_id = qrcode_id;
	}

	public Long getQrcode_id() {
		return qrcode_id;
	}

	public void setRelation_no(String relation_no) {
		this.relation_no = relation_no;
	}

	public String getRelation_no() {
		return relation_no;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public String getKj_key() {
		return kj_key;
	}

	public void setKj_key(String kj_key) {
		this.kj_key = kj_key;
	}

	public String getKj_merno() {
		return kj_merno;
	}

	public void setKj_merno(String kj_merno) {
		this.kj_merno = kj_merno;
	}

	
	
}
