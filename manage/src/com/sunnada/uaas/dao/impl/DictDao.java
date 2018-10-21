package com.sunnada.uaas.dao.impl;

import com.sunnada.uaas.dao.IDictDao;
import org.springframework.stereotype.Repository;

import com.sunnada.uaas.entity.Dict;

@Repository("dictDao")
public class DictDao extends com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport<Dict, Long> implements IDictDao {

}
