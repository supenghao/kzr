package com.dhk.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
* @ClassName: ResponseUtils 
* @Description: 返回工具类
* @author ZZL
* @date 2017年10月9日 上午10:51:50 
*
 */
public class ResponseUtils {
	
	private static final Logger log= LogManager.getLogger();
	
	public static void render(HttpServletResponse response,String text) {  
        response.setContentType("application/json;charset=UTF-8");  
        response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        try {  
            if(StringUtils.isBlank(text)){  
                text="";  
            }  
            response.getWriter().write(text);  
            response.getWriter().flush();  
            response.getWriter().close();  
        } catch (IOException e) {  
        	log.error(e.getMessage());  
            if(!"class org.apache.catalina.connector.ClientAbortException".equals(e.getClass().toString()))  
                e.printStackTrace();  
        }  
    }  
}
