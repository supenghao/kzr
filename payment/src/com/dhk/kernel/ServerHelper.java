package com.dhk.kernel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dhk.kernel.exception.BaseException;

public class ServerHelper {
	static Logger logger = Logger.getLogger(ServerHelper.class);
	
	//DreamConf conf = new DreamConf();
	
	private Map<String,ServerSupport> servers = new HashMap<String,ServerSupport>();

	private ServerSupport findOrcreateInstance(ServerDef def) {
		ServerSupport instance = (ServerSupport) servers.get(def.getName());
		if (instance == null) {
			try {
				instance = (ServerSupport) def.getServer().newInstance();
				
				instance.setName(def.getName());

				instance.loadProperties(def.getProperties());

			} catch (Exception e) {
				throw new BaseException("server " + def.getName() + " 实例化失败", e);
			}
			servers.put(def.getName(), instance);
		}
		return instance;

	}
	/**
	 * 根据服务名称取后台服务定义
	 * @param name
	 * @return
	 */
	public ServerDef getServerDef(String name) {
		return (ServerDef) DreamConf.getServerDefs().get(name);
	}
	/**
	 * 取后台服务集合
	 * @return
	 */
	public Collection<ServerDef> getServerDefs() {
		return DreamConf.getServerDefs().values();
	}
	/**
	 * 根据名称取后台服务实例
	 * @param name
	 * @return
	 */
	public Server getServerService(String name) {
		ServerDef def = getServerDef(name);
		if (def == null)
			throw new BaseException("未定义后台服务" + name);
		return findOrcreateInstance(def);
	}
	/**
	 * 重启后台服务（先关闭后启动）
	 * @param backendName
	 */
	public void restartBackend(String serverName) {
		ServerDef def = (ServerDef) DreamConf.getServerDefs().get(serverName);
		if (def == null)
			throw new BaseException("系统没有定义后台服务:" + serverName);

		Server server = findOrcreateInstance(def);
		if (server.isStop()) {
			server.start();
		} else {
			server.stop();
			server.start();
		}

	}
	/**
	 * 启动后台服务
	 * @param backendName
	 */
	public void startServer(String serverName) {

		ServerDef def = (ServerDef) DreamConf.getServerDefs().get(serverName);
		if (def == null)
			throw new BaseException("系统没有定义后台服务:" + serverName);

		startServer(def);

	}
	private void startServer(ServerDef def) {

		ServerSupport server = findOrcreateInstance(def);
		if (server.isStop()) {
			
			server.start();
		}
	}

	/**
	 * 启动所有后台服务（Server启动时调用）
	 */
	public void startServers() {

		logger.debug("开始启动Server后台服务...");
		Map<String,ServerDef> serverDefs = DreamConf.getServerDefs();
		for (Iterator<ServerDef> iter = serverDefs.values().iterator(); iter.hasNext();) {
			ServerDef serverDef = (ServerDef) iter.next();

			startServer(serverDef);

		}

	}
	/**
	 * 停止后台服务
	 * @param backendName
	 */
	public void stopServer(String ServerName) {
		ServerDef def = (ServerDef) DreamConf.getServerDefs().get(ServerName);
		if (def == null)
			throw new BaseException("系统没有定义后台服务:" + ServerName);

		Server server = findOrcreateInstance(def);
		if (!server.isStop()) {
			logger.debug("后台服务" + ServerName + "正在停止");
			server.stop();
			logger.debug("后台服务" + ServerName + "成功停止");

		}
	}
	/**
	 * 停止所有的后台服务
	 */
	public void stopServers() {

		logger.debug("开始停止Server后台服务");
		Map<String,ServerDef> serverDefs = DreamConf.getServerDefs();
		for (Iterator<ServerDef> iter = serverDefs.values().iterator(); iter.hasNext();) {
			ServerDef serverDef = (ServerDef) iter.next();
			
			Server server = findOrcreateInstance(serverDef);
			if (!server.isStop()) {

				server.stop();
			}

		}

	}

}
