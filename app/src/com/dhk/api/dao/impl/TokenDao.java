package com.dhk.api.dao.impl;
import com.dhk.api.entity.Token;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.dao.ITokenDao;

   /**
    * t_s_token DAO 接口实现类<br/>
    * 2016-12-20 11:46:56 qch
    */ 
@Repository("TokenDao")
public class TokenDao extends JdbcBaseDaoSupport<Token, Long> implements ITokenDao {
}

