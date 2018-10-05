package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IUserAccountDao;
import com.dhk.entity.UserAccount;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_s_user_account DAO 接口实现类<br/>
    * 2017-01-09 09:04:57 Gnaily
    */ 
@Repository("UserAccountDao")
public class UserAccountDao extends JdbcBaseDaoSupport<UserAccount, Long> implements IUserAccountDao {
}

