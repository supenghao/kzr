package com.dhk.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.dhk.api.tool.M;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 *
 * @ClassName CacheManager
 * @Description 继承了 spring 的 AbstractCacheManager 管理 RedisCache 类缓存管理
 * @author K
 * @Date 2016年6月27日 下午1:55:49
 * @version 1.0.0
 * @param <T>
 */
@Component("cacheManager")
public class RedisCacheManager<T extends Object> extends AbstractCacheManager {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Properties ps = loadProperties("/cache.properties");
        Collection<RedisCache> cache = new ArrayList<RedisCache>();
        if (ps != null) {
            Set<Object> keys = ps.keySet();
            for (Object key : keys) {
                String keyStr = (String) key;
                cache.add(new RedisCache(keyStr,Long.parseLong(ps.getProperty(keyStr)),redisTemplate));
            }
            return cache;
        }
        return Collections.emptyList();
    }


    public static Properties loadProperties(String resource) {
        Properties properties = new Properties();

        try {
            InputStream is = RedisCacheManager.class.getResourceAsStream(resource);
            if(is == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                is = classLoader.getResourceAsStream(resource);
            }

            if(is != null) {
                properties.load(is);
                is.close();
            }
        } catch (IOException var4) {
            M.logger.info("找不到配置文件:" + resource);
        }

        return properties;
    }
}  