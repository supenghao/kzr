package com.dhk.api.controller;


import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.NetworkInterface.NetworkParams;
import com.dhk.api.core.impl.BaseNetwork;
import com.dhk.api.dao.IAPPUserDao;
import com.dhk.api.entity.APPUser;
import com.dhk.api.service.ISystemParamService;
import com.dhk.init.Constant;
import com.fast.pay.utils.EncryptUtils;
import com.xdream.kernel.controller.BaseController;


/**
 * 快捷支付回调
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/kjcallBack")
public class KjCallBackController extends BaseController {
	@Autowired
	private static final Logger log= Logger.getLogger(KjCallBackController.class);
	@Resource(name="systemParamService")
	ISystemParamService systemParamService;
	@Resource(name = "APPUserDao")
	private IAPPUserDao appUserDao;
	@RequestMapping(value="/purchase", produces = "text/html; charset=utf-8")
	public void purchase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("回调开始");
        Map map=new HashMap();
		FileOutputStream fos=null; 
		 
		Enumeration enu=request.getParameterNames(); 
		NetworkParams pp = new NetworkParams();
		while(enu.hasMoreElements()){  
		String paraName=(String)enu.nextElement();  
		log.info("参数11:"+paraName+": "+request.getParameter(paraName));
		 
		 map.put(paraName, request.getParameter(paraName));
		 pp.addParam(paraName, request.getParameter(paraName));
		}
		if(Constant.kjmerNo.equals(map.get("app_id")+"")){
			try {
			String signStr=map.get("data")+"";
			log.info("获取的签名数据为："+signStr);
			log.info("解密签名wei："+EncryptUtils.decrypt(signStr, Constant.kjkey));
			JSONObject josnZc=JSONObject.parseObject(EncryptUtils.decrypt(signStr, Constant.kjkey));
			//注册失败时候清除报户信息
			if(!"true".equals(josnZc.getString("success"))){
				String merchantid = josnZc.getString("mct_number");
				String sql = "update t_s_user set kj_merno =:kj_merno,kj_key=:kj_key  where kj_merno=:id";
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("kj_merno", "");
				paramap.put("kj_key", "");
				paramap.put("id", merchantid);
				int change = appUserDao.update(sql, paramap);
			}else{
				String merchantid = josnZc.getString("mct_number");
				String key = josnZc.getString("secret_key");
				String sql = "update t_s_user set kj_merno =:kj_merno,kj_key=:kj_key  where kj_merno=:id";
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("kj_merno",merchantid);
				paramap.put("kj_key", key);
				paramap.put("id", merchantid);
				int change = appUserDao.update(sql, paramap);
			} 
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			responseJson(response, "success");
			return ;
		}
		 
		if(!StringUtils.isEmpty(map.get("app_id")+"")){
			String signStr=map.get("data")+"";
			log.info("获取的签名数据为："+signStr);
			APPUser user = null;
			String sql ="select * from  t_s_user where kj_merno=:kj_merno";
			Map<String, Object> mapp = new HashMap<String, Object>();
			mapp.put("kj_merno", map.get("app_id")+"");
			try {
				user = appUserDao.findBy(sql, mapp);
				log.info("解密签名："+EncryptUtils.decrypt(signStr, user.getKjKey()));
				JSONObject josn=JSONObject.parseObject(EncryptUtils.decrypt(signStr, user.getKjKey()));
				String action=map.get("action")+"";
				 
					if("RCM_RH5".equals(action) || "CCT_PYI".equals(action) || "CCT_PYO".equals(action)){
						 
						String url = systemParamService.findParam("server_url_i")+"/kjCallBack/purchase";
			    	    BaseNetwork net = new BaseNetwork(url);
						String retstr;
						 
						try {
							retstr = net.getResultStr(pp);
							log.info("trans返回参数:"+retstr);
							 
						}catch (Exception e) {
							log.info(e.getMessage());
						}
						log.info("快捷参数:"+ JSON.toJSONString(pp)+" 访问地址："+url);
					}
				 
				
				log.info("快捷回调返回参数："+josn.toString());
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
		}
		responseJson(response, "success");
		return ;
	}
	public static void responseJson(HttpServletResponse response,String json) throws Exception{
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
	public static void main(String[] args) {
//		String a="ApcEibzQTfh9bP73P7Zc6wDIQh7h+3ehvH7xKC9Kcr+juNfJYjLj7cONoFZ6WO6Z8rVcXex43WevG8865hiyMH9S2kG8d4tDVPjVm7V0NUVwpJCuX32ZcEN4q4t0zVrzngZA50DiplOvIK3mTaDvvrAu5IeuIzK2UrSI5irxvw/r13k/FzA/M6zpJ42hV99BRti6YcfWSRwfyIvHZLKizqXKZbSe1nNSnn5Ra5B8F0RgbM8EJXMBoqnEU5PpNH+DbtiSRo42ptfFz5pSyWcfnVM7bKQFqGMVyCLDf2AvEL0N3pQa+A5uPBtsrrEiWKD8HgIPE/iFA26jf9CAM8DZyQDOStpfPVnq4Vxd54WI3G8Dfjp7q/KZLFjZJDTewKh/";
//        a="5VVXiBJ78zkHU3Y9XXKwQuPJ+h1XtTBQWX6+NoMMAtvrpIfEtB7NDTqfT3UKTwRerjFgGq+U7O7YnjpeBk0oztIZMXFtiVLt8LE+w4D1G2AvDLH4gb1RzYnvT9DEyZlT0rsr9Jo5aN1JlGfvGYke9z2H4N7ZxAn0qrBG6DcPlzlrqUQNkNEI7+Hmam+SkJ5UntEFz3C8iAFe7gNGkGsbgp+xbY6szz2DFrTmV/09SnYEe9Z3WdTCL4NKrf51nt3Fkg1Eiqp3NeGX7NC9hEGr/0NzeNkWR3QRXKRvfsxvbyRW3O918TZIOo5UD9JHCz7OzeD7+mNeUikV9Hz+Gn/9lQ==";
		   String a="88UP6oFt++CQ/D7cM+7zIItfjbIahcEi8Yw6RhNfPWGQgxvIe57NZCGkAFuuffD8wN097iwiEfiLEZhQP3tdkxzvv/Y6YYKo8kFSjDxWWbdfNDmGRodj6Ys9cBRDIYGq51RLt0uxBWGZha8wg7GJpBQThVEopIi0CVS0y0A0pKMl65g18RwwmxTkValJUCRKDEXM8CRPyagBGX3GSgDLq2z5i48eWam+FYvymAl9QkA=";
		System.out.println(EncryptUtils.decrypt(a, "7ba93cce852afdaffca595d842a9ea85"));
//	   System.out.println(new BigDecimal(400).multiply(new BigDecimal(0.79)).divide(new BigDecimal(100)));
//		FastPayResponse fastPayResponse1=new FastPayResponse();
//		fastPayResponse1.setMessage("成功");
//		System.out.println(fastPayResponse1.getMessage().indexOf("成功"));
	}
}
