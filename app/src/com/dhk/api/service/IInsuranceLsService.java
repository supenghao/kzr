package com.dhk.api.service;

import com.dhk.api.dto.IdentityDto;
import com.dhk.api.entity.InsuranceLs;
import com.dhk.api.dto.QResponse;

/**
 * t_s_insurance_ls service 接口<br/>
 * 2017-02-20 12:04:33 qch
 */
public interface IInsuranceLsService {

	InsuranceLs insertInsuranceLs(InsuranceLs ls);

	QResponse getUserinsurances(IdentityDto dto);
}
