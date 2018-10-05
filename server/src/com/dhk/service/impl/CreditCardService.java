package com.dhk.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.ICreditCardDao;
import com.dhk.entity.CreditCard;
import com.dhk.service.ICreditCardService;
import com.sunnada.kernel.sql.SQLConf;
@Service("CreditCardService")
public class CreditCardService implements ICreditCardService {
	@Resource(name = "CreditCardDao") 
	private ICreditCardDao creditCardDao;

	public CreditCard findByCardNo(String cardNo) {
		String sql=SQLConf.getSql("creditcard", "findByCardNo");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("cardNo", cardNo);
		try{
			List<CreditCard> creditCardList = creditCardDao.find_Personal(sql, map);
			if(creditCardList!=null&&creditCardList.size()>0){
				     return  creditCardList.get(0);
			}
			return null;
		}catch (Exception e) {
			return null;
		}
	}
}

