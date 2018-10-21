package com.dhk.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dhk.utils.Disguiser;
import com.dhk.utils.MyBeanUtils;
import com.dhk.utils.RSA;
import com.dhk.utils.SSLClient;
import com.sunnada.kernel.DreamConf;



/**
 * hlb服务
 * @author kebingshou
 *
 */
@Service("hlbService")
public class HlbService {
	static Logger logger = Logger.getLogger(HlbService.class);


	public static final String url = DreamConf.getPropertie("GZF_URL");
	public static final String merNo = DreamConf.getPropertie("GZF_MERNO");
	public static final String key = DreamConf.getPropertie("GZF_KEY");

	public JSONObject findOrder(String orderDate,String orderNo) throws Exception {
		JSONObject retJson = new JSONObject();
		
		String url ="http://pay.trx.helipay.com/trx/quickPayApi/interface.action";//post地址测试
		String key = "dRrZAsZhdYNN4EyvVTNI3QnjaS54L32g";//秘钥
		//报文必填值
		String P1_bizType = "QuickPayQuery";//交易类型
		String P2_orderId = orderNo;//订单号
		String P3_customerNumber = "C1800002008";//商户生成的用户唯一标识
		//密文所需map
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("P1_bizType",P1_bizType );
		map.put("P2_orderId",P2_orderId );
		map.put("P3_customerNumber",P3_customerNumber );
		String oriMessage = MyBeanUtils.getSigned(map, null,key);
		logger.info("原始串:"+oriMessage);
		String sign = Disguiser.disguiseMD5(oriMessage.trim());
		
		DefaultHttpClient httpClient = new SSLClient();
	    HttpPost postMethod = new HttpPost(url);
	    List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	    nvps.add(new BasicNameValuePair("P1_bizType",P1_bizType ));
		nvps.add(new BasicNameValuePair("P2_orderId",P2_orderId ));
		nvps.add(new BasicNameValuePair("P3_customerNumber",P3_customerNumber ));
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
	        		checkMap.put("rt6_orderAmount",jsonRet.getString("rt6_orderAmount") );
	        		checkMap.put("rt7_createDate",jsonRet.getString("rt7_createDate") );
	        		checkMap.put("rt8_completeDate",jsonRet.getString("rt8_completeDate") );
	        		checkMap.put("rt9_orderStatus",jsonRet.getString("rt9_orderStatus") );
	        		checkMap.put("rt10_serialNumber",jsonRet.getString("rt10_serialNumber") );
	        		checkMap.put("rt11_bankId",jsonRet.getString("rt11_bankId") );
	        		checkMap.put("rt12_onlineCardType",jsonRet.getString("rt12_onlineCardType") );
	        		checkMap.put("rt13_cardAfterFour",jsonRet.getString("rt13_cardAfterFour") );
	        		checkMap.put("rt14_bindId",jsonRet.getString("rt14_bindId") );
	        		checkMap.put("rt15_userId",jsonRet.getString("rt15_userId") );
	        		
	        		String checkStr = MyBeanUtils.getSigned(checkMap,null,key);//验证签名的原始字符串
	        		String checksign = Disguiser.disguiseMD5(checkStr.trim());
	                if (newSign.equalsIgnoreCase(checksign)) {
	                    if ("0000".equals(jsonRet.getString("rt2_retCode"))) {
	                    	retJson.put("code","0000");
	        				retJson.put("message","成功");
	        				return retJson;
	                    } else {
	                    	retJson.put("code","Fail");
	        				retJson.put("message",jsonRet.getString("rt3_retMsg"));
	        				return retJson;

	                    }
	                } else {
	                	retJson.put("code","9997");
	                	retJson.put("message","验证签名失败");
        				return retJson;	                
        			}
	                
	            }else{
	            	logger.info("http的状态为:"+statusCode);
		            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
		            logger.info("错误信息为:"+str);
                	retJson.put("code","9997");
    				retJson.put("message","http的状态为:"+statusCode);
    				return retJson;	      
	            }
	            
	        } catch (Exception e) {
	        	logger.error("HlbPayBankPay Exception："+e);
				retJson.put("code","9997");
				retJson.put("message","错误信息:"+e.getMessage());
				return retJson;	      
	        }		
	}


	public JSONObject findWithdraw(String orderDate,String orderNo) throws Exception {
		JSONObject retJson = new JSONObject();
	    //报文所需参数
		String url ="http://transfer.trx.helipay.com/trx/transfer/interface.action";//post地址测试
		String key = "MucZgRPkBzAm2x8EQPw6FV7EfWZs3SQZ";//代付秘钥
		String rsa_pub_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOUQ7dJsQNDltC7a9VY9ZHAD5mtg9H5Ow6X2YurMR+zNE/q2T/aROHYtvzI/zPCIdYRDr5RcWvCtskJgt/fFzjbyFTJJvMiXF/CJNmMvkVWclTxnCcr4r/1c+obtvnBS6B0Z+2soBUPsqsHStQUZfKj0ZER4KB+Ai35643dfq+u5AgMBAAECgYEAwEILOhe99xc0ujbwv+dbS2EannmL/A9jywIXV/cJT0l5QW6PguW0PAx9c1F1U1VVPSQKVD2H3oYLHgdLoVyWjJTNI75BIAx8uqSQ6nOqsl2sfrUtSNluEELr+H/lEUdBH3Ul7P7Qw8gFhOT6f9BPByS2wT+SQQb5cKkQ84NMEgECQQD5IaEaK2n3uN4G3ptUz0NrnYIxN+bq/Y7AXimHTa6qoFuGnLm6qbIEMz/LxffNv8Sba57IaCc+IJCwn8fGuvGhAkEA62Gt24VtCuUHY6f+MT5OSFFQ2okew/kJBzSvVJlQy6FogWO6Yyz/tjc9nqYg+CgfocLKdhHvE3JsbvQ4Xn9zGQJBAM2ookptLlQuSzMWjtnrI3fyFai1wi2Y1UAeO2ATk64NJKEyPexG68ql1/NK4K77sLywkepUeJjD302/CPcYGiECQHx1iLVjZpX1JiQWpvyowkHQ5Vy4VlEvNAvgWrjz3Fnfvd90uRBJsehKa0Wg+BvfM2KYGe+2aeCxhw5gChJFd7kCQB4qjfF6yKIlXpKO3tv3DcoyoOAo0SGHFG3l4dNjrB4Si7TWAXprVaY27uLvcHzv8QiuHidsltV9CYf9ySWBITo=";//RSA加密正式私钥
		//报文必填值
		String P1_bizType = "TransferQuery";//交易类型
		String P2_orderId = orderNo;//订单号
		String P3_customerNumber = "C1800002008";//商户编号
		
		//密文所需map
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("P1_bizType",P1_bizType );
		map.put("P2_orderId",P2_orderId );
		map.put("P3_customerNumber",P3_customerNumber );
		String oriMessage = MyBeanUtils.getSigned(map, null,key);
		oriMessage = oriMessage.substring(0, oriMessage.lastIndexOf("&"));
		logger.info("原始串:"+oriMessage);
//		String sign = Disguiser.disguiseMD5(oriMessage.trim());
		String sign = RSA.sign(oriMessage, RSA.getPrivateKey(rsa_pub_key));
		DefaultHttpClient httpClient = new SSLClient();
	    HttpPost postMethod = new HttpPost(url);
	    List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	    nvps.add(new BasicNameValuePair("P1_bizType",P1_bizType ));
		nvps.add(new BasicNameValuePair("P2_orderId",P2_orderId ));
		nvps.add(new BasicNameValuePair("P3_customerNumber",P3_customerNumber ));
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
	        		checkMap.put("rt6_serialNumber",jsonRet.getString("rt6_serialNumber") );
	        		checkMap.put("rt7_orderStatus",jsonRet.getString("rt7_orderStatus") );
	        		String rt7_orderStatus = jsonRet.getString("rt7_orderStatus") ;
	        		String checkStr = MyBeanUtils.getSigned(checkMap,null,key);//验证签名的原始字符串
	        		String checksign = Disguiser.disguiseMD5(checkStr.trim());
	                if (newSign.equalsIgnoreCase(checksign)) {
	                    if ("0000".equals(jsonRet.getString("rt2_retCode"))) {
	                    	if("SUCCESS".equals(rt7_orderStatus)) {
	                    		retJson.put("code","0000");
		        				retJson.put("message","成功");
	                    	}else if("INIT".equals(rt7_orderStatus)||"DOING".equals(rt7_orderStatus)) {
	            				retJson.put("code","9997");
		        				retJson.put("message","交易处理中");
	                    	}else {
	                			retJson.put("code","fail");
		        				retJson.put("message","交易失败");
	                    	}
	            		
	                    	
	        				//retJson.put("code","0000");
	        				//retJson.put("message","成功");
	        			} else {

	        				retJson.put("code","Fail");
	        				retJson.put("message",jsonRet.getString("rt3_retMsg"));
	                    }
	                } else {
	                	logger.info("签名失败，signReturn:" + checkStr+",sign:"+checksign);
	        			retJson.put("code","9997");	   
        				retJson.put("message","签名失败");

	        			}
	                
	            }else{
	            	logger.info("http的状态为:"+statusCode);
		            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
		            logger.info("错误信息为:"+str);
		        	retJson.put("code","9997");	   
    				retJson.put("message","http的状态为:"+statusCode);
	            }
	            
	        } catch (Exception e) {
	        	logger.error("HlbPayBankPay Exception："+e);
 				retJson.put("message","错误:"+e.getMessage());
	        }
	        logger.info("findWithdraw返回报文:"+retJson);
			return retJson;
	

	}



	public  String getSign(SortedMap<String, String> packageParams, String merKey) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (StringUtils.isNotEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(v);
			}
		}
		sb.append(merKey);
		logger.info("源串为:"+sb.toString());
		String sign =getSHA256StrJava(sb.toString()).toLowerCase();
		logger.info("加密后:" + sign);
		return sign;
	}

	public  String getSHA256StrJava(String str){
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}
	private  String byte2Hex(byte[] bytes){
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i=0;i<bytes.length;i++){
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length()==1){
				//1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
	private  void addMap(SortedMap<String, String> map,String key,String value){
		if(map!=null&&StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(value))
			map.put(key, value);
	}

	public  JSONObject sendForHlb(String url, List<BasicNameValuePair> nvps) {
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(10000).setConnectionRequestTimeout(2000)
					.setSocketTimeout(10000).build();
			post.setConfig(requestConfig);
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			resp = client.execute(post);
			int statusCode = resp.getStatusLine().getStatusCode();
			if (200 == statusCode) {
				String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
				JSONObject jsonRet = JSONObject.parseObject(str);
				return jsonRet;
			}else{
				logger.error("http请求错误："+statusCode);
			}

		} catch (SocketTimeoutException e){
			logger.error(e.getMessage());
		}catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (client != null)
					client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (resp != null)
					resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		HlbService hlbService = new HlbService();
		hlbService.findWithdraw("20170115", "T18011511590000105");
	}

}

