package com.dhk.payment.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.kernel.controller.BaseController;
import com.dhk.payment.entity.result.PayCallbackResult;
import com.dhk.payment.util.RsaDataEncryptUtil;

import redis.clients.jedis.JedisPool;


/**
 * 放融回调
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/hxCallBack")
public class HXCallBackController extends BaseController {
	@Autowired
	JedisPool jedisPool;
	private static Logger logger = Logger.getLogger(HXCallBackController.class);


	@RequestMapping(value="/purchase")
	public void purchase(HttpServletRequest request,HttpServletResponse response,PayCallbackResult payResult) throws Exception{
		System.out.println("回调开始");
		System.out.println("data:"+JSON.toJSONString(payResult));
		StringBuffer sb = new StringBuffer();
		sb.append("client_trans_id=");
		sb.append(payResult.getClient_trans_id());
		sb.append("|resp_code=");
		sb.append(payResult.getResp_code());
		Boolean b = RsaDataEncryptUtil.rsaDataEncryptPub.verify(sb.toString().getBytes("UTF-8"), payResult.getSign());
		if(b){
			logger.info("签名成功");
		}else{
			logger.info("签名失败");
		}

	
	
		return;
	}



}
