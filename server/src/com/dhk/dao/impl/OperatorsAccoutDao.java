package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IOperatorsAccoutDao;
import com.dhk.entity.OperatorsAccout;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * operators_accout DAO 接口实现类<br/>
    * 2017-02-19 09:02:11 bianzk
    */ 
@Repository("operatorsAccoutDao")
public class OperatorsAccoutDao extends JdbcBaseDaoSupport<OperatorsAccout, Long> implements IOperatorsAccoutDao {
}

