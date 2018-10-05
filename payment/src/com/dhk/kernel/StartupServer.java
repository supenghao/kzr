package com.dhk.kernel;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.dhk.web.WebHelper;

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
	    DreamHelper.stopServer();
	    //server.stop();
	    
//	    List<ServerSupport> servers = DreamConf.serverList;
//		Iterator<ServerSupport> it = servers.iterator();
//		while(it.hasNext()){
//			ServerSupport server = it.next();
//			server.stop();
//		}
	}
	private void startServer(String isLoadSql) throws Exception{
		DreamHelper.startServer();
		if (isLoadSql==null || isLoadSql.trim().toLowerCase().equals("true")){
			WebHelper.start();
		}
	}
	private void startServer() throws Exception{
		DreamHelper.startServer();
		//WebHelper.start();
		try{
//			FieldEntity entity = FieldMapConf.findMapFieldEntity("dept");
//			System.out.println("test:"+entity.getColumns());
//			System.out.println("html:"+entity.getQueryEntitys());
			
			//server.start();
			
//			SpringUtil springUtil = new SpringUtil();
//	    	ITestDao testDao = (ITestDao)springUtil.getBean("testDao");
//			ITestService testService = (ITestService)springUtil.getBean("testService");
	    	
//	    	TestVo voq = new TestVo();
//	    	voq.setName("测试");
//	    	voq.setAdd_text("测试文本");
//	    	System.out.println("insert id:"+testDao.insert(voq));
	    	
//	    	TestVo update = new TestVo();
//	    	update.setId(3l);
//	    	update.setName("测试6");
//	    	update.setText("6666666666");
//	    	System.out.println("update id:"+testDao.update(update));
//			TestVo vo = testDao.load(5L);
//	    	System.out.println("update id:"+vo.getName());
//	    	Map<String,Object> map = new HashMap<String,Object>();
//	    	map.put("name", "测试");
	    	//map.put("text", "测试文本");
	    	//List<TestVo> vos = testDao.find(map,"name","desc");
//	    	List<TestVo> vos = testService.find(map,"name","desc");
	    	//List<TestVo> vos =testDao.find("select * from t_test where name=:name and text=:text", map);
//	    	for(TestVo vo : vos){
//	    		StringUtil.debug(vo.getText()+"=="+vo.getName());
//	    	}
//	    	
//	    	StringUtil.debug("------------------------------------");
//	    	Pager<TestVo> pager = new Pager<TestVo>();
//	    	pager.setCurPageNum(1);
//	    	pager.setPageSize(2);
//	    	Pager pager1 = testService.findForPage("select * from t_test  order by id", null, pager);
//	    	List<TestVo> ps = pager1.getLists();
//	    	for(TestVo vo : ps){
//	    		StringUtil.debug(vo.getText()+"=="+vo.getName());
//	    	}
	    	
//			List<ServerSupport> servers = DreamConf.serverList;
//			Iterator<ServerSupport> it = servers.iterator();
//			while(it.hasNext()){
//				ServerSupport server = it.next();
//				server.start();
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
