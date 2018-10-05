package com.dhk.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;

import com.sunnada.kernel.DreamConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dhk.dao.IAPPUserDao;
import com.dhk.entity.APPUser;
import com.dhk.service.IAPPUserService;
import com.sunnada.kernel.util.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("APPUserService")
public class APPUserService implements IAPPUserService {

	@Resource(name = "APPUserDao")
	private IAPPUserDao appUserDao;

	@Autowired
	JedisPool jedisPool;


	/**
	 * 获得订单号
	 * 
	 * @param userId
	 *            app用户ID
	 * @param phoneNum
	 *            app用户电话号码
	 * @return 订单号
	 */
	public String getOrderNo(long userId, String phoneNum) {
		String time=StringUtil.getCurrentDateTime("yyMMddHHmmss");
		long uniqueId=0;
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			uniqueId = jedis.incr("uniqueId");
			long temp= uniqueId;
			if(uniqueId<=50){//有可能出现并发的时候会产生重复的 uniqueId    ，比如0001有多个
				uniqueId = jedis.incr("uniqueId2");
			}
			if(temp>89999){
				jedis.set("uniqueId","0");
			}
			if(45000<temp&&temp<45010){
				jedis.set("uniqueId2","0");
			}
		}catch (Exception e){
			uniqueId=new Random().nextInt(89999);
			e.printStackTrace();
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}

		return "K"+time+String.format("%05d", uniqueId);
		
	}
	public APPUser findById(long id) {
		APPUser user = null;
		String sql ="select * from  t_s_user where id=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		try {
			user = appUserDao.findBy(sql, map);
		} catch (Exception e) {
			return null;
		}
		return user;
	}

	public static void main(String[] args) {


		for (int i=0;i<8888;i++){
			System.out.println(String.format("%04d", i));
		}
	}


}
