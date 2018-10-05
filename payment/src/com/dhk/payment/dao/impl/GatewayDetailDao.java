package com.dhk.payment.dao.impl ;
import org.springframework.stereotype.Repository;
import com.dhk.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.payment.entity.GatewayDetail;
import com.dhk.payment.dao.IGatewayDetailDao;

   /**
    * t_gateway_detail DAO 接口实现类<br/>
    * 2017-01-05 10:37:00 bianzk
    */ 
@Repository("gatewayDetailDao")
public class GatewayDetailDao extends JdbcBaseDaoSupport<GatewayDetail, Long> implements IGatewayDetailDao {
}

