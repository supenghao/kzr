package com.sunnada.uaas.dao.impl;

import com.sunnada.uaas.dao.ISystemParamDao;
import org.springframework.stereotype.Repository;

import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.sunnada.uaas.entity.SystemParam;

@Repository("systemParamDao")
public class SystemParamDao extends JdbcBaseDaoSupport<SystemParam, Long> implements ISystemParamDao {

}
