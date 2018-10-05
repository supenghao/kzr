package com.dhk.payment.util;

import com.dhk.kernel.util.JsonUtil;

public class ResponseApiUtil {

	public static String makeFailJson() throws Exception{
		ResponeObj rObj = new ResponeObj();
		rObj.setCode("fail");
		rObj.setMessage("交易失败");
		String json = JsonUtil.toJson(rObj);
		return json;
	}
	public static String makeFailJson(String code,String message) throws Exception{
		ResponeObj rObj = new ResponeObj();
		rObj.setCode(code);
		rObj.setMessage(message);
		String json = JsonUtil.toJson(rObj);
		return json;
	}
	public static String makeFailJson(String message) throws Exception{
		ResponeObj rObj = new ResponeObj();
		rObj.setCode("fail");
		rObj.setMessage(message);
		String json = JsonUtil.toJson(rObj);
		return json;
	}
	//--------------------------------------------------------------------------
	public static String makeFailJson2Juhed(String code,String message,JUHEData data) throws Exception{
		ResponeJUHEObj rObj = new ResponeJUHEObj();
		rObj.setCode(code);
		rObj.setMessage(message);
		rObj.setData(data);
		String json = JsonUtil.toJson(rObj);
		return json;
	}
	public static String makeFailJson2Jd(String code,String message,JdBankCardVerifyData data) throws Exception{
		JdObj rObj = new JdObj();
		rObj.setCode(code);
		rObj.setMessage(message);
		rObj.setData(data);
		String json = JsonUtil.toJson(rObj);
		return json;
	}
	public static String makeFailJson2Juhed(String message,JUHEData data) throws Exception{
		ResponeJUHEObj rObj = new ResponeJUHEObj();
		rObj.setCode("fail");
		rObj.setMessage(message);
		rObj.setData(data);
		String json = JsonUtil.toJson(rObj);
		return json;
	}
	
	public static String makeSuccessJson2Juhed(JUHEData data) throws Exception{
		ResponeJUHEObj rObj = new ResponeJUHEObj();
		rObj.setCode("success");
		rObj.setMessage("交易成功");
		rObj.setData(data);
		String json = JsonUtil.toJson(rObj);
		return json;
	}
	
	public static String makeSuccessJson2Jd(JdBankCardVerifyData data) throws Exception{
		JdObj rObj = new JdObj();
		rObj.setCode("success");
		rObj.setMessage("交易成功");
		rObj.setData(data);
		String json = JsonUtil.toJson(rObj);
		return json;
	}
	
		public static String makeSuccessJson(String code,String message) throws Exception{
			ResponeObj rObj = new ResponeObj();
			rObj.setCode(code);
			rObj.setMessage(message);
			String json = JsonUtil.toJson(rObj);
			return json;
		}
}
