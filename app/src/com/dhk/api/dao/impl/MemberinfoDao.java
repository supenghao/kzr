package com.dhk.api.dao.impl;
import com.dhk.api.dao.IMemberinfoDao;
import com.dhk.api.entity.Memberinfo;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_s_memberinfo DAO 接口实现类<br/>
    * 2016-12-21 05:04:13 qch
    */ 
@Repository("MemberinfoDao")
public class MemberinfoDao extends JdbcBaseDaoSupport<Memberinfo, Long> implements IMemberinfoDao {
}

