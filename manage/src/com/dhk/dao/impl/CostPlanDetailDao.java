package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.ICostPlanDetailDao;
import com.dhk.entity.CostPlanDetail;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_s_cost_plan DAO 接口实现类<br/>
    * 2017-01-09 08:53:23 Gnaily
    */ 
@Repository("CostPlanDetailDao")
public class CostPlanDetailDao extends JdbcBaseDaoSupport<CostPlanDetail, Long> implements ICostPlanDetailDao {
}

