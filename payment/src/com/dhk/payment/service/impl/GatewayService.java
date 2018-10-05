package com.dhk.payment.service.impl;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.kernel.sql.SQLConf;
import com.dhk.payment.dao.IGatewayDao;
import com.dhk.payment.entity.Gateway;
import com.dhk.payment.service.IGatewayService;
@Service("gatewayService")
public class GatewayService implements IGatewayService {
	@Resource(name = "gatewayDao") 
	private IGatewayDao gatewayDao;
	
	public Gateway findByCode(String gatewayCode) throws Exception{
		Gateway gateway = null;
		
		String sql = SQLConf.getSql("t_gateway", "findByCode");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("gatewayCode", gatewayCode);
		try{
			gateway = gatewayDao.findBy(sql, map);
		}catch(Exception e){
			//e.printStackTrace();
			return null;
		}
		return gateway;
	}
}

