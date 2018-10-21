package com.dhk.api.dao.impl;
import com.dhk.api.entity.Account;
import com.dhk.api.dao.IAccountDao;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_user_account DAO 接口实现类<br/>
    * 2017-02-09 03:49:52 qch
    */ 
@Repository("AccountDao")
public class AccountDao extends JdbcBaseDaoSupport<Account, Long> implements IAccountDao {
}

