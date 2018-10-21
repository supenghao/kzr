package com.dhk.api.dao;
import com.xdream.kernel.dao.IBaseDao;
import com.dhk.api.entity.CreditCard;

import java.util.List;
import java.util.Map;

/**
    * t_s_user_creditcard DAO 接口<br/>
    * 2017-02-08 09:31:12 qch
    */ 
public interface ICreditCardDao extends IBaseDao<CreditCard, Long>{

    List<CreditCard> find_Personal(String sql, Map<String, Object> paramMap) throws Exception;

    int updateBy_Personal(String sql, CreditCard creditCard)throws Exception;

    long insert_Personal(String sql, CreditCard creditCard) throws Exception;

    int delete_Personal(String sql, Map<String, Object> map)  throws Exception;

    int update_Personal(String sql, Map<String, Object> map)  throws Exception;

}

