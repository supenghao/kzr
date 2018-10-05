package com.dhk.payment.util;

import java.util.HashMap;
import java.util.Map;

import com.dhk.kernel.util.JsonUtil;

public class ZhongMaoUtil {

	public static Map<String,String> parse(String trans) throws Exception{
		Map<String,String> maps = new HashMap<String,String>();
		
		String data[] = trans.split("&");
		for (int i = 0; i < data.length; i++) {
			String tmp[] = data[i].split("=", 2);
			String key = tmp[0];
			String value = tmp[1];
			maps.put(key, value);
		}
		
		return maps;
	}
	public static String makeSuccJson(String code,String message) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		result.put("message", message);
		
		String json = JsonUtil.toJson(result);
		return json;
	}
	public static String makeJson(String code,String message,ZhongMaoObj zmObj) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		result.put("message", message);
//		if (code.equals("0000")){
//			result.put("data", zmObj);
//		}
		result.put("data", zmObj);
		
		String json = JsonUtil.toJson(result);
		return json;
	}
	public static String makePaymentJson(String code,String message,PaymentResponse paymentResponse) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		result.put("message", message);
		if (code.equals("0000")){
			result.put("data", paymentResponse);
		}
//		result.put("data", paymentResponse);
		
		String json = JsonUtil.toJson(result);
		return json;
	}
	
	public static String makeBalanceJson(String code,String message,ZhongMaoBalance balance) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		result.put("message", message);
		if (code.equals("0000")){
			result.put("data", balance);
		}
		String json = JsonUtil.toJson(result);
		return json;
	}
	
	public static void main(String[] args) throws Exception{
		ZhongMaoObj zmObj = new ZhongMaoObj();
		zmObj.setTransDate("20170106");
		zmObj.setTransNo("1234567");
		zmObj.setTransTime("110300");
		System.out.println(makeJson("0001","交易成功",zmObj));
	}
}
