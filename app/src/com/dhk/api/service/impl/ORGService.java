package com.dhk.api.service.impl;

import javax.annotation.Resource;

import com.dhk.api.dto.LoginDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.service.IORGService;
import org.springframework.stereotype.Service;

import com.dhk.api.dao.IORGDao;
import com.dhk.api.dto.AgentIdentifyDto;

@Service("ORGService")
public class ORGService implements IORGService {

	@Resource(name = "ORGDao")
	private IORGDao ORGDao;

	@Override
	public QResponse login(LoginDto dto) {
		// String sql = SQLConf.getSql("user", "findUserByName");
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("username", username);
		// ORG u = ORGDao.findBy(sql, map);
		// if (u.get.equals(pwd)) {
		// Token t = new Token();
		// t.setToken(M.createUUID());
		// t.setUserid(u.getId() + "");
		// tokenService.updateToken(t);
		// return t;
		// }
		return QResponse.ERROR;
	}

	@Override
	public QResponse agentTotal(AgentIdentifyDto dto) {
		QResponse ret = new QResponse(true, "");
		AgentTotal total = new AgentTotal();
		total.appUserNums = "100";
		total.noUserNums = "200";
		total.userNums = "300";
		total.cmtAmtC = "3000";
		total.cmtAmtD = "3002";
		total.cmtNumC = "2342";
		total.cmtNumD = "3342";
		ret.data = total;
		return ret;
	}

	public class AgentTotal {

		private String appUserNums, noUserNums, userNums, cmtAmtD, cmtAmtC,
				cmtNumD, cmtNumC;

		/**
		 * 获取 appUserNums 变量
		 * 
		 * @return 返回 appUserNums 变量
		 */
		public String getAppUserNums() {
			return appUserNums;
		}

		/**
		 * 设置 appUserNums 变量
		 * 
		 * @param appUserNums
		 */
		public void setAppUserNums(String appUserNums) {
			this.appUserNums = appUserNums;
		}

		/**
		 * 获取 noUserNums 变量
		 * 
		 * @return 返回 noUserNums 变量
		 */
		public String getNoUserNums() {
			return noUserNums;
		}

		/**
		 * 设置 noUserNums 变量
		 * 
		 * @param noUserNums
		 */
		public void setNoUserNums(String noUserNums) {
			this.noUserNums = noUserNums;
		}

		/**
		 * 获取 userNums 变量
		 * 
		 * @return 返回 userNums 变量
		 */
		public String getUserNums() {
			return userNums;
		}

		/**
		 * 设置 userNums 变量
		 * 
		 * @param userNums
		 */
		public void setUserNums(String userNums) {
			this.userNums = userNums;
		}

		/**
		 * 获取 cmtAmtD 变量
		 * 
		 * @return 返回 cmtAmtD 变量
		 */
		public String getCmtAmtD() {
			return cmtAmtD;
		}

		/**
		 * 设置 cmtAmtD 变量
		 * 
		 * @param cmtAmtD
		 */
		public void setCmtAmtD(String cmtAmtD) {
			this.cmtAmtD = cmtAmtD;
		}

		/**
		 * 获取 cmtAmtC 变量
		 * 
		 * @return 返回 cmtAmtC 变量
		 */
		public String getCmtAmtC() {
			return cmtAmtC;
		}

		/**
		 * 设置 cmtAmtC 变量
		 * 
		 * @param cmtAmtC
		 */
		public void setCmtAmtC(String cmtAmtC) {
			this.cmtAmtC = cmtAmtC;
		}

		/**
		 * 获取 cmtNumD 变量
		 * 
		 * @return 返回 cmtNumD 变量
		 */
		public String getCmtNumD() {
			return cmtNumD;
		}

		/**
		 * 设置 cmtNumD 变量
		 * 
		 * @param cmtNumD
		 */
		public void setCmtNumD(String cmtNumD) {
			this.cmtNumD = cmtNumD;
		}

		/**
		 * 获取 cmtNumC 变量
		 * 
		 * @return 返回 cmtNumC 变量
		 */
		public String getCmtNumC() {
			return cmtNumC;
		}

		/**
		 * 设置 cmtNumC 变量
		 * 
		 * @param cmtNumC
		 */
		public void setCmtNumC(String cmtNumC) {
			this.cmtNumC = cmtNumC;
		}
	}
}
