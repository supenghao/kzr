package com.dhk.api.dao.impl;
import com.dhk.api.dao.IHomeAdDao;
import com.dhk.api.entity.HomeAd;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_headad DAO 接口实现类<br/>
    * 2017-01-14 09:42:37 qch
    */ 
@Repository("HomeAdDao")
public class HomeAdDao extends JdbcBaseDaoSupport<HomeAd, Long> implements IHomeAdDao {
}

