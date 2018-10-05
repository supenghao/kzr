package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 子商户费率设置
 * @author juxin-ecitic
 *
 */
public class MerchantSettingFeeRequest extends BaseBeanRequest {
	
	public String tradeType;//交易类型
	public String T0Rate;//T0费率
	public String T1Rate;//T1费率
	public String Fee;//T0手续费
	
	
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getT0Rate() {
		return T0Rate;
	}
	public void setT0Rate(String t0Rate) {
		T0Rate = t0Rate;
	}
	public String getT1Rate() {
		return T1Rate;
	}
	public void setT1Rate(String t1Rate) {
		T1Rate = t1Rate;
	}
	public String getFee() {
		return Fee;
	}
	public void setFee(String fee) {
		Fee = fee;
	}
	

}
