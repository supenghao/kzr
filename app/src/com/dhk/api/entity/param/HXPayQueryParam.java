package com.dhk.api.entity.param;

/**
 * 
	* @ClassName: HXPayQueryParam 
	* @Description: 订单查询
	* @author ZZL
	* @date 2018年1月7日 下午11:07:56 
	*
 */
public class HXPayQueryParam {
	
	private String client_trans_id;//流水号
	private String method = "pay_qry";
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
	/*public void setMethod(String method) {
		this.method = method;
	}*/
	public String getOrig_tran_id() {
		return orig_tran_id;
	}
	public void setOrig_tran_id(String orig_tran_id) {
		this.orig_tran_id = orig_tran_id;
	}
	
}
