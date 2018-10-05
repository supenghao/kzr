package com.aimi.demo.common;


import com.sunnada.kernel.DreamConf;

/**
 * 
 * @author juxin-ecitic
 *
 */
public class AimiConfig {



	/**
	 * 域名
	 */
	public static final String DOMAIN_NAME_URL = DreamConf.getPropertie("AIMI_DOMAIN_NAME_URL");

	/**
	 * 签名密钥
	 */
	public static final String MD5_KEY = DreamConf.getPropertie("AIMI_MD5_KEY");

	/**
	 * AES加密密钥
	 */
	public static final String AES_KEY = MD5_KEY.substring(0,16);

	/**
	 * 接入商ID
	 */
	public static final String ACCESSID = DreamConf.getPropertie("AIMI_ACCESSID");


}
