package com.dhk.payment.entity.param;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
	* @ClassName: HXRegisterParam 
	* @Description: 入网注册参数
	* @author ZZL
	* @date 2018年1月7日 下午11:20:40 
	*
 */
public class HXRegisterParam extends ParamBaseEntity{
	
	@NotBlank(message="流水号不能为空")
	private String client_trans_id;//流水号
	 @NotBlank(message="商户号不能为空")
	private String merchant_code;//商户号
	 @NotBlank(message="商户名称不能为空")
	private String merchant_name;//商户名称
	 @NotBlank(message="商户所在省份不能为空")
	private String merchant_province;//商户所在省份
	 @NotBlank(message="商户所在城市不能为空")
	private String merchant_city;//商户所在城市
	 @NotBlank(message="商户地址不能为空")
	private String merchant_address;//商户地址
	 @NotBlank(message="姓名不能为空")
	private String family_name;//姓名
	 @NotBlank(message="证件号不能为空")
	private String id_card;//证件号
	 @NotBlank(message="手机号不能为空")
	private String mobile;//手机号
	 @NotBlank(message="总行联行号不能为空")
	private String payee_bank_id;//总行联行号
	 @NotBlank(message="结算账号不能为空")
	private String payee_bank_no;//结算账号
	 @NotBlank(message="总行名称不能为空")
	private String payee_bank_name;//总行名称
	 
	private String payee_branch_name = "";//开户行全称
	
	private String payee_branch_code = "";//开户行联行号
	
	private String payee_bank_province = "";//开户行省份
	
	private String payee_bank_city = "";//开户行城市
	 @NotBlank(message="手续费扣率不能为空")
	private String rate_t0;//消费交易手续费扣率
	 
	private String counter_fee_t0;//单笔消费交易手续费
	
//	private String counter_fee_t1;//单笔提现手续费
	 @NotBlank(message="操作标识不能为空")
	private String merchant_oper_flag;//操作标识
	private String method = "register";//业务接口
	
	public String getMerchant_code() {
		return merchant_code;
	}
	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}
	public String getMerchant_name() {
		return merchant_name;
	}
	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}
	public String getMerchant_province() {
		return merchant_province;
	}
	public void setMerchant_province(String merchant_province) {
		this.merchant_province = merchant_province;
	}
	public String getMerchant_city() {
		return merchant_city;
	}
	public void setMerchant_city(String merchant_city) {
		this.merchant_city = merchant_city;
	}
	public String getMerchant_address() {
		return merchant_address;
	}
	public void setMerchant_address(String merchant_address) {
		this.merchant_address = merchant_address;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPayee_bank_id() {
		return payee_bank_id;
	}
	public void setPayee_bank_id(String payee_bank_id) {
		this.payee_bank_id = payee_bank_id;
	}
	public String getPayee_bank_no() {
		return payee_bank_no;
	}
	public void setPayee_bank_no(String payee_bank_no) {
		this.payee_bank_no = payee_bank_no;
	}
	public String getPayee_bank_name() {
		return payee_bank_name;
	}
	public void setPayee_bank_name(String payee_bank_name) {
		this.payee_bank_name = payee_bank_name;
	}
	public String getPayee_branch_name() {
		return payee_branch_name;
	}
	public void setPayee_branch_name(String payee_branch_name) {
		this.payee_branch_name = payee_branch_name;
	}
	public String getPayee_branch_code() {
		return payee_branch_code;
	}
	public void setPayee_branch_code(String payee_branch_code) {
		this.payee_branch_code = payee_branch_code;
	}
	public String getPayee_bank_province() {
		return payee_bank_province;
	}
	public void setPayee_bank_province(String payee_bank_province) {
		this.payee_bank_province = payee_bank_province;
	}
	public String getPayee_bank_city() {
		return payee_bank_city;
	}
	public void setPayee_bank_city(String payee_bank_city) {
		this.payee_bank_city = payee_bank_city;
	}
	public String getRate_t0() {
		return rate_t0;
	}
	public void setRate_t0(String rate_t0) {
		this.rate_t0 = rate_t0;
	}
	public String getCounter_fee_t0() {
		return counter_fee_t0;
	}
	public void setCounter_fee_t0(String counter_fee_t0) {
		this.counter_fee_t0 = counter_fee_t0;
	}
//	public String getCounter_fee_t1() {
//		return counter_fee_t1;
//	}
//	public void setCounter_fee_t1(String counter_fee_t1) {
//		this.counter_fee_t1 = counter_fee_t1;
//	}
	public String getMerchant_oper_flag() {
		return merchant_oper_flag;
	}
	public void setMerchant_oper_flag(String merchant_oper_flag) {
		this.merchant_oper_flag = merchant_oper_flag;
	}
	public String getMethod() {
		return method;
	}
/*	public void setMethod(String method) {
		this.method = method;
	}*/
	public String getClient_trans_id() {
		return client_trans_id;
	}
	public void setClient_trans_id(String client_trans_id) {
		this.client_trans_id = client_trans_id;
	}
	
	
}
