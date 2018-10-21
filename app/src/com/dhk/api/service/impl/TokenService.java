package com.dhk.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.ITokenDao;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.service.ITokenService;
import com.dhk.api.entity.Token;
import org.springframework.stereotype.Service;

import com.dhk.api.service.IUserService;
import com.xdream.kernel.sql.SQLConf;

@Service("TokenService")
public class TokenService implements ITokenService {

	@Resource(name = "TokenDao")
	private ITokenDao TokenDao;

	@Resource(name = "UserService")
	private IUserService userService;

	public void updateToken(Token t) {
		if (t == null)
			return;
		String sql = SQLConf.getSql("token", "updateToken");
		TokenDao.updateBy(sql, t);
	}

	@Override
	public Token findTokenById(Object userid) {
		try{
		if (userid == null)
			return null;
		String sql = SQLConf.getSql("token", "findToken");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		List<Token> l = TokenDao.find(sql, map);
		if (l == null || l.size() != 1)
			return null;
		return l.get(0);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Token findTokenByName(String userName) {
		if (userName == null)
			return null;
		String sql = SQLConf.getSql("token", "findTokenbyName");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		List<Token> l = TokenDao.find(sql, map);
		if (l == null || l.size() != 1)
			return null;
		return l.get(0);
	}

	@Override
	public void insertToken(Token t) {
		if (t == null)
			return;
		String sql = SQLConf.getSql("token", "insertToken");
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		t.setTime(time);
		TokenDao.insert(sql, t);
	}

	@Override
	public boolean checkToken(String userid, String token) {
		try{
		if (userid == null || token == null) {
			return false;
		}
		boolean state = userService.checkUserState(userid);
		if (!state)
			return state;
		Token t = findTokenById(userid);
		if (t != null && t.getToken().equals(token)) {
			return true;
		}
		return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean checkToken(IdentityDto dto) {
		return checkToken(dto.getUserId(), dto.getToken());
	}
}
