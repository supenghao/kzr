package com.dhk.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.IOrgProfitDao;
import com.dhk.entity.OrgProfit;
import com.dhk.service.IOrgProfitService;
import com.sunnada.kernel.sql.SQLConf;
@Service("OrgProfitService")
public class OrgProfitService implements IOrgProfitService {
	@Resource(name = "OrgProfitDao") 
	private IOrgProfitDao orgProfitDao;


	public void batchInsert(List<OrgProfit> orgProfit){
		String sql= SQLConf.getSql("orgprofit", "insert");
		orgProfitDao.batchExc(sql, orgProfit);
	}

}

