package com.dhk.service;

import com.dhk.entity.CreditCard;

   /**
    * t_s_user_creditcard service 接口<br/>
    * 2017-02-10 11:29:19 Gnaily
    */ 
public interface ICreditCardService {
	/**
	 * 根据信用卡卡号查询信用卡信息 
	 * @param cardNo 信用卡卡号
	 * @return 信用卡
	 */
	public CreditCard findByCardNo(String cardNo);

}

