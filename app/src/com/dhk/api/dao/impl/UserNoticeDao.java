package com.dhk.api.dao.impl;

import com.dhk.api.dao.IUserNoticeDao;
import com.dhk.api.entity.UserNotice;
import org.springframework.stereotype.Repository;

import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;

@Repository("userNoticeDao")
public class UserNoticeDao extends JdbcBaseDaoSupport<UserNotice, Long> implements IUserNoticeDao {

}
