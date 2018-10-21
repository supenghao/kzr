package com.dhk.api.dao.impl;
import com.dhk.api.dao.ITransWaterDao;
import com.dhk.api.entity.TransWater;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_trans_water DAO 接口实现类<br/>
    * 2017-01-05 02:56:36 qch
    */ 
@Repository("TransWaterDao")
public class TransWaterDao extends JdbcBaseDaoSupport<TransWater, Long> implements ITransWaterDao {
}

