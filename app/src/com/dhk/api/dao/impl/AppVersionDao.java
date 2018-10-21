package com.dhk.api.dao.impl;
import com.dhk.api.dao.IAppVersionDao;
import com.dhk.api.entity.AppVersion;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_appVersion DAO 接口实现类<br/>
    * 2017-01-13 05:04:36 qch
    */ 
@Repository("AppVersionDao")
public class AppVersionDao extends JdbcBaseDaoSupport<AppVersion, Long> implements IAppVersionDao {
}

