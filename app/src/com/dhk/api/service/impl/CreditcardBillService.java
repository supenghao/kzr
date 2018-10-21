package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.ICreditcardBillDao;
import com.dhk.api.entity.CreditcardBill;
import com.dhk.api.service.ICreditcardBillService;
import org.springframework.stereotype.Service;

import com.dhk.api.tool.M;

@Service("CreditcardBillService")
public class CreditcardBillService implements ICreditcardBillService {

	@Resource(name = "CreditcardBillDao")
	private ICreditcardBillDao CreditcardBillDao;

	@Override
	public List<CreditcardBill> getBillList(String creditcardId) {
		String sql = "select * from t_s_creditcard_bill where CREDITCARD_ID=:CREDITCARD_ID order by BILL_DAY desc limit 12";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CREDITCARD_ID", creditcardId);
		return CreditcardBillDao.find(sql, map);
	}

	/*
	 * 获取最新的有效的账单信息
	 */
	@Override
	public CreditcardBill getLastestBill(String creditcard_id) {
		M.logger.debug("getLastestBill:id=" + creditcard_id);
		String sql = "select * from t_s_creditcard_bill where creditcard_id=:creditcard_id and valueable='1' order by bill_day desc limit 1";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("creditcard_id", creditcard_id);
		List<CreditcardBill> l = CreditcardBillDao.find(sql, map);
		CreditcardBill b = null;
		if (l == null || l.size() == 0) {
			return null;
		} else {
			b = l.get(0);
			// b.setId(null);
			return b;
		}

	}

}
