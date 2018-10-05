package com.sunnada.uaas.dao.impl;

import com.sunnada.uaas.entity.Org;
import org.springframework.stereotype.Repository;

import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.sunnada.uaas.dao.IOrgDao;

@Repository("orgDao")
public class OrgDao extends JdbcBaseDaoSupport<Org, Long> implements IOrgDao{

}
