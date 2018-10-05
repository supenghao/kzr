package com.dhk.entity;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_ad_detail 实体类<br/>
    * 2017-02-15 04:42:13 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_ad_detail")
public class AdDetail  extends Entity {
	private String adCode;
	private String detailText;
	private String imgUrl;
	private String alinkUrl;
	private Long sort;
	public void setAdCode(String adCode){
		this.adCode=adCode;
	}
	public String getAdCode(){
		return adCode;
	}
	public void setDetailText(String detailText){
		this.detailText=detailText;
	}
	public String getDetailText(){
		return detailText;
	}
	public void setImgUrl(String imgUrl){
		this.imgUrl=imgUrl;
	}
	public String getImgUrl(){
		return imgUrl;
	}
	public void setAlinkUrl(String alinkUrl){
		this.alinkUrl=alinkUrl;
	}
	public String getAlinkUrl(){
		return alinkUrl;
	}
	public void setSort(Long sort){
		this.sort=sort;
	}
	public Long getSort(){
		return sort;
	}
}

