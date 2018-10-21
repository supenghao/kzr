package com.dhk.service;

/**
    * t_s_message service 接口<br/>
    * 2017-02-15 11:41:36 bianzk
    */ 
public interface IMessageService {
	
	/**
	 * 写入消息
	 * @param userId
	 * @param content
	 */
	public void writeMessage(long userId, String content);
}

