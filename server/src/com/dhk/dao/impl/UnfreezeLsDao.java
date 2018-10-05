package com.dhk.dao.impl;

import com.dhk.dao.IUnfreezeLsDao;
import com.dhk.entity.UnfreezeLs;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("unfreezeLsDao")
public class UnfreezeLsDao extends JdbcBaseDaoSupport<UnfreezeLs, Long> implements IUnfreezeLsDao {

}
