package com.dhk.service;


import com.dhk.entity.TransWater;

public interface ICallBackService {
 /**
  * 充值回调
  * @param respCode
  * @param transWater
  */
 public void czhd(String respCode, String RespDesc, TransWater transWater);

 /**
  * 提现
  * @param respCode
  * @param transWater
  */
 public void txhd(String respCode, String RespDesc, TransWater transWater);

 /**
  * 还款
  * @param respCode
  * @param transWater
  */
 public void  repay(String respCode,String RespDesc,TransWater transWater);
 
 /**
  * 还款 资金不过夜模式
  * @param respCode
  * @param transWater
  */
 public Boolean  repayDate(String respCode,String RespDesc,TransWater transWater);


 /**
  * 还款消费
  * @param respCode
  * @param transWater
  */
 public void  repayCost(String respCode,String RespDesc,TransWater transWater);


 /**
  * 纯消费
  * @param respCode
  * @param transWater
  */
 public void  cost(String respCode,String RespDesc,TransWater transWater);

 public void  costNew(String respCode,String RespDesc,TransWater transWater);
 
 public void backCallProxyPay(String transId,String code,String msg);

/**
 * 还款消息  资金不过夜模式
 * @param code
 * @param message
 * @param transWater
 */
 public Boolean repayCostDate(String code, String message, TransWater transWater);

 public void  jwpay(String respCode,String RespDesc,TransWater transWater);
}

