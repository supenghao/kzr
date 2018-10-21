package com.sunnada.kernel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class BackendHelper {
	static Logger logger = Logger.getLogger(BackendHelper.class);
	
	//DreamConf conf = new DreamConf();
	
	private Map<String,BackendAdapter> backends = new HashMap<String,BackendAdapter>();
	
	private BackendAdapter findOrcreateInstance(BackendDef def) {
		BackendAdapter instance = (BackendAdapter) backends.get(def.getName());
		if (instance == null) {
			try {
				instance = (BackendAdapter) def.getServerClass().newInstance();
				if (def.getPort() != -1)
					instance.setPort(def.getPort());
				instance.setName(def.getName());

				instance.loadProperties(def.getProperties());

			} catch (Exception e) {
				throw new com.sunnada.kernel.exception.BaseException("backend " + def.getName() + " 实例化失败", e);
			}
			backends.put(def.getName(), instance);
		}
		return instance;

	}
	/**
	 * 根据服务名称取后台服务定义
	 * @param name
	 * @return
	 */
	public BackendDef getBackendDef(String name) {
		return (BackendDef) DreamConf.getBackendDefs().get(name);
	}
	/**
	 * 取后台服务集合
	 * @return
	 */
	public Collection<BackendDef> getBackendDefs() {
		return DreamConf.getBackendDefs().values();
	}
	/**
	 * 根据名称取后台服务实例
	 * @param name
	 * @return
	 */
	public Backend getBackendService(String name) {
		BackendDef def = getBackendDef(name);
		if (def == null)
			throw new com.sunnada.kernel.exception.BaseException("未定义后台服务" + name);
		return findOrcreateInstance(def);
	}
	/**
	 * 重启后台服务（先关闭后启动）
	 * @param backendName
	 */
	public void restartBackend(String backendName) {
		BackendDef def = (BackendDef) DreamConf.getBackendDefs().get(backendName);
		if (def == null)
			throw new com.sunnada.kernel.exception.BaseException("系统没有定义后台服务:" + backendName);

		Backend backend = findOrcreateInstance(def);
		if (backend.isStop()) {
			backend.start();
		} else {
			backend.stop();
			backend.start();
		}

	}
	private void startBackend(BackendDef def) {

		BackendAdapter backend = findOrcreateInstance(def);
		if (def.isEnabled() && backend.isStop()) {
//			List requires = def.getRequires();
//			for (int i = 0; i < requires.size(); i++) {
//				String requireService = (String) requires.get(i);
//				BackendDef requireDef = (BackendDef) DreamConf.getBackendDefs()
//						.get(requireService);
//				if (requireDef == null)
//					continue; // 本机没有定义依赖服务，继续
//				// TODO 解决循环引用
//				startBackend(requireDef);
//			}
			backend.start();
		}
	}
	/**
	 * 启动后台服务
	 * @param backendName
	 */
	public void startBackend(String backendName) {

		BackendDef def = (BackendDef) DreamConf.getBackendDefs().get(backendName);
		if (def == null)
			throw new com.sunnada.kernel.exception.BaseException("系统没有定义后台服务:" + backendName);

		startBackend(def);

	}
	/**
	 * 启动所有后台服务（BackendServer启动时调用）
	 */
	public void startBackends() {

		logger.debug("开始启动Backend后台服务...");
		Map<String,BackendDef> backendDefs = DreamConf.getBackendDefs();
		for (Iterator<BackendDef> iter = backendDefs.values().iterator(); iter.hasNext();) {
			BackendDef backendDef = (BackendDef) iter.next();

			startBackend(backendDef);

		}

	}
	/**
	 * 停止后台服务
	 * @param backendName
	 */
	public void stopBackend(String backendName) {
		BackendDef def = (BackendDef) DreamConf.getBackendDefs().get(backendName);
		if (def == null)
			throw new com.sunnada.kernel.exception.BaseException("系统没有定义后台服务:" + backendName);

		Backend backend = findOrcreateInstance(def);
		if (!backend.isStop()) {
			logger.debug("后台服务" + backendName + "正在停止");
			backend.stop();
			logger.debug("后台服务" + backendName + "成功停止");

		}
	}
	/**
	 * 停止所有的后台服务
	 */
	public void stopBackends() {

		logger.debug("开始停止Backend后台服务");
		Map<String,BackendDef> backendDefs = DreamConf.getBackendDefs();
		for (Iterator<BackendDef> iter = backendDefs.values().iterator(); iter.hasNext();) {
			BackendDef backendDef = (BackendDef) iter.next();
			
			Backend backend = findOrcreateInstance(backendDef);
			if (!backend.isStop()) {

				backend.stop();
			}

		}

	}


}
