package com.dhk.api.dao.impl;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.entity.Beneficiary;
import com.dhk.api.dao.IBeneficiaryDao;

   /**
    * t_s_beneficiary DAO 接口实现类<br/>
    * 2017-02-20 12:05:15 qch
    */ 
@Repository("BeneficiaryDao")
public class BeneficiaryDao extends JdbcBaseDaoSupport<Beneficiary, Long> implements IBeneficiaryDao {
}

