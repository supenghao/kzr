package com.aimi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSONObject;
import com.dhk.api.entity.RepayCost;
import com.dhk.api.entity.RepayPlan;
import com.mysql.fabric.xmlrpc.base.Array;
 

public class RedisUtils {
	private static String HOST = "106.14.140.21";
	private static int PORT = 16379;
//	private static String HOST = "127.0.0.1";
//	private static int PORT = 6379;
	private static int MAX_IDLE = 200;
	private static int REDIS_DB = 0;
	public static int REDIS_SY_EXPIRE = 1440;
	private static JedisPool jedisPool = null;
	private final static String REDIS_SERTYPE_TYPE_STANDALONE = "1";// 单点
	private final static String REDIS_SERTYPE_TYPE_CLUSTER = "2";// 集群
//	private static String SERVER_TYPE = REDIS_SERTYPE_TYPE_STANDALONE;// 默认为单点模式

	/*
	 * 初始化redis连接池
	 */
	private static void initPool() {
		try {
			 
			JedisPoolConfig config = new JedisPoolConfig();
			 
			config.setMaxIdle(MAX_IDLE);// 最大空闲连接数
			jedisPool = new JedisPool(config, HOST, PORT);
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 获取jedis实例
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool == null) {
				initPool();
			}
			Jedis jedis = jedisPool.getResource();
			jedis.auth("kzr@123");
			jedis.select(REDIS_DB);
			
			return jedis;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public static void returnResource(JedisPool pool, Jedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

	/**
	 * 设置key,value的值
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
		} finally {
			// 返还到连接池
			returnResource(jedisPool, jedis);
		}
	}
	
	/**
	 * 设置key,value的值
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value,int time) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
			jedis.expire(key, time);
		} catch (Exception e) {
			e.printStackTrace();
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
		} finally {
			// 返还到连接池
			returnResource(jedisPool, jedis);
		}
	}

	/**
	 * 获取key的值
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("finally")
	public static String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = getJedis();
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			// 释放redis对象
			jedisPool.returnBrokenResource(jedis);
		} finally {
			// 返还到连接池
			returnResource(jedisPool, jedis);
			return value;
		}
	}

	public static void setJh(){
		RepayPlan rep=new RepayPlan();
		List<RepayCost> repayCostList=new ArrayList<RepayCost>();
		String exec="17:10:46";
		RepayCost cost = new RepayCost();
		//cost.setRepay_plan_id(Long.valueOf(592496));
		cost.setExec_time(exec);// 设置执行时间
//		cost.setStatus("0");
		cost.setCost_amount(Double.valueOf(100));
		repayCostList.add(cost);
		rep.setId(Long.valueOf(592496));
		rep.setOrderNo("PL9B6641617164003965420531615744");
		rep.setRepay_amount(Double.valueOf(97));
		rep.setUser_id("28924");
		rep.setCredit_card_no("6259063515694381");
		rep.setExec_time(exec);
		rep.setRecord_id(Long.valueOf("77995"));
		rep.setRepay_day("20180707");
		rep.setRepayCostList(repayCostList);
		rep.setStatus("0");
		 
		Jedis jedis = getJedis();
		jedis.zadd("repayPlanZset_"+"20180706", Long.parseLong(exec.replaceAll(":","")),  JSONObject.toJSON(rep).toString());
	}
	
	public static void setcostJh(){
		 
		String exec="18:40:46";
		RepayCost cost = new RepayCost();
		cost.setRepay_plan_id(Long.valueOf(592806));
		cost.setExec_time(exec);// 设置执行时间
        cost.setStatus("0");
		cost.setCost_amount(Double.valueOf(100));
		cost.setId(Long.valueOf(862826));
		Jedis jedis = getJedis();
		jedis.zadd("repayCostZset_"+"20180708", Long.parseLong(exec.replaceAll(":","")),  JSONObject.toJSON(cost).toString());
	}

	 
	public static void main(String[] args) {
		// 连接本地的 Redis 服务
		setcostJh();
	}
}