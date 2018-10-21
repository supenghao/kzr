package com.dhk.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.core.impl.PlanTool;
import com.dhk.api.dao.ICostPlanDao;
import com.dhk.api.dto.DelCreditCarDto;
import com.dhk.api.tool.M;
import com.dhk.api.dto.QResponse;
import com.dhk.api.entity.CostPlan;
import com.dhk.api.service.ICostPlanService;
import com.dhk.api.service.ICreditCardService;
import com.dhk.api.service.ISystemParamService;
import org.junit.Test;
import org.springframework.stereotype.Service;

@Service("CostPlanService")
public class CostPlanService implements ICostPlanService {

	@Resource(name = "CostPlanDao")
	private ICostPlanDao CostPlanDao;

	@Resource(name = "CreditCardService")
	private ICreditCardService creditCarService;

	@Resource(name = "systemParamService")
	protected ISystemParamService systemParamService;

	@Override
	public QResponse insertCostPlan(List<CostPlan> l) {
		String sql = null;
		Long id = 0l;
		String exetime = "";
		for (CostPlan p : l) {
			String stat = systemParamService.findParam("trans_begin_time");
			String endt = systemParamService.findParam("trasn_end_time");
			try {
				exetime = PlanTool.genRandomTime(stat, endt);
			} catch (Exception e) {
				M.logger.debug(M.getTrace(e));
				throw new RuntimeException("genRandomTime ERROR");
			}
			p.setExec_time(exetime);// 设置执行时间
			if (sql == null)
				sql = M.getInsertSqlWhenFilesIsNotNull(p);
			id = CostPlanDao.insert(sql, p);
			p.setId(Long.parseLong(id + ""));
		}
		return new QResponse();
	}

	@Override
	public QResponse getCostPlanList(DelCreditCarDto dto) {
		// Calendar c = Calendar.getInstance();
		String nowdate = M.dformat.format(new Date());
		String sql = "select * from t_s_cost_plan where user_id =:user_id and card_no=:card_no and cost_datetime >= :nowtime order by cost_datetime desc,id desc";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_no", dto.getCardNo());
		map.put("nowtime", nowdate);
		map.put("user_id", dto.getUserId());
		List<CostPlan> l = CostPlanDao.find(sql, map);
		return new QResponse(l);
	}

	@Override
	public int getCostPlanCount(String userId,String cardNo) {
		String nowdate = M.dformat.format(new Date());
		String sql = "select count(1) from t_s_cost_plan where user_id =:user_id and card_no=:card_no and cost_datetime >= :nowtime order by cost_datetime desc,id desc";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_no", cardNo);
		map.put("nowtime", nowdate);
		map.put("user_id", userId);

		return CostPlanDao.count2Integer(sql, map);
	}


	@Override
	public void cancelCostPlan(DelCreditCarDto dto){
		String sql = "delete from t_s_cost_plan where user_id =:user_id and card_no=:card_no";
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_no", dto.getCardNo());
		map.put("user_id", dto.getUserId());
		
		CostPlanDao.delete(sql, map);
		
	}

	@Test
	public void dateTest() {
		String nowdate = M.dformat.format(new Date());
		System.out.println(nowdate);
	}
}
