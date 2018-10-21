package com.dhk.api.service;

import java.util.List;

import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.RepayPlanDto;
import com.dhk.api.entity.RepayPlan;
import com.dhk.api.dto.QResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * t_n_repay_plan service 接口<br/>
 * 2017-01-15 01:32:07 qch
 */
public interface IRepayPlanService {

	/**
	 * 写入还款计划和手续费
	 * 
	 * @param dto
	 * @return
	 */
	public QResponse insertRepayPlans(RepayPlanDto dto)  throws Exception;
	
	/**
	 * 写入还款计划和手续费 资金不过夜模式
	 * 
	 * @param dto
	 * @return
	 */
	public QResponse insertRepayDatePlans(RepayPlanDto dto)  throws Exception;



	/**
	 * 生成还款计划和手续费
	 *
	 * @param dto
	 * @return
	 */
	public QResponse genRepayPlansNew(RepayPlanDto dto, HttpServletRequest request) throws Exception;


	/**
	 * 获取大于当前时间的还款计划列表
	 * @return
	 */
	public QResponse getRepayPlanList(IdentityDto dto, String repayRecordId) ;
	
	public List<RepayPlan> findByCardNo4User(String cardNo, String userId);
	
	/**
	 * 
	 * @Title findUserRepayPlan 
	 * @Description TODO // 根据用户id、卡号、执行状态 查询
	 * @param cardNo
	 * @param isRun
	 * @param userId
	 * @return  List<RepayPlan>
	 * @author jaysonQiu
	 */
	public List<RepayPlan> findUserRepayPlan(String cardNo,String isRun,Long userId);
	
	
	/**
	 * 生成还款计划和手续费
	 *
	 * @param dto
	 * @return
	 */
	public QResponse genRepayPlansOneDate(RepayPlanDto dto, HttpServletRequest request) throws Exception;



}
