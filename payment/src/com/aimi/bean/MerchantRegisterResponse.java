package com.aimi.bean;


import com.aimi.bean.base.BaseBeanResponse;

/**
 * 子商户注册返回
 * @author juxin-ecitic
 *
 */
public class MerchantRegisterResponse extends BaseBeanResponse {
	
	public String merchantId;//子商户号

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	

}
