package com.dhk.api.service.impl;

import javax.annotation.Resource;

import com.dhk.api.dao.impl.UserDao;
import com.dhk.api.dto.MemberInfoDto;
import com.dhk.api.entity.User;
import com.dhk.api.service.IMemberinfoService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.tool.M;
import org.springframework.stereotype.Service;

@Service("MemberinfoService")
public class MemberinfoService implements IMemberinfoService {

	@Resource(name = "UserDao")
	private UserDao userDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Override
	public Boolean editMemberInfo(MemberInfoDto dto) {
		if (dto != null) {
			String userId = dto.getUserId();
			String tostr = dto.getToken();
			boolean t = tokenService.checkToken(userId, tostr);
			if (t) {
				User u = new User(dto);
				String sql = M
						.getUpdateSqlWhenFilesIsNotNull(u, "id=" + userId);
				//u.setId(Long.parseLong(dto.getUserId()));
				userDao.updateBy(sql, u);
				return true;
			}
		}
		return null;
	}

	// /**
	// * 插入默认的成员信息
	// *
	// * @param userid
	// * @param nickName
	// * @param mobilePhone
	// */
	// public void insertDefaultMemberInfo(String userid, String mobilePhone) {
	// Memberinfo m = new Memberinfo();
	// m.setUserId(userid);
	// m.setNickName(mobilePhone);
	// m.setUserMobile(mobilePhone);
	// String sql = M.getInsertSqlWhenFilesIsNotNull(m);
	// memberinfoDao.insert(sql, m);
	// }
}
