package com.dhk.dao.impl;

import com.dhk.dao.IOrgAgentApplyCashDao;
import com.dhk.entity.OrgAgentApplyCash;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * t_s_org_apply_cash DAO 接口实现类<br/>
 * 2017-02-14 07:32:17 bianzk
 */
@Repository("OrgAgentApplyCashDao")
public class OrgAgentApplyCashDao extends JdbcBaseDaoSupport<OrgAgentApplyCash, Long> implements IOrgAgentApplyCashDao {
}

