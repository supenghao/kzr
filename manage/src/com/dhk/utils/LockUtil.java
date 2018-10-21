package com.dhk.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;

public class LockUtil {
	
	private static final Logger log= LogManager.getLogger();


	public  static int lock(String key, int second,Jedis jedis) {
		try{
			long cnt = jedis.incr(key.getBytes());
			if(cnt==1){
				jedis.expire(key.getBytes(), second);
				return 1;
			}	
			return 0;
		}	catch(Exception e){
				log.error("lock connect to Redis error 1: "+e.getMessage());
				log.error("lock connect to Redis error 1: ",e);
				return 0;
		}
	}

	public static void unlock(String key,Jedis jedis) {
		try{
			jedis.del(key.getBytes());
		}
		catch(Exception e){
			log.error("lock connect to Redis error 2:"+e.getMessage());
			log.error("lock connect to Redis error 2: ",e);
		}
	}
}
