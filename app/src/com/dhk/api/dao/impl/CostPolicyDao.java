package com.dhk.api.dao.impl;
import com.dhk.api.dao.ICostPolicyDao;
import com.dhk.api.entity.CostPolicy;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_cost_policy DAO 接口实现类<br/>
    * 2017-01-10 03:49:11 qch
    */ 
@Repository("CostPolicyDao")
public class CostPolicyDao extends JdbcBaseDaoSupport<CostPolicy, Long> implements ICostPolicyDao {
}

