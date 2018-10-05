package com.dhk.payment.service.impl;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.kernel.sql.SQLConf;
import com.dhk.payment.dao.ITranslsDao;
import com.dhk.payment.entity.Transls;
import com.dhk.payment.service.ITranslsService;
@Service("translsService")
public class TranslsService implements ITranslsService {
	@Resource(name = "translsDao") 
	private ITranslsDao translsDao;
	
	public int insert(Transls transls) throws Exception{
        String sql = SQLConf.getSql("t_transls", "insert");
		
		return translsDao.insert(sql, transls);
	}
	
	public int updateByRequestNo(String respCode,String respDesc,String requestNo) throws Exception{
		String sql = SQLConf.getSql("t_transls", "updateByRequestNo");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("respCode", respCode);
		map.put("respDesc", respDesc);
		map.put("requestNo", requestNo);
		
		return translsDao.update(sql, map);
	}
}

