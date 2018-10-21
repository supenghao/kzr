package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.IRechargeDao;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.service.IRechargeService;
import com.dhk.api.service.ITokenService;
import org.springframework.stereotype.Service;

import com.dhk.api.entity.Recharge;

@Service("RechargeService")
public class RechargeService implements IRechargeService {

	@Resource(name = "RechargeDao")
	private IRechargeDao rechargeDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Override
	public boolean updateRecharge(IdentityDto dto, double amount) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			double is = getRecharge(dto);
			double value = is + amount;
			if (value < 0) {
				value = 0;
			}
			String sql = "update t_s_recharge set amount=:amount where user_id=:user_id";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", dto.getUserId());
			map.put("amount", value);
			int i = rechargeDao.update(sql, map);
			return i == 1 ? true : false;
		}
		return false;
	}

	@Override
	public double getRecharge(IdentityDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			String sql = "select amount from t_s_recharge where user_id=:user_id";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", dto.getUserId());
			List<Recharge> i = rechargeDao.find(sql, map);
			return i.get(0).getAmount();
		}
		return 0;
	}

	@Override
	public void insertRecharge(int id) {
		String sql = "insert into t_s_recharge(user_id,amount) values(:user_id,:amount)";
		Recharge r = new Recharge();
		r.setAmount(0.00d);
		r.setUser_id(id + "");
		rechargeDao.insert(sql, r);
	}

}
