package com.dhk.api.core;

public interface ShortMsg {
	/**
	 * 发送一个随机验证码
	 * 
	 * @param phone
	 * @return 发送的验证码
	 */
	String sendCheckCode(String phone, String url);

	/**
	 * 发送消息到手机上
	 * 
	 * @param phone
	 * @param msg
	 * @return
	 */
	boolean sendMsg(String phone, String msg);
}
