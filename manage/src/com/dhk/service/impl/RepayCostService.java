package com.dhk.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.IRepayCostDao;
import com.dhk.entity.RepayCost;
import com.dhk.service.IRepayCostService;
import com.dhk.service.IRepayPlanDetailService;
import com.sunnada.kernel.sql.SQLConf;

@Service("repayCostService")
public class RepayCostService implements IRepayCostService {
	@Resource(name = "repayCostDao") 
	private IRepayCostDao repayCostDao;
	
	@Resource(name = "RepayPlanDetailService")
	private IRepayPlanDetailService repayPlanDetailService;



	public int checkRepayPlanStatus(Long repayPlanId) {
		//0|未全部执行完；1|全部成；2|失败(部分失败)

		String sql=SQLConf.getSql("repaycost", "findAllByRepayPlanId");
		Map<String, Object> rpMap=new HashMap<String, Object>();
		rpMap.put("repay_plan_id", repayPlanId);
		List<RepayCost> repayCostList=repayCostDao.find(sql, rpMap);

		boolean hasError = false;
		boolean hasUnExecute = false;
		if(repayCostList==null || repayCostList.isEmpty() || repayCostList.size()<=0){
			return 2;
		}else{
			for (int i=0;i<repayCostList.size();i++){
				String status = repayCostList.get(i).getStatus();
				if("2".equals(status))hasError = true;
				if("0".equals(status)||"u".equals(status))hasUnExecute = true;
			}
		}
		if(hasError){
			 return 2;
		}else if(hasUnExecute){
			return 0;
		}else{
			return 1;
		}
	}


	public boolean doUpdateRepayCostStatus(Long id, String status) {
		String sql=SQLConf.getSql("repaycost", "updateRepayCostStatus");
		Map<String, Object> rpMap=new HashMap<String, Object>();
		rpMap.put("id", id);
		rpMap.put("status", status);
		if(repayCostDao.update(sql, rpMap)>0){
			return true;
		}else{
			return false;
		}
	}


	public RepayCost findRepayCostById(long id) {
		String sql="select*from t_n_repay_cost  where id=:id";
		Map<String, Object> rpMap=new HashMap<String, Object>();
		rpMap.put("id", id);
		try {
			return repayCostDao.findBy(sql, rpMap);
		} catch (Exception e) {
			return null;
		}
	}

}

