package com.dhk.api.dao.impl;
import com.dhk.api.dao.ICreditcardBillDao;
import com.dhk.api.entity.CreditcardBill;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_creditcard_bill DAO 接口实现类<br/>
    * 2017-01-14 04:04:17 qch
    */ 
@Repository("CreditcardBillDao")
public class CreditcardBillDao extends JdbcBaseDaoSupport<CreditcardBill, Long> implements ICreditcardBillDao {
}

