package com.dhk.api.entity;
import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

   /**
    * t_s_beneficiary 实体类<br/>
    * 2017-02-20 12:05:15 qch
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_beneficiary")
public class Beneficiary  extends Entity {
	private Long insurancelsid;
	private String applirelation;
	private String beneficiaryname;
	private String beneficiarycertificatetype;
	private String beneficiarycertificateno;
	private String beneficiaryphone;
	private String beneficiarysex;
	private String beneficiarybirthday;
	private String beneficiaryemail;
	public void setInsurancelsid(Long insurancelsid){
		this.insurancelsid=insurancelsid;
	}
	public Long getInsurancelsid(){
		return insurancelsid;
	}
	public void setApplirelation(String applirelation){
		this.applirelation=applirelation;
	}
	public String getApplirelation(){
		return applirelation;
	}
	public void setBeneficiaryname(String beneficiaryname){
		this.beneficiaryname=beneficiaryname;
	}
	public String getBeneficiaryname(){
		return beneficiaryname;
	}
	public void setBeneficiarycertificatetype(String beneficiarycertificatetype){
		this.beneficiarycertificatetype=beneficiarycertificatetype;
	}
	public String getBeneficiarycertificatetype(){
		return beneficiarycertificatetype;
	}
	public void setBeneficiarycertificateno(String beneficiarycertificateno){
		this.beneficiarycertificateno=beneficiarycertificateno;
	}
	public String getBeneficiarycertificateno(){
		return beneficiarycertificateno;
	}
	public void setBeneficiaryphone(String beneficiaryphone){
		this.beneficiaryphone=beneficiaryphone;
	}
	public String getBeneficiaryphone(){
		return beneficiaryphone;
	}
	public void setBeneficiarysex(String beneficiarysex){
		this.beneficiarysex=beneficiarysex;
	}
	public String getBeneficiarysex(){
		return beneficiarysex;
	}
	public void setBeneficiarybirthday(String beneficiarybirthday){
		this.beneficiarybirthday=beneficiarybirthday;
	}
	public String getBeneficiarybirthday(){
		return beneficiarybirthday;
	}
	public void setBeneficiaryemail(String beneficiaryemail){
		this.beneficiaryemail=beneficiaryemail;
	}
	public String getBeneficiaryemail(){
		return beneficiaryemail;
	}
}

