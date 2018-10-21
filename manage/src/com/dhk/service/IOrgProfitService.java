package com.dhk.service;
import java.util.List;

import com.dhk.entity.OrgProfit;

   /**
    * t_org_profit service 接口<br/>
    * 2017-01-24 17:42:51 zzl
    */ 
public interface IOrgProfitService {
	

	public void batchInsert(List<OrgProfit> orgProfit);

}

