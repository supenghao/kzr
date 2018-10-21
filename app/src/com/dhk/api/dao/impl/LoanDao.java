package com.dhk.api.dao.impl;

import com.dhk.api.dao.ILoanDao;
import com.dhk.api.entity.Loan;
import org.springframework.stereotype.Repository;

import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

@Repository("loanDao")
public class LoanDao extends JdbcBaseDaoSupport<Loan, Long> implements ILoanDao {

}
