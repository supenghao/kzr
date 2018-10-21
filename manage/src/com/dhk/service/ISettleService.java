package com.dhk.service;
import java.math.BigDecimal;
import java.util.List;

import com.dhk.entity.Settle;
import com.dhk.entity.TransWater;

   /**
    * t_s_settle service 接口<br/>
    * 2017-02-11 10:44:04 Gnaily
    */ 
public interface ISettleService {


	   /**
		* 插入结算信息
		* @param settle 结算信息
		* @return
		*/
	   public long doInsertSettle(Settle settle);

	   /**
		* 根据日期和代理商ID查找
		* @param date
		* @return
		*/
	   public Settle findByOrgIdAndCurDate(long orgId);
	   /**
		* 更新结算信息
		* @param settle
		* @return
		*/
	   public boolean doUpdateSettle(Settle settle);

	   public boolean doUpdateSettle(BigDecimal balance ,int count,long id);
	   /**
		* 按交易流水对代理商进行分润结算
		* @param transWaters
		*/
	   public void doSettle(List<TransWater> transWaters) throws Exception;

	   public void txOperatoerSett(BigDecimal amount, Long transId) throws Exception;


	   public void doSettleTest(List<TransWater> transWaters) throws Exception;
	
	
}

