package com.dhk.api.dao.impl;
import com.dhk.api.entity.ORG;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.dao.IORGDao;

   /**
    * t_org DAO 接口实现类<br/>
    * 2017-01-13 04:03:14 qch
    */ 
@Repository("ORGDao")
public class ORGDao extends JdbcBaseDaoSupport<ORG, Long> implements IORGDao {
}

