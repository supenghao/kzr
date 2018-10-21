package com.dhk.init;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.dhk.api.dao.IParamFeeDao;
import com.dhk.api.dao.IRegionDao;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.third.YunPainSmsUtil;
import com.dhk.api.tool.ParamDef;
/**
 * 
	* @ClassName: LoadInitData 
	* @Description: 数据加载
	* @author ZZL
	* @date 2018年1月29日 下午2:32:55 
	*
 */
@Service  
public class LoadInitData implements InitializingBean, ServletContextAware {  
	@Resource(name = "ParamFeeDao")
	private IParamFeeDao paramFeeDao;
	@Resource(name = "RegionDao")
	private IRegionDao regionDao;
    //注解加载查询sevice层  
	static Logger logger = Logger.getLogger(LoadInitData.class);
    //启动执行的方法，也可以下写到下面的方法中  
    @Override  
    public void setServletContext(ServletContext arg0) {  
    	String findFeeSql = "select * from  t_param_fee";
    	Map<String, Object> findFeeMap = new HashMap<String, Object>();
    	List<ParamFee> paramFees = paramFeeDao.find(findFeeSql, findFeeMap);
    	for(ParamFee paramFee : paramFees){
    		Constant.feeMaps.put(paramFee.getCode(), paramFee);
    	}
        logger.debug("费率数据加载成功！");  
/*        String findsql = "select * from t_region order by id asc";
    	Map<String, Object> areamap = new HashMap<String, Object>();
        List<Region> regions =	regionDao.find(findsql, areamap);
        for(Region region : regions){
        	
        }
        logger.debug("数据加载成功！");  */
        Constant.pmerNo = ParamDef.findConfName("pmerNo");
        Constant.key = ParamDef.findConfName("key");
        Constant.postUrl = ParamDef.findConfName("postUrl");
        Constant.asynNotifyUrl = ParamDef.findConfName("asynNotifyUrl");
        Constant.synNotifyUrl = ParamDef.findConfName("synNotifyUrl");
        YunPainSmsUtil.CHECK_CODE_TEMPLATE = ParamDef.findConfName("CHECK_CODE_TEMPLATE");
        YunPainSmsUtil.CHECK_MESSAGE = ParamDef.findConfName("CHECK_MESSAGE");
        
        Constant.appId = ParamDef.findConfName("appId");
        Constant.transType = ParamDef.findConfName("transType");
        Constant.requestUrl = ParamDef.findConfName("requestUrl");
        Constant.bktdtype = ParamDef.findConfName("bktdtype");
        Constant.kjmerNo = ParamDef.findConfName("kjmerNo");
        Constant.kjkey = ParamDef.findConfName("kjkey");
        Constant.kjpostUrl = ParamDef.findConfName("kjpostUrl");
        
        Constant.USER_ID = ParamDef.findConfName("USER_ID");
        Constant.BG_URL = ParamDef.findConfName("BG_URL");
        Constant.AGENT_NO = ParamDef.findConfName("AGENT_NO");
        Constant.PAGE_URL = ParamDef.findConfName("PAGE_URL");
        Constant.GOODS_DESC = ParamDef.findConfName("GOODS_DESC");
        Constant.GOODS_NAME = ParamDef.findConfName("GOODS_NAME");
        Constant.YT_POSTURL = ParamDef.findConfName("YT_POSTURL");
        Constant.YTJX_POSTURL = ParamDef.findConfName("YTJX_POSTURL");
        Constant.BUS_CODE_YX = ParamDef.findConfName("BUS_CODE_YX");
        Constant.BUS_CODE_JX = ParamDef.findConfName("BUS_CODE_JX");
        
        Constant.YX_USER_ID = ParamDef.findConfName("YX_USER_ID");
        Constant.JX_USER_ID = ParamDef.findConfName("JX_USER_ID");
    }  
  
    @Override  
    public void afterPropertiesSet() throws Exception {  
        // TODO Auto-generated method stub  
  
    }  
  
}  