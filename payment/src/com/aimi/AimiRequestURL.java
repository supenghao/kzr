package com.aimi;


/**
 * aimi 请求url
 * @author juxin-ecitic
 *
 */
public class AimiRequestURL {
	
	
	
	/**
	 * 商户注册路径url
	 */
	public static final String MERCHANT_REGISTER_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/ledger/register.do";
	
	/**
	 * 子商户修改url
	 */
	public static final String MERCHANT_MODIFY_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/ledger/modify.do";
	
	/**
	 * 子商户资质上传url
	 */
	public static final String MERCHANT_UPLOADIAMGE_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/ledger/uploadLedgerQualifications.do";
	
	/**
	 * 子商户信息查询url
	 */
	public static final String MERCHANT_QUERY_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/merchant/queryMerchant.do";
	
	/**
	 * 子商户资质查询url
	 */
	public static final String MERCHANT_IAMGE_QUERY_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/merchantCredentials/queryMerchantCredentials.do";
	
	/**
	 * 子商户费率设置url
	 */
	public static final String MERCHANT_SETTINGFEE_URL = AimiConfig.DOMAIN_NAME_URL+"/pub/fee/setFee.do";
	
	/**
	 * 子商户费率查询url
	 */
	public static final String MERCHANT_FEEQUERY_URL = AimiConfig.DOMAIN_NAME_URL+"/pub/fee/getFee.do";
	
	/**
	 * 统一下单url
	 */
	public static final String UNIFIED_PAYMENT_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/pay/unifiedPayment.do";

	/**
	 * 支付订单状态查询url
	 */
	public static final String PAYORDER_QUERY_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/pay/queryTrade.do";
	
	/**
	 * 银联绑付款卡url
	 */
	public static final String UNION_BINDBANK_URL = AimiConfig.DOMAIN_NAME_URL+"/unionport/unionportbindbank.do";
	
	/**
	 * 银联发送短信url
	 */
	public static final String UNION_SENDSMS_URL = AimiConfig.DOMAIN_NAME_URL+"/unionport/sendsms.do";
	
	/**
	 * 银联确认支付url
	 */
	public static final String UNION_PORTPAY_URL = AimiConfig.DOMAIN_NAME_URL+"/unionport/unionportpay.do";
	
	/**
	 * 代付订单查询url
	 */
	public static final String WITHDRAW_QUERY_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/sdk/queryPayClearingDetails.do";
	
	/**
	 * 代付请求url
	 */
	public static final String WITHDRAW_PAY_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/sdk/orderClearing.do";
	
	/**
	 * 查询账户余额url
	 */
	public static final String QUERY_BALANCE_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/sdk/queryBalance.do";
	
	/**
	 * 查询交易明细url
	 */
	public static final String QUERY_BALACENDETAILS_URL = AimiConfig.DOMAIN_NAME_URL+"/saleapi/sdk/queryPayClearingDetails.do";
	
}
