package com.sunnada.kernel;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class StartupServer extends HttpServlet{
    Logger logger = Logger.getLogger(getClass());
    
//    private ServerSupport server = new IdtestService();
    
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    try{
	    	System.setProperty(DreamConstant.DREAM_HOME, config.getServletContext().getRealPath("."));
	    	String isLoadSql = this.getInitParameter("isLoadSql");
            startServer(isLoadSql);
	    }catch(Exception e){
	       e.printStackTrace();
	       logger.debug("启动失败..",e);
	    }
	}
	public void destroy() {
	    super.destroy();
	    com.sunnada.kernel.DreamHelper.stopServer();
	}
	private void startServer(String isLoadSql) throws Exception{
		com.sunnada.kernel.DreamHelper.startServer();
		if (isLoadSql==null || isLoadSql.trim().toLowerCase().equals("true")){
			com.sunnada.web.WebHelper.start();
		}
	}
	private void startServer() throws Exception{
		com.sunnada.kernel.DreamHelper.startServer();
		try{

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
