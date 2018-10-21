package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 子商户信息查询
 * @author juxin-ecitic
 *
 */
public class MerchantInfQueryRequest extends BaseBeanRequest {
	public String bindMobile;//绑定手机号
	public String time;//时间戳
	
	public String getBindMobile() {
		return bindMobile;
	}
	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	

}
