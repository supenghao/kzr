package com.dhk.payment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dhk.utils.DateTimeUtil;
import com.sunnada.kernel.util.JsonUtil;

public class PayResult {

	private String code;
	private String message;
	private String transactionType;

	private PayResultData data;
	
	private Long translsId;

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Long getTranslsId() {
		return translsId;
	}

	public void setTranslsId(Long translsId) {
		this.translsId = translsId;
	}

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
	
	
	
	/**
	 * 生成自定义失败交易结果
	 * @param msg 失败原因
	 * @return
	 */
	public static PayResult genCustomFailPayResult(String msg){
		PayResult pr=new PayResult();
		PayResultData data=new PayResultData();
		data.setTransDate(DateTimeUtil.getNowDateTime("yyyyMMdd"));
		data.setTransTime(DateTimeUtil.getNowDateTime("HHmmss"));
		pr.setData(data);
		pr.setCode("fail");
		pr.setMessage(msg);
		return pr;
	}

	/**
	 * 生成自定义失败交易结果
	 * @param msg 失败原因
	 * @return
	 */
	public static PayResult genCustomSuccessPayResult(){
		PayResult pr=new PayResult();
		PayResultData data=new PayResultData();
		data.setTransDate(DateTimeUtil.getNowDateTime("yyyyMMdd"));
		data.setTransTime(DateTimeUtil.getNowDateTime("HHmmss"));
		pr.setData(data);
		pr.setCode("0000");
		pr.setMessage("成功");
		return pr;
	}



	public static void main(String[] args) throws Exception{
		//String json="{'message':'交易成功','data':{'transDate':'20170111','transTime':'134701','transNo':'3199445332428800'},'code':'0000'}";
		Map<String , Object> map=new LinkedHashMap<String, Object>();
		map.put("message", "交易成功");
//		Map<String , Object> data=new LinkedHashMap<String, Object>();
//		data.put("transDate", "20170101");
//		data.put("transTime", "153500");
//		data.put("transNo", "3199445332428800");
//		map.put("data", data);
		map.put("code", "0000");
		String json= JsonUtil.toJson(map);
		PayResult result = (PayResult)JsonUtil.toObj(json, PayResult.class);
		System.out.println("aa:"+result.getCode());
		
		System.out.println("aa:"+result.getMessage());
	}
	
	

}
