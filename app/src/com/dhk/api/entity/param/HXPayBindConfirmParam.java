package com.dhk.api.entity.param;

/**
 * 
	* @ClassName: HXPayBindSubmitParam 
	* @Description: 绑定确认
	* @author ZZL
	* @date 2018年5月22日 下午7:11:12 
	*
 */
public class HXPayBindConfirmParam {
	
	private String client_trans_id;//流水号
	private String method = "BIND_CONFIRM";
	private String mobile;//手机号
	private String cvv;//安全码
	private String expire_date;//有效期
	private String family_name;//姓名
	private String id_card;//身份证
	private String pay_bank_no;//卡号
	private String verify_code;//短信验证码
	private String bind_apply_code;//绑卡申请流水号
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


	public String getVerify_code() {
		return verify_code;
	}


	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}


	public String getBind_apply_code() {
		return bind_apply_code;
	}


	public void setBind_apply_code(String bind_apply_code) {
		this.bind_apply_code = bind_apply_code;
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
