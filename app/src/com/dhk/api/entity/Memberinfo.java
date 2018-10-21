package com.dhk.api.entity;

import com.dhk.api.dto.MemberInfoDto;
import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_s_memberinfo 实体类<br/>
 * 2016-12-21 05:04:13 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_s_memberinfo")
public class Memberinfo extends Entity {

	private String userId;
	private String cityCode;
	private String districtCode;
	private String headPic;
	private String nickName;
	private String provinceCode;
	private String sex;
	private String terminalCode;
	private String userEmail;
	private String userMobile;
	private String usersTreet;

	public Memberinfo(MemberInfoDto dto) {
		this.cityCode = dto.getCityCode();
		this.districtCode = dto.getDistrictCode();
		this.headPic = dto.getHeadPic();
		this.nickName = dto.getNickName();
		this.provinceCode = dto.getProvinceCode();
		this.sex = dto.getSex();
		this.userEmail = dto.getUserEmail();
		this.userMobile = dto.getUserMobile();
		this.usersTreet = dto.getUsersTreet();
		this.terminalCode = dto.getTerminalCode();
	}

	public Memberinfo() {
	}

	public Memberinfo(User user) {
		this.cityCode = user.getCity_code();
		this.districtCode = user.getDistrict_code();
		this.headPic = user.getHead_pic_url();
		this.nickName = user.getNick_name();
		this.provinceCode = user.getProvince_code();
		this.sex = user.getSex();
		this.userEmail = user.getUser_email();
		this.userMobile = user.getMobilephone();
		this.usersTreet = user.getUser_street();
		this.terminalCode = user.getTerminal_code();
		this.userId = user.getId() == null ? "" : user.getId() + "";
	}

	/**
	 * 获取 userId 变量
	 * 
	 * @return 返回 userId 变量
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置 userId 变量
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return sex;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUsersTreet(String usersTreet) {
		this.usersTreet = usersTreet;
	}

	public String getUsersTreet() {
		return usersTreet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Memberinfo [userid=" + userId + ", cityCode=" + cityCode
				+ ", districtCode=" + districtCode + ", headPic=" + headPic
				+ ", nickName=" + nickName + ", provinceCode=" + provinceCode
				+ ", sex=" + sex + ", terminalCode=" + terminalCode
				+ ", userEmail=" + userEmail + ", userMobile=" + userMobile
				+ ", usersTreet=" + usersTreet + "]";
	}

}
