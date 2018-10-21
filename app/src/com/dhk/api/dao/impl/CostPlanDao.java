package com.dhk.api.dao.impl;
import com.dhk.api.dao.ICostPlanDao;
import com.dhk.api.entity.CostPlan;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_cost_plan DAO 接口实现类<br/>
    * 2017-01-14 04:55:47 qch
    */ 
@Repository("CostPlanDao")
public class CostPlanDao extends JdbcBaseDaoSupport<CostPlan, Long> implements ICostPlanDao {
}

