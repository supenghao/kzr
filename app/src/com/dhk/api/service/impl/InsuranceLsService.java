package com.dhk.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.IInsuranceLsDao;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.entity.User;
import com.dhk.api.service.IInsuranceLsService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.tool.M;
import com.dhk.api.entity.InsuranceLs;
import com.dhk.api.service.IUserService;
import org.springframework.stereotype.Service;

import com.dhk.api.dto.QResponse;

@Service("InsuranceLsService")
public class InsuranceLsService implements IInsuranceLsService {

	@Resource(name = "InsuranceLsDao")
	private IInsuranceLsDao insuranceLsDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Resource(name = "UserService")
	protected IUserService userService;

	@Override
	public InsuranceLs insertInsuranceLs(InsuranceLs ls) {
		Date d = new Date();
		ls.setLsDate(M.dformat.format(d));
		ls.setLsTime(M.tformat.format(d));
		String sql = "insert into t_s_insurance_ls (lsDate,lsTime,applyNo,policyNo,status,resultCode,resultText,quoteSchemeId,userid,insuranceBeginDate,insuranceEndDate,insuredAmount,premium)"
				+ " values "
				+ "(:lsDate,:lsTime,:applyNo,:policyNo,:status,:resultCode,:resultText,:quoteSchemeId,:userid,:insuranceBeginDate,:insuranceEndDate,:insuredAmount,:premium)";
		// Map<String, Object> map = new HashMap<String, Object>();
		Long id = insuranceLsDao.insert(sql, ls);
		ls.setId((long) id);
		return ls;
	}

	private final String sql_find = "select applyno,policyno,quoteschemeid,insurancebegindate,insuranceenddate,insuredamount,premium from t_s_insurance_ls where resultcode='0000' and userid=:userid order by id desc limit 1";

	@Override
	public QResponse getUserinsurances(IdentityDto dto) {
		if (tokenService.checkToken(dto)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userid", dto.getUserId());
			List<InsuranceLs> li = insuranceLsDao.find(sql_find, map);
			if (li.isEmpty())
				return new QResponse(new InsuranceLs());
			InsuranceLsRetBeen ret = new InsuranceLsRetBeen(li.get(0));
			ret.applyobject = "网络平台个人支付账户";
			ret.username = ((User) userService.getBaseInfo(dto).data)
					.getRealname();
			return new QResponse(ret);
		}
		return QResponse.ERROR_SECURITY;
	}

	public class InsuranceLsRetBeen extends InsuranceLs {
		private static final long serialVersionUID = -860265060673452858L;
		private String username, applyobject;

		public InsuranceLsRetBeen(InsuranceLs ls) {
			this.applyNo = ls.getApplyNo();
			this.policyNo = ls.getPolicyNo();
			this.quoteSchemeId = ls.getQuoteSchemeId();
			this.insuranceBeginDate = ls.getInsuranceBeginDate();
			this.insuranceEndDate = ls.getInsuranceEndDate();
			this.insuredAmount = ls.getInsuredAmount();
			this.premium = ls.getPremium();
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getApplyobject() {
			return applyobject;
		}

		public void setApplyobject(String applyobject) {
			this.applyobject = applyobject;
		}
	}
}
