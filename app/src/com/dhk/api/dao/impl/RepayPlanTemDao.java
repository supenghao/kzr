package com.dhk.api.dao.impl;
import com.dhk.api.entity.RepayPlanTem;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.dao.IRepayPlanTemDao;

   /**
    * t_s_repayplan_tem DAO 接口实现类<br/>
    * 2017-02-18 08:59:20 qch
    */ 
@Repository("RepayPlanTemDao")
public class RepayPlanTemDao extends JdbcBaseDaoSupport<RepayPlanTem, Long> implements IRepayPlanTemDao {
}

