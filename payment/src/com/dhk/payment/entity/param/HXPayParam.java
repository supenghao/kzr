package com.dhk.payment.entity.param;

import java.math.BigDecimal;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.dhk.payment.config.HxtcConfig;
import com.dhk.payment.util.ArithUtil;

public class HXPayParam {
	
	@NotBlank(message="流水号不能为空")
	private String client_trans_id;//流水号
	@NotBlank(message="手机号不能为空")
	private String mobile ;//手机号
	@NotBlank(message="姓名不能为空")
	private String family_name;//姓名
	@NotBlank(message="身份证不能为空")
	private String id_card;//身份证
	@NotBlank(message="信用卡卡号不能为空")
	private String pay_bank_no;//信用卡卡号
/*		private String pay_bank_name = "招商银行";
	private String pay_bank_code = "03070000";*/

/*		private String trans_time = new SimpleDateFormat("yyyyMMddHHmmss".format(new Date(;
	private String trans_date = new SimpleDateFormat("yyyyMMdd".format(new Date(;*/
	@NotBlank(message="订单金额不能为空")
	private String trans_amount;//订单金额
	@NotBlank(message="费率不能为空")
	private String rate_t0;//费率
	@NotBlank(message="单笔消费交易手续费不能为空")
	private String counter_fee_t0;//单笔消费交易手续费
	//operation_fee=trans_amount*(rate_t0/100
	private String operation_fee ;//= (int)Math.ceil(NumberUtils.toDouble(businessReq.get("trans_amount"*(NumberUtils.toDouble(businessReq.get("rate_t0"/100+"";//手续费
	//pay_amount=trans_amount-operation_fee-counter_fee_t0
	private String pay_amount ;//= (intMath.floor(NumberUtils.toDouble(businessReq.get("trans_amount" - NumberUtils.toDouble(businessReq.get("operation_fee" - NumberUtils.toDouble(businessReq.get("counter_fee_t0"+"";//到账金额
	@NotBlank(message="有效期不能为空")
	private String expire_date ;//有效期
	@NotBlank(message="安全码不能为空")
	private String cvv;//安全码
	@NotBlank(message="回调地址不能为空")
	private String back_notify_url;//回调地址
	@NotBlank(message="终端商户号不能为空")
	private String third_merchant_code;//终端商户号
	
	private String method = "PAY";
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getTrans_amount() {
		return trans_amount;
	}
	public void setTrans_amount(String trans_amount) {
		this.trans_amount = trans_amount;
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
	public String getOperation_fee() {
//		operation_fee = (int)Math.ceil(NumberUtils.toDouble(trans_amount)*(NumberUtils.toDouble(rate_t0)/100))+"";
		operation_fee = ArithUtil.multiply(trans_amount, ArithUtil.divide(NumberUtils.toDouble(rate_t0), 100D, 4, BigDecimal.ROUND_FLOOR)+"")+"";
		return  operation_fee;
	}
	public void setOperation_fee(String operation_fee) {
		this.operation_fee = operation_fee;
	}
	public String getPay_amount() {
//		pay_amount = (int)Math.floor(NumberUtils.toDouble(trans_amount) - NumberUtils.toDouble(operation_fee) - NumberUtils.toDouble(counter_fee_t0))+"";
//		    double _rate = ArithUtil.divide(Double.parseDouble(operation_fee), 100D, 4, BigDecimal.ROUND_FLOOR);
//		    int _transAmount = Integer.parseInt(trans_amount);
		    pay_amount  = (int) Math.floor(NumberUtils.toDouble(trans_amount) - NumberUtils.toDouble(operation_fee) - NumberUtils.toDouble(counter_fee_t0)) + "";
		return  pay_amount;
	}
	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}
	public String getExpire_date() {
		return expire_date;
	}
	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getBack_notify_url() {
		back_notify_url= HxtcConfig.costCallBackUrl;
		return back_notify_url;
	}
	public void setBack_notify_url(String back_notify_url) {
		this.back_notify_url = back_notify_url;
	}
	public String getThird_merchant_code() {
		return third_merchant_code;
	}
	public void setThird_merchant_code(String third_merchant_code) {
		this.third_merchant_code = third_merchant_code;
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
