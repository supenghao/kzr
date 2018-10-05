package com.dhk.quartz;

import com.alibaba.fastjson.JSONObject;
import com.dhk.ExecutorServiceUtil;
import com.dhk.dao.IRepayRecordDao;
import com.dhk.entity.RepayCost;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 帅 on 2017/8/23.
 */
@Component
public class RepayCostTask implements ApplicationListener<ContextRefreshedEvent> {

    @Resource(name = "CreditCardCostService")
    private ICreditCardCostService creditCardCostService;

    @Resource(name = "CreditCardService")
    private ICreditCardService creditCardService;

    @Resource(name = "RepayPlanDetailService")
    private IRepayPlanDetailService repayPlanDetailService;

    @Resource(name = "repayCostService")
    IRepayCostService repayCostService;
	@Resource(name = "repayRecordDao")
	private IRepayRecordDao repayRecordDao;
	@Resource(name = "repayRecordService")
	private IRepayRecordService repayRecordService;

    @Autowired
	JedisPool jedisPool;

	private static final Logger log= LogManager.getLogger();


    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() != null){
                 return;   //防止调用2次
        }

        ExecutorServiceUtil.cachedThreadPool.execute(new Runnable() {
            public void run() {
				log.info("----------------repayCost开始消费---------------");
				Jedis jedis =jedisPool.getResource();
				while(true){
					try {
						List<String> temp = jedis.blpop(0,"repayCostQueue");
						if(temp!=null){
							final RepayCost repayCost = JSONObject.parseObject(temp.get(1),RepayCost.class);
							log.info("开始处理repayPlanCost任务，id={}",repayCost.getId());
                            final  RepayPlanDetail repayPlanDetail= repayPlanDetailService.findRepayPlanById(repayCost.getRepay_plan_id());//先通过数据库获取，如果慢的话可以也缓存在redis中

							ExecutorServiceUtil	.cachedThreadPool.execute(new Runnable() {
								public void run() {
                                    //creditCardCostService.creditRepayCost(repayPlanDetail, repayCost);
                                    creditCardCostService.creditDateRepayCost(repayPlanDetail, repayCost); //资金不过夜模式

								}
							});
						}
					}catch (Exception e){
						e.printStackTrace();
						try {
							Thread.sleep(1000);
						}catch (Exception ex){

						}
					}
				}
            }
        });
    }
}
