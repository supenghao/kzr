package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.IAppVersionDao;
import com.dhk.api.dto.AppVersionDto;
import com.dhk.api.entity.AppVersion;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.dhk.api.dto.QResponse;
import com.dhk.api.service.IAppVersionService;

@Service("AppVersionService")
@Scope("singleton")
public class AppVersionService implements IAppVersionService {

	@Resource(name = "AppVersionDao")
	private IAppVersionDao AppVersionDao;

	@Override
	public QResponse update(AppVersionDto dto) {
		String sql = "select * from t_s_appVersion where VERSION_TYPE = :VERSION_TYPE";
		String versionType = dto.getVersionType();
		if (versionType == null || versionType.isEmpty()) {
			return new QResponse(false, "未知平台信息");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("VERSION_TYPE", versionType);
		List<AppVersion> li = AppVersionDao.find(sql, map);
		if (li.isEmpty()) {
			return new QResponse(false, "未找到该平台软件");
		}
		AppVersion r = li.get(0);
		if (r.getCur_version().compareToIgnoreCase(dto.getVersion()) > 0) {
		//if (r.getVersion_num().intValue()>Integer.parseInt(dto.getVersion())){
			QResponse ret = new QResponse(true, "");
			ret.data = r;
			return ret;
		} else {
			QResponse ret = new QResponse(false, "已经是最新版本");
			return ret;
		}

	}

}
