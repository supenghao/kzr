package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.entity.BankCss;
import com.dhk.api.service.IBankCssService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.dhk.api.dao.IBankCssDao;

@Service("BankCssService")
public class BankCssService implements IBankCssService {

	@Resource(name = "BankCssDao")
	private IBankCssDao bankCssDao;

	@Override
	@Cacheable(value="t_s_bank_css", key="#bank4code")
	public BankCss getBankcssByBank4code(String bank4code) {
		if (bank4code == null)
			return null;
		String sql = "select * from t_s_bank_css where bankcode=:bankcode";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bankcode", bank4code);
		List<BankCss> li = bankCssDao.find(sql, map);
		if (li.size() != 0 && li.get(0).getBaccolor() != null
				&& li.get(0).getLogoimgname() != null) {
			return li.get(0);
		} else {
			sql = "select * from t_s_bank_css where bankcode='0000'";
			li = bankCssDao.find(sql, map);
			return li.get(0);
		}
	}

}
