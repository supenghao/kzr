package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.IUserMsgDao;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.UserMsgDto;
import com.dhk.api.entity.UserMsg;
import com.dhk.api.service.ITokenService;
import com.dhk.api.service.IUserMsgService;
import com.dhk.api.tool.StrUtils;
import org.springframework.stereotype.Service;

@Service("UserMsgService")
public class UserMsgService implements IUserMsgService {
	@Resource(name = "UserMsgDao")
	private IUserMsgDao userMsgDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Override
	public QResponse getShortMsg(UserMsgDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			String sql = null;
			Map<String, Object> map = new HashMap<String, Object>();
			if (!StrUtils.isEmpty(dto.getReadid())) {// 设置消息为已读
				sql = "update t_s_message set status='1' where id=:id and user_id=:user_id and status = '0'";
				map.put("user_id", dto.getUserId());
				map.put("id", dto.getReadid());
				userMsgDao.update(sql, map);
				return QResponse.OK;
			}
			sql = "select * from t_s_message  where user_id=:user_id  order by id desc limit 30";
			map.put("user_id", dto.getUserId());
			List<UserMsg> l = userMsgDao.find(sql, map);
			return new QResponse(l);
		}
		return QResponse.ERROR_SECURITY;
	}
}
