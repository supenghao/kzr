package com.dhk.payment.service;
import com.dhk.payment.entity.Gateway;

   /**
    * t_gateway service 接口<br/>
    * 2017-01-05 10:36:23 bianzk
    */ 
public interface IGatewayService {
	public Gateway findByCode(String gatewayCode) throws Exception;
}

