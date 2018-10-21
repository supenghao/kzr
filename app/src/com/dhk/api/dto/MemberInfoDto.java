package com.dhk.api.dto;

public class MemberInfoDto extends IdentityDto {
	// 修改用户数据新增接口参数
	private String headPic, sex, nickName, userMobile, userEmail;
	private String cityCode, provinceCode, districtCode, usersTreet;

	/**
	 * 获取 headPic 变量
	 * 
	 * @return 返回 headPic 变量
	 */
	public String getHeadPic() {
		return headPic;
	}

	/**
	 * 设置 headPic 变量
	 * 
	 * @param headPic
	 */
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	/**
	 * 获取 sex 变量
	 * 
	 * @return 返回 sex 变量
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置 sex 变量
	 * 
	 * @param sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 获取 nickName 变量
	 * 
	 * @return 返回 nickName 变量
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * 设置 nickName 变量
	 * 
	 * @param nickName
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * 获取 userMobile 变量
	 * 
	 * @return 返回 userMobile 变量
	 */
	public String getUserMobile() {
		return userMobile;
	}

	/**
	 * 设置 userMobile 变量
	 * 
	 * @param userMobile
	 */
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	/**
	 * 获取 userEmail 变量
	 * 
	 * @return 返回 userEmail 变量
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * 设置 userEmail 变量
	 * 
	 * @param userEmail
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * 获取 cityCode 变量
	 * 
	 * @return 返回 cityCode 变量
	 */
	public String getCityCode() {
		return cityCode;
	}

	/**
	 * 设置 cityCode 变量
	 * 
	 * @param cityCode
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 获取 provinceCode 变量
	 * 
	 * @return 返回 provinceCode 变量
	 */
	public String getProvinceCode() {
		return provinceCode;
	}

	/**
	 * 设置 provinceCode 变量
	 * 
	 * @param provinceCode
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	/**
	 * 获取 districtCode 变量
	 * 
	 * @return 返回 districtCode 变量
	 */
	public String getDistrictCode() {
		return districtCode;
	}

	/**
	 * 设置 districtCode 变量
	 * 
	 * @param districtCode
	 */
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	/**
	 * 获取 usersTreet 变量
	 * 
	 * @return 返回 usersTreet 变量
	 */
	public String getUsersTreet() {
		return usersTreet;
	}

	/**
	 * 设置 usersTreet 变量
	 * 
	 * @param usersTreet
	 */
	public void setUsersTreet(String usersTreet) {
		this.usersTreet = usersTreet;
	}

	@Override
	public String toString() {
		return "MemberInfoDto [headPic=" + headPic + ", sex=" + sex
				+ ", nickName=" + nickName + ", userMobile=" + userMobile
				+ ", userEmail=" + userEmail + ", cityCode=" + cityCode
				+ ", provinceCode=" + provinceCode + ", districtCode="
				+ districtCode + ", usersTreet=" + usersTreet + "]";
	}
}
