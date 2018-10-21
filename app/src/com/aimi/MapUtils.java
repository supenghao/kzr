package com.aimi;

import java.lang.reflect.Field;
import java.util.*;

public class MapUtils {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object mapToObject(Map map, Class<?> beanClass) throws Exception {
		if (map == null)
			return null;
		Object obj = beanClass.newInstance();
		//org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		return obj;
	}


	/**
	 *  获取对象所有属性
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> objectToMap(Object obj) {
		List<Field> fieldList = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		Class tempClass = obj.getClass();
		// 获取f对象对应类中的所有属性域
		while (tempClass != null) {// 当父类为null的时候说明到达了最上层的父类(Object类).
			fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			tempClass = tempClass.getSuperclass(); // 得到父类,然后赋给自己
		}
		try {
			for (Field f : fieldList) {
				f.setAccessible(true);
				map.put(f.getName(), f.get(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
