package com.dhk.payment.dao.impl ;
import org.springframework.stereotype.Repository;
import com.dhk.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.payment.entity.Gateway;
import com.dhk.payment.dao.IGatewayDao;

   /**
    * t_gateway DAO 接口实现类<br/>
    * 2017-01-05 10:36:23 bianzk
    */ 
@Repository("gatewayDao")
public class GatewayDao extends JdbcBaseDaoSupport<Gateway, Long> implements IGatewayDao {
}

