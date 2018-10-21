package com.dhk.api.dao.impl;
import com.dhk.api.dao.IUserDao;
import com.dhk.api.entity.User;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_user DAO 接口实现类<br/>
    * 2016-12-30 10:32:56 qch
    */ 
@Repository("UserDao")
public class UserDao extends JdbcBaseDaoSupport<User, Long> implements IUserDao {
}

