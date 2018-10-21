package com.dhk.quartz;

import com.dhk.entity.Settle;
import com.dhk.entity.TransWater;
import com.dhk.service.IOrgService;
import com.dhk.service.ISettleService;
import com.dhk.service.ITransWaterService;
import com.dhk.utils.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 代理商分润结算
 */
public class SettleTask {
	
	@Autowired
	JedisPool jedisPool;

	@Resource(name = "SettleService")
	private ISettleService settleService;

	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;

	@Resource(name = "OrgService")
	private IOrgService orgService;

	private static int count =0;
	

	private static final Logger log= LogManager.getLogger();
	public void runTask(){
		List<TransWater> transWaters=transWaterService.findUnSettedTransWaters();
		try {
			settleService.doSettle(transWaters);
			log.info("执行代理商分润成功");
		} catch (Exception e) {
			log.error("执行代理商分润错误："+e.getMessage());
			log.error("执行代理商分润错误："+e.toString());
			e.printStackTrace();
		}

		log.info("分润执行了："+count);
		if(count>=5){
			count=0;
			log.info("开始执行分润任务写入余额");

			Jedis jedis = null;
			try{
				jedis = jedisPool.getResource();
				Map<String,String> settleMap = jedis.hgetAll("settleMap");
				Map<String,String> settleCountMap = jedis.hgetAll("settleCountMap");
				jedis.del("settleCountMap");
				for (Map.Entry<String, String> entry : settleMap.entrySet()){
					Long orgId = Long.parseLong(entry.getKey());
					jedis.hdel("settleMap",orgId+"");
					// 生成还款消费计划
					BigDecimal balance = new BigDecimal(entry.getValue()).setScale(2,BigDecimal.ROUND_HALF_UP);
					String transCount = settleCountMap.get(orgId+"");
					if(transCount==null)transCount="0";
					Settle settle = settleService.findByOrgIdAndCurDate(orgId);
					if (settle == null) {
						settle = new Settle();
						settle.setOrgid(orgId);
						settle.setBalance(balance);
						settle.setSettledate(DateTimeUtil.getNowDateTime("yyyyMMdd"));
						settle.setOrgRelationNo("");
						settle.setTransCount(Integer.parseInt(transCount));
						settle.setStatus("1");
						settleService.doInsertSettle(settle);
						// 更新代理商余额
						orgService.seriUpdateBalance(orgId, balance);
					} else {
						settleService.doUpdateSettle(balance,Integer.parseInt(transCount),settle.getId());
						// 更新代理商余额
						orgService.seriUpdateBalance(orgId, balance);
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}finally {
				if (jedis!=null){
					jedis.close();
				}
			}
		}else{
			count++;
		}
	}
	
}
