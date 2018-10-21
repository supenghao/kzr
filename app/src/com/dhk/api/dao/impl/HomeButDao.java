package com.dhk.api.dao.impl;
import com.dhk.api.dao.IHomeButDao;
import com.dhk.api.entity.HomeBut;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_headbut DAO 接口实现类<br/>
    * 2017-01-14 09:42:53 qch
    */ 
@Repository("HomeButDao")
public class HomeButDao extends JdbcBaseDaoSupport<HomeBut, Long> implements IHomeButDao {
}

