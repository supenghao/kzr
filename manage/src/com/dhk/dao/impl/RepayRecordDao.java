package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IRepayRecordDao;
import com.dhk.entity.RepayRecord;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_n_repay_record DAO 接口实现类<br/>
    * 2017-02-19 02:13:28 bianzk
    */ 
@Repository("repayRecordDao")
public class RepayRecordDao extends JdbcBaseDaoSupport<RepayRecord, Long> implements IRepayRecordDao {
}

