package com.dhk.kernel;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 缺省的后台服务实现
 * @author Administrator
 */
public abstract class BackendAdapter implements Backend {
	static Logger logger = Logger.getLogger(BackendAdapter.class);

	//public static final String DOMAIN = "magicserver";

	protected String name;

	protected int port;

	protected Properties properties;

	protected boolean isStop = true;

	// FileInputStream propertyIs;
	//
	// File propertyFile;

	public abstract void doStart() throws Exception;

	public abstract void doStop() throws Exception;

	/**
	 * 后台服务名称
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 后台服务端口
	 * 
	 * @see com.gd.magic.BackendService#getPort()
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 后台服务属性
	 * 
	 * @see com.gd.magic.BackendService#getProperties()
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * 取后台服务某个属性
	 * @see com.gd.magic.BackendService#getProperty(java.lang.String)
	 */
	public String getProperty(String name) {
		return properties.getProperty(name);

	}

	/**
	 * 取后台服务某个属性，如不存在则取缺省值
	 * @param name
	 * @param def
	 * @return
	 */
	public String getProperty(String name, String def) {
		String o = getProperty(name);
		return o != null ? o : def;

	}

	/**
	 * 判断后台服务是否处于停止状态
	 * @see com.gd.magic.BackendService#isStop()
	 */
	public boolean isStop() {
		return isStop;
	}

	/**
	 * 加载属性配置
	 */
	public void loadProperties(Properties props) {

		// 首先接收外部配置文件的properties
		properties = props == null ? new Properties() : props;
		properties.put("port", port + "");

		// // 从运行目录中读取配置参数并覆盖
		// File propertyDir = new File(MagicFactory.getAppContext().getRealPath(
		// MagicFactory.DIR_WORKSPACE + "/backends/" + name));
		// propertyDir.mkdirs();
		//
		// try {
		//
		// propertyFile = new File(propertyDir, "property.ini");
		//
		// if (!propertyFile.exists())
		// propertyFile.createNewFile();
		//
		// propertyIs = new FileInputStream(propertyFile);
		// properties.load(propertyIs);
		//
		// } catch (IOException ioe) {
		// throw new MagicException(ioe);
		// }
		// // 必须先保存,否则由于建立了非append的outputstream,文件内容会被清空
		//
		// saveProperties();

		// port = Integer.parseInt(properties.getProperty("port"));

	}

	/**
	 * 保存配置
	 */
	public void saveProperties() {

		// FileOutputStream propertyOs = null;
		// try {
		// propertyOs = new FileOutputStream(propertyFile, false);
		//
		// properties.store(propertyOs, "");
		// } catch (IOException ioe) {
		// logger.error("存储属性文件时出错", ioe);
		// } finally {
		// if (propertyOs != null)
		// try {
		// propertyOs.close();
		// } catch (Exception e) {
		// }
		// }
	}

	/**
	 * 设置某个属性
	 */
	public void saveProperty(String name, String value) {
		properties.setProperty(name, value);

		saveProperties();
	}

	/**
	 * 设置名称
	 * @see com.gd.magic.BackendService#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置端口
	 * @see com.gd.magic.BackendService#setPort(int)
	 */
	public void setPort(int port) {
		this.port = port;

	}

	/**
	 * 启动服务
	 * @see com.gd.magic.BackendService#start()
	 */
	public synchronized void start() {
		if (!isStop)
			return;

		isStop = false;
		try {
			long begin = System.currentTimeMillis();

			doStart();
			long end = System.currentTimeMillis();
			logger.debug("[后台服务] " + name + "成功启动,耗时" + (end - begin) + "毫秒");
		} catch (Exception e) {
			logger.error("[后台服务] " + name + "启动失败", e);
			isStop = true;
		}
	}

	/**
	 * 停止服务
	 * @see com.gd.magic.BackendService#stop()
	 */
	public synchronized void stop() {
		if (isStop)
			return;

		isStop = true;
		try {
			doStop();
			logger.debug("[后台服务] " + name + "成功停止");
		} catch (Exception e) {
			logger.error("[后台服务] " + name + "停止失败", e);
			isStop = false;
		}
	}

}
