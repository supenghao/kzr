package com.dhk.service.impl;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.IMessageDao;
import com.dhk.entity.Message;
import com.dhk.service.IMessageService;
import com.sunnada.kernel.sql.SQLConf;
import com.sunnada.kernel.util.StringUtil;
@Service("MessageService")
public class MessageService implements IMessageService {
	@Resource(name = "MessageDao") 
	private IMessageDao messageDao;
	
	private boolean insertMessage(Message message){
		message.setCreatetime(StringUtil.getCurrentTime());
		String sql=SQLConf.getSql("message", "insertMessage");
		try {
			if(messageDao.insert(sql, message)!=0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
		
	}

	/**
	 * 向消息表中写入消息
	 * @param userId
	 * @param content
	 */
	public void writeMessage(long userId, String content){
		 Message msg=new Message();
    	 msg.setUser_id(userId);
    	 msg.setMessage_content(content);
    	 msg.setStatus("0");
    	 insertMessage(msg);
	}
}

