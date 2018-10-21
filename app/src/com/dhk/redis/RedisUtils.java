package com.dhk.redis;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/*
 repayPlanZset  还款计划zset
 repayPlanQueue 还款计划queue
 repayPlanLock  还款计划锁
 repayCostZset  消费计划zset
 repayCostLock  消费计划锁
 repayCostQueue 消费计划队列
 repayplan_tem_ 还款计划临时表

 repayUndefined_20170905   返回未知错误的表  t_n_repay_cost、t_n_repay_plan的记录都存在里面
 repayUndefinedQueue
 taskIsRuning_20170905  弄上日期  方便过期处理  任务是否在执行中
 */
@Component
public class RedisUtils {
    //操作redis客户端
    @Autowired
    private JedisPool jedisPool;

    public  void set(String key,String value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            jedis.set(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }
    public  void del(String key){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }
    public  void set(String key,String value,int liveTime){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            jedis.set(key, value);
            jedis.expire(key, liveTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }
    public  String get(String key){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }
    /**
     *
     * 默认是永久时间
     * @param key
     * @param score
     * @param data
     */
    public void zadd(String key,double score,JSONObject data){

        Jedis jedis = null;
        try{
            jedis = getJedis();
            jedis.zadd(key,score,data.toString());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }


    }

    /**
     * 获取一个jedis 客户端
     * @return
     */
    public  Jedis getJedis(){
            return jedisPool.getResource();
    }



}
