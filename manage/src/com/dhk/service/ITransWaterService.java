package com.dhk.service;
import java.math.BigDecimal;
import java.util.List;

import com.dhk.entity.APPUser;
import com.dhk.entity.Org;
import com.dhk.entity.TransWater;
import com.dhk.payment.PayResult;

/**
    * t_s_trans_water service 接口<br/>
    * 2017-01-19 01:21:54 Gnaily
    */ 
public interface ITransWaterService {
	/**
	 * 插入流水
	 * @param tw 交易流水
	 * @return
	 */
	public long doInsert(TransWater tw);

	
	public TransWater findById(long id);
	
	public TransWater findByTransNo(String transNo);

	
	
	public int modifyTransls(TransWater tw);
	/***----------------------------20170305新增以下接口-------**/
	
	
	
	
	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
			BigDecimal fee,BigDecimal external,Long planId,Long costId,String transType);

	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
						   BigDecimal fee,BigDecimal external,Long planId,Long costId,String transType,String respCode,String respRes);

	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
						   BigDecimal fee,BigDecimal external,Long planId,String transType,String cardType,String isOrgRecash);

	

	public void  modifyTransls(Long translsId,String transNo,String proxyPayType,String respCode,String respMsg,String transDate,String transTime,String transactionType);



   /**
	* 查询未结算且时间在当前时间之前的
	* @param isBusiness
	* @param transDate
	* @return
	* @throws Exception
	*/
   public List<TransWater> findByIsBusAndDate(String isBusiness, String transDate) throws Exception;
   /**
	* 更新流水状态为运营商已结算
	* @param id
	* @param isBusiness
	* @throws Exception
	*/
   public void updateIsBus(Long id,String isBusiness) throws Exception;

   /**
	* 查询未结算的流水
	* @return
	*/
   public List<TransWater> findUnSettedTransWaters();

   public boolean updateSettle(Long id);

	public void  writeFastRepayTransWater(String transNo,APPUser user,String cardNo,BigDecimal amount,
										  BigDecimal fee,BigDecimal external,Long planId,String proxyPayType,
										  String proxyPayChannel,PayResult pr);


	public long writeRechageTransWater(String transNo,APPUser user, String debiteCardNo, BigDecimal amount, BigDecimal fee,
									   BigDecimal external, PayResult pr);


	public void  writeRecashTransWater(String transNo,APPUser user,String cardNo,BigDecimal amount,
									   BigDecimal fee,BigDecimal external,
									   String proxyPayChannel,String proxyPayType,PayResult pr);

	public void  writeRecashTransWater(String transNo,Org org,String cardNo,BigDecimal amount,
									   BigDecimal fee,BigDecimal external,
									   String proxyPayChannel,String proxyPayType,PayResult pr);

	public long addTransls(String transNo, Org org, String cardNo, BigDecimal amount,
						   BigDecimal fee, BigDecimal external, Long planId, String transType, String cardType);
	
}

