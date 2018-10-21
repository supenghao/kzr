package com.dhk.api.dao.impl;
import com.dhk.api.dao.IInsuranceLsDao;
import com.dhk.api.entity.InsuranceLs;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_insurance_ls DAO 接口实现类<br/>
    * 2017-02-20 12:04:33 qch
    */ 
@Repository("InsuranceLsDao")
public class InsuranceLsDao extends JdbcBaseDaoSupport<InsuranceLs, Long> implements IInsuranceLsDao {
}

