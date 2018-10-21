package com.dhk.dao.impl ;
import com.dhk.utils.AESUtil;
import org.springframework.stereotype.Repository;

import com.dhk.dao.ICreditCardDao;
import com.dhk.entity.CreditCard;
import com.sunnada.kernel.dao.jdbc.JdbcBaseDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
    * t_s_user_creditcard DAO 接口实现类<br/>
    * 2017-02-10 11:29:19 Gnaily
    */ 
@Repository("CreditCardDao")
public class CreditCardDao extends JdbcBaseDaoSupport<CreditCard, Long> implements ICreditCardDao {

   public List<CreditCard> find_Personal(String sql, Map<String, Object> paramMap) throws Exception {
      Map<String, Object> newMap  = new HashMap<String, Object>();
      newMap.putAll(paramMap);
      encryptMap(newMap);
      List<CreditCard> result =  super.find(sql,newMap);
      return result;
   }
   public void encryptMap(Map<String,Object> map) throws Exception{
      String tem="|cardno|realname|cerdid|cvn2|phoneno|expDate|";//需要编码的串
      for (Map.Entry<String, Object> entry : map.entrySet()) {
         String key = entry.getKey();
         if(tem.indexOf("|"+key.toLowerCase().replaceAll("_","")+"|")>=0){
            entry.setValue(AESUtil.Encrypt(entry.getValue()+""));
         }
      }
   }


}

