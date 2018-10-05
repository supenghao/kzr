package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IOrgRateDao;
import com.dhk.entity.OrgRate;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_s_org_rate DAO 接口实现类<br/>
    * 2017-02-09 11:04:21 Gnaily
    */ 
@Repository("OrgRateDao")
public class OrgRateDao extends JdbcBaseDaoSupport<OrgRate, Long> implements IOrgRateDao {
}

