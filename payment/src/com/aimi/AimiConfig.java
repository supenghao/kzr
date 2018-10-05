package com.aimi;

/**
 * 
 * @author juxin-ecitic
 *
 */
public class AimiConfig {
	
	
	/**
	 * 域名 
	 */
	public static  String DOMAIN_NAME_URL = "http://xapi.shengyunpay.com";
	
	/**
	 * 签名密钥
	 */
	public static  String MD5_KEY = "d14cda1255374f3b80f7696e1ca8570f";
	
	/**
	 * AES加密密钥
	 */
	public static  String AES_KEY = MD5_KEY.substring(0,16);
	
	/**
	 * 接入商ID
	 */
	public static  String ACCESSID = "100000019";

	public static  String purchaseUrl = "";

	public static  String proxyPayUrl = "";

}
