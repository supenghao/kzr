package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IMessageDao;
import com.dhk.entity.Message;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_s_message DAO 接口实现类<br/>
    * 2017-02-15 11:41:36 bianzk
    */ 
@Repository("MessageDao")
public class MessageDao extends JdbcBaseDaoSupport<Message, Long> implements IMessageDao {
}

