package com.dhk.api.controller;


import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dhk.api.tool.StringUtils;
import com.xdream.kernel.controller.BaseController;


/**
 * 快捷H5
 * @author zyx
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/ytcallBack")
public class YtCallBackController extends BaseController {
	@Autowired
	private static final Logger log= Logger.getLogger(YtCallBackController.class);

	@RequestMapping(value="/purchase", produces = "text/html; charset=utf-8")
	public void purchase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("回调开始");
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
		String paraName=(String)enu.nextElement();  
		try {
			
			log.info("参数11:"+paraName+": "+new URLDecoder().decode(request.getParameter(paraName)+"","utf-8"));  
		} catch (Exception e) {
			log.info("参数11:"+paraName+": "+request.getParameter(paraName));  
		}
		  
		}
		try {
 
			String userid = request.getParameter("userid");
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
								   	"user.userid='"+userid+"';"+
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
