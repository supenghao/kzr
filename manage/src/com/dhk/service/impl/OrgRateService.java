package com.dhk.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.IOrgRateDao;
import com.dhk.entity.OrgRate;
import com.dhk.service.IOrgRateService;
import com.sunnada.kernel.sql.SQLConf;
@Service("OrgRateService")
public class OrgRateService implements IOrgRateService {
	@Resource(name = "OrgRateDao") 
	private IOrgRateDao orgRateDao;
	
	public List<OrgRate> findByQrcodeId(long id){
		String sql = SQLConf.getSql("orgrate", "findByQrcodeId");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qrcodeId", id);
		return orgRateDao.find(sql, map);
	}

	public int update(OrgRate orgRate) {
		String sql= SQLConf.getSql("orgrate", "update");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qrcodeId", orgRate.getQrcodeId());
		map.put("orgId", orgRate.getOrgId());
		map.put("diffRate", orgRate.getDiffRate());
		return orgRateDao.update(sql, map);
		
	}

	public long insert(OrgRate orgRate) {
		String sql= SQLConf.getSql("orgrate", "insert");
		return orgRateDao.insert(sql, orgRate);
	}

	

	public List<OrgRate> findBy(long qrcodeId, long orgId) {
		String sql = SQLConf.getSql("orgrate", "find");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qrcodeId", qrcodeId);
		map.put("orgId", orgId);
		return orgRateDao.find(sql, map);
	}

	/**
	 * 用二维码ID查询id最小的记录
	 * @param QRCODE_ID
	 * @return
	 * @throws Exception
	 */
	public OrgRate findByQrcodeIdAndMinId(Long qrcodeId) throws Exception {
		// TODO Auto-generated method stub
		String sql = SQLConf.getSql("orgrate", "findByQrcodeIdAndMinId");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qrcodeId", qrcodeId);
		List<OrgRate> orgRates=orgRateDao.find(sql, map);
		if(orgRates!=null && !orgRates.isEmpty() && orgRates.size()>0){
			return orgRates.get(0);
		}
		return null;
	}

	

	
}

