package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.dhk.api.dao.IQrcodeDao;
import com.dhk.api.entity.Qrcode;
import com.dhk.api.service.IQrcodeService;
import com.xdream.kernel.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("QrcodeService")
public class QrcodeService implements IQrcodeService {
	protected static Logger logger = Logger.getLogger(QrcodeService.class);
	@Resource(name = "QrcodeDao")
	private IQrcodeDao qrcodeDao;

	@Override
	public Qrcode getQrcodeByInvitation(String INVITATION_CODE) {

		logger.info("INVITATION_CODE:"+INVITATION_CODE+"|");

		String sql = "select * from t_s_org_qrcode where INVITATION_CODE=:INVITATION_CODE and (ISUSE ='2')";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("INVITATION_CODE", INVITATION_CODE);
		
		Qrcode code = null;
		List<Qrcode> codes = qrcodeDao.find(sql,map);
		logger.info("codes:"+codes);
		if (codes!=null && !codes.isEmpty() && codes.size()>0){
			code = codes.get(0);
		}else{
			logger.info("邀请码不存在或状态异常:"+INVITATION_CODE);
		}
		return code;
	}

	@Override
	public void changeQrcodeState(String id, String state) {
		String sql = "update t_s_org_qrcode set ISUSE=:state where id=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("state", state);
		qrcodeDao.update(sql, map);

		sql="insert into t_s_org_qrcode_log(QRCODE_ID,CREATE_DATE,STATUS)values(:qrcodeId,:createDate,:status)";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("qrcodeId", id);
		param.put("createDate", StringUtil.getCurrentDate());
		param.put("status", "3");
		qrcodeDao.update(sql, param);

	}
}
