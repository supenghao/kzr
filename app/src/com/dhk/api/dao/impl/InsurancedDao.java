package com.dhk.api.dao.impl;
import com.dhk.api.dao.IInsurancedDao;
import com.dhk.api.entity.Insuranced;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_insured DAO 接口实现类<br/>
    * 2017-02-20 12:04:55 qch
    */ 
@Repository("InsurancedDao")
public class InsurancedDao extends JdbcBaseDaoSupport<Insuranced, Long> implements IInsurancedDao {
}

