package com.dhk.api.dao.impl;
import com.dhk.api.dao.IRepayPlanDao;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.entity.RepayPlan;

/**
    * t_n_repay_plan DAO 接口实现类<br/>
    * 2017-01-15 01:32:07 qch
    */ 
@Repository("RepayPlanDao")
public class RepayPlanDao extends JdbcBaseDaoSupport<RepayPlan, Long> implements IRepayPlanDao {
}

