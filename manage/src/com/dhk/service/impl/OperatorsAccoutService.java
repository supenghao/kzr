package com.dhk.service.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhk.dao.IOperatorsAccoutDao;
import com.dhk.entity.OperatorsAccout;
import com.dhk.service.IOperatorsAccoutService;
import com.sunnada.kernel.sql.SQLConf;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("operatorsAccoutService")
public class OperatorsAccoutService implements IOperatorsAccoutService {
	@Resource(name = "operatorsAccoutDao") 
	private IOperatorsAccoutDao operatorsAccoutDao;
	@Autowired
	JedisPool jedisPool;

	public void doUpdateCredit(BigDecimal cost) {
		cost.setScale(2, RoundingMode.HALF_UP);
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.lpush("operatorsAccoutQueue",cost.toString());
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}
	}

	
	public OperatorsAccout find(){
		String sql=SQLConf.getSql("operatorsaccout", "find");
		
		try {
			return operatorsAccoutDao.findBy(sql, null);
		} catch (Exception e) {
			return null;
		}
		
	}




}

