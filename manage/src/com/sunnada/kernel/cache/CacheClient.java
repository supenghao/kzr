package com.sunnada.kernel.cache;

public interface CacheClient {
	/**
	 * 无限期
	 */
	public static int defaultExpireTime = 0; 
	/**
	 * 根据Class信息获取简单对象
	 * 
	 * @param key
	 * @param cls
	 * @return
	 */
	<T> T get(String key);  
	/**
	 * 删除对象
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	boolean delete(String key) throws Exception;
	/**
	 * 清空缓存
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	boolean flushAll() throws Exception;
	/**
	 * 多少秒后清空缓存
	 * 
	 * @param delay
	 * @return
	 * @throws Exception
	 */
	boolean flushAll(int delay) throws Exception;
	/**
	 * 新增对象,采用默认过期时间
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	<T> boolean add(String key, T value) throws Exception;
	/**
	 * 新增对象并设置过期时间，如果不过期，则expiry需设置为0
	 * 
	 * @param key
	 * @param value
	 * @param expiry
	 * @return
	 * @throws Exception
	 */
	<T> boolean add(String key, T value, int expiry) throws Exception;
	/**
	 * 设置对象，如果对象存在则更新，不存在则新增 ,采用默认过期时间
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	<T> boolean set(String key, T value) throws Exception;
	/**
	 * 设置对象，如果对象存在则更新，不存在则新增,并重置超时时间（毫秒） ，如果不过期，则expiry需设置为0
	 * 
	 * @param key
	 * @param value
	 * @param expiry
	 * @return
	 * @throws Exception
	 */
	<T> boolean set(String key, T value, int expiry) throws Exception;
}

