package com.dhk.payment.entity.param;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
	* @ClassName: HXPayWithdrawParam 
	* @Description: 提现参数实体
	* @author ZZL
	* @date 2018年1月7日 下午11:07:40 
	*
 */
public class HXPayWithdrawParam {
	
	@NotBlank(message="流水号不能为空")
	private String client_trans_id;//流水号
	private String method = "pay_withdraw";//接口类型
	@NotBlank(message="商户号不能为空")
	private String third_merchant_code;//终端商户号
/*	@NotBlank(message="订单日期不能为空")
	private String trans_date;//订单日期yyyyMMdd
*/	@NotBlank(message="订单金额不能为空")
	private String trans_amount;//订单金额 单位(分)，小于等于可提现金额
	
    private String back_notify_url;//结算回调地址

	public String getClient_trans_id() {
		return client_trans_id;
	}
	public void setClient_trans_id(String client_trans_id) {
		this.client_trans_id = client_trans_id;
	}
	public String getMethod() {
		return method;
	}
	public String getThird_merchant_code() {
		return third_merchant_code;
	}
	public void setThird_merchant_code(String third_merchant_code) {
		this.third_merchant_code = third_merchant_code;
	}
/*	public String getTrans_date() {
		return trans_date;
	}
	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}*/
	public String getTrans_amount() {
		return trans_amount;
	}
	public void setTrans_amount(String trans_amount) {
		this.trans_amount = trans_amount;
	}
	public String getBack_notify_url() {
		return back_notify_url;
	}
	public void setBack_notify_url(String back_notify_url) {
		this.back_notify_url = back_notify_url;
	}
	
	
}
