package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.ISettleDao;
import com.dhk.entity.Settle;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_s_settle DAO 接口实现类<br/>
    * 2017-02-11 10:44:04 Gnaily
    */ 
@Repository("SettleDao")
public class SettleDao extends JdbcBaseDaoSupport<Settle, Long> implements ISettleDao {
}

