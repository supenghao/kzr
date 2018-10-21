package com.aimi.demo.service;


import com.aimi.demo.bean.PayOrderQueryRequest;
import com.aimi.demo.bean.PayOrderQueryResponse;
import com.aimi.demo.bean.UnifiedPaymentRequest;
import com.aimi.demo.bean.UnifiedPaymentResponse;
import com.aimi.demo.common.AimiRequestURL;
import com.aimi.demo.utils.AimiHttpUtils;
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
