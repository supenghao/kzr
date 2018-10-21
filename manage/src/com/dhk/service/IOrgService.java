package com.dhk.service;
import java.math.BigDecimal;

import com.dhk.entity.Org;

   /**
    * t_org service 接口<br/>
    * 2016-12-21 08:42:51 Gnaily
    */ 
public interface IOrgService {
	

	public Org findById(Long id);

	public int seriUpdateBalance(Long orgId,BigDecimal balance);
	public int seriUpdateRecashFreeze(Long orgId,BigDecimal cash);
	public boolean seriUpdateBalanceAndRecashFreeze(Org org);

}

