package com.dhk.api.dao.impl;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.api.entity.CardBill;
import com.dhk.api.dao.ICardBillDao;

   /**
    * t_s_card_bill DAO 接口实现类<br/>
    * 2016-12-25 10:04:08 qch
    */ 
@Repository("CardBillDao")
public class CardBillDao extends JdbcBaseDaoSupport<CardBill, Long> implements ICardBillDao {
}

