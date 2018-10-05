package com.aimi.service;


import com.aimi.AimiHttpUtils;
import com.aimi.AimiRequestURL;
import com.aimi.bean.*;
import org.springframework.stereotype.Service;

/**
 * 银联绑卡接口
 * @author juxin-ecitic
 *
 */
@Service("aimiUnionPortService")
public class AimiUnionPortService {

	/**
	 * 银联绑定银行卡
	 */
	public UnionBindBankResponse unionBindBank(UnionBindBankRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, UnionBindBankResponse.class, AimiRequestURL.UNION_BINDBANK_URL);
	}

	
	/**
	 * 银联发送短信
	 */
	public UnionSendSmsResponse unionSendsms(UnionSendSmsRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, UnionSendSmsResponse.class, AimiRequestURL.UNION_SENDSMS_URL);
	}


	/**
	 * 银联确认支付
	 */
	public UnionPortPayResponse unionPortPay(UnionPortPayRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, UnionPortPayResponse.class, AimiRequestURL.UNION_PORTPAY_URL);
	}

	
}
