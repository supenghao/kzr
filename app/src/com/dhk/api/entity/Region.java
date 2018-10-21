package com.dhk.api.entity;

import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

/**
 * t_region 实体类<br/>
 * 2017-02-19 02:20:10 zzl
 */
@SuppressWarnings("serial")
@Table(name = "t_region")
public class Region extends Entity {

	private String FID;
	private String REGION;
	private String BANK_CODE;
	public String getFID() {
		return FID;
	}
	public void setFID(String fID) {
		FID = fID;
	}
	public String getREGION() {
		return REGION;
	}
	public void setREGION(String rEGION) {
		REGION = rEGION;
	}
	public String getBANK_CODE() {
		return BANK_CODE;
	}
	public void setBANK_CODE(String bANK_CODE) {
		BANK_CODE = bANK_CODE;
	}

}
