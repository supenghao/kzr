package com.dhk.api.dao.impl;
import com.dhk.api.dao.IRechargeDao;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.entity.Recharge;

/**
    * t_s_recharge DAO 接口实现类<br/>
    * 2017-02-15 10:45:22 qch
    */ 
@Repository("RechargeDao")
public class RechargeDao extends JdbcBaseDaoSupport<Recharge, Long> implements IRechargeDao {
}

