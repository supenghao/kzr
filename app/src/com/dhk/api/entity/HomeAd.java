package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_ad_detail 实体类<br/>
 * 2017-02-17 08:34:30 qch
 */
@SuppressWarnings("serial")
@Table(name = "t_ad_detail")
public class HomeAd extends Entity {

	private String adcode;
	private String detailtext;
	private String imgurl;
	private String alinkurl;
	private Long sort;

	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	public String getAdcode() {
		return adcode;
	}

	public void setDetailtext(String detailtext) {
		this.detailtext = detailtext;
	}

	public String getDetailtext() {
		return detailtext;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setAlinkurl(String alinkurl) {
		this.alinkurl = alinkurl;
	}

	public String getAlinkurl() {
		return alinkurl;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public Long getSort() {
		return sort;
	}
}
