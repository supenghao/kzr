package com.dhk.utils;

public class HXPayBaseReult {
	private String appId;//应用code
	private String serverTransId;//系统流水号
	private String clientTransId;//交易流水
	private Long transTimestamp;//请求时间
	private String data;//应答数据
	private String respCode;//应答码
	private String respMsg;//应答描述
	private String sign;//签名
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getServerTransId() {
		return serverTransId;
	}
	public void setServerTransId(String serverTransId) {
		this.serverTransId = serverTransId;
	}
	public String getClientTransId() {
		return clientTransId;
	}
	public void setClientTransId(String clientTransId) {
		this.clientTransId = clientTransId;
	}
	public Long getTransTimestamp() {
		return transTimestamp;
	}
	public void setTransTimestamp(Long transTimestamp) {
		this.transTimestamp = transTimestamp;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	
}
