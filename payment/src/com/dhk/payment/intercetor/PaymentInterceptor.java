package com.dhk.payment.intercetor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PaymentInterceptor extends HandlerInterceptorAdapter{
	private static Map<String,String> ipMap = new HashMap<String,String>();
	static{
		//睿淘
//		ipMap.put("120.27.214.89", "120.27.214.89");
//		ipMap.put("172.16.107.211", "172.16.107.211");
//		
//		ipMap.put("120.27.237.130", "120.27.237.130");
//		ipMap.put("172.16.107.212", "172.16.107.212");
		
		//wenwen2
//		ipMap.put("39.108.178.166", "39.108.178.166");
//		ipMap.put("172.18.35.98", "172.18.35.98");
//		
//		ipMap.put("39.108.178.234", "39.108.178.234");
//		ipMap.put("172.18.35.99", "172.18.35.99");
		
		//妥妥1
//		ipMap.put("121.43.191.92", "121.43.191.92");
//		ipMap.put("172.16.0.7", "172.16.0.7");
//		
//		ipMap.put("121.196.206.17", "121.196.206.17");
//		ipMap.put("172.16.0.6", "172.16.0.6");

		
		ipMap.put("118.31.77.207", "118.31.77.207");
		ipMap.put("172.16.0.29", "172.16.0.29");
		
		ipMap.put("118.31.73.184", "118.31.73.184");
		ipMap.put("172.16.0.28", "172.16.0.28");
		
		//妥妥3
//		ipMap.put("116.62.240.114", "116.62.240.114");
//		ipMap.put("172.16.0.23", "172.16.0.23");
//		
//		ipMap.put("116.62.241.184", "116.62.241.184");
//		ipMap.put("172.16.0.22", "172.16.0.22");
		
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//HttpSession session = request.getSession();
		 String uri = request.getRequestURI();
		 //String contextPath = request.getContextPath();
		 System.out.println("进来了：："+uri);
//		 if (uri.contains("/zm/proxyPay")){
//			 String remoteIp = getIpAddr(request);
//			 if (ipMap.get(remoteIp)==null){
//				 return false;
//			 }
//			 
//		 }
	     return true;
	}
	
	/**
	* 获取访问者IP
	* 
	* 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	* 
	* 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	* 如果还不存在则调用Request .getRemoteAddr()。
	* 
	* @param request
	* @return
	*/
	private static String getIpAddr(HttpServletRequest request) throws Exception{
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
}
