package com.dhk.api.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.dhk.api.entity.RepayPlan;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RepayPlanDto;
import com.dhk.api.entity.RepayPlanTem;

/**
 * t_s_repayplan_tem service 接口<br/>
 * 2017-02-18 08:59:20 qch
 */
public interface IRepayPlanTemService {

	/**
	 * 获取有效的临时表数据
	 * 
	 * @param dto
	 * @return
	 */
	JSONArray txgetValueableTemList(RepayPlanDto dto);

	/**
	 * 批量插入
	 * 
	 * @param ps
	 */
	void insertRepayPlanList(List<RepayPlan> ps);

	/**
	 * 删除id的字段
	 * 
	 * @param dto
	 * @return
	 */
	QResponse txRemoveRepayTemByid(RepayPlanDto dto);

	/**
	 * 计算总费用
	 * 
	 * @param dto
	 * @return
	 */
	QResponse getRepayPlanTemfee(RepayPlanDto dto);

	void clear(String userId,String creditCarNo);
}
