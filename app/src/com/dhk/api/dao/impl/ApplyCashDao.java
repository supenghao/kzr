package com.dhk.api.dao.impl;
import com.dhk.api.dao.IApplyCashDao;
import com.dhk.api.entity.ApplyCash;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_user_apply_cash DAO 接口实现类<br/>
    * 2017-02-14 10:48:52 qch
    */ 
@Repository("ApplyCashDao")
public class ApplyCashDao extends JdbcBaseDaoSupport<ApplyCash, Long> implements IApplyCashDao {
}

