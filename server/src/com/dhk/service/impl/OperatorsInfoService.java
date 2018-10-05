package com.dhk.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.IOperatorsInfoDao;
import com.dhk.entity.OperatorsInfo;
import com.dhk.service.IOperatorsInfoService;
import com.sunnada.kernel.sql.SQLConf;
@Service("operatorsInfoService")
public class OperatorsInfoService implements IOperatorsInfoService {
	@Resource(name = "operatorsInfoDao") 
	private IOperatorsInfoDao operatorsInfoDao;

	public OperatorsInfo findInfo(){
		String sql = SQLConf.getSql("operators_info", "findInfo");
		OperatorsInfo info = null;
		List<OperatorsInfo> infos = operatorsInfoDao.find(sql, null);
		if (infos!=null && !infos.isEmpty() && infos.size()>0){
			info = infos.get(0);
		}
		return info;
	}

}

