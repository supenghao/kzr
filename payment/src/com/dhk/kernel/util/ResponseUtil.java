package com.dhk.kernel.util;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.dhk.payment.yilian.QuickPay;

public class ResponseUtil {

	
	public static void sendFailJson(HttpServletResponse response,String message) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", "fail");
		result.put("message", message);
		String json = JsonUtil.toJson(result);
		responseJson(response, json);
	}
	
	public static Map<String,Object> makeSuccessJson() throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", "success");
		result.put("message", "操作成功");
		return result;
		
	}
	public static String makejson(String code,String message) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		result.put("message", message);
		String json = JsonUtil.toJson(result);
		return json;
	}
	public static void responseJson(HttpServletResponse response,String json) throws Exception{
		PrintWriter out = null;
		try{
			response.setContentType("text/html);charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.print(json);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			if (out!=null){
				out.flush();
				out.close();
			}
				
		}
	}
	
	public static void successJson(HttpServletResponse response,QuickPay quickPay) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", "success");
		result.put("message", "操作成功");
		result.put("data", quickPay);
		result.put("code",quickPay.getRespCode());
		result.put("message",quickPay.getRespDesc());
		result.put("transactionType","");	
		String json = JsonUtil.toJson(result);
		responseJson(response, json);
	}
	
	public static void errorJson(HttpServletResponse response,String errorMsg) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", "9997");
		result.put("message", errorMsg);
		String json = JsonUtil.toJson(result);
		responseJson(response, json);
	}
}
