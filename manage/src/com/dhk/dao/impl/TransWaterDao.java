package com.dhk.dao.impl ;
import org.springframework.stereotype.Repository;

import com.dhk.dao.ITransWaterDao;
import com.dhk.entity.TransWater;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

   /**
    * t_s_trans_water DAO 接口实现类<br/>
    * 2017-01-19 01:21:54 Gnaily
    */ 
@Repository("TransWaterDao")
public class TransWaterDao extends JdbcBaseDaoSupport<TransWater, Long> implements ITransWaterDao {

}

