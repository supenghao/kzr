package com.dhk.api.dao.impl;
import org.springframework.stereotype.Repository;

import com.dhk.api.dao.IRegionDao;
import com.dhk.api.entity.Region;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_region DAO 接口实现类<br/>
    * 2016-12-21 05:04:13 zzl
    */ 
@Repository("RegionDao")
public class RegionDao extends JdbcBaseDaoSupport<Region, Long> implements IRegionDao {
}

