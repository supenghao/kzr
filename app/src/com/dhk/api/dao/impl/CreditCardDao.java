package com.dhk.api.dao.impl;
import com.dhk.api.dao.ICreditCardDao;
import com.dhk.api.entity.CreditCard;
import com.dhk.api.tool.AESUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
    * t_s_user_creditcard DAO 接口实现类<br/>
    * 2017-02-08 09:31:12 qch
    */ 
@Repository("CreditCardDao")
public class CreditCardDao extends JdbcBaseDaoSupport<CreditCard, Long> implements ICreditCardDao {

   public List<CreditCard> find_Personal(String sql, Map<String, Object> paramMap) throws Exception {
         Map<String, Object> newMap  = new HashMap<String, Object>();
         newMap.putAll(paramMap);
         encryptMap(newMap);
         List<CreditCard> result =  super.find(sql,newMap);
         if(result!=null){
            for(CreditCard creditCard:result) {
               creditCard.decrypt();
            }
         }
         return result;
   }

   public int updateBy_Personal(String sql, CreditCard creditCard)throws Exception  {
      CreditCard newObj=new CreditCard();
      BeanUtils.copyProperties(creditCard,newObj);
      newObj.encrypt();
      return  super.updateBy(sql,newObj);
   }

   public long insert_Personal(String sql, CreditCard creditCard) throws Exception {
      CreditCard newObj=new CreditCard();
      BeanUtils.copyProperties(creditCard,newObj);
      newObj.encrypt();
      return super.insert(sql,newObj);
   }

   public int delete_Personal(String sql, Map<String, Object> map)  throws Exception{
      Map<String, Object> newMap  = new HashMap<>();
      newMap.putAll(map);
      encryptMap(newMap);
      return super.delete(sql,newMap);
   }

   public int update_Personal(String sql, Map<String, Object> map)  throws Exception{
      Map<String, Object> newMap  = new HashMap<>();
      newMap.putAll(map);
      encryptMap(newMap);
      return super.update(sql,newMap);
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

