package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.IUserDao;
import com.dhk.api.dto.MessageGDto;
import com.dhk.api.entity.Notices;
import com.dhk.api.service.INoticesService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.dao.impl.NoticesDao;
import org.springframework.stereotype.Service;

import com.xdream.kernel.sql.SQLConf;

@Service("NoticesService")
public class NoticesService implements INoticesService {

	@Resource(name = "NoticesDao")
	private NoticesDao noticesDao;

	@Resource(name = "UserDao")
	private IUserDao UserDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Override
	public List<Notices> getMessageList(MessageGDto dto) {
		boolean c = tokenService.checkToken(dto.getUserId(), dto.getToken());
		if (c) {
			String sql = "select * from t_s_org_notice order by NOTICE_CREATE desc limit 1";
			List<Notices> mess = noticesDao.find(sql, null);
			return mess;
		}
		return null;
	}
	
	@Override
	public List<Notices> getNotices2id(Long id){
		String sql = "select * from t_s_org_notice where id>:id order by id desc";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		List<Notices> mess = noticesDao.find(sql, map);
		return mess;
	}
	@Override
	public Integer getMaxNoticesId(){
		String sql = "select ifnull(max(id),0) maxId from t_s_org_notice";
		return noticesDao.count2Integer(sql, null);
	}

	@Override
	public Notices getMessageDetail(MessageGDto dto) {// 漏洞:id为判断是否有权限
		boolean c = tokenService.checkToken(dto.getUserId(), dto.getToken());
		if (c) {
			String sql = SQLConf.getSql("user", "findMessageById");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dto.getMessageCode());
			List<Notices> mess = noticesDao.find(sql, map);
			if (mess != null && mess.size() > 0) {
				return mess.get(0);
			} else {
				return new Notices();
			}
		}
		return null;
	}
}
