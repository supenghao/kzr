package com.aimi.bean;


import com.aimi.bean.base.BaseBeanResponse;

/**
 * 子商户照片上传返回
 * @author juxin-ecitic
 *
 */
public class MerchantUploadImageResponse extends BaseBeanResponse {
	
	public String complete;//商户资质信息是否已上传完整  false:否  true:是.

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}
	
	

}
