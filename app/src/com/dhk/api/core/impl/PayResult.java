package com.dhk.api.core.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.xdream.kernel.util.JsonUtil;

public class PayResult {

	private String code = "qfail";
	private String message = "支付 失 败";
	private PayResultData data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PayResultData getData() {
		return data;
	}

	public void setData(PayResultData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PayResult [code=" + code + ", message=" + message + ", data="
				+ data + "]";
	}

	public static void main(String[] args) throws Exception {
		// String
		// json="{'message':'交易成功','data':{'transDate':'20170111','transTime':'134701','transNo':'3199445332428800'},'code':'0000'}";
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("message", "交易成功");
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("transDate", "20170101");
		data.put("transTime", "153500");
		data.put("transNo", "3199445332428800");
		map.put("data", data);
		map.put("code", "0000");
		String json = JsonUtil.toJson(map);
		PayResult result = (PayResult) JsonUtil.toObj(json, PayResult.class);
		System.out.println("aa:" + result.getCode());

		System.out.println("aa:" + result.getData().getTransDate());
	}

}
