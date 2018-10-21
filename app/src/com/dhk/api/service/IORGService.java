package com.dhk.api.service;

import com.dhk.api.dto.LoginDto;
import com.dhk.api.dto.AgentIdentifyDto;
import com.dhk.api.dto.QResponse;

/**
 * t_org service 接口<br/>
 * 2017-01-13 04:03:14 qch
 */
public interface IORGService {

	QResponse login(LoginDto dto);

	QResponse agentTotal(AgentIdentifyDto dto);
}
