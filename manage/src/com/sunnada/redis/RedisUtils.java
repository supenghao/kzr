package com.sunnada.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;

@Component
public class RedisUtils {
    //操作redis客户端
    private static Jedis jedis;
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    public  void setObject(String key,Object object){
        getJedis().set(key.getBytes(), SerializationUtils.serialize(object));
    }
    public  void setObject(String key,Object object,int liveTime){
        getJedis().set(key.getBytes(), SerializationUtils.serialize(object));
        getJedis().expire(key.getBytes(), liveTime);
    }

    public  void set(String key,String value){
        getJedis().set(key, value);
    }

    public  void del(String key){
        getJedis().del(key);
    }
    public  void set(String key,String value,int liveTime){
       getJedis().set(key, value);
       getJedis().expire(key, liveTime);
    }


    public  String get(String key){
       return  getJedis().get(key);
    }

    public  Object getObject(String key){
        return  SerializationUtils.deserialize(getJedis().get(key.getBytes()));
    }

    

    public void setList(String key,Object object){
        jedis.lpush(key.getBytes(), SerializationUtils.serialize(object));
    }

    /**
     *
     * @param lockKey
     * @param waitTime
     * @param expireTime
     * @return
     */
    public boolean lock(String lockKey, int waitTime,int expireTime) {
        boolean lockSuccess = false;
        Jedis jedis = null;
        try{
            jedis = getJedis();
            long start = System.currentTimeMillis();
            do{
                long result = jedis.setnx(lockKey, "1");
                if(result == 1){
                    jedis.expire("lockKey",expireTime);
                    lockSuccess = true;
                    break;
                }
                //如果不等待，则直接返回
                if(waitTime == 0){
                    break;
                }
                //等待300ms继续加锁
                Thread.sleep(300);
            }while((System.currentTimeMillis()-start) < waitTime*1000);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(jedis!=null&&!lockSuccess&&jedis.ttl(lockKey)==-1){    //防止死锁
                jedis.expire("lockKey",expireTime);
            }
        }

        return lockSuccess;
    }

    public void unLock(String lockKey) {
        Jedis jedis = getJedis();
        try{
            jedis.del(lockKey);
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
    }


    /**
     * 获取一个jedis 客户端
     * @return
     */
    public Jedis getJedis(){
        jedisConnectionFactory.getShardInfo();
        return jedisConnectionFactory.getShardInfo().createResource();
    }


}
