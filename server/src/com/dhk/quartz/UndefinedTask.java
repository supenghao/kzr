package com.dhk.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dhk.ExecutorServiceUtil;
import com.dhk.entity.TransWater;
import com.dhk.service.ICallBackService;
import com.dhk.service.ITransWaterService;
import com.dhk.service.impl.HxtcService;
import com.dhk.service.impl.YblService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by 帅 on 2017/8/23.
 */
@Component
public class UndefinedTask implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
	JedisPool jedisPool;

	private static final Logger log= LogManager.getLogger();
    @Resource(name = "TransWaterService")
    private ITransWaterService transWaterService;
    @Resource(name = "callBackService")
    private ICallBackService callBackService;
    @Resource(name = "hxtcService")
    private HxtcService hxtcService;
    @Resource(name = "yblService")
    private YblService yblService;
    
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() != null){
                 return;   //防止调用2次
        }

        ExecutorServiceUtil.cachedThreadPool.execute(new Runnable() {
            public void run() {
				log.info("UndefinedTask开始启动消费 ");
				Jedis jedis =jedisPool.getResource();
				while(true){
					try {
						List<String> temp = jedis.brpop(0,"UndefinedTask");
						if(temp!=null){
							new Thread(new Runnable(){  
					            public void run(){  
					            	try {
									JSONObject jsonObject = JSONObject.parseObject(temp.get(1));

									String orderNo = jsonObject.getString("orderNo");
									String translsId = jsonObject.getString("translsId");
									long time = jsonObject.getLongValue("time");
									int count = jsonObject.getIntValue("count");
		                            log.info("消费："+jsonObject);
									long currentTime=System.currentTimeMillis();
		                            System.out.println(currentTime);
		                            if(time-currentTime>0){
									    log.info("开始休眠："+(time-currentTime));
											Thread.sleep(time-currentTime+10);
		                                log.info("开始休眠结束");
									}

		                            TransWater transWater =transWaterService.findById(Long.parseLong(translsId));
		                            String transType = transWater.getTrans_type();
		                            if (!"9997".equals(transWater.getRespCode())){
		                                log.info("该订单已处理："+orderNo);
		                                return;
		                            }
		                            JSONObject retJson = new JSONObject();
		                            if("1".equals(transType)||"4".equals(transType)){
		                               // retJson= hxtcService.findOrder(transWater.getHostTransDate(),orderNo);
		                            	retJson= yblService.findOrder(transWater.getHostTransDate(),orderNo);
		                            }else if("2".equals(transType)||"5".equals(transType)){
		                                retJson= hxtcService.findWithdraw(transWater.getHostTransDate(),orderNo);
		                            }else{
		                                log.info("transType异常："+transType);
		                                return;
		                            }
		                            log.info("调用查询返回："+retJson);
		                            if(retJson==null||"9997".equals(retJson.getString("code"))){
		                                if(count<10){
		                                    count++;
		                                    jsonObject.put("count",count);
		                                    jsonObject.put("time",time+186000);  //一分钟后再查询
		                                    log.info("放回队列继续执行："+jsonObject);
		                                    Jedis jedis1 =jedisPool.getResource();
		                                    try {
		                                    jedis1.lpush("UndefinedTask",jsonObject.toJSONString());
		                                } finally {
		                                	  //这里很重要，一旦拿到的jedis实例使用完毕，必须要返还给池中
		                                	if (jedis1 != null) {
		                                		jedis1.close();
		                                		}
		                                	}
		                                    return;
		                                }
		                            }
		                            String code=retJson.getString("code");
		                            String message=retJson.getString("message");

		                            //0:纯消费，1：还款消费，2：快速还款，3：普通还款,4:充值，5,：提现
		                            if("4".equals(transType)){
		                                callBackService.czhd(code,message,transWater);
		                            }else if("5".equals(transType)){//提现     有区代理商提现，用户提现
		                                callBackService.txhd(code,message,transWater);
		                            }else if("2".equals(transType)){//还款
		                                //callBackService.repay(code,message,transWater);
		                                callBackService.repayDate(code,message,transWater); //资金不过夜

		                            }else if("1".equals(transType)){
		                                //callBackService.repayCost(code,message,transWater);
		                            	callBackService.repayCostDate(code,message,transWater);
		                            }else if("0".equals(transType)){
		                                callBackService.cost(code,message,transWater);
		                            }
					            	}catch (Exception e){
										e.printStackTrace();
										 log.error("UndefinedTask处理异常", e);
									}
						   }}).start();
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
