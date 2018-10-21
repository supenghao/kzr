package com.dhk.api.service.impl;

import javax.annotation.Resource;

import com.dhk.api.dao.IBeneficiaryDao;
import com.dhk.api.entity.Beneficiary;
import org.springframework.stereotype.Service;

import com.dhk.api.service.IBeneficiaryService;

@Service("BeneficiaryService")
public class BeneficiaryService implements IBeneficiaryService {
	
	@Resource(name = "BeneficiaryDao")
	private IBeneficiaryDao beneficiaryDao;

	@Override
	public boolean insertBeneficiary(Beneficiary ls) {
		String sql = "insert into t_s_beneficiary(insuranceLsId,beneficiaryName,beneficiaryCertificateNo) values(:insurancelsid,:beneficiaryname,:beneficiarycertificateno)";
		// Map<String, Object> map = new HashMap<String, Object>();
		beneficiaryDao.insert(sql, ls);
		return true;
	}
}
