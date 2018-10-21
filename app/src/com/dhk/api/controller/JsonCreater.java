package com.dhk.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.tool.M;
import com.dhk.api.entity.ApiResponse;
import com.dhk.api.tool.SimpleJson;

public class JsonCreater {

	/**
	 * 快速返回JSONObject对象
	 * 
	 * @param result
	 * @param userid
	 * @param token
	 * @param errorCode
	 * @param errorMsg
	 * @return
	 */
	public static JSONObject getJson(ApiResponse response) {
		return SimpleJson.parseJsonObject(response);
	}

	/**
	 * 快速返回JSONObject对象
	 * 
	 * @param result
	 * @param msg
	 * @return
	 */
	public static JSONObject getJson(int result, int errorCode,
			String errorMsg, Object beans) {
		JSONObject ret = SimpleJson.parseJsonObject(beans);
		ret.put("result", result + "");
		ret.put("errorCode", errorCode + "");
		ret.put("errorMsg", errorMsg);
		return ret;
	}

	/**
	 * 快速返回JSONObject对象
	 *
	 * @param result
	 * @return
	 */
	public static JSONObject getJson(int result, int errorCode,
									 String errorMsg) {
		JSONObject ret = new JSONObject();
		ret.put("result", result + "");
		ret.put("errorCode", errorCode + "");
		ret.put("errorMsg", errorMsg);
		return ret;
	}

	/**
	 * 快速返回JSONObject对象
	 * @return
	 */
	public static JSONObject getJsonOk() {
		JSONObject ret = new JSONObject(7);
		ret.put("result", "1");
		return ret;
	}

	/**
	 * 序列化bean对象
	 * 
	 * @param beans
	 *            不能是List封装的bean
	 * @return
	 */
	public static JSONObject getJsonOk(Object beans) {
		JSONObject ret = SimpleJson.parseJsonObject(beans);
		ret.put("result", "1");
		return ret;
	}

	public static JSONObject getJsonOkWithArrays(Object beans) {
		JSONObject ob = new JSONObject();
		ob.put("result", "1");
		ob.put("messageList", beans);
		return ob;
	}

	/**
	 * 快速返回JSONObject对象
	 * 
	 * @param result
	 * @param errorCode
	 * @param errorMsg
	 * @return
	 */
	public static JSONObject getJsonError(int errorCode, String errorMsg) {
		JSONObject ret = new JSONObject(7);
		ret.put("result", "0");
		ret.put("errorCode", errorCode + "");
		ret.put("errorMsg", errorMsg);
		return ret;
	}

	public static String resultString(JSON ob) {
		String ret = SimpleJson.parseString("resData", ob);
		if (!M.debug)
			System.out.println("ret-->" + ret);
		return ret;
	}
}
