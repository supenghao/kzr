package com.dhk.quartz;

import com.dhk.ExecutorServiceUtil;
import com.dhk.dao.IRepayRecordDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.util.List;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.service.ICreditCardRepayService;
import com.dhk.service.ICreditCardService;
import com.dhk.service.IRepayRecordService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Created by 帅 on 2017/8/23.
 */
@Component
public class RepayPlanTask implements ApplicationListener<ContextRefreshedEvent> {

    @Resource(name="CreditCardRepayService")
    private ICreditCardRepayService creditCardRepayService;

    @Resource(name = "CreditCardService")
    private ICreditCardService creditCardService;

    @Resource(name = "repayRecordDao")
    private IRepayRecordDao repayRecordDao;

    @Resource(name = "repayRecordService")
    private IRepayRecordService repayRecordService;
    @Autowired
    JedisPool jedisPool;
    private static final Logger log= LogManager.getLogger();


    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() != null){
                 return;
        }

        ExecutorServiceUtil.cachedThreadPool.execute(new Runnable() {
            public void run() {
                log.info("------------------repay开始消费------------------- ");
                Jedis jedis =jedisPool.getResource();
				while(true){
					try {
						List<String> temp = jedis.blpop(0,"repayPlanQueue");
						if(temp!=null){
							final RepayPlanDetail repayPlanDetail = JSONObject.parseObject(temp.get(1),RepayPlanDetail.class);
                            log.info("开始处理repayPlan任务，id={}",repayPlanDetail.getId());

							ExecutorServiceUtil	.cachedThreadPool.execute(new Runnable() {
								public void run() {
									//creditCardRepayService.creditRepay(repayPlanDetail);
									creditCardRepayService.creditRepayDate(repayPlanDetail); //资金不过夜模式
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
