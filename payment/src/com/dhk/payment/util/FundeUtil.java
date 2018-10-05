package com.dhk.payment.util;

import java.util.HashMap;
import java.util.Map;

import com.dhk.kernel.util.JsonUtil;

public class FundeUtil {

	public static String makeJson(String code,String message,String applyNo,String policyNo) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		result.put("message", message);
		if ("0000".equals(code)){
			Map<String,String> data = new HashMap<String,String>();
			data.put("applyNo", applyNo);
			data.put("policyNo", policyNo);
			result.put("data", data);
		}
		
		String json = JsonUtil.toJson(result);
		return json;
	}
	
	public static String makeJson(String code,String message,FundeResult data) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		result.put("message", message);
		if ("0000".equals(code)){
			result.put("data", data);
		}
		
		String json = JsonUtil.toJson(result);
		return json;
	}
	
}
