package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IOrgDao;
import com.dhk.entity.Org;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_org DAO 接口实现类<br/>
    * 2016-12-21 08:42:51 Gnaily
    */ 
@Repository("OrgDao")
public class OrgDao extends JdbcBaseDaoSupport<Org, Long> implements IOrgDao {
}

