package com.dhk.api.dao.impl;
import com.dhk.api.dao.IRepayRecordDao;
import com.dhk.api.entity.RepayRecord;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_n_repay_record DAO 接口实现类<br/>
    * 2017-02-19 03:00:57 qch
    */ 
@Repository("RepayRecordDao")
public class RepayRecordDao extends JdbcBaseDaoSupport<RepayRecord, Long> implements IRepayRecordDao {
}

