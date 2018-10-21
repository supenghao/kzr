package com.dhk.api.dto;

public class MessageGDto extends IdentityDto {

	// 公告消息查询详细增加的输入参数
	private String messageCode;

	/**
	 * 获取 messageCode 变量
	 * 
	 * @return 返回 messageCode 变量
	 */
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * 设置 messageCode 变量
	 * 
	 * @param messageCode
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageDto [messageCode=" + messageCode + "]";
	}
}
