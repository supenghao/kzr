package com.dhk.api.dao.impl;
import com.dhk.api.entity.Notices;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.dao.INoticesDao;

   /**
    * t_s_org_notice DAO 接口实现类<br/>
    * 2017-01-05 02:43:47 qch
    */ 
@Repository("NoticesDao")
public class NoticesDao extends JdbcBaseDaoSupport<Notices, Long> implements INoticesDao {
}

