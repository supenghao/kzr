package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IRepayPlanDetailDao;
import com.dhk.entity.RepayPlanDetail;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_n_repay_plan DAO 接口实现类<br/>
    * 2017-01-09 09:02:35 Gnaily
    */ 
@Repository("RepayPlanDetailDao")
public class RepayPlanDetailDao extends JdbcBaseDaoSupport<RepayPlanDetail, Long> implements IRepayPlanDetailDao {
}

