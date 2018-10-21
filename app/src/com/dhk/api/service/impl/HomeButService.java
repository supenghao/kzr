package com.dhk.api.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import com.dhk.api.dao.IHomeButDao;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.SuperDto;
import com.dhk.api.entity.HomeBut;
import com.dhk.api.service.IHomeButService;
import com.dhk.api.service.ISystemParamService;
import org.springframework.stereotype.Service;

@Service("HomeButService")
public class HomeButService implements IHomeButService {

	@Resource(name = "HomeButDao")
	private IHomeButDao HomeButDao;

	@Resource(name = "systemParamService")
	private ISystemParamService systemParamService;

	@Override
	public QResponse getFunBtnList(SuperDto dto) {
		String sql = "select * from t_s_headbut where BT_SHOW = '1' order by BT_NO";
		List<HomeBut> l = HomeButDao.find(sql, null);
		List<HomeButBeen> retd = new LinkedList<HomeButBeen>();
		for (HomeBut ad : l) {
			ad.setBt_img_url(systemParamService.findParam("app_url_o")+"/recource/ccm/button/" + ad.getBt_img_url());
			retd.add(new HomeButBeen(ad));
		}
		QResponse ret = new QResponse(retd);
		return ret;
	}

	public class HomeButBeen {
		private String funBtnLink, funBtnName, funBtnUrl;

		public HomeButBeen() {
			
		}

		public HomeButBeen(HomeBut b) {
			this.funBtnLink = b.getBt_link_url();
			this.funBtnName = b.getBt_name();
			this.funBtnUrl = b.getBt_img_url();
		}

		public String getFunBtnLink() {
			return funBtnLink;
		}

		public void setFunBtnLink(String funBtnLink) {
			this.funBtnLink = funBtnLink;
		}

		public String getFunBtnName() {
			return funBtnName;
		}

		public void setFunBtnName(String funBtnName) {
			this.funBtnName = funBtnName;
		}

		public String getFunBtnUrl() {
			return funBtnUrl;
		}

		public void setFunBtnUrl(String funBtnUrl) {
			this.funBtnUrl = funBtnUrl;
		}

	}
}
