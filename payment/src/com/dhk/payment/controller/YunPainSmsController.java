package com.dhk.payment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dhk.kernel.controller.BaseController;
import com.dhk.kernel.util.JsonUtil;
import com.dhk.kernel.util.ResponseUtil;
import com.dhk.payment.util.YunPainSmsObj;
import com.dhk.payment.util.YunPainSmsUtil;

/**
 * YunPain短信
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/yunpain/sms")
public class YunPainSmsController extends BaseController{

    //private final static String APIKEY = "73ed467f0c135289d69a0b2ea4cea9a7";
	//private final static String APIKEY = "54bd1a42b0f1f630ffbd4952f1b86308";//妥妥
	
	private final static String APIKEY = "c831abdf3b5f2bf831005111111112564bf7518d";//睿淘             "8a2da5cba6ffd0a30e58fe5b4d55510b";//稳稳
    /**
	 * 发送验证码 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/sendCheckCode")
	public void sendCheckCode(HttpServletRequest request,HttpServletResponse response) throws Exception{	
		String phone = request.getParameter("phone");
		String checkCode = request.getParameter("checkCode");
		
		if (StringUtils.isBlank(phone)){
        	ResponseUtil.sendFailJson(response, "手机号不能为空");
			return;
        }
        if (StringUtils.isBlank(checkCode)){
        	ResponseUtil.sendFailJson(response, "验证码不能为空");
			return;
        }
        try{
        	String content = YunPainSmsUtil.CHECK_CODE_TEMPLATE.replace("code", checkCode);
        	String backJson = YunPainSmsUtil.sendSms(APIKEY, content, phone);
        	YunPainSmsObj obj = (YunPainSmsObj)JsonUtil.toObj(backJson, YunPainSmsObj.class);
        	String code = "";
        	String message = obj.getMsg();
        	if (obj.getCode().equals("0")){
        		code = "0000";        		
        	}else{
        		code = obj.getCode();
        	}
        	String json = ResponseUtil.makejson(code, message);  
        	ResponseUtil.responseJson(response, json);
        }catch(Exception e){
			e.printStackTrace();
			ResponseUtil.sendFailJson(response, "操作失败");
			return;
		}
	}
}
