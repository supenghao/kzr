package com.dhk.service;
import java.math.BigDecimal;

import com.dhk.FeeInfo;
import com.dhk.entity.FeeParam;

   /**
    * t_param_fee service 接口<br/>
    * 2017-02-10 09:31:05 Gnaily
    */ 
public interface IFeeParamService {
	/**
	 * 根据代码查询费率参数
	 * @param code
	 * @return
	 */
	public FeeParam findBy(String code);
	
//	/**
//	 * 根据交易类型和交易金额计算手续费信息
//	 * @param transAmount
//	 * @param transType
//	 * @return
//	 */
//	public FeeInfo compute(BigDecimal transAmount, TransType transType,String payStyle);
	
	public  FeeInfo computeFeeInfo(BigDecimal amount,String feeCode,String costCode) ;
}

