package com.dhk.payment.dao.impl;
import org.springframework.stereotype.Repository;

import com.dhk.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.payment.dao.IUserDao;
import com.dhk.payment.entity.User;

/**
    * t_s_user DAO 接口实现类<br/>
    * 2016-12-30 10:32:56 qch
    */ 
@Repository("UserDao")
public class UserDao extends JdbcBaseDaoSupport<User, Long> implements IUserDao {
}

