package com.dhk.dao.impl;

import com.dhk.dao.IAPPUserApplyCashDao;
import com.dhk.entity.APPUserApplyCash;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * t_s_user_apply_cash DAO 接口实现类<br/>
 * 2017-02-14 07:34:53 bianzk
 */
@Repository("APPUserApplyCashDao")
public class APPUserApplyCashDao extends JdbcBaseDaoSupport<APPUserApplyCash, Long> implements IAPPUserApplyCashDao {
}

