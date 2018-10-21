package com.dhk.api.dao.impl;
import com.dhk.api.dao.ICardBinDao;
import com.dhk.api.entity.CardBin;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

/**
    * t_card_bin DAO 接口实现类<br/>
    * 2017-02-07 09:22:44 qch
    */ 
@Repository("CardBinDao")
public class CardBinDao extends JdbcBaseDaoSupport<CardBin, Long> implements ICardBinDao {
}

