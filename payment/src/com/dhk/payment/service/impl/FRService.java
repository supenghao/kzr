package com.dhk.payment.service.impl;

import com.aimi.AimiConfig;
import com.aimi.bean.*;
import com.aimi.bean.base.ProxyPayRequest;
import com.aimi.service.AimiPayService;
import com.aimi.service.AimiProxyPayService;
import com.aimi.service.AimiUnionPortService;

import com.alibaba.fastjson.JSONObject;
import com.dhk.payment.service.IFRService;
import com.dhk.payment.util.BaseUtil;
import com.dhk.payment.yilian.QuickPay;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 沃付聚合支付
 * @author Administrator
 *
 */
@Service("frService")
public class FRService implements IFRService {

	static Logger logger = Logger.getLogger(FRService.class);


	@Resource(name = "aimiPayService")
	AimiPayService aimiPayService;

	@Resource(name = "aimiUnionPortService")
	AimiUnionPortService aimiUnionPortService;

	@Resource(name = "aimiProxyPayService")
	AimiProxyPayService aimiProxyPayService;

	/**
	 * 快捷支付
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public QuickPay creditPurchase(Map<String, Object> paramMap) throws Exception {
		//获取参数
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), "0");//交易金额

		//数据库查询到用户账户信息，这里这个过程没有数据库连接,先从请求从获取
		String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);//卡号
		String phoneNo = BaseUtil.objToStr(paramMap.get("phoneNo"), null);//预留手机号
		String cvn2 = BaseUtil.objToStr(paramMap.get("cvn2"), null);//cvn2
		String expDate = BaseUtil.objToStr(paramMap.get("expDate"), null);//有效期，月年
		String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);//有效期，月年
		String merchantId = BaseUtil.objToStr(paramMap.get("merchantId"), null);//有效期，月年

		QuickPay quickPay = new QuickPay();

		// 1.银联绑卡
		UnionBindBankResponse unionBindBankResponse = unionBindBanl( cardNo, cvn2, expDate, merchantId, phoneNo);
		if(!"0".equals(unionBindBankResponse.getCode())){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("1.失败："+unionBindBankResponse.getMsg());
			return quickPay;
		}

		//2.收款接口
		UnifiedPaymentResponse unifiedPaymentResponse = unifiedPaymentCode( merchantId, orderNo, transAmt);
		if(!"0".equals(unifiedPaymentResponse.getCode())){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("2.失败："+unifiedPaymentResponse.getMsg());
			return quickPay;
		}

		//3.发送短信
		UnionSendSmsResponse unionSendSmsResponse = unionSendsms( cardNo, unifiedPaymentResponse.getExtData(), merchantId, phoneNo);
		if(!"0".equals(unionSendSmsResponse.getCode())){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("3.失败："+unionSendSmsResponse.getMsg());
			return quickPay;
		}

		//4.确认支付接口
		UnionPortPayResponse unionPortPayResponse = unionPortPay( cardNo, unionSendSmsResponse.getExtData(), merchantId, phoneNo, "000000", cvn2, expDate);
		if(!"0".equals(unionPortPayResponse.getCode())){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("4.失败："+unionPortPayResponse.getMsg());
		}else{
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
		}
		return quickPay;
	}


	/**
	 * 代付
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public QuickPay proxyPay(Map<String, Object> paramMap) throws Exception {
		//获取参数
		String accountName = BaseUtil.objToStr(paramMap.get("accountName"), null);//账户名
		String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);//卡号
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), "0");//交易金额
		String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);
		String bankName = BaseUtil.objToStr(paramMap.get("bankName"), null);
		String merchantId = BaseUtil.objToStr(paramMap.get("merchantId"), null);

		if(StringUtils.isNotEmpty(bankName)){
			if(bankName.indexOf("银行")>=0){
				bankName =  bankName.substring(0,bankName.indexOf("银行")+2);
			}
		}
		QuickPay quickPay = new QuickPay();
		WithdrawPayResponse withdrawPayResponse=withdrawPay( orderNo, transAmt, cardNo, accountName, bankName,merchantId);
	    if(!"0".equals(withdrawPayResponse.getCode())){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("4.失败："+withdrawPayResponse.getMsg());
		}else {
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
		}
		
		return quickPay;
	}


	/**
	 * 1.绑定支付卡
	 * 使用(UNIONPAYPORT)下单时 要先绑定支付银行卡,修改支付卡信息也是调用该接口
	 */
	public UnionBindBankResponse unionBindBanl(String acctNo,String cvv2,String expDate,String merchantId,String phone){
		UnionBindBankRequest bindBankRequest = new UnionBindBankRequest();
		bindBankRequest.setAcctNo(acctNo);
		bindBankRequest.setCvv2(cvv2);
		bindBankRequest.setExpDate(expDate);
		bindBankRequest.setMerchantId(merchantId);
		bindBankRequest.setPhone(phone);
		try {
			UnionBindBankResponse response = aimiUnionPortService.unionBindBank(bindBankRequest);
			logger.info("银联绑定支付卡返回:"+ JSONObject.toJSONString(response));
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UnionBindBankResponse();
	}


	/*
	 * 2.收款接口
	*/
	public UnifiedPaymentResponse unifiedPaymentCode(String merchantId,String tradeNo,String tradeAmt){
		UnifiedPaymentRequest request = new UnifiedPaymentRequest();
		request.setMerchantId(merchantId);//子商户号
		request.setTradeNo(tradeNo);//第三方平台订单号
		request.setNotifyUrl(AimiConfig.purchaseUrl);//支付成功回调地址
		request.setCallbackUrl(AimiConfig.purchaseUrl);//到账通知地址
		request.setTradeAmt(tradeAmt);//订单金额 单位:分
		request.setTradeType("UNIONPAYPORT");//支付类型 参见文档附录4
		request.setManualSettle("Y");//是否清分  默认Y
		request.setGoodsName("测试订单");//商品名称
		request.setSettleType("0");//0：T0，1：T1
		request.setChlcode("0014");
		try {
			UnifiedPaymentResponse response = aimiPayService.unifiedPayment(request);
			logger.info("收款接口返回:"+ JSONObject.toJSONString(response));
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  new UnifiedPaymentResponse();
	}


	/**
	 * 3.调用银联发送短信获取验证码,注意！！短信发送接口也会返回一个extData(银联支付凭证),该extData用于确认支付接口上传
	 */

	public UnionSendSmsResponse unionSendsms(String acctNo,String ExtData,String merchantId,String phone){
		UnionSendSmsRequest request  = new UnionSendSmsRequest();
		request.setAcctNo(acctNo);//付款银行卡卡号
		//收款接口返回的银联支付凭证
		request.setExtData(ExtData);
		request.setMerchantId(merchantId);//子商户号
		request.setPhone(phone);//付款卡手机号
		try {
			UnionSendSmsResponse response = aimiUnionPortService.unionSendsms(request);
			logger.info("发送短信接口返回:"+ JSONObject.toJSONString(response));
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  new  UnionSendSmsResponse();
	}

	/**
	 * 4.确认支付接口
	 */
	public UnionPortPayResponse unionPortPay(String acctNo,String extData,String merchantId,String phone,String verifyCode,String cvv2,String expDate){
		UnionPortPayRequest request = new UnionPortPayRequest();
		request.setAcctNo(acctNo);//付款卡卡号
		//此处上传的银联支付必须为短信接口返回的extData
		request.setExtData(extData);
		request.setMerchantId(merchantId);//子商户号
		request.setPhone(phone);//付款卡手机号
		request.setVerifyCode(verifyCode);//短信验证码
		request.setCvv2(cvv2);
		request.setExpDate(expDate);
		try {
			UnionPortPayResponse response = aimiUnionPortService.unionPortPay(request);
			logger.info("银联确认支付返回:"+JSONObject.toJSONString(response));
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UnionPortPayResponse();
	}


	public WithdrawPayResponse withdrawPay(String orderNo,String tradeAmt,String recAccountNo,String recAccountName,String rcvBankName,String merchantId){
		ProxyPayRequest request = new ProxyPayRequest();
		request.setBatchId(orderNo);//批次号 不能重复
		request.setTranAmount(tradeAmt);
		request.setPayId(orderNo);
		request.setRcvBankName(rcvBankName);
		request.setRecAccountNo(recAccountNo);
		request.setCallBackUrl(AimiConfig.proxyPayUrl);
		request.setMerchantId(merchantId);
		System.out.println("merchantId:"+merchantId);
		try {
			WithdrawPayResponse response = aimiProxyPayService.proxyPay(request);
			logger.info("代付："+JSONObject.toJSONString(response));
			return  response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new WithdrawPayResponse();
	}

}
