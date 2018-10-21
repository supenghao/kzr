package com.dhk.api.service;

import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.SuperDto;

/**
 * t_s_headbut service 接口<br/>
 * 2017-01-14 09:42:53 qch
 */
public interface IHomeButService {

	QResponse getFunBtnList(SuperDto dto);
}
