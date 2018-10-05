package com.dhk.controller;


import com.aimi.demo.common.AimiConfig;
import com.aimi.demo.utils.AESUtil;
import com.aimi.demo.utils.Signature;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dhk.entity.TransWater;
import com.dhk.service.ITransWaterService;
import com.dhk.service.ICallBackService;
import com.dhk.utils.Disguiser;
import com.dhk.utils.LockUtil;
import com.dhk.utils.MyBeanUtils;
import com.dhk.utils.Tool;
import com.sunnada.kernel.controller.BaseController;

import com.sunnada.kernel.util.ResponseUtil;
import com.sunnada.kernel.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * 放融回调
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/hlbCallBack")
public class HlbCallBackController extends BaseController {
	@Autowired
	JedisPool jedisPool;
	private static final Logger log= LogManager.getLogger();

	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;

	@Resource(name = "callBackService")
	private ICallBackService callBackService;



	@RequestMapping(value="/backCallPay")
	public void purchase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("回调开始");
		String key = "dRrZAsZhdYNN4EyvVTNI3QnjaS54L32g";//秘钥
		String sign = request.getParameter("sign");
		if(StringUtils.isEmpty(sign)) {
			log.error("参数为空");
			response.getWriter().println("FAIL");
			return;
		}	
		//报文必填值
		String rt1_bizType = request.getParameter("rt1_bizType");
		String rt2_retCode = request.getParameter("rt2_retCode");
		String rt3_retMsg = request.getParameter("rt3_retMsg");
		String rt4_customerNumber = request.getParameter("rt4_customerNumber");
		String rt5_orderId = request.getParameter("rt5_orderId");
		String rt6_serialNumber = request.getParameter("rt6_serialNumber");
		String rt7_completeDate = request.getParameter("rt7_completeDate");
		String rt8_orderAmount = request.getParameter("rt8_orderAmount");
		String rt9_orderStatus = request.getParameter("rt9_orderStatus");
		String rt10_bindId = request.getParameter("rt10_bindId");
		String rt11_bankId = request.getParameter("rt11_bankId");
		String rt12_onlineCardType = request.getParameter("rt12_onlineCardType");
		String rt13_cardAfterFour = request.getParameter("rt13_cardAfterFour");
		String rt14_userId = request.getParameter("rt14_userId");
		Map<String,String> checkMap = new LinkedHashMap<String,String>();
		checkMap.put("rt1_bizType",rt1_bizType );
		checkMap.put("rt2_retCode",rt2_retCode );
		checkMap.put("rt3_retMsg",rt3_retMsg );
		checkMap.put("rt4_customerNumber",rt4_customerNumber );
		checkMap.put("rt5_orderId",rt5_orderId );
		
		checkMap.put("rt6_serialNumber",rt6_serialNumber );
		checkMap.put("rt7_completeDate",rt7_completeDate );
		checkMap.put("rt8_orderAmount",rt8_orderAmount );
		checkMap.put("rt9_orderStatus",rt9_orderStatus );
		checkMap.put("rt10_bindId",rt10_bindId );
		checkMap.put("rt11_bankId",rt11_bankId );
		checkMap.put("rt12_onlineCardType",rt12_onlineCardType );
		checkMap.put("rt13_cardAfterFour",rt13_cardAfterFour );
		checkMap.put("rt14_userId",rt14_userId );
		String message = "";
		String resultCode ="";
		String checkStr = MyBeanUtils.getSigned(checkMap,null,key);//验证签名的原始字符串
		String checksign = Disguiser.disguiseMD5(checkStr.trim());
		String outTradeNo = rt5_orderId;
	/*	INIT:未支付
		SUCCESS：成功
		CANCELLED：已取消
		REFUNDED：已退款
		FAILED：失败
		DOING：处理中*/
        if (sign.equalsIgnoreCase(checksign)) {
        	if("SUCCESS".equals(rt9_orderStatus)) {
	        	resultCode="0000";
				message="交易成功";  
        	}else if("INIT".equals(rt9_orderStatus)||"DOING".equals(rt9_orderStatus)) {
        		resultCode="9997";
				message="交易处理中";
        	}else {
        		resultCode="fail";
    			message="交易失败";
        	}
		}else {
			resultCode="9997";
			message="验证签名失败";
		}		
		//{"t0Rate":"0.00480","payType":"UNIONPAYPORT","t0Fee":"100","payTime":"20171207191029","bankType":"0","t1Rate":"0.00480","outTradeNo":"W17120719101800037","resultCode":"1","sign":"76869159264145DBFDB049C417D0BB52","amt":1200,"transactionId":"1512645019336505893","actualAmt":1094}
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			int lock = LockUtil.lock(outTradeNo, 60, jedis); //防止重复通知
			//String val = jedis.hget("repayUndefined_"+ StringUtil.getCurrentDateTime("yyyyMMdd"),outTradeNo);
			if(lock==1){ //加锁成功
				//jedis.hdel("repayUndefined_"+ StringUtil.getCurrentDateTime("yyyyMMdd"),outTradeNo);
				log.info("开始处理订单："+outTradeNo);
				//TransWater transWater =transWaterService.findById(Long.parseLong(val));
				//TransWater transWater =transWaterService.findById(Long.parseLong(outTradeNo));
				Thread.sleep(3000); //休息3000毫秒
				TransWater transWater =transWaterService.findByTransNo(outTradeNo);
				log.info("异步transWater"+transWater.getRespCode());
				if ("0000".equals(transWater.getRespCode())){
					log.info("该订单已处理："+outTradeNo);
					ResponseUtil.responseJson(response, "success");
					return;
				}
				String transType = transWater.getTrans_type();
				//0:纯消费，1：还款消费，2：快速还款，3：普通还款,4:充值，5,：提现
				if("4".equals(transType)){
					callBackService.czhd(resultCode,message,transWater);
				}else if("5".equals(transType)){//提现     有区代理商提现，用户提现
					callBackService.txhd(resultCode,message,transWater);
				}else if("2".equals(transType)){
					//callBackService.repay(resultCode,message,transWater);
					callBackService.repayDate(resultCode,message,transWater); //资金不过夜模式

				}else if("1".equals(transType)){
					//callBackService.repayCost(resultCode,message,transWater);
					callBackService.repayCostDate(resultCode,message,transWater); //资金不过夜模式
				}else if("0".equals(transType)){
					callBackService.cost(resultCode,message,transWater);
				}
				response.getWriter().println("success");
				LockUtil.unlock(outTradeNo, jedis); //解锁
			}else{
				log.info("等待下次回调："+outTradeNo);
				response.getWriter().println("fail");
			}
		}catch (Exception e){
			e.printStackTrace();
			log.error(Tool.getTrace(e));
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}
		return;
	}

}
