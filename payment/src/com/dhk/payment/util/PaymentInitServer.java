package com.dhk.payment.util;

import com.aimi.AimiConfig;
import com.dhk.kernel.ServerSupport;
import com.dhk.payment.config.KjConfig;
import com.dhk.payment.config.YblConfig;
import com.dhk.payment.config.GzfConfig;
import com.dhk.payment.config.HlbConfig;
import com.dhk.payment.config.HxtcConfig;
import com.dhk.payment.config.YiLianConfig;

public class PaymentInitServer extends ServerSupport{

	@Override
	public void doStart() throws Exception {
		YiLianConfig.key = ParamDef.findYiLianByName("key");
		YiLianConfig.merchantId = ParamDef.findYiLianByName("merchantId");
		YiLianConfig.productId = ParamDef.findYiLianByName("productId");
		YiLianConfig.shopNo = ParamDef.findYiLianByName("shopNo");
		YiLianConfig.version = ParamDef.findYiLianByName("version");
		YiLianConfig.url = ParamDef.findYiLianByName("url");
		YiLianConfig.callBackUrl = ParamDef.findYiLianByName("callBackUrl");

		AimiConfig.ACCESSID= ParamDef.findFrByName("ACCESSID");
		AimiConfig.DOMAIN_NAME_URL= ParamDef.findFrByName("DOMAIN_NAME_URL");
		AimiConfig.MD5_KEY= ParamDef.findFrByName("MD5_KEY");
		AimiConfig.AES_KEY= AimiConfig.MD5_KEY.substring(0,16);
		AimiConfig.purchaseUrl= ParamDef.findFrByName("purchaseUrl");
		AimiConfig.proxyPayUrl= ParamDef.findFrByName("proxyPayUrl");


		GzfConfig.key =ParamDef.findGzfByName("key");
		GzfConfig.merno =ParamDef.findGzfByName("merno");
		GzfConfig.postUrl =ParamDef.findGzfByName("postUrl");
		
		HlbConfig.key = ParamDef.findHlbByName("key");
		HlbConfig.customerNumber = ParamDef.findHlbByName("customerNumber");
		HlbConfig.rsa_pub_key = ParamDef.findHlbByName("rsa_pub_key");
		HlbConfig.callBackUrl = ParamDef.findHlbByName("callBackUrl");

		
		HxtcConfig.appIddh = ParamDef.findHXTCByName("appIddh");
		HxtcConfig.appId = ParamDef.findHXTCByName("appId");
		HxtcConfig.transType = ParamDef.findHXTCByName("transType");
		HxtcConfig.request_url = ParamDef.findHXTCByName("request_url");
		HxtcConfig.costCallBackUrl = ParamDef.findHXTCByName("costCallBackUrl");
		HxtcConfig.replanCallBackUrl = ParamDef.findHXTCByName("replanCallBackUrl");
		HxtcConfig.proxyPayCallBackUrl = ParamDef.findHXTCByName("proxyPayCallBackUrl");
		
		YblConfig.yblmerNo = ParamDef.findYBLByName("yblmerNo");
		YblConfig.yblkey = ParamDef.findYBLByName("yblkey");
		YblConfig.yblpostUrl = ParamDef.findYBLByName("yblpostUrl");
		YblConfig.costCallBackUrl = ParamDef.findYBLByName("costCallBackUrl");
		
		KjConfig.kjmerNo = ParamDef.findKjByName("kjmerNo");
		KjConfig.kjkey = ParamDef.findKjByName("kjkey");
		KjConfig.kjpostUrl = ParamDef.findKjByName("kjpostUrl");
		KjConfig.costCallBackUrl = ParamDef.findKjByName("costCallBackUrl");
		KjConfig.replanCallBackUrl = ParamDef.findKjByName("replanCallBackUrl");
		KjConfig.proxyPayCallBackUrl = ParamDef.findKjByName("proxyPayCallBackUrl");
		
	}

	@Override
	public void doStop() throws Exception {
		
	}


}
