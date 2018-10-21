package com.dhk.api.dao.impl;

import com.dhk.api.dao.IAPPUserDao;
import com.dhk.api.entity.APPUser;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * t_s_user DAO 接口实现类<br/>
 * 2017-01-17 10:05:35 Gnaily
 */
@Repository("APPUserDao")
public class APPUserDao extends JdbcBaseDaoSupport<APPUser, Long> implements IAPPUserDao {
}

