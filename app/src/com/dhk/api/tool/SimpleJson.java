package com.dhk.api.tool;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SimpleJson {

	/**
	 * 将所有非空字符串序列化成json字符串
	 * 
	 * @param o
	 *            java bean 对象
	 * @return
	 */
	public static String parseString(Object o) {
		return JSON.toJSONString(o);
	}

	/**
	 * 序列化成一个key的json字符串
	 * 
	 * @param key
	 * @param o
	 * @return
	 */
	public static String parseString(String key, Object o) {
		String j = JSON.toJSONString(o);
		StringBuilder b = new StringBuilder(j.length() + key.length() + 7);
		return b.append("{\"").append(key).append("\":").append(j).append("}")
				.toString();
	}

	/**
	 * 将JSONObject对象包装在某个key下面
	 * 
	 * @param key
	 * @param o
	 * @return
	 */
	public static String parseString(String key, JSONObject o) {
		String j = JSON.toJSONString(o);
		StringBuilder b = new StringBuilder(j.length() + key.length() + 7);
		return b.append("{\"").append(key).append("\":").append(j).append("}")
				.toString();
	}

	/**
	 * 将java bean对象序列化成JSONObject
	 * 
	 * @param o
	 * @return
	 */
	public static JSONObject parseJsonObject(Object o) {
		String text = JSON.toJSONString(o);
		return JSON.parseObject(text);
	}

	public static JSONArray parseJsonArray(Object o) {
		String text = JSON.toJSONString(o);
		return JSON.parseArray(text);
	}

	/**
	 * 将json字符创直接序列化成java bean
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	/**
	 * 将某个key下的json字符串序列化成java bean
	 * 
	 * @param key
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String key, String json, Class<T> clazz) {
		JSONObject j = getJsonObject(json);
		return j.getObject(key, clazz);
	}

	/**
	 * 将json字符串转化为JSONObject
	 * 
	 * @param key
	 *            key下面的
	 * @param json
	 * @return
	 */
	public static JSONObject getJsonObject(String key, String json) {
		return JSON.parseObject(json).getJSONObject(key);
	}

	/**
	 * 将json字符串转化为JSONObject
	 * 
	 * @param json
	 * @return
	 */
	public static JSONObject getJsonObject(String json) {
		return JSON.parseObject(json);
	}

	/**
	 * 获取json中摸个key下面的字符串
	 * 
	 * @param key
	 * @param parent
	 * @return
	 */
	public static String getChildString(String key, String parent) {
		JSONObject job = JSON.parseObject(parent);
		return job.getString(key);
	}

	public static <T> List<T> parse2List(String json, Class<T> clazz) {
		return JSON.parseArray(json, clazz);
	}

	public static <T> List<T> parse2List(String key, String json, Class<T> clazz) {
		json = getChildString(key, json);
		return JSON.parseArray(json, clazz);
	}

}
