package com.dhk.payment.service;
import com.dhk.payment.entity.Transls;

   /**
    * t_transls service 接口<br/>
    * 2017-01-06 11:37:45 bianzk
    */ 
public interface ITranslsService {
	
	public int insert(Transls transls) throws Exception;
	
	public int updateByRequestNo(String respCode,String respDesc,String requestNo) throws Exception;
}

