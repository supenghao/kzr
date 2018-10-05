package com.dhk.yunpain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sunnada.kernel.DreamConf;
import com.sunnada.kernel.util.JsonUtil;
import com.sunnada.kernel.util.StringUtil;

public class YunPainSmsMsg {
	private final static String APIKEY = DreamConf.getPropertie("SHORT_MESSAGE_KEY");//稳稳
	private static final Logger log= LogManager.getLogger();
	public static YunPainSmsObj sendCode(String checkCode,String phone){
		
		try {
			
			String content = YunPainSmsUtil.CHECK_CODE_TEMPLATE.replace("code", checkCode);
			log.info("发送短信："+phone+"-"+content);
	    	String backJson = YunPainSmsUtil.sendSms(APIKEY, content, phone);
	    	YunPainSmsObj obj = (YunPainSmsObj)JsonUtil.toObj(backJson, YunPainSmsObj.class);
	    	return obj;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	public static YunPainSmsObj sendMsg(String billDate,String cardNo,String phone){
		
		return null;
	}
	
	public static YunPainSmsObj sendNotice(String user,String errorMsg,String phone){
		try {

			String content = YunPainSmsUtil.TRANS_NOTICE_MESSAGE.replace("user", user).replace("errorMsg", errorMsg);
			String backJson = YunPainSmsUtil.sendSms(APIKEY, content, phone);
			YunPainSmsObj obj = (YunPainSmsObj)JsonUtil.toObj(backJson, YunPainSmsObj.class);
			return obj;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static YunPainSmsObj sendSuccess(String cardNo,String repayDate,String phone){
		return null;
//		try {
//			String content = YunPainSmsUtil.TRANS_SUCCESS_MESSAGE.replace("cardNo", cardNo).replace("repayDate", repayDate);
//			String backJson = YunPainSmsUtil.sendSms(APIKEY, content, phone);
//			YunPainSmsObj obj = (YunPainSmsObj)JsonUtil.toObj(backJson, YunPainSmsObj.class);
//			return obj;
//		} catch (Exception e) {
//			return null;
//		}
	}
	
	public static void main(String[] args) {
		sendNotice("***7999","交易失败，请求超时","15659978913");
	}

}
