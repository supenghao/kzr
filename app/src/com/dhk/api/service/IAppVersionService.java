package com.dhk.api.service;
import com.dhk.api.dto.AppVersionDto;
import com.dhk.api.dto.QResponse;

/**
    * t_s_appVersion service 接口<br/>
    * 2017-01-13 05:04:36 qch
    */ 
public interface IAppVersionService {

	QResponse update(AppVersionDto dto);
}

