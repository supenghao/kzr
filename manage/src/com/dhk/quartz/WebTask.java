package com.dhk.quartz;

import com.alibaba.fastjson.JSONObject;
import com.dhk.dao.IOperatorsAccoutDao;
import com.sunnada.kernel.sql.SQLConf;
import com.sunnada.kernel.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 把zset的任务放到队列中
 *  repayPlanZset—>  repayPlanQueue
 *  repayCostZset—>  repayCostQueue
 */
@Component
public class WebTask {
    @Autowired
    JedisPool jedisPool;

    @Autowired
    private RedissonClient redissonClient;


    @Resource(name = "operatorsAccoutDao")
    private IOperatorsAccoutDao operatorsAccoutDao;

    private static final Logger log= LogManager.getLogger();

    //  把repayPlanZset的任务放到队列中      20秒执行一次
    @Scheduled(cron = "0/7 * * * * ?")
    public void repayPlan() {

        Jedis jedis = jedisPool.getResource();
        RLock fairLock = redissonClient.getFairLock("repayPlanLock");
        boolean res=false;
        try {
            res = fairLock.tryLock(0, 15, TimeUnit.SECONDS);
            if(res){
                String currentDate = StringUtil.getCurrentDateTime("yyyyMMdd");
                String curTime = StringUtil.getCurrentDateTime("HHmmss");

                if(Integer.parseInt(curTime)>225500){     //防止最后几秒钟的订单没被扫描到
                    curTime="240000";
                }   

//                curTime="240000";
//                currentDate="20170930";
                String key = "repayPlanZset_"+ currentDate;
                Set<String> set =  jedis.zrangeByScore(key,1L,Integer.parseInt(curTime));
                if(set.size()>0){
                    long count = jedis.zremrangeByScore(key,1L,Integer.parseInt(curTime));   //要保证新加的订单 不再这个时间段内
                    if(count!=set.size()){//出现异常，正常情况下不可能出现不一致的情况，除非服务器的时间差距超过5分钟
                        log.error("处理repayPlanZset出现问题，请检查服务器时间时候一致");
                        fairLock.expire(20,TimeUnit.SECONDS); //设置超时20秒
                        Thread.sleep(5000);
                        for(String repayPlanStr:set){
                            JSONObject  repayPlanJson = JSONObject.parseObject(repayPlanStr);
                            String execTime = repayPlanJson.getString("exec_time").replaceAll(":","");
                            jedis.zadd(key, Long.parseLong(execTime), repayPlanJson.toJSONString());
                        }
                        set =  jedis.zrangeByScore(key,1L,Integer.parseInt(curTime));
                        if(set.size()>0){
                            jedis.zremrangeByScore(key,1L,Integer.parseInt(curTime));
                            jedis.lpush("repayPlanQueue", set.toArray(new String[]{}));
                        }
                    }else{
                        jedis.lpush("repayPlanQueue", set.toArray(new String[]{}));
                        jedis.expire("randomhour_"+currentDate,172800);//2天
                        jedis.expire("repayPlanZset_"+currentDate,172800);//2天

                        log.info("repayPlanZset处理成功,执行{}调任务", set.size());
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if(res) fairLock.unlock();
            if(jedis!=null)jedis.close();
        }

    }

    //  把repayCostZset的任务放到队列中      20秒执行一次
    @Scheduled(cron = "0/7 * * * * ?")
    public void repayCost() {
        Jedis jedis = jedisPool.getResource();
        RLock fairLock = redissonClient.getFairLock("repayCostLock");
        boolean res =false;
        try{
            String currentDate = StringUtil.getCurrentDateTime("yyyyMMdd");
            res = fairLock.tryLock(0, 15, TimeUnit.SECONDS);
            if(res){
                String curTime = StringUtil.getCurrentDateTime("HHmmss");
//                curTime="240000";
//                currentDate="20170930";
                if(Integer.parseInt(curTime)>235900){     //防止最后几秒钟的订单没被扫描到
                    curTime="240000";
                }
                Set<String> set =  jedis.zrangeByScore("repayCostZset_"+currentDate,1L,Integer.parseInt(curTime));
                if(set.size()>0){
                    jedis.zremrangeByScore("repayCostZset_"+currentDate,1L,Integer.parseInt(curTime));     //要保证新加的订单 不再这个时间段内
                    jedis.lpush("repayCostQueue", set.toArray(new String[]{}));
                    log.info("repayCostZset处理成功,执行{}调任务", set.size());
                    jedis.expire("repayCostZset_"+currentDate,172800);//2天
                }

            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();

        }finally {
           if(res) fairLock.unlock();
           if(jedis!=null) jedis.close();
        }
    }

    //  更新运营商账户的金额          5分钟执行一次
    @Scheduled(cron = "0 0/5 * * * ?")
    public void updateOperatorsAccout() {
        Jedis jedis = jedisPool.getResource();
        try {
                BigDecimal allCost =  new BigDecimal(0);
                while (true){
                    String cost = jedis.rpop("operatorsAccoutQueue");
                    if(cost==null){
                        break;
                    }
                    BigDecimal bigDecimal =  new BigDecimal(cost);
                    allCost = allCost.add(bigDecimal);
                }
                if(allCost.doubleValue()!=0){
                    String sql= SQLConf.getSql("operatorsaccout", "updateCredit");
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("creditcard_balance", allCost);
                    operatorsAccoutDao.update(sql, map);
                    log.info("updateOperatorsAccout处理成功,添加金额为{}", allCost);
                }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }finally {
           if(jedis!=null)jedis.close();
        }

    }

    public static void main(String[]args){
        String curTime="235911";
        if(Integer.parseInt(curTime)>235900){     //防止最后几秒钟的订单没被扫描到
            curTime="240000";
            System.out.println(curTime);
        }
    }

}