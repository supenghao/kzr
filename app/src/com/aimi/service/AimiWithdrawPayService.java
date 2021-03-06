package com.aimi.service;


import com.aimi.AimiHttpUtils;
import com.aimi.AimiRequestURL;
import com.aimi.bean.*;

/**
 * 代付接口
 * @author juxin-ecitic
 *
 */
public class AimiWithdrawPayService {

	/**
	 * 代付订单查询
	 */
	public WithdrawQueryResponse withdrawQuery(WithdrawQueryRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request,WithdrawQueryResponse.class, AimiRequestURL.WITHDRAW_QUERY_URL);
	}

	/**
	 * 代付请求
	 */
	public WithdrawPayResponse withdrawPay(WithdrawPayRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request,WithdrawPayResponse.class,AimiRequestURL.WITHDRAW_PAY_URL);
	}

	/**
	 * 查询账户余额度
	 */
	public QueryBalanceResponse queryBalance(QueryBalanceRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request,QueryBalanceResponse.class,AimiRequestURL.QUERY_BALANCE_URL);
	}
	
	/**
	 * 查询账户交易明细
	 */
	public QueryBalanceDetailsResponse queryBalanceDetails(QueryBalanceDetailsRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, QueryBalanceDetailsResponse.class, AimiRequestURL.QUERY_BALACENDETAILS_URL);
	}

}
