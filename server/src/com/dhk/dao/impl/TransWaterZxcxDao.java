package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.ITransWaterZxcxDao;
import com.dhk.entity.TransWaterZxcx;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_s_trans_water DAO 接口实现类<br/>
    * 2017-01-19 01:21:54 Gnaily
    */ 
@Repository("TransWaterZxcxDao")
public class TransWaterZxcxDao extends JdbcBaseDaoSupport<TransWaterZxcx, Long> implements ITransWaterZxcxDao {

}

