package com.dhk.api.service.impl;


import com.dhk.api.entity.APPUser;
import com.dhk.api.dao.IAPPUserDao;
import com.dhk.api.service.IAPPUserService;
import com.xdream.kernel.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
		String time= StringUtil.getCurrentDateTime("yyMMddHHmmss");
		long uniqueId=0;
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			uniqueId = jedis.incr("uniqueId");
			if(uniqueId>100000){
				jedis.set("uniqueId","0");
			}
		}catch (Exception e){
			uniqueId=new Random().nextInt(100000);
			e.printStackTrace();
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}
		return "W"+time+uniqueId;
		
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

	public int updateImageUrl(String imageType,Long userId,String imageUrl) throws Exception{
		int i = 0;
		if ("front".equals(imageType)){
			i = updateIdFrontUrl(userId,imageUrl);
		}else if ("side".equals(imageType)){
			i = updateOppositeUrl(userId,imageUrl);
		}else if ("hand".equals(imageType)){
			i = updateIdHandUrl(userId,imageUrl);
		}else if ("bankCard".equals(imageType)){
			i = updateBankPicUrl(userId,imageUrl);
		}
		return i;
	}

	public int updateIdFrontUrl(Long userId,String imageUrl) throws Exception{
		String sql = "update t_s_user set ID_FRONT_URL=:idFrontUrl where ID=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("idFrontUrl", imageUrl);
		return appUserDao.update(sql, map);
	}
	public int updateOppositeUrl(Long userId,String imageUrl) throws Exception{
		String sql = "update t_s_user set ID_OPPOSITE_URL=:idOppositeUrl where ID=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("idOppositeUrl", imageUrl);
		return appUserDao.update(sql, map);
	}
	public int updateIdHandUrl(Long userId,String imageUrl) throws Exception{
		String sql = "update t_s_user set ID_HAND_URL=:idHandUrl where ID=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("idHandUrl", imageUrl);
		return appUserDao.update(sql, map);
	}
	public int updateBankPicUrl(Long userId,String imageUrl) throws Exception{
		String sql = "update t_s_user set BANK_PIC_URL=:bankPicUrl where ID=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("bankPicUrl", imageUrl);
		return appUserDao.update(sql, map);
	}
	
	public static void main(String[] args) {
		for (int i=0;i<100;i++){
			System.out.println(new Random().nextInt(100000));
		}
	}


}
