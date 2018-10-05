package com.dhk.payment.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dhk.kernel.util.StringUtil;
import com.dhk.payment.config.HlbConfig;
import com.dhk.payment.service.IhlbService;
import com.dhk.payment.util.BaseUtil;
import com.dhk.payment.util.Disguiser;
import com.dhk.payment.util.MyBeanUtils;
import com.dhk.payment.util.RSA;
import com.dhk.payment.util.SSLClient;
import com.dhk.payment.yilian.QuickPay;


/**
 * 合利宝
 * @author Administrator
 *
 */
@Service("hlbService")
public class HlbService implements IhlbService {

	static Logger logger = Logger.getLogger(HlbService.class);
	


	/**
	 * 交易
	 */
	@Override
	public QuickPay creditPurchase(Map<String, Object> paramMap) throws Exception {
		QuickPay quickPay = new QuickPay();
		//报文所需参数
//		String upMerId = inParam.getString("upMerId", null);//上游商户号
		String url ="http://pay.trx.helipay.com/trx/quickPayApi/interface.action";//post地址测试
		String key = "dRrZAsZhdYNN4EyvVTNI3QnjaS54L32g";//秘钥
		//报文必填值
		String P1_bizType = "QuickPayBindPay";//交易类型
		String P2_customerNumber = "C1800002008";//商户编号
		String P3_bindId = (String) paramMap.get("bindId");//合利宝生成的唯一绑卡ID
		String P4_userId =  (String) paramMap.get("userId");//商户生成的用户唯一标识
		String P5_orderId = BaseUtil.objToStr(paramMap.get("orderNo"), null);//订单号
		String P6_timestamp = StringUtil.formatDate(new Date(), "yyyyMMddhhmmss") ;//订单时间
		String P7_currency = "CNY";//证件类型
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
		BigDecimal transAmt_b = new BigDecimal(transAmt).divide(new BigDecimal(100));
		String P8_orderAmount = transAmt_b.toPlainString();//金额 元为单位		
		//P8_orderAmount ="1.02";
		String P9_goodsName = "商品消费" ;//金额
		String P10_goodsDesc = "商品消费";//金额
		String P11_terminalType = "IMEI";//金额
		String P12_terminalId = P3_bindId;//金额
		String P13_orderIp = "127.0.0.1";//金额
		String P14_period = "";//金额
		String P15_periodUnit = "";//金额
		String P16_serverCallbackUrl =  HlbConfig.callBackUrl;
		String P17_validateCode = "";//金额
		
		//密文所需map
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("P1_bizType",P1_bizType );
		map.put("P2_customerNumber",P2_customerNumber );
		map.put("P3_bindId",P3_bindId );
		map.put("P4_userId",P4_userId );
		map.put("P5_orderId",P5_orderId );
		map.put("P6_timestamp",P6_timestamp );
		map.put("P7_currency",P7_currency );
		map.put("P8_orderAmount",P8_orderAmount );
		map.put("P9_goodsName",P9_goodsName );
		map.put("P10_goodsDesc",P10_goodsDesc );
		map.put("P11_terminalType",P11_terminalType );
		map.put("P12_terminalId",P12_terminalId );
		map.put("P13_orderIp",P13_orderIp );
		map.put("P14_period",P14_period );
		map.put("P15_periodUnit",P15_periodUnit );
		map.put("P16_serverCallbackUrl",P16_serverCallbackUrl );
		//map.put("P17_validateCode",P17_validateCode );
		String oriMessage = MyBeanUtils.getSigned(map, null,key);
		logger.info("原始串:"+oriMessage);
		String sign = Disguiser.disguiseMD5(oriMessage.trim());
		
		DefaultHttpClient httpClient = new SSLClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),5000);   // 设置连接超时时间(单位毫秒)  
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 30000); // 设置读数据超时时间(单位毫秒)  
	    HttpPost postMethod = new HttpPost(url);
	    List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	    nvps.add(new BasicNameValuePair("P1_bizType",P1_bizType ));
		nvps.add(new BasicNameValuePair("P2_customerNumber",P2_customerNumber ));
		nvps.add(new BasicNameValuePair("P3_bindId",P3_bindId ));
		nvps.add(new BasicNameValuePair("P4_userId",P4_userId ));
		nvps.add(new BasicNameValuePair("P5_orderId",P5_orderId ));
		nvps.add(new BasicNameValuePair("P6_timestamp",P6_timestamp ));
		nvps.add(new BasicNameValuePair("P7_currency",P7_currency ));
		nvps.add(new BasicNameValuePair("P8_orderAmount",P8_orderAmount ));
		nvps.add(new BasicNameValuePair("P9_goodsName",P9_goodsName ));
		nvps.add(new BasicNameValuePair("P10_goodsDesc",P10_goodsDesc ));
		nvps.add(new BasicNameValuePair("P11_terminalType",P11_terminalType ));
		nvps.add(new BasicNameValuePair("P12_terminalId",P12_terminalId ));
		nvps.add(new BasicNameValuePair("P13_orderIp",P13_orderIp ));
		nvps.add(new BasicNameValuePair("P14_period",P14_period ));
		nvps.add(new BasicNameValuePair("P15_periodUnit",P15_periodUnit ));
		nvps.add(new BasicNameValuePair("P16_serverCallbackUrl",P16_serverCallbackUrl ));
		nvps.add(new BasicNameValuePair("P17_validateCode",P17_validateCode ));
		nvps.add(new BasicNameValuePair("sign",sign ));
		logger.info("提交的报文为:"+nvps);
	        try {
	            postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
	            HttpResponse resp = httpClient.execute(postMethod);
	            int statusCode = resp.getStatusLine().getStatusCode();
	            if (200 == statusCode) {
	            	String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
	            	logger.info("收到的报文为:"+str);
	                JSONObject jsonRet = JSONObject.parseObject(str);
	                String newSign = jsonRet.getString("sign");
	                //密文所需map
	        		Map<String,String> checkMap = new LinkedHashMap<String,String>();
	        		checkMap.put("rt1_bizType",jsonRet.getString("rt1_bizType") );
	        		checkMap.put("rt2_retCode",jsonRet.getString("rt2_retCode") );
	        		checkMap.put("rt3_retMsg",jsonRet.getString("rt3_retMsg") );
	        		checkMap.put("rt4_customerNumber",jsonRet.getString("rt4_customerNumber") );
	        		checkMap.put("rt5_orderId",jsonRet.getString("rt5_orderId") );
	        		checkMap.put("rt6_serialNumber",jsonRet.getString("rt6_serialNumber") );
	        		
	        		checkMap.put("rt7_completeDate",jsonRet.getString("rt7_completeDate") );
	        		checkMap.put("rt8_orderAmount",jsonRet.getString("rt8_orderAmount") );
	        		checkMap.put("rt9_orderStatus",jsonRet.getString("rt9_orderStatus") );
	        		checkMap.put("rt10_bindId",jsonRet.getString("rt10_bindId") );
	        		checkMap.put("rt11_bankId",jsonRet.getString("rt11_bankId") );
	        		checkMap.put("rt12_onlineCardType",jsonRet.getString("rt12_onlineCardType") );
	        		checkMap.put("rt13_cardAfterFour",jsonRet.getString("rt13_cardAfterFour") );
	        		checkMap.put("rt14_userId",jsonRet.getString("rt14_userId") );
	        		
	        		String checkStr = MyBeanUtils.getSigned(checkMap,null,key);//验证签名的原始字符串
	        		String checksign = Disguiser.disguiseMD5(checkStr.trim());
	                if (newSign.equalsIgnoreCase(checksign)) {
	                    if ("0000".equals(jsonRet.getString("rt2_retCode"))||"0001".equals(jsonRet.getString("rt2_retCode"))) {
	                    	quickPay.setRespCode("9997");
	        				quickPay.setRespDesc("结算中");
	        				return quickPay;	                    
	        			} else {
	                    	quickPay.setRespCode("fail");
	        				quickPay.setRespDesc(jsonRet.getString("rt3_retMsg"));
	        				return quickPay;
	                    }
	                } else {
	                	quickPay.setRespCode("fail");
        				quickPay.setRespDesc("验证签名失败");
        				return quickPay;	                }
	                
	            }else{
	            	logger.info("http的状态为:"+statusCode);
		            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
		            logger.info("错误信息为:"+str);
		            quickPay.setRespCode("fail");
    				quickPay.setRespDesc("http的状态为:"+statusCode);
    				return quickPay;
	            }
	            
	        } catch (Exception e) {
	        	logger.error("HlbPayBankPay Exception："+e);
				quickPay.setRespCode("fail");
 				quickPay.setRespDesc("失败为:"+e.getMessage());
 				return quickPay;
	        }
	}
	
	/**
	 * 交易
	 */
	public QuickPay creditPurchase1(Map<String, Object> paramMap) throws Exception {
		QuickPay quickPay = new QuickPay();
		//报文所需参数
//		String upMerId = paramMap.get("upMerId", null);//上游商户号
		String url ="http://pay.trx.helipay.com/trx/quickPayApi/interface.action";//post地址测试
		String key = "dRrZAsZhdYNN4EyvVTNI3QnjaS54L32g";//秘钥
		//报文必填值
		String P1_bizType = "QuickPayBindPayValidateCode";//交易类型
		String P2_customerNumber = "C1800002008";//商户编号
		
		//数据库查询到用户账户信息，这里这个过程没有数据库连接,先从请求从获取
		String P3_bindId = (String) paramMap.get("bindId");//合利宝生成的唯一绑卡ID
		String P4_userId =  (String) paramMap.get("userId");//商户生成的用户唯一标识
		String P5_orderId = BaseUtil.objToStr(paramMap.get("orderNo"), null);//订单号
		String orderDate = BaseUtil.objToStr(paramMap.get("orderDate"), null);
		String orderTime = BaseUtil.objToStr(paramMap.get("orderTime"), null);
		String P6_timestamp = orderDate + orderTime;//订单时间
		String P7_currency = "CNY";//证件类型
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
		BigDecimal transAmt_b = new BigDecimal(transAmt).divide(new BigDecimal(100));
		String P8_orderAmount = transAmt_b.toPlainString();//金额 元为单位
		//P8_orderAmount ="1.02";
		String P9_phone =  BaseUtil.objToStr(paramMap.get("phoneNo"), null);//预留手机号
		
		//密文所需map
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("P1_bizType",P1_bizType );
		map.put("P2_customerNumber",P2_customerNumber );
		map.put("P3_bindId",P3_bindId );
		map.put("P4_userId",P4_userId );
		map.put("P5_orderId",P5_orderId );
		map.put("P6_timestamp",P6_timestamp );
		map.put("P7_currency",P7_currency );
		map.put("P8_orderAmount",P8_orderAmount );
		map.put("P9_phone",P9_phone );
		String oriMessage = MyBeanUtils.getSigned(map, null,key);
		logger.info("原始串:"+oriMessage);
		String sign = Disguiser.disguiseMD5(oriMessage.trim());
		DefaultHttpClient httpClient = new SSLClient();
	    HttpPost postMethod = new HttpPost(url);
	    List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	    nvps.add(new BasicNameValuePair("P1_bizType",P1_bizType ));
		nvps.add(new BasicNameValuePair("P2_customerNumber",P2_customerNumber ));
		nvps.add(new BasicNameValuePair("P3_bindId",P3_bindId ));
		nvps.add(new BasicNameValuePair("P4_userId",P4_userId ));
		nvps.add(new BasicNameValuePair("P5_orderId",P5_orderId ));
		nvps.add(new BasicNameValuePair("P6_timestamp",P6_timestamp ));
		nvps.add(new BasicNameValuePair("P7_currency",P7_currency ));
		nvps.add(new BasicNameValuePair("P8_orderAmount",P8_orderAmount ));
		nvps.add(new BasicNameValuePair("P9_phone",P9_phone ));
		nvps.add(new BasicNameValuePair("sign",sign ));
		logger.info("提交的报文为:"+nvps);
	        try {
	            postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
	            HttpResponse resp = httpClient.execute(postMethod);
	            int statusCode = resp.getStatusLine().getStatusCode();
	            if (200 == statusCode) {
	            	String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
	            	logger.info("收到的报文为:"+str);
	                JSONObject jsonRet = JSONObject.parseObject(str);
	                String newSign = jsonRet.getString("sign");
	                //密文所需map
	        		Map<String,String> checkMap = new LinkedHashMap<String,String>();
	        		checkMap.put("rt1_bizType",jsonRet.getString("rt1_bizType") );
	        		checkMap.put("rt2_retCode",jsonRet.getString("rt2_retCode") );
	        		//checkMap.put("rt3_retMsg",jsonRet.getString("rt3_retMsg") );
	        		checkMap.put("rt4_customerNumber",jsonRet.getString("rt4_customerNumber") );
	        		checkMap.put("rt5_orderId",jsonRet.getString("rt5_orderId") );
	        		checkMap.put("rt6_phone",jsonRet.getString("rt6_phone") );
	        		
	        		String checkStr = MyBeanUtils.getSigned(checkMap,null,key);//验证签名的原始字符串
	        		String checksign = Disguiser.disguiseMD5(checkStr.trim());
	                if (newSign.equalsIgnoreCase(checksign)) {
	                    if ("0000".equals(jsonRet.getString("rt2_retCode"))) {
	                        return HlbPayBandCardPaySMSSubmit(paramMap);
	                    } else {
	                    	quickPay.setRespCode("fail");
	        				quickPay.setRespDesc(jsonRet.getString("rt3_retMsg"));
	        				return quickPay;
	                    }
	                } else {
	                	quickPay.setRespCode("fail");
        				quickPay.setRespDesc("验证签名失败");
        				return quickPay;
	                }
	                
	            }else{
	            	logger.info("http的状态为:"+statusCode);
		            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
		            logger.info("错误信息为:"+str);
		            quickPay.setRespCode("fail");
    				quickPay.setRespDesc("http的状态为:"+statusCode);
    				return quickPay;
	            }
	            
	        } catch (Exception e) {
	        	logger.error("HlbPayBankPay Exception："+e);
				quickPay.setRespCode("fail");
 				quickPay.setRespDesc("失败为:"+e.getMessage());
 				return quickPay;
	        }
	
	}
	
	/**
	 * 调用接口
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	private QuickPay HlbPayBandCardPaySMSSubmit(Map<String, Object> paramMap) throws Exception {
		QuickPay quickPay = new QuickPay();
		//报文所需参数
//		String upMerId = inParam.getString("upMerId", null);//上游商户号
		String url ="http://pay.trx.helipay.com/trx/quickPayApi/interface.action";//post地址测试
		String key = "dRrZAsZhdYNN4EyvVTNI3QnjaS54L32g";//秘钥
		//报文必填值
		String P1_bizType = "QuickPayBindPay";//交易类型
		String P2_customerNumber = "C1800002008";//商户编号
		String P3_bindId = (String) paramMap.get("bindId");//合利宝生成的唯一绑卡ID
		String P4_userId =  (String) paramMap.get("userId");//商户生成的用户唯一标识
		String P5_orderId = BaseUtil.objToStr(paramMap.get("orderNo"), null);//订单号
		String P6_timestamp = StringUtil.formatDate(new Date(), "yyyyMMddhhmmss") ;//订单时间
		String P7_currency = "CNY";//证件类型
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
		BigDecimal transAmt_b = new BigDecimal(transAmt).divide(new BigDecimal(100));
		String P8_orderAmount = transAmt_b.toPlainString();//金额 元为单位		
		P8_orderAmount ="1.02";
		String P9_goodsName = "商品消费" ;//金额
		String P10_goodsDesc = "商品消费";//金额
		String P11_terminalType = "IMEI";//金额
		String P12_terminalId = P3_bindId;//金额
		String P13_orderIp = "127.0.0.1";//金额
		String P14_period = "";//金额
		String P15_periodUnit = "";//金额
		String P16_serverCallbackUrl =  HlbConfig.callBackUrl;
		String P17_validateCode = "";//金额
		
		//密文所需map
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("P1_bizType",P1_bizType );
		map.put("P2_customerNumber",P2_customerNumber );
		map.put("P3_bindId",P3_bindId );
		map.put("P4_userId",P4_userId );
		map.put("P5_orderId",P5_orderId );
		map.put("P6_timestamp",P6_timestamp );
		map.put("P7_currency",P7_currency );
		map.put("P8_orderAmount",P8_orderAmount );
		map.put("P9_goodsName",P9_goodsName );
		map.put("P10_goodsDesc",P10_goodsDesc );
		map.put("P11_terminalType",P11_terminalType );
		map.put("P12_terminalId",P12_terminalId );
		map.put("P13_orderIp",P13_orderIp );
		map.put("P14_period",P14_period );
		map.put("P15_periodUnit",P15_periodUnit );
		map.put("P16_serverCallbackUrl",P16_serverCallbackUrl );
		//map.put("P17_validateCode",P17_validateCode );
		String oriMessage = MyBeanUtils.getSigned(map, null,key);
		logger.info("原始串:"+oriMessage);
		String sign = Disguiser.disguiseMD5(oriMessage.trim());
		
		DefaultHttpClient httpClient = new SSLClient();
	    HttpPost postMethod = new HttpPost(url);
	    List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	    nvps.add(new BasicNameValuePair("P1_bizType",P1_bizType ));
		nvps.add(new BasicNameValuePair("P2_customerNumber",P2_customerNumber ));
		nvps.add(new BasicNameValuePair("P3_bindId",P3_bindId ));
		nvps.add(new BasicNameValuePair("P4_userId",P4_userId ));
		nvps.add(new BasicNameValuePair("P5_orderId",P5_orderId ));
		nvps.add(new BasicNameValuePair("P6_timestamp",P6_timestamp ));
		nvps.add(new BasicNameValuePair("P7_currency",P7_currency ));
		nvps.add(new BasicNameValuePair("P8_orderAmount",P8_orderAmount ));
		nvps.add(new BasicNameValuePair("P9_goodsName",P9_goodsName ));
		nvps.add(new BasicNameValuePair("P10_goodsDesc",P10_goodsDesc ));
		nvps.add(new BasicNameValuePair("P11_terminalType",P11_terminalType ));
		nvps.add(new BasicNameValuePair("P12_terminalId",P12_terminalId ));
		nvps.add(new BasicNameValuePair("P13_orderIp",P13_orderIp ));
		nvps.add(new BasicNameValuePair("P14_period",P14_period ));
		nvps.add(new BasicNameValuePair("P15_periodUnit",P15_periodUnit ));
		nvps.add(new BasicNameValuePair("P16_serverCallbackUrl",P16_serverCallbackUrl ));
		nvps.add(new BasicNameValuePair("P17_validateCode",P17_validateCode ));
		nvps.add(new BasicNameValuePair("sign",sign ));
		logger.info("提交的报文为:"+nvps);
	        try {
	            postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
	            HttpResponse resp = httpClient.execute(postMethod);
	            int statusCode = resp.getStatusLine().getStatusCode();
	            if (200 == statusCode) {
	            	String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
	            	logger.info("收到的报文为:"+str);
	                JSONObject jsonRet = JSONObject.parseObject(str);
	                String newSign = jsonRet.getString("sign");
	                //密文所需map
	        		Map<String,String> checkMap = new LinkedHashMap<String,String>();
	        		checkMap.put("rt1_bizType",jsonRet.getString("rt1_bizType") );
	        		checkMap.put("rt2_retCode",jsonRet.getString("rt2_retCode") );
	        		checkMap.put("rt3_retMsg",jsonRet.getString("rt3_retMsg") );
	        		checkMap.put("rt4_customerNumber",jsonRet.getString("rt4_customerNumber") );
	        		checkMap.put("rt5_orderId",jsonRet.getString("rt5_orderId") );
	        		checkMap.put("rt6_serialNumber",jsonRet.getString("rt6_serialNumber") );
	        		
	        		checkMap.put("rt7_completeDate",jsonRet.getString("rt7_completeDate") );
	        		checkMap.put("rt8_orderAmount",jsonRet.getString("rt8_orderAmount") );
	        		checkMap.put("rt9_orderStatus",jsonRet.getString("rt9_orderStatus") );
	        		checkMap.put("rt10_bindId",jsonRet.getString("rt10_bindId") );
	        		checkMap.put("rt11_bankId",jsonRet.getString("rt11_bankId") );
	        		checkMap.put("rt12_onlineCardType",jsonRet.getString("rt12_onlineCardType") );
	        		checkMap.put("rt13_cardAfterFour",jsonRet.getString("rt13_cardAfterFour") );
	        		checkMap.put("rt14_userId",jsonRet.getString("rt14_userId") );
	        		
	        		String checkStr = MyBeanUtils.getSigned(checkMap,null,key);//验证签名的原始字符串
	        		String checksign = Disguiser.disguiseMD5(checkStr.trim());
	                if (newSign.equalsIgnoreCase(checksign)) {
	                    if ("0000".equals(jsonRet.getString("rt2_retCode"))) {
	                    	quickPay.setRespCode("9997");
	        				quickPay.setRespDesc("结算中");
	        				return quickPay;	                    
	        			} else {
	                    	quickPay.setRespCode("fail");
	        				quickPay.setRespDesc(jsonRet.getString("rt3_retMsg"));
	        				return quickPay;
	                    }
	                } else {
	                	quickPay.setRespCode("fail");
        				quickPay.setRespDesc("验证签名失败");
        				return quickPay;	                }
	                
	            }else{
	            	logger.info("http的状态为:"+statusCode);
		            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
		            logger.info("错误信息为:"+str);
		            quickPay.setRespCode("fail");
    				quickPay.setRespDesc("http的状态为:"+statusCode);
    				return quickPay;
	            }
	            
	        } catch (Exception e) {
	        	logger.error("HlbPayBankPay Exception："+e);
				quickPay.setRespCode("fail");
 				quickPay.setRespDesc("失败为:"+e.getMessage());
 				return quickPay;
	        }
	}

	/**
	 * 代付
	 */
	@Override
	public QuickPay proxyPay(Map<String, Object> paramMap) throws Exception {
		QuickPay quickPay = new QuickPay();
		//报文所需参数
//		String upMerId = inParam.getString("upMerId", null);//上游商户号
		String url ="http://transfer.trx.helipay.com/trx/transfer/interface.action";//post地址测试
		String key = "MucZgRPkBzAm2x8EQPw6FV7EfWZs3SQZ";//代付秘钥
		String rsa_pub_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOUQ7dJsQNDltC7a9VY9ZHAD5mtg9H5Ow6X2YurMR+zNE/q2T/aROHYtvzI/zPCIdYRDr5RcWvCtskJgt/fFzjbyFTJJvMiXF/CJNmMvkVWclTxnCcr4r/1c+obtvnBS6B0Z+2soBUPsqsHStQUZfKj0ZER4KB+Ai35643dfq+u5AgMBAAECgYEAwEILOhe99xc0ujbwv+dbS2EannmL/A9jywIXV/cJT0l5QW6PguW0PAx9c1F1U1VVPSQKVD2H3oYLHgdLoVyWjJTNI75BIAx8uqSQ6nOqsl2sfrUtSNluEELr+H/lEUdBH3Ul7P7Qw8gFhOT6f9BPByS2wT+SQQb5cKkQ84NMEgECQQD5IaEaK2n3uN4G3ptUz0NrnYIxN+bq/Y7AXimHTa6qoFuGnLm6qbIEMz/LxffNv8Sba57IaCc+IJCwn8fGuvGhAkEA62Gt24VtCuUHY6f+MT5OSFFQ2okew/kJBzSvVJlQy6FogWO6Yyz/tjc9nqYg+CgfocLKdhHvE3JsbvQ4Xn9zGQJBAM2ookptLlQuSzMWjtnrI3fyFai1wi2Y1UAeO2ATk64NJKEyPexG68ql1/NK4K77sLywkepUeJjD302/CPcYGiECQHx1iLVjZpX1JiQWpvyowkHQ5Vy4VlEvNAvgWrjz3Fnfvd90uRBJsehKa0Wg+BvfM2KYGe+2aeCxhw5gChJFd7kCQB4qjfF6yKIlXpKO3tv3DcoyoOAo0SGHFG3l4dNjrB4Si7TWAXprVaY27uLvcHzv8QiuHidsltV9CYf9ySWBITo=";//RSA加密正式私钥
		//报文必填值
	/*	String accountName = BaseUtil.objToStr(paramMap.get("accountName"), null);
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
		String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);
		String cerdId = BaseUtil.objToStr(paramMap.get("cerdId"), null);
		String phoneNo = BaseUtil.objToStr(paramMap.get("phoneNo"), null);
		String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);*/
		
		String P1_bizType = "CreditCardRepayment";//交易类型
		String P2_customerNumber = "C1800002008";//商户编号
		String P3_userId = (String) paramMap.get("userId");//商户生成的用
		String P4_bindId =  (String) paramMap.get("bindId");//合利宝生成的唯一绑卡ID
		String P5_orderId = BaseUtil.objToStr(paramMap.get("orderNo"), null);//订单号
		String orderDate = BaseUtil.objToStr(paramMap.get("orderDate"), null);
		String orderTime = BaseUtil.objToStr(paramMap.get("orderTime"), null);
		String P6_timestamp = orderDate + orderTime;//订单时间
		String P7_currency = "CNY";//证件类型
		
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
		BigDecimal transAmt_b = new BigDecimal(transAmt).divide(new BigDecimal(100));
		String P8_orderAmount = transAmt_b.toPlainString();//金额
		//P8_orderAmount ="2.50";
		String P9_feeType = "PAYER";//PAYER:付款方收取手续费		RECEIVER:收款方收取手续费
		String P10_summary = "还款";//
		//密文所需map
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("P1_bizType",P1_bizType );
		map.put("P2_customerNumber",P2_customerNumber );
		map.put("P3_userId",P3_userId );
		map.put("P4_bindId",P4_bindId );
		map.put("P5_orderId",P5_orderId );
		map.put("P6_timestamp",P6_timestamp );
		map.put("P7_currency",P7_currency );
		map.put("P8_orderAmount",P8_orderAmount );
		map.put("P9_feeType",P9_feeType );
		map.put("P10_summary",P10_summary );
		String oriMessage = MyBeanUtils.getSigned(map, null,key);
		oriMessage = oriMessage.substring(0, oriMessage.lastIndexOf("&"));
		logger.info("原始串:"+oriMessage);
//		String sign = Disguiser.disguiseMD5(oriMessage.trim());
		String sign = RSA.sign(oriMessage, RSA.getPrivateKey(rsa_pub_key));
		 
		DefaultHttpClient httpClient = new SSLClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),5000);   // 设置连接超时时间(单位毫秒)  
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 30000); // 设置读数据超时时间(单位毫秒)  
	    HttpPost postMethod = new HttpPost(url);
	    List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	    nvps.add(new BasicNameValuePair("P1_bizType",P1_bizType ));
		nvps.add(new BasicNameValuePair("P2_customerNumber",P2_customerNumber ));
		nvps.add(new BasicNameValuePair("P3_userId",P3_userId ));
		nvps.add(new BasicNameValuePair("P4_bindId",P4_bindId ));
		nvps.add(new BasicNameValuePair("P5_orderId",P5_orderId ));
		nvps.add(new BasicNameValuePair("P6_timestamp",P6_timestamp ));
		nvps.add(new BasicNameValuePair("P7_currency",P7_currency ));
		nvps.add(new BasicNameValuePair("P8_orderAmount",P8_orderAmount ));
		nvps.add(new BasicNameValuePair("P9_feeType",P9_feeType ));
		nvps.add(new BasicNameValuePair("P10_summary",P10_summary ));
		nvps.add(new BasicNameValuePair("sign",sign ));
		logger.info("提交的报文为:"+nvps);
	        try {
	            postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
	            HttpResponse resp = httpClient.execute(postMethod);
	            int statusCode = resp.getStatusLine().getStatusCode();
	            if (200 == statusCode) {
	            	String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
	            	logger.info("收到的报文为:"+str);
	                JSONObject jsonRet = JSONObject.parseObject(str);
	                String newSign = jsonRet.getString("sign");
	                //密文所需map
	        		Map<String,String> checkMap = new LinkedHashMap<String,String>();
	        		checkMap.put("rt1_bizType",jsonRet.getString("rt1_bizType") );
	        		checkMap.put("rt2_retCode",jsonRet.getString("rt2_retCode") );
	        		//checkMap.put("rt3_retMsg",jsonRet.getString("rt3_retMsg") );
	        		checkMap.put("rt4_customerNumber",jsonRet.getString("rt4_customerNumber") );
	        		checkMap.put("rt5_userId",jsonRet.getString("rt5_userId") );
	        		checkMap.put("rt6_orderId",jsonRet.getString("rt6_orderId") );
	        		checkMap.put("rt7_serialNumber",jsonRet.getString("rt7_serialNumber") );
	        		checkMap.put("rt8_bindId",jsonRet.getString("rt8_bindId") );
	        		
	        		String checkStr = MyBeanUtils.getSigned(checkMap,null,key);//验证签名的原始字符串
	        		String checksign = Disguiser.disguiseMD5(checkStr.trim());
	                if (newSign.equalsIgnoreCase(checksign)) {
	                    if ("0000".equals(jsonRet.getString("rt2_retCode"))||"0001".equals(jsonRet.getString("rt2_retCode"))) {
	                    	quickPay.setRespCode("9997");
	            			quickPay.setRespDesc("结算中");
	                    } else {
	                    	quickPay.setRespCode("fail");
	        				quickPay.setRespDesc(jsonRet.getString("rt3_retMsg"));
	                    }
	                } else {
		            	logger.error("验证签名失败");
	                	quickPay.setRespCode("9997");
            			quickPay.setRespDesc("签名失败");
	                }
	                
	            }else{
	            	logger.error("http的状态为:"+statusCode);
	            	quickPay.setRespCode("9997");
        			quickPay.setRespDesc("http的状态为:"+statusCode);
	            }
	            
	        } catch (Exception e) {
	        	logger.error("HlbPayBankPay Exception："+e);
            	quickPay.setRespCode("9997");
    			quickPay.setRespDesc("错误:"+e.getMessage());
	        }
			return quickPay;
	}
	
	public static void main(String[] args) throws Exception {
		HlbService hlbService = new HlbService();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("bindId", "97ffb863483642c792a9d01d06eca429");
		//paramMap.put("bindId", "9045d199108d437ebfc571f284e52246");
		paramMap.put("userId", "K00000000029029");
		paramMap.put("orderNo", "K18012409252100680-h1");
		paramMap.put("orderDate", StringUtil.getCurrentDateTime("yyyyMMdd"));
		paramMap.put("orderTime", StringUtil.getCurrentDateTime("HHmmss"));
		paramMap.put("transAmt", "445500");
		hlbService.proxyPay(paramMap);
		/*String P3_bindId = (String) paramMap.get("bindId");//合利宝生成的唯一绑卡ID
		String P4_userId =  (String) paramMap.get("userId");//商户生成的用户唯一标识
		String P5_orderId = BaseUtil.objToStr(paramMap.get("orderNo"), null);//订单号
		String P6_timestamp = StringUtil.formatDate(new Date(), "yyyyMMddhhmmss") ;//订单时间
		String P7_currency = "CNY";//证件类型
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);*/
		
	}

}
