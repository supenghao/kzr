package com.sunnada.kernel;

import java.util.Properties;

public interface Server {
	/**
	 * 服务名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 取服务所有属性
	 * @return
	 */
	public Properties getProperties();
	
	/**
	 * 取某个属性
	 * @param name
	 * @return
	 */
	public String getProperty(String name);
	
	/**
	 * 是否为停止状态
	 * @return
	 */
	public boolean isStop();
	
	/**
	 * 设置服务名
	 * @param name
	 */
	public void setName(String name);

	/**
	 * 启动服务
	 */
	public void start();

	/**
	 * 停止服务
	 */
	public void stop();
	
	/**
	 * 加载属性
	 * @param properties
	 */
	public void loadProperties(Properties properties);

}
