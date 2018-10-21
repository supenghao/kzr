package com.dhk.api.entity.param;

/**
 * 
	* @ClassName: HXPayWithdrawQueryParam 
	* @Description: 提现查询参数
	* @author ZZL
	* @date 2018年1月7日 下午11:21:01 
	*
 */
public class HXPayWithdrawQueryParam {
	
	private String client_trans_id = "";//流水号
	private String method = "pay_withdraw_qry";
	private String orig_tran_id;//原订单号
	
	public String getClient_trans_id() {
		return client_trans_id;
	}
	public void setClient_trans_id(String client_trans_id) {
		this.client_trans_id = client_trans_id;
	}
	public String getMethod() {
		return method;
	}
/*	public void setMethod(String method) {
		this.method = method;
	}*/
	public String getOrig_tran_id() {
		return orig_tran_id;
	}
	public void setOrig_tran_id(String orig_tran_id) {
		this.orig_tran_id = orig_tran_id;
	}
	
	
	
}
