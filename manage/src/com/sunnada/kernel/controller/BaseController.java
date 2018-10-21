package com.sunnada.kernel.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("serial")
public abstract class BaseController  implements java.io.Serializable{
	

	@ExceptionHandler
    public String exception(HttpServletRequest request, Exception e) {  
          
        //添加自己的异常处理逻辑，如日志记录　　　
        request.setAttribute("exceptionMessage", e.getMessage());  
          
        // 根据不同的异常类型进行不同处理
        if(e instanceof SQLException) 
            return "testerror";   
        else
            return "error";  
    }  
	
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	 
	
	/*public void responseJson(HttpServletResponse response,String json) throws Exception{
		PrintWriter out = null;
		try{
			response.setContentType("text/html);charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.print(json);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			if (out!=null){
				out.flush();
				out.close();
			}
				
		}
	}
	public void sendFailJson(HttpServletResponse response,String message) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", "fail");
		result.put("message", message);
		String json = JsonUtil.toJson(result);
		this.responseJson(response, json);
	}
	
	public Map<String,Object> makeSuccessJson() throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", "success");
		result.put("message", "操作成功");
		return result;
		
	}*/
}
