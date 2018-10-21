package com.dhk.api.third;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.dhk.api.dto.AddCreditCarDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.tool.OrderNoUtil;
import com.xdream.kernel.util.StringUtil;

/**
 * 
 * 合利宝 绑定卡
 * @author kebingshou
 *
 */
public class HlbBankCardUtil {
	private static Logger logger = Logger.getLogger(HlbBankCardUtil.class);
    private  static final String PREFIX = "K";

	public static QResponse HlbPayAuthBandCardSMS(AddCreditCarDto dto) throws Exception{
		//报文所需参数
		QResponse ret = new QResponse();
		String url ="http://pay.trx.helipay.com/trx/quickPayApi/interface.action";//post地址测试
		String key = "dRrZAsZhdYNN4EyvVTNI3QnjaS54L32g";//秘钥
		//报文必填值
		String P1_bizType = "QuickPayBindCardValidateCode";//交易类型
		String P2_customerNumber = "C1800002008";//商户编号
		String userId = dto.getUserId();
		int cnt = 14-userId.length();
		for(int i=0;i<cnt;i++)
			userId = "0"+userId;
		String P3_userId = PREFIX+userId;//商户编号
		String P4_orderId = generateTransId();//订单号
		String P5_timestamp =StringUtil.getCurrentDateTime("yyyyMMddhhmmss");//订单时间
		String P6_cardNo = dto.getCardNo();//卡号
		String P7_phone = dto.getPhoneNo();//手机号码
		ret.data = P4_orderId;
		//密文所需map
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("P1_bizType",P1_bizType );
		map.put("P2_customerNumber",P2_customerNumber );
		map.put("P3_userId",P3_userId );
		map.put("P4_orderId",P4_orderId );
		map.put("P5_timestamp",P5_timestamp );
		map.put("P6_cardNo",P6_cardNo );
		map.put("P7_phone",P7_phone );
		String oriMessage = MyBeanUtils.getSigned(map, null,key);
		logger.info("原始串:"+oriMessage);
		String sign = Disguiser.disguiseMD5(oriMessage.trim());
		DefaultHttpClient httpClient = new SSLClient();
	    HttpPost postMethod = new HttpPost(url);
	    List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	    nvps.add(new BasicNameValuePair("P1_bizType",P1_bizType ));
		nvps.add(new BasicNameValuePair("P2_customerNumber",P2_customerNumber ));
		nvps.add(new BasicNameValuePair("P3_userId",P3_userId ));
		nvps.add(new BasicNameValuePair("P4_orderId",P4_orderId ));
		nvps.add(new BasicNameValuePair("P5_timestamp",P5_timestamp ));
		nvps.add(new BasicNameValuePair("P6_cardNo",P6_cardNo ));
		nvps.add(new BasicNameValuePair("P7_phone",P7_phone ));
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
	                        ret.state = true;
	                        ret.msg = "短信发送成功";
	                        return ret;
	                    } else {
	                        ret.state = false;
	                        ret.msg = jsonRet.getString("rt3_retMsg");
	                        return ret;
	                    }
	                } else {
                        ret.state = false;
                        ret.msg =  "验证签名失败";
                        return ret;
                        }
	                
	            }else{
	            	logger.info("http的状态为:"+statusCode);
		            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
		            logger.info("错误信息为:"+str); 
                    ret.state = false;
                    ret.msg =  "http的状态为:"+statusCode;
                    return ret;
	            }
	            
	        } catch (Exception e) {
	        	logger.error("HlbPayBankPay Exception："+e);
                  ret.state = false;
                  ret.msg =  "错误信息:"+e.getMessage();
                  return ret;
	        }
	}
	
	
	public static QResponse HlbPayAuthBandCardSubmit(AddCreditCarDto dto) throws Exception{
		//报文所需参数
//		String upMerId = inParam.getString("upMerId", null);//上游商户号
		String url ="http://pay.trx.helipay.com/trx/quickPayApi/interface.action";//post地址测试
		String key = "dRrZAsZhdYNN4EyvVTNI3QnjaS54L32g";//秘钥
		QResponse ret = new QResponse();
		//报文必填值
		String P1_bizType = "QuickPayBindCard";//交易类型
		String P2_customerNumber = "C1800002008";//商户编号
		String userId = dto.getUserId();
		int cnt = 14-userId.length();
		for(int i=0;i<cnt;i++)
			userId = "0"+userId;
		String P3_userId = PREFIX+userId;//商户编号		
		String P4_orderId = dto.getOrderId();//订单号
		String P5_timestamp = StringUtil.getCurrentDateTime("yyyyMMddhhmmss");//订单时间
		String P6_payerName = dto.getRealname();//姓名
		String P7_idCardType = "IDCARD";//证件类型
		String P8_idCardNo = dto.getCerdId();//身份证号
		String P9_cardNo = dto.getCardNo();//卡号
		String expDate = dto.getExpDate(); // inParam.getString("expDate", null);//信用卡有效期
		String P10_year = "";//信用卡有效期年份
		String P11_month = "";//信用卡有效期月份
		if(StringUtils.isNotEmpty(expDate)){
			if(expDate.length()>2){
				P11_month = expDate.substring(2);
				P10_year = expDate.substring(0,2);
			}else{
				  ret.state = false;
                  ret.msg =  "信用卡有效期格式错误";
                  return ret;
			}
		}
		
		String P12_cvv2 = dto.getCvn2();//cnv2
		String P13_phone = dto.getPhoneNo();//手机号
		String P14_validateCode = dto.getCheckCode();//短信验证码
		
		//密文所需map
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("P1_bizType",P1_bizType );
		map.put("P2_customerNumber",P2_customerNumber );
		map.put("P3_userId",P3_userId );
		map.put("P4_orderId",P4_orderId );
		map.put("P5_timestamp",P5_timestamp );
		map.put("P6_payerName",P6_payerName );
		map.put("P7_idCardType",P7_idCardType );
		map.put("P8_idCardNo",P8_idCardNo );
		map.put("P9_cardNo",P9_cardNo );
		map.put("P10_year",P10_year );
		map.put("P11_month",P11_month );
		map.put("P12_cvv2",P12_cvv2 );
		map.put("P13_phone",P13_phone );
		map.put("P14_validateCode",P14_validateCode );
		String oriMessage = MyBeanUtils.getSigned(map, null,key);
		logger.info("原始串:"+oriMessage);
		String sign = Disguiser.disguiseMD5(oriMessage.trim());
		
		DefaultHttpClient httpClient = new SSLClient();
	    HttpPost postMethod = new HttpPost(url);
	    List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	    nvps.add(new BasicNameValuePair("P1_bizType",P1_bizType ));
		nvps.add(new BasicNameValuePair("P2_customerNumber",P2_customerNumber ));
		nvps.add(new BasicNameValuePair("P3_userId",P3_userId ));
		nvps.add(new BasicNameValuePair("P4_orderId",P4_orderId ));
		nvps.add(new BasicNameValuePair("P5_timestamp",P5_timestamp ));
		nvps.add(new BasicNameValuePair("P6_payerName",P6_payerName ));
		nvps.add(new BasicNameValuePair("P7_idCardType",P7_idCardType ));
		nvps.add(new BasicNameValuePair("P8_idCardNo",P8_idCardNo ));
		nvps.add(new BasicNameValuePair("P9_cardNo",P9_cardNo ));
		nvps.add(new BasicNameValuePair("P10_year",P10_year ));
		nvps.add(new BasicNameValuePair("P11_month",P11_month ));
		nvps.add(new BasicNameValuePair("P12_cvv2",P12_cvv2 ));
		nvps.add(new BasicNameValuePair("P13_phone",P13_phone ));
		nvps.add(new BasicNameValuePair("P14_validateCode",P14_validateCode ));
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
	        		checkMap.put("rt7_bindStatus",jsonRet.getString("rt7_bindStatus") );
	        		checkMap.put("rt8_bankId",jsonRet.getString("rt8_bankId") );
	        		checkMap.put("rt9_cardAfterFour",jsonRet.getString("rt9_cardAfterFour") );
	        		checkMap.put("rt10_bindId",jsonRet.getString("rt10_bindId") );
	        		checkMap.put("rt11_serialNumber",jsonRet.getString("rt11_serialNumber") );
	        		
	        		String checkStr = MyBeanUtils.getSigned(checkMap,null,key);//验证签名的原始字符串
	        		String checksign = Disguiser.disguiseMD5(checkStr.trim());
	                if (newSign.equalsIgnoreCase(checksign)) {
	                    if ("0000".equals(jsonRet.getString("rt2_retCode"))) {
	                    	 ret.state = true;
	                         ret.msg =  "绑定卡成功";
	                         ret.data = jsonRet.getString("rt10_bindId");  //绑定卡id
	                         return ret;
	                    } else {
	                    	 ret.state = false;
	                         ret.msg =  "绑定卡成功失败:"+jsonRet.getString("rt3_retMsg");
	                         return ret;
	                    }
	                } else {
	                	 ret.state = false;
                         ret.msg =  "绑定卡成功失败:验证签名失败";
                         return ret;
	                }
	                
	            }else{
	            	logger.info("http的状态为:"+statusCode);
		            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
		            logger.info("错误信息为:"+str);
		            ret.state = false;
                    ret.msg =  "绑定卡成功失败:"+str;
                    return ret;
	            }
	            
	        } catch (Exception e) {
				  ret.state = false;
                  ret.msg =  "绑定卡成功失败:"+e.getMessage();
                  return ret;
	        }
	}
	
	static String generateTransId() {
		String time = StringUtil.getCurrentDateTime("HHmmssS");
		String orderNo = OrderNoUtil.genOrderNo(time,16);
		return orderNo;
	}
	
	public static void main(String[] args) {
		System.err.println(generateTransId());
	} 
}
