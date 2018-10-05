package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanRequest;

/**
 * 子商户费率查询
 * @author juxin-ecitic
 *
 */
public class MerchantFeeQueryRequest extends BaseBeanRequest {
	
	public String tradeType;//交易类型

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	
	
	

}
