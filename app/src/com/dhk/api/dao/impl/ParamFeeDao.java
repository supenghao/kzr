package com.dhk.api.dao.impl;
import com.dhk.api.dao.IParamFeeDao;
import com.dhk.api.entity.ParamFee;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_param_fee DAO 接口实现类<br/>
    * 2017-02-11 08:45:05 qch
    */ 
@Repository("ParamFeeDao")
public class ParamFeeDao extends JdbcBaseDaoSupport<ParamFee, Long> implements IParamFeeDao {
}

