package com.dhk.dao ;
import com.dhk.entity.CreditCard;
import com.sunnada.kernel.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
    * t_s_user_creditcard DAO 接口<br/>
    * 2017-02-10 11:29:19 Gnaily
    */ 
public interface ICreditCardDao extends IBaseDao<CreditCard, Long>{


   List<CreditCard> find_Personal(String sql, Map<String, Object> paramMap) throws Exception;
}

