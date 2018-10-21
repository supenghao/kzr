package com.dhk.service;
import java.util.List;

import com.dhk.entity.CostPlanDetail;

   /**
    * t_s_cost_plan service 接口<br/>
    * 2017-01-09 08:53:23 Gnaily
    */ 
public interface ICostPlanDetailService {
	
	/**
	 * 获取未执行的消费计划
	 * @return
	 */
	public  List<CostPlanDetail> getUnexecuteCostPlan();
	
	/**
	 * 更新消费计划的执行状态
	 * @param cpd 
	 * @return
	 */
	public int updateStatus(CostPlanDetail cpd);
	
	
}

