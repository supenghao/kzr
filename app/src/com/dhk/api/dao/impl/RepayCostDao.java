package com.dhk.api.dao.impl;
import com.dhk.api.dao.IRepayCostDao;
import com.dhk.api.entity.RepayCost;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_n_repay_cost DAO 接口实现类<br/>
    * 2017-03-02 10:53:08 qch
    */ 
@Repository("RepayCostDao")
public class RepayCostDao extends JdbcBaseDaoSupport<RepayCost, Long> implements IRepayCostDao {
}

