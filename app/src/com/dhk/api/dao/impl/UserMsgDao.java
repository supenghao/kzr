package com.dhk.api.dao.impl;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.entity.UserMsg;
import com.dhk.api.dao.IUserMsgDao;

   /**
    * t_s_message DAO 接口实现类<br/>
    * 2017-02-11 12:00:36 qch
    */ 
@Repository("UserMsgDao")
public class UserMsgDao extends JdbcBaseDaoSupport<UserMsg, Long> implements IUserMsgDao {
}

