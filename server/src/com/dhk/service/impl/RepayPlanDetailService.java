package com.dhk.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.IRepayPlanDetailDao;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.service.IRepayPlanDetailService;
import com.sunnada.kernel.sql.SQLConf;

@Service("RepayPlanDetailService")
public class RepayPlanDetailService implements IRepayPlanDetailService {
	@Resource(name = "RepayPlanDetailDao")
	private IRepayPlanDetailDao repayPlanDetailDao;
	

	public int doUpdateStatus(RepayPlanDetail repayPlanDetail) {
		String sql=SQLConf.getSql("repayplandetail", "updateStatus");
		repayPlanDetail.setRepaySuccessTime(System.currentTimeMillis());
		return repayPlanDetailDao.updateBy(sql, repayPlanDetail);
	}

	public int doUpdateStatus(long  id,String status) {
		String sql="update t_n_repay_plan set STATUS=:status where ID=:id";
		Map map = new HashMap();
		map.put("id",id);
		map.put("status",status);
		return repayPlanDetailDao.update(sql, map);
	}


	public RepayPlanDetail findRepayPlanById(long id) {
		String sql=SQLConf.getSql("repayplandetail", "findRepayPlanById");
		Map<String, Object> rpMap=new HashMap<String, Object>();
		rpMap.put("id", id);
		try {
			return repayPlanDetailDao.findBy(sql, rpMap);
		} catch (Exception e) {
			
			return null;
		}
	}

	public List<RepayPlanDetail> unexecuteRepayBeforeToday(){
		String sql=SQLConf.getSql("repayplandetail", "unexecuteRepayBeforeToday");
		return repayPlanDetailDao.find(sql, null);
	}

	@Override
	public List<RepayPlanDetail> loadAllrpdByRecordId(Long recordId) {
		String sql=SQLConf.getSql("repayplandetail", "findRepayPlanByRecordId");
		Map<String, Object> rpMap=new HashMap<String, Object>();
		rpMap.put("record_id", recordId);
		return repayPlanDetailDao.find(sql, rpMap);
	}

	@Override
	public boolean isLastRepay(Long recordId, Long id) {
		String sql=SQLConf.getSql("repayplandetail", "findRepayPlanByRecordId");
		Map<String, Object> rpMap=new HashMap<String, Object>();
		rpMap.put("record_id", recordId);
		List<RepayPlanDetail> list =  repayPlanDetailDao.find(sql, rpMap);
		if(list.size()==0)
			return false;
		RepayPlanDetail rpt = list.get(0);
		if(id.equals(rpt.getId()))
			return true;
		return false;
	}
	

}
