package com.dhk.api.service.impl;

import java.util.Map;

import com.dhk.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dhk.api.service.ApplicationService;
import com.xdream.kernel.sql.SQLConf;

@Service(value = "AppService")
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private JdbcTemplate template;

	@Override
	public boolean insertUser(String name, String pwd) {
		String sql = SQLConf.getSql("webApp", "insertUser");
		template.update(sql, name, pwd);
		return true;
	}

	@Override
	public User findUserByName(String loginName) {
		String sql = SQLConf.getSql("webApp", "findUserByName");
		Map<String,Object> map = template.queryForMap(sql);
		User u = new User();
		return null;
	}

	@Override
	public boolean cheakUserPsw(String userName, String pwd) {
		// TODO Auto-generated method stub
		return false;
	}
}
