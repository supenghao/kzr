package com.dhk.api.service.impl;

import javax.annotation.Resource;

import com.dhk.api.dao.IInsurancedDao;
import com.dhk.api.service.IInsurancedService;
import com.dhk.api.entity.Insuranced;
import org.springframework.stereotype.Service;

@Service("InsurancedService")
public class InsurancedService implements IInsurancedService {
	@Resource(name = "InsurancedDao")
	private IInsurancedDao insurancedDao;

	@Override
	public boolean insertInsuranced(Insuranced ls) {
		String sql = "insert into t_s_insured(insuranceLsId,insuredName,insuredCertificateNo) values(:insurancelsid,:insuredname,:insuredcertificateno)";
		// Map<String, Object> map = new HashMap<String, Object>();
		insurancedDao.insert(sql, ls);
		return true;
	}

}
