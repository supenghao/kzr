package com.sunnada.uaas.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunnada.uaas.dao.ISystemParamDao;
import com.sunnada.uaas.service.ISystemParamService;
import org.springframework.stereotype.Service;

import com.sunnada.uaas.entity.SystemParam;

@Service("systemParamService")
public class SystemParamService implements ISystemParamService {

	@Resource(name = "systemParamDao")
	private ISystemParamDao systemParamDao;
	
//	@Resource(name = "systemCache")
//	private MemClient systemCache;
	/**
	 * 根据变量名取参数值
	 * @param paramName
	 * @return
	 */
	public SystemParam findByParamName(String paramName){
		SystemParam systemParam = null;
		
		//Object obj = systemCache.get(CacheKeyDef.SYSTEM_PARAM_KEY+paramName);
		//if (obj==null){
			String sql = com.sunnada.kernel.sql.SQLConf.getSql("system_param", "findByParamName");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("param_name", paramName);			
			try{
				systemParam = systemParamDao.findBy(sql, map);
				//systemCache.set(CacheKeyDef.SYSTEM_PARAM_KEY+paramName, systemParam);
			}catch(Exception e){
				//e.printStackTrace();
			}
//		}else{			
//			systemParam = (SystemParam)obj;
//		}		
		return systemParam;
	}
	
	/**
	 * 取得系统参数表中产品图片路径
	 * @return
	 */
	public String findProductImageUrl(){
		SystemParam systemParam = findByParamName("product_image_url");
		if (systemParam!=null)
			return systemParam.getParam_text();
		return null;
	}
	
	/**
	 * 取得系统参数表中用户图片路径
	 * @return
	 */
	public String findUserImageUrl(){
		SystemParam systemParam = findByParamName("appuser_image_url");
		if (systemParam!=null)
			return systemParam.getParam_text();
		return null;
	}
	/**
	 * 取得系统参数中产品小类图片路径
	 * @return
	 */
	public String findProductCategoryImageUrl(){
		SystemParam systemParam = findByParamName("product_category_image_url");
		if (systemParam!=null)
			return systemParam.getParam_text();
		return null;
	}
	/**
	 * 查找所有
	 * @return
	 */
	public List<SystemParam> findAll(){
		
		String sql = com.sunnada.kernel.sql.SQLConf.getSql("system_param", "findAll");
		
		List<SystemParam> params = systemParamDao.find(sql, null);
		
		return params;
	}
	public int update(SystemParam param){
		String sql = com.sunnada.kernel.sql.SQLConf.getSql("system_param", "update");
		int i= systemParamDao.updateBy(sql, param);
//		Object obj = systemCache.get(CacheKeyDef.SYSTEM_PARAM_KEY+param.getParam_name());
//		if (obj==null){
//			try{
//				systemCache.set(CacheKeyDef.SYSTEM_PARAM_KEY+param.getParam_name(), param);
//			}catch(Exception e){}
//		}else{
//			try{
//				systemCache.delete(CacheKeyDef.SYSTEM_PARAM_KEY+param.getParam_name());
//				systemCache.set(CacheKeyDef.SYSTEM_PARAM_KEY+param.getParam_name(), param);
//			}catch(Exception e){}
//		}
		return i;
	}
}
