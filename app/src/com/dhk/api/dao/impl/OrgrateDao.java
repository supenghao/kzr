package com.dhk.api.dao.impl;
import com.dhk.api.dao.IOrgrateDao;
import com.dhk.api.entity.Orgrate;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_org_rate DAO 接口实现类<br/>
    * 2017-02-11 06:36:13 qch
    */ 
@Repository("OrgrateDao")
public class OrgrateDao extends JdbcBaseDaoSupport<Orgrate, Long> implements IOrgrateDao {
}

