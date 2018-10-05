package com.sunnada.kernel;

import java.util.Properties;

public interface Backend {

	/**
	 * 服务名称
	 * @return
	 */
	public String getName();

	/**
	 * 服务端口
	 * @return
	 */
	public int getPort();

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
	 * 加载属性
	 * @param properties
	 */
	public void loadProperties(Properties properties);

	/**
	 * 将属性序列化
	 */
	public void saveProperties();

	/**
	 * 设置属性
	 * @param name
	 * @param value
	 */
	public void saveProperty(String name, String value);

	/**
	 * 设置服务名
	 * @param name
	 */
	public void setName(String name);

	/**
	 * 设置端口
	 * @param port
	 */
	public void setPort(int port);

	/**
	 * 启动服务
	 */
	public void start();

	/**
	 * 停止服务
	 */
	public void stop();
}
