package com.aimi.bean;


import com.aimi.bean.base.BaseBeanResponse;

/**
 * 子商户信息返回
 * @author juxin-ecitic
 *
 */
public class MerchantInfQueryResponse extends BaseBeanResponse {
	
	public String merchantList;//商户信息列表, 注意此字段是个json字符串

	public String getMerchantList() {
		return merchantList;
	}

	public void setMerchantList(String merchantList) {
		this.merchantList = merchantList;
	}

	

	
	

}
