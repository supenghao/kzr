package com.dhk.kernel;

import java.io.File;

/**
 * 缺省的应用服务器上下文实现
 * @author Administrator
 * 
 */
public class DefaultAppContext implements AppContext {

	private File homePath = null;

	public DefaultAppContext() {
		String s = System.getProperty(DreamConstant.DREAM_HOME);
		if (s != null)
			homePath = new File(s);
		else
			homePath = new File(new File("").getAbsolutePath());
	}

	/**
	 * 根据某个抽象路径取物理路径
	 */
	public String getRealPath(String pathName) {
		// if (pathName.length()>0&&pathName.charAt(0)=='/')
		// pathName = pathName.substring(1);
		return new File(homePath, pathName).getAbsolutePath();
		// if (homePath != null)
		// return homePath + pathName;
		// else
		// return pathName;
	}

}

