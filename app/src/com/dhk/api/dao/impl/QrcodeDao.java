package com.dhk.api.dao.impl;
import com.dhk.api.entity.Qrcode;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.dao.IQrcodeDao;

   /**
    * t_s_org_qrcode DAO 接口实现类<br/>
    * 2017-02-09 03:34:34 qch
    */ 
@Repository("QrcodeDao")
public class QrcodeDao extends JdbcBaseDaoSupport<Qrcode, Long> implements IQrcodeDao {
}

