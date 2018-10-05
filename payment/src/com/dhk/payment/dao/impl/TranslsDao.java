package com.dhk.payment.dao.impl ;
import org.springframework.stereotype.Repository;
import com.dhk.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.payment.entity.Transls;
import com.dhk.payment.dao.ITranslsDao;

   /**
    * t_transls DAO 接口实现类<br/>
    * 2017-01-06 11:37:45 bianzk
    */ 
@Repository("translsDao")
public class TranslsDao extends JdbcBaseDaoSupport<Transls, Long> implements ITranslsDao {
}

