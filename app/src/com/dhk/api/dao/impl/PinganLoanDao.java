package com.dhk.api.dao.impl;

import com.dhk.api.dao.IPinganLoanDao;
import com.dhk.api.entity.PinganLoan;
import org.springframework.stereotype.Repository;

import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

@Repository("pinganLoanDao")
public class PinganLoanDao extends JdbcBaseDaoSupport<PinganLoan, Long> implements IPinganLoanDao {

}
