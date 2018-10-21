package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.entity.OperatorsInfo;
import com.dhk.dao.IOperatorsInfoDao;

   /**
    * t_s_operators_info DAO 接口实现类<br/>
    * 2017-04-27 05:28:06 bianzk
    */ 
@Repository("operatorsInfoDao")
public class OperatorsInfoDao extends JdbcBaseDaoSupport<OperatorsInfo, Long> implements IOperatorsInfoDao {
}

