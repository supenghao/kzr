package com.dhk.api.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.dhk.api.dto.SuperDto;
import com.dhk.api.dao.IHomeAdDao;
import com.dhk.api.entity.HomeAd;
import com.dhk.api.service.IHomeAdService;
import org.springframework.stereotype.Service;

import com.dhk.api.dto.QResponse;
import com.dhk.api.service.ISystemParamService;

@Service("HomeAdService")
public class HomeAdService implements IHomeAdService {

	@Resource(name = "HomeAdDao")
	private IHomeAdDao HomeAdDao;

	@Resource(name = "systemParamService")
	private ISystemParamService systemParamService;

	@Override
	public QResponse getHomeAd(SuperDto dto) {
		String sql = "select * from t_ad_detail where adCode = '100' order by sort limit 6";
		List<HomeAd> l = HomeAdDao.find(sql, null);
		String  adUrl = systemParamService.findParam("manage_url_o")+"/resource/ccm/ad/";
		for (HomeAd homeAd : l) {
			homeAd.setImgurl(adUrl + homeAd.getImgurl());
		}
		QResponse ret = new QResponse(l);
		return ret;
	}
}
