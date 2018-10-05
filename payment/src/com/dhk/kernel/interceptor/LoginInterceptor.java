package com.dhk.kernel.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		 String uri = request.getRequestURI();
		 String contextPath = request.getContextPath();
		 
		 if (!uri.contains("/app/") && !uri.contains("/system/login/login") 
				 && !uri.contains("/system/login/loginInit") && !uri.contains("/system/login/logout")){
			 if (session.getAttribute("xdream_Oper")==null){
				 //重定向到登录页面  
				 response.sendRedirect(request.getContextPath() + "/system/login/loginInit");  
				 return false;
			 }
		 }
	     return true;
	}
}
