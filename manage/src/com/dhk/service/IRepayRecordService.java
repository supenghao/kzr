package com.dhk.service;
import java.math.BigDecimal;
import java.util.List;

import com.dhk.entity.RepayRecord;

   /**
    * t_n_repay_record service 接口<br/>
    * 2017-02-19 02:13:28 bianzk
    */ 
public interface IRepayRecordService {
	
	
	/**
	 * 修改计划冻结金额 2017年7月14日添加订单号 LZ
	 * @param bd
	 * @param recordId
	 * @return
	 */
	public boolean reCalcAmountRepayRecord(BigDecimal bd,BigDecimal fee,long recordId);
	
	
	public  RepayRecord findByCardNoAndRepayMonth(String cardNo,String repayMonth,Long userId);
	
	public  RepayRecord findByCardNo(Long userId,String cardNo,String repay_month);

	

	/**
	 * 查询未解冻的记录 
	 * @return
	 * @throws Exception
	 * @date 2017年7月14日
	 */
	public List<RepayRecord> findUnFreezeRecord() throws Exception;
	/**
	 * 给计划添加订单号
	 * @param orderNo 订单号
	 * @return
	 * @throws Exception
	 * @date 2017年7月14日
	 */
	public int updateRecordOrderNo(String orderNo, Long id) throws Exception;

    /**
	* 根据订单号查询还款是否全部成功
	* @param orderNo 订单号
	* @return
	* @throws Exception
	* @date 2017年7月14日
	*/
	 public boolean repayRecordIsSuccess(String recordId);

	   /**
		* 解冻余额
		* @param userId
		* @param amount
		* @param recordId
		* @param cardNo
		* @param repayMonth
		* @throws Exception
		*/

	 public void unFreezePlanAmount(Long userId,Long recordId,String cardNo,String repayMonth);

	   /**
		*修改状态
		* @param id
		* @param status   0|处理中或未处理；1|成功；2|失败
		* @return
		*/
	 public int doUpdateStatus(Long id,int status);

	   /**
		* 一次性修改 t_n_repay_cost t_n_repay_plan  t_n_repay_record的狀態
		* @param recordId
		* @param recordStatus
		* @param repayPlanId
		* @param repayPlanStatus
		* @param repayCostId
		* @param repayCostStatus
		*/
	 public void updateAllstatus(Long recordId,String recordStatus,Long repayPlanId,String repayPlanStatus,Long repayCostId,String repayCostStatus);

	   /**
		* 是否全部成功   如果全部成功 手工处理的时候要马上结算
		* @param repayRecordId
		* @return
		*/
	   public boolean isAllSuccess(Long repayRecordId);


}

