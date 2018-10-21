package com.dhk.api.entity;
import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

   /**
    * t_s_insured 实体类<br/>
    * 2017-02-20 12:04:55 qch
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_insured")
public class Insuranced  extends Entity {
	private Long insurancelsid;
	private String applirelation;
	private String insuredname;
	private String insuredcertificatetype;
	private String insuredcertificateno;
	private String insuredphone;
	private String insuredsex;
	private String insuredbirthday;
	private String insuredemail;
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
	public void setInsuredname(String insuredname){
		this.insuredname=insuredname;
	}
	public String getInsuredname(){
		return insuredname;
	}
	public void setInsuredcertificatetype(String insuredcertificatetype){
		this.insuredcertificatetype=insuredcertificatetype;
	}
	public String getInsuredcertificatetype(){
		return insuredcertificatetype;
	}
	public void setInsuredcertificateno(String insuredcertificateno){
		this.insuredcertificateno=insuredcertificateno;
	}
	public String getInsuredcertificateno(){
		return insuredcertificateno;
	}
	public void setInsuredphone(String insuredphone){
		this.insuredphone=insuredphone;
	}
	public String getInsuredphone(){
		return insuredphone;
	}
	public void setInsuredsex(String insuredsex){
		this.insuredsex=insuredsex;
	}
	public String getInsuredsex(){
		return insuredsex;
	}
	public void setInsuredbirthday(String insuredbirthday){
		this.insuredbirthday=insuredbirthday;
	}
	public String getInsuredbirthday(){
		return insuredbirthday;
	}
	public void setInsuredemail(String insuredemail){
		this.insuredemail=insuredemail;
	}
	public String getInsuredemail(){
		return insuredemail;
	}
}

