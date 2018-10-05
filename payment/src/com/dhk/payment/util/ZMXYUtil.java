package com.dhk.payment.util;

import java.util.HashMap;
import java.util.Map;

import com.dhk.kernel.util.JsonUtil;


public class ZMXYUtil {
	public static String makeJson(String code,String message,ZMXYObj zmxyObj) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		result.put("message", message);
		result.put("data", zmxyObj);		
		String json = JsonUtil.toJson(result);
		return json;
	}
	
}
