package com.dhk.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.ICostPlanDetailDao;
import com.dhk.entity.CostPlanDetail;
import com.dhk.service.ICostPlanDetailService;
import com.sunnada.kernel.sql.SQLConf;
@Service("CostPlanDetailService")
public class CostPlanDetailService implements ICostPlanDetailService {
	@Resource(name = "CostPlanDetailDao") 
	private ICostPlanDetailDao costPlanDetailDao;

	public List<CostPlanDetail> getUnexecuteCostPlan() {
		String sql=SQLConf.getSql("costplandetail", "unexecute");
		synchronized(this){
			return costPlanDetailDao.find(sql, null);
		}
	}
	
	public int updateStatus(CostPlanDetail costPlanDetail) {
		String sql=SQLConf.getSql("costplandetail", "updateStatus");
		return costPlanDetailDao.updateBy(sql, costPlanDetail);
		
	}


}

