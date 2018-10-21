package com.dhk.api.dao.impl;

import com.dhk.api.dao.ISystemParamDao;
import com.dhk.api.entity.SystemParam;
import org.springframework.stereotype.Repository;

import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

@Repository("systemParamDao")
public class SystemParamDao extends JdbcBaseDaoSupport<SystemParam, Long> implements ISystemParamDao {

}
