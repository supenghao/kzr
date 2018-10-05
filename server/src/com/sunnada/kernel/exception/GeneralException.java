package com.sunnada.kernel.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GeneralException  implements HandlerExceptionResolver{
	private Log log = LogFactory.getLog(this.getClass());
	
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception){
//		log.error("Catch Exception: ",exception);
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("errMessage", exception.toString());//将错误信息传递给view
//		ModelAndView mv = new ModelAndView("404",map);
//		//ModelAndView mv = new ModelAndView("allException",map);
//	    return mv;  
		
		try{
			//ResponseUtil.sendFailJson(response, "系统错");
		}catch(Exception e){}
		return null;
	}
}
