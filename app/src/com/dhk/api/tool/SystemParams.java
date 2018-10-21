package com.dhk.api.tool;

import java.util.ResourceBundle;

/**
 * 获取系统运行配置文件类
 */
public class SystemParams {

	private static String DEFAULT_FILE = "sys";
	private static ResourceBundle DEFAULT_BUNDLE = null;

	public static String get(String key) {
		if (DEFAULT_BUNDLE == null) {
			DEFAULT_BUNDLE = ResourceBundle.getBundle(DEFAULT_FILE);
		}
		if (DEFAULT_BUNDLE.containsKey(key)) {
			return DEFAULT_BUNDLE.getString(key);
		} else {
			throw new RuntimeException("在配置文件(" + DEFAULT_FILE + ")中未找到'" + key
					+ "'的值.");
		}
	}

	public static void clear() {
		if (DEFAULT_BUNDLE != null) {
			ResourceBundle.clearCache();
			DEFAULT_BUNDLE = null;
		}
	}
}
