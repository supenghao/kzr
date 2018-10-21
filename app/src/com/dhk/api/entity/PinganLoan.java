package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

@SuppressWarnings("serial")
@Table(name = "t_s_pingan_loan")
public class PinganLoan extends Entity{

	private Long userId;
	private String userName;//姓名
	private String loanType;//贷款类型 E 宅e贷 I保单贷 
	private String sex;//性别
	private String phone;//电话
	private String houseAddr;//房产地址
	private String houseArea;//房屋面积
	private String userMemo;//规化用途
	private String landNature;//土地性质
	
	private String workAddr;//工作地址
	private String insuranceAmount;//保单年缴额
	private String years;//年限
	private String yearsPayed;//已缴年限
	
	private String applyDate;
	private String applyTime;
	private String cstatus;
	
	private String operId;
	private String operateDate;
	private String operateTime;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHouseAddr() {
		return houseAddr;
	}
	public void setHouseAddr(String houseAddr) {
		this.houseAddr = houseAddr;
	}
	public String getHouseArea() {
		return houseArea;
	}
	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}
	public String getUserMemo() {
		return userMemo;
	}
	public void setUserMemo(String userMemo) {
		this.userMemo = userMemo;
	}
	public String getLandNature() {
		return landNature;
	}
	public void setLandNature(String landNature) {
		this.landNature = landNature;
	}
	public String getWorkAddr() {
		return workAddr;
	}
	public void setWorkAddr(String workAddr) {
		this.workAddr = workAddr;
	}
	public String getInsuranceAmount() {
		return insuranceAmount;
	}
	public void setInsuranceAmount(String insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getYearsPayed() {
		return yearsPayed;
	}
	public void setYearsPayed(String yearsPayed) {
		this.yearsPayed = yearsPayed;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getCstatus() {
		return cstatus;
	}
	public void setCstatus(String cstatus) {
		this.cstatus = cstatus;
	}
	public String getOperId() {
		return operId;
	}
	public void setOperId(String operId) {
		this.operId = operId;
	}
	public String getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	
	
}
