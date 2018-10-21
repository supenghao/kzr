package com.dhk.api.entity.param;

/**
 * 
	* @ClassName: HXPayBindParam 
	* @Description: 绑定银行卡
	* @author ZZL
	* @date 2018年5月22日 下午7:10:53 
	*
 */
public class HXPayBindParam {
	
	private String client_trans_id;//流水号
	private String method = "BIND_APPLY";
	private String mobile;
	private String cvv;
	private String expire_date;
	private String family_name;
	private String id_card;
	private String pay_bank_no;
	private String third_merchant_code;
	
	public String getMethod() {
		return method;
	}
	/*public void setMethod(String method) {
		this.method = method;
	}*/

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getExpire_date() {
		return expire_date;
	}

	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public String getPay_bank_no() {
		return pay_bank_no;
	}

	public void setPay_bank_no(String pay_bank_no) {
		this.pay_bank_no = pay_bank_no;
	}


	public String getClient_trans_id() {
		return client_trans_id;
	}

	public void setClient_trans_id(String client_trans_id) {
		this.client_trans_id = client_trans_id;
	}

	public String getThird_merchant_code() {
		return third_merchant_code;
	}

	public void setThird_merchant_code(String third_merchant_code) {
		this.third_merchant_code = third_merchant_code;
	}

	
	
}
