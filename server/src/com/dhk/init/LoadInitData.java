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

import com.dhk.dao.IFeeParamDao;
import com.dhk.entity.FeeParam;
import com.dhk.utils.ParamDef;  
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
	@Resource(name = "FeeParamDao") 
	private IFeeParamDao feeParamDao;
    //注解加载查询sevice层  
	static Logger logger = Logger.getLogger(LoadInitData.class);
    //启动执行的方法，也可以下写到下面的方法中  
    @Override  
    public void setServletContext(ServletContext arg0) {  
    	String findFeeSql = "select * from  t_param_fee";
    	Map<String, Object> findFeeMap = new HashMap<String, Object>();
    	List<FeeParam> paramFees = feeParamDao.find(findFeeSql, findFeeMap);
    	for(FeeParam paramFee : paramFees){
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
        
        Constant.appIddh = ParamDef.findConfName("appIddh");
         Constant.appId = ParamDef.findConfName("appId");
        Constant.transType = ParamDef.findConfName("transType");
        Constant.requestUrl = ParamDef.findConfName("requestUrl");
        
        Constant.yblmerNo= ParamDef.findConfName("yblmerNo");
        Constant.yblkey = ParamDef.findConfName("yblkey");
        Constant.yblpostUrl = ParamDef.findConfName("yblpostUrl");
        Constant.tdtype=ParamDef.findConfName("tdtype");
        Constant.costtype=ParamDef.findConfName("costtype");
    }  
  
    @Override  
    public void afterPropertiesSet() throws Exception {  
        // TODO Auto-generated method stub  
  
    }  
  
}  