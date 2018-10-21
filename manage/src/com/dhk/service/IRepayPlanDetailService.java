package com.dhk.service;
import java.util.List;

import com.dhk.entity.RepayPlanDetail;

   /**
    * t_n_repay_plan service 接口<br/>
    * 2017-01-09 09:02:35 Gnaily
    */ 
public interface IRepayPlanDetailService {
	
	
	public int  doUpdateStatus(RepayPlanDetail rpd);
    public int doUpdateStatus(long  id,String status);

	public RepayPlanDetail findRepayPlanById(long id);

	/**
	 * 查询完成还款未消费的还款计划
	 * @param userId
	 * @param cardNo
	 * @param type
	 * @return
	 */

	/**
	 * 查询当天之前未执行的还款计划
	 * @param status
	 * @return
	 */
	public List<RepayPlanDetail> unexecuteRepayBeforeToday();
	
	public List<RepayPlanDetail> loadAllrpdByRecordId(Long recordId);
	public boolean isLastRepay(Long recordId, Long id);
	
	/**
	 * 
	 * @Title upRepayPlanRun 
	 * @Description TODO //根据id修改is_run的执行状态
	 * @param id
	 * @param status
	 * @return  Integer
	 * @author jaysonQiu
	 */

}

