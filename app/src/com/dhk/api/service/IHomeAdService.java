package com.dhk.api.service;

import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.SuperDto;

/**
 * t_s_headad service 接口<br/>
 * 2017-01-14 09:42:37 qch
 */
public interface IHomeAdService {

	QResponse getHomeAd(SuperDto dto);
}
