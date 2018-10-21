package com.dhk.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.service.IApplyCashService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.tool.M;
import com.dhk.api.dao.IApplyCashDao;
import com.dhk.api.entity.ApplyCash;
import org.springframework.stereotype.Service;

import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RechargeDto;

@Service("ApplyCashService")
public class ApplyCashService implements IApplyCashService {
	@Resource(name = "ApplyCashDao")
	private IApplyCashDao applyCashDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Override
	public QResponse insertApplyCash(RechargeDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			insertApplyCash(dto.getUserId(), dto.getAmount());
			return QResponse.OK;
		}
		return QResponse.ERROR_SECURITY;
	}

	public void insertApplyCash(String user_id, String amount) {
		String sql = "insert into t_s_user_apply_cash(USER_ID,APPLY_DATE,AMOUNT) values(:user_id,:apply_date,:amount)";
		ApplyCash cash = new ApplyCash();
		cash.setApply_date(M.dformat.format(new Date()));
		cash.setUser_id(user_id);
		cash.setAmount(amount);
		applyCashDao.insert(sql, cash);
	}

	@Override
	public List<ApplyCash> getApplyCashByUserid(String userid) {
		String sql = "select * from t_s_user_apply_cash where user_id=:user_id order by id desc limit 30";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userid);
		return applyCashDao.find(sql, map);
	}

	@Deprecated
	public double getCurrentApplyCash(String userId) {
		String sql = "select ifnull(sum(amount),0) amount from t_s_user_apply_cash where user_id=:user_id and status='0'";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		return Double.parseDouble(applyCashDao.find(sql, map).get(0)
				.getAmount());
	}
}
