package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanResponse;

/**
 * 子商户注册返回
 * @author juxin-ecitic
 *
 */
public class MerchantModifyResponse extends BaseBeanResponse {
	public String merchantId;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	
	

}
