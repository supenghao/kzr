package com.dhk.entity;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_ad 实体类<br/>
    * 2017-02-15 04:41:32 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_ad")
public class Ad  extends Entity {
	private String adCode;
	private String adText;
	private Long adWidth;
	private Long adHeight;
	public void setAdCode(String adCode){
		this.adCode=adCode;
	}
	public String getAdCode(){
		return adCode;
	}
	public void setAdText(String adText){
		this.adText=adText;
	}
	public String getAdText(){
		return adText;
	}
	public void setAdWidth(Long adWidth){
		this.adWidth=adWidth;
	}
	public Long getAdWidth(){
		return adWidth;
	}
	public void setAdHeight(Long adHeight){
		this.adHeight=adHeight;
	}
	public Long getAdHeight(){
		return adHeight;
	}
}

