package com.dhk.api.service;
import java.util.List;

import com.dhk.api.dto.DelCreditCarDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.entity.CostPlan;

/**
    * t_s_cost_plan service 接口<br/>
    * 2017-01-14 04:55:47 qch
    */ 
public interface ICostPlanService {

	QResponse insertCostPlan(List<CostPlan> l);

	QResponse getCostPlanList(DelCreditCarDto dto);
	
    int getCostPlanCount(String userId,String cardNo);
	
	void cancelCostPlan(DelCreditCarDto dto);
}

