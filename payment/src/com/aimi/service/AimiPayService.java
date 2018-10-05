package com.aimi.service;


import com.aimi.AimiHttpUtils;
import com.aimi.AimiRequestURL;
import com.aimi.bean.PayOrderQueryRequest;
import com.aimi.bean.PayOrderQueryResponse;
import com.aimi.bean.UnifiedPaymentRequest;
import com.aimi.bean.UnifiedPaymentResponse;
import org.springframework.stereotype.Service;

/**
 * 支付接口
 * @author juxin-ecitic
 *
 */
@Service("aimiPayService")
public class AimiPayService {

	
	/**
	 * 统一支付接口
	 */
	public UnifiedPaymentResponse unifiedPayment(UnifiedPaymentRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, UnifiedPaymentResponse.class, AimiRequestURL.UNIFIED_PAYMENT_URL);
	}

	/**
	 * 支付订单状态查询
	 */
	public PayOrderQueryResponse payOrderQuery(PayOrderQueryRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, PayOrderQueryResponse.class, AimiRequestURL.PAYORDER_QUERY_URL);
	}

}
