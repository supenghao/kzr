package com.dhk.api.controller;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.entity.result.PayCallbackResult;
import com.dhk.api.tool.StringUtils;
import com.xdream.kernel.controller.BaseController;


/**
 * 汇享天成回调
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/callBack")
public class CallBackController extends BaseController {
	@Autowired
	private static final Logger log= Logger.getLogger(CallBackController.class);

	@RequestMapping(value="/purchase", produces = "text/html; charset=utf-8")
	public void purchase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("回调开始");
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
		String paraName=(String)enu.nextElement();  
		log.info("参数11:"+paraName+": "+request.getParameter(paraName));  
		}
		try {
//			String responseResult = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">"+
//					"<html>"+ 
//					"<head>"+ 
//				    "<meta charset=\"utf-8\">"+ 
//				    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>"+ 
//				    "<meta http-equiv=\"Cache-Control\" content=\"no-cache\"/>"+ 
//				    "<title>消费</title>"+ 
//				    "<link href=\"/app/resource/scss/main.css\" rel=\"stylesheet\" type=\"text/css\">"+ 
//				    "<link href=\"/app/resource/css/swiper-3.4.2.min.css\" rel=\"stylesheet\" type=\"text/css\">"+ 
//				"</head>"+ 
//					"<body>"+
//					"<div class=\"pop-tips\">计划生成成功</div>"+
//					"<div class=\"pop-list-wrap\">"+
//					    "<div class=\"pop-main\">"+
//					        "<div class=\"pop-list\">"+
//					            "<ul class=\"list lr-list info-list\">"+
//					            "</ul>"+
//					            "<div class=\"btn-box\">"+
//					                "<button class=\"btn\" id=\"pop_back\">返回</button>"+
//					            "</div>"+
//					        "</div>"+
//					    "</div>"+
//					"</div>"+
//					    "</body>"+
//                  "</html>";
			String user_id = request.getParameter("user_id");
			String token = request.getParameter("token");
			String responseResult = "<!DOCTYPE html>"+
							"<html lang=\"en\">"+
							"<body needLogin=\"1\">"+
							"<script src=\"/resource/js/jquery.min.js\"></script>"+
							"<script src=\"/resource/js/common.js\"></script>"+
							"<script src=\"/resource/js/swiper-3.4.2.jquery.min.js\"></script>"+
							"<script src=\"/resource/js/layer_mobile/layer.js\"></script>"+
							"<script src=\"/resource/js/rem.js\"></script>"+
							"<script src=\"/resource/js/mui.min.js\"></script>"+
							"<script src=\"/resource/js/md5.js\" type=\"text/javascript\"></script>"+
							"<script>"+
								   "mui.plusReady(function() {"+
								   	"var data = plus.webview.currentWebview();"+
								   "var user ={};"+
								   	"user.userid='"+user_id+"';"+
									"user.token='"+token+"';"+
									"plus.nativeUI.toast('提交成功',{verticalAlign:'center'});"+
								   	" openWindow(\"/callback.html\",{user:user});"+
								"});"+
							"</script>"+
							"</body>"+
							"</html>";
			
			StringUtils.responseStr(response,responseResult );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
}
