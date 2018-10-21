package com.dhk.redis;

import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;


public class RedisCache  implements org.springframework.cache.Cache  {

    /**
     * Redis
     */

    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存名称
     */
    private String name;

    /**
     * 超时时间
     */
    private long timeout;


    public RedisCache(String name,long timeout,RedisTemplate redisTemplate){
        this.name=name;
        this.timeout=timeout;
        this.redisTemplate=redisTemplate;
    }


    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public Object getNativeCache() {
        // TODO Auto-generated method stub
        return this.redisTemplate;
    }


    @Override
    public ValueWrapper get(Object key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        } else {
            final String finalKey;
            finalKey =  getKey(key);
            Object object = null;
            object = redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] key = finalKey.getBytes();
                    byte[] value = connection.get(key);
                    if (value == null) {
                        return null;
                    }
                    return SerializationUtils.deserialize(value);
                }
            });
            return (object != null ? new SimpleValueWrapper(object) : null);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Class<T> type) {
        if (StringUtils.isEmpty(key) || null == type) {
            return null;
        } else {
            final String finalKey;
            final Class<T> finalType = type;
            if (key instanceof String) {
                finalKey = (String) key;
            } else {
                finalKey = key.toString();
            }
            final Object object = redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] key = finalKey.getBytes();
                    byte[] value = connection.get(key);
                    if (value == null) {
                        return null;
                    }
                    return SerializationUtils.deserialize(value);
                }
            });
            if (finalType != null && finalType.isInstance(object) && null != object) {
                return (T) object;
            } else {
                return null;
            }
        }
    }

    @Override
    public void put(final Object key, final Object value) {



        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        } else {
            final String finalKey;

            finalKey =  getKey(key);

            if (!StringUtils.isEmpty(finalKey)) {
                final Object finalValue = value;
                redisTemplate.execute(new RedisCallback<Boolean>() {
                    @Override
                    public Boolean doInRedis(RedisConnection connection) {
                        connection.set(finalKey.getBytes(), SerializationUtils.serialize(finalValue));
                        // 设置超时间
                        connection.expire(finalKey.getBytes(), timeout);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return null;
    }

    /*
     * 根据Key 删除缓存
     */
    @Override
    public void evict(Object key) {
        if (null != key) {
            final String finalKey;
            finalKey =  getKey(key);
            if (!StringUtils.isEmpty(finalKey)) {
                redisTemplate.execute(new RedisCallback<Long>() {
                    public Long doInRedis(RedisConnection connection) throws DataAccessException {
                        return connection.del(finalKey.getBytes());
                    }
                });
            }
        }
    }

    /*
     * 清楚系统缓存
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub
        // redisTemplate.execute(new RedisCallback<String>() {
        // public String doInRedis(RedisConnection connection) throws DataAccessException {
        // connection.flushDb();
        // return "ok";
        // }
        // });
    }

    private String getKey(Object keyParam) {
        return keyParam + "@_@" + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
