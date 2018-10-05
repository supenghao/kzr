package com.aimi.service;


import com.aimi.AimiHttpUtils;
import com.aimi.AimiRequestURL;
import com.aimi.bean.*;
import com.aimi.bean.base.ProxyPayRequest;
import org.springframework.stereotype.Service;

/**
 * 代付接口
 * @author juxin-ecitic
 *
 */
@Service("aimiProxyPayService")
public class AimiProxyPayService {

	/**
	 * 代付请求
	 */
	public WithdrawPayResponse proxyPay(ProxyPayRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request,WithdrawPayResponse.class, AimiRequestURL.WITHDRAW_PAY_URL);
	}



}
