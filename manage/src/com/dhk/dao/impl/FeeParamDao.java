package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.IFeeParamDao;
import com.dhk.entity.FeeParam;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_param_fee DAO 接口实现类<br/>
    * 2017-02-10 09:31:05 Gnaily
    */ 
@Repository("FeeParamDao")
public class FeeParamDao extends JdbcBaseDaoSupport<FeeParam, Long> implements IFeeParamDao {
}

