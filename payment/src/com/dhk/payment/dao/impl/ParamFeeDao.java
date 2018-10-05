package com.dhk.payment.dao.impl;
import org.springframework.stereotype.Repository;

import com.dhk.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.payment.dao.IParamFeeDao;
import com.dhk.payment.entity.ParamFee;

/**
    * t_param_fee DAO 接口实现类<br/>
    * 2017-02-11 08:45:05 qch
    */ 
@Repository("ParamFeeDao")
public class ParamFeeDao extends JdbcBaseDaoSupport<ParamFee, Long> implements IParamFeeDao {
}

