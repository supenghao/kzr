package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IAPPUserDao;
import com.dhk.entity.APPUser;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_s_user DAO 接口实现类<br/>
    * 2017-01-17 10:05:35 Gnaily
    */ 
@Repository("APPUserDao")
public class APPUserDao extends JdbcBaseDaoSupport<APPUser, Long> implements IAPPUserDao {
}

