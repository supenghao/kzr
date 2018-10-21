package com.dhk.api.third;

import com.xdream.kernel.DreamConf;
import com.xdream.kernel.util.JsonUtil;

public class YunPainSmsMsg {
	private final static String APIKEY = DreamConf.getPropertie("SHORT_MESSAGE_KEY");
	
	public static YunPainSmsObj sendCode(String checkCode,String phone){

		try {

			String content = YunPainSmsUtil.CHECK_CODE_TEMPLATE.replace("code", checkCode);
	    	String backJson = YunPainSmsUtil.sendSms(APIKEY, content, phone);
	    	YunPainSmsObj obj = (YunPainSmsObj)JsonUtil.toObj(backJson, YunPainSmsObj.class);
	    	if(!"0".equals(obj.getCode())){
				System.out.println(obj.getMsg());
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public static YunPainSmsObj sendCheck(String result,String phone){
		try {
			String content = YunPainSmsUtil.CHECK_MESSAGE.replace("result", result);
			String backJson = YunPainSmsUtil.sendSms(APIKEY, content, phone);
			YunPainSmsObj obj = (YunPainSmsObj)JsonUtil.toObj(backJson, YunPainSmsObj.class);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	
	public static YunPainSmsObj sendNotice(String cardNo,String repayDate,String phone){
		
		try {
			
			String content = YunPainSmsUtil.TRANS_NOTICE_MESSAGE.replace("cardNo", cardNo).replace("repayDate", repayDate);
			String backJson = YunPainSmsUtil.sendSms(APIKEY, content, phone);
			YunPainSmsObj obj = (YunPainSmsObj)JsonUtil.toObj(backJson, YunPainSmsObj.class);
			return obj;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public static void main(String[] args) {
		sendCode("321456", "15659978913");
//		sendMsg("15号","123456789","18005005865");
	//	sendNotice("123456789","2017-06-01","1565978913");
	}

}
