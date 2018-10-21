package com.dhk.api.service;

import com.dhk.api.dto.CostPlanDto;
import com.dhk.api.dto.DelCreditCarDto;
import com.dhk.api.dto.QResponse;

/**
 * t_s_cost_policy service 接口<br/>
 * 2017-01-10 03:49:11 qch
 */
public interface ICostPolicyService {

	public QResponse insertCostPolicy(CostPlanDto dto);

	public QResponse getCostPlanList(DelCreditCarDto dto);
}
