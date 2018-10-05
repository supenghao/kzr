package com.dhk.payment.util;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UwApplyInsured")
public class ApplyInsured implements Serializable{

	private String appliRelation;
	private String uwAddress;
	private String uwBirthday;
	private String uwCertificateNo;
	private String uwCertificateType;
	private String uwCreateDate;
	private String uwCreatedUser;
	private String uwEmail;
	private String uwMobileTelephone;
	private String uwOccupationCode;
	private String uwPersonnelName;
	private String uwPersonnelSex;
	private String uwPostCode;
	private String uwUpdateDate;
	private String uwUpdatedUser;
	public String getAppliRelation() {
		return appliRelation;
	}
	public void setAppliRelation(String appliRelation) {
		this.appliRelation = appliRelation;
	}
	public String getUwAddress() {
		return uwAddress;
	}
	public void setUwAddress(String uwAddress) {
		this.uwAddress = uwAddress;
	}
	public String getUwBirthday() {
		return uwBirthday;
	}
	public void setUwBirthday(String uwBirthday) {
		this.uwBirthday = uwBirthday;
	}
	public String getUwCertificateNo() {
		return uwCertificateNo;
	}
	public void setUwCertificateNo(String uwCertificateNo) {
		this.uwCertificateNo = uwCertificateNo;
	}
	public String getUwCertificateType() {
		return uwCertificateType;
	}
	public void setUwCertificateType(String uwCertificateType) {
		this.uwCertificateType = uwCertificateType;
	}
	public String getUwCreateDate() {
		return uwCreateDate;
	}
	public void setUwCreateDate(String uwCreateDate) {
		this.uwCreateDate = uwCreateDate;
	}
	public String getUwCreatedUser() {
		return uwCreatedUser;
	}
	public void setUwCreatedUser(String uwCreatedUser) {
		this.uwCreatedUser = uwCreatedUser;
	}
	public String getUwEmail() {
		return uwEmail;
	}
	public void setUwEmail(String uwEmail) {
		this.uwEmail = uwEmail;
	}
	public String getUwMobileTelephone() {
		return uwMobileTelephone;
	}
	public void setUwMobileTelephone(String uwMobileTelephone) {
		this.uwMobileTelephone = uwMobileTelephone;
	}
	public String getUwOccupationCode() {
		return uwOccupationCode;
	}
	public void setUwOccupationCode(String uwOccupationCode) {
		this.uwOccupationCode = uwOccupationCode;
	}
	public String getUwPersonnelName() {
		return uwPersonnelName;
	}
	public void setUwPersonnelName(String uwPersonnelName) {
		this.uwPersonnelName = uwPersonnelName;
	}
	public String getUwPersonnelSex() {
		return uwPersonnelSex;
	}
	public void setUwPersonnelSex(String uwPersonnelSex) {
		this.uwPersonnelSex = uwPersonnelSex;
	}
	public String getUwPostCode() {
		return uwPostCode;
	}
	public void setUwPostCode(String uwPostCode) {
		this.uwPostCode = uwPostCode;
	}
	public String getUwUpdateDate() {
		return uwUpdateDate;
	}
	public void setUwUpdateDate(String uwUpdateDate) {
		this.uwUpdateDate = uwUpdateDate;
	}
	public String getUwUpdatedUser() {
		return uwUpdatedUser;
	}
	public void setUwUpdatedUser(String uwUpdatedUser) {
		this.uwUpdatedUser = uwUpdatedUser;
	}
	
}
