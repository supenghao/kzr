package com.dhk.api.tool;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeenUtil {

	/**
	 * 将been装换成map
	 * 
	 * @param been
	 * @return
	 */
	public static <T> Map<String, String> been2Map(T been) {
		@SuppressWarnings("unchecked")
		Class<T> c = (Class<T>) been.getClass();
		Field[] fields = c.getDeclaredFields();
		Map<String, String> ret = new HashMap<String, String>();
		for (int i = 0; fields != null && i < fields.length; i++) {
			fields[i].setAccessible(true); // 暴力反射
			String column = fields[i].getName();
			Object ov = null;
			try {
				ov = fields[i].get(been);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("传入对象不匹配!");
			}
			if (column != null && ov != null) {
				ret.put(column, ov.toString());
			}
		}
		return ret;
	}
}
