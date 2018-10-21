package com.dhk.dao.impl;

import org.springframework.stereotype.Repository;

import com.dhk.dao.ICreditCardBillDao;
import com.dhk.entity.CreditCardBill;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

@Repository("CreditCardBillDao")
public class CreditCardBillDao extends JdbcBaseDaoSupport<CreditCardBill, Long> implements ICreditCardBillDao{

}
