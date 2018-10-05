package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IRepayCostDao;
import com.dhk.entity.RepayCost;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_n_repay_cost DAO 接口实现类<br/>
    * 2017-03-07 04:53:33 bianzk
    */ 
@Repository("repayCostDao")
public class RepayCostDao extends JdbcBaseDaoSupport<RepayCost, Long> implements IRepayCostDao {
}

