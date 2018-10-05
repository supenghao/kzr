package com.dhk.dao.impl;
import org.springframework.stereotype.Repository;

import com.dhk.dao.ICardBinDao;
import com.dhk.entity.CardBin;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_card_bin DAO 接口实现类<br/>
    * 2017-02-07 09:22:44 qch
    */ 
@Repository("CardBinDao")
public class CardBinDao extends JdbcBaseDaoSupport<CardBin, Long> implements ICardBinDao {
}

