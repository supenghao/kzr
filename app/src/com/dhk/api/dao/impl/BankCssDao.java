package com.dhk.api.dao.impl;
import com.dhk.api.entity.BankCss;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.dao.IBankCssDao;

   /**
    * t_s_bank_css DAO 接口实现类<br/>
    * 2017-02-16 07:01:40 qch
    */ 
@Repository("BankCssDao")
public class BankCssDao extends JdbcBaseDaoSupport<BankCss, Long> implements IBankCssDao {
}

