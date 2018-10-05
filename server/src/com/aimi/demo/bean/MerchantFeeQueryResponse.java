package com.aimi.demo.bean;

import com.aimi.demo.bean.base.BaseBeanResponse;

/**
 * 子商户费率
 * @author juxin-ecitic
 *
 */
public class MerchantFeeQueryResponse extends BaseBeanResponse{
	
	public String T0Rate;//T0费率
	public String T1Rate;//T1费率
	public String Fee;//T0手续费
	public String tradeType;//交易类型
	
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
	
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	@Override
	public String toString() {
		return "MerchantFeeQueryResponse [T0Rate=" + T0Rate + ", T1Rate=" + T1Rate + ", Fee=" + Fee + ", tradetype="
				+ tradeType + "]";
	}
	
	
}
