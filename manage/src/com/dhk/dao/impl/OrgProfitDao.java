package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IOrgProfitDao;
import com.dhk.entity.OrgProfit;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * OrgProfit DAO 接口实现类<br/>
    * 2016-12-21 08:42:51 Gnaily
    */ 
@Repository("OrgProfitDao")
public class OrgProfitDao extends JdbcBaseDaoSupport<OrgProfit, Long> implements IOrgProfitDao {
}

