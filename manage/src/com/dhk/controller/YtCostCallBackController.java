package com.dhk.controller;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.entity.TransWater;
import com.dhk.init.Constant;
import com.dhk.service.ICallBackService;
import com.dhk.service.ITransWaterService;
import com.dhk.utils.LockUtil;
import com.dhk.utils.SignatureUtil;
import com.dhk.utils.Tool;
import com.sunnada.kernel.util.ResponseUtil;
import com.xdream.kernel.controller.BaseController;


/**
 * 
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/ytcallBack")
public class YtCostCallBackController extends BaseController {
	@Autowired
	JedisPool jedisPool;
	private static final Logger log= LogManager.getLogger();
	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;

	@Resource(name = "callBackService")
	private ICallBackService callBackService;
	@RequestMapping(value="/purchase")
	public void purchase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("回调开始");
		Map<String, String[]> params = request.getParameterMap();
		Map<String,String> requestMap = new HashMap<>();
		for (String key : params.keySet()) {
		log.info("参数11:"+key+": "+request.getParameter(key));  
		if(StringUtils.isNotBlank(request.getParameter(key))){
		requestMap.put(key, request.getParameter(key));
		}
		}
		String outTradeNo = request.getParameter("ORDER_ID");
		String orderStatus = request.getParameter("RESP_CODE");
		String amt = request.getParameter("PAY_AMOUNT");
		if(StringUtils.isEmpty(amt)){
			amt="0.00";
		}
		String message = "";
		String resultCode ="";
		requestMap.remove("signData");
		String sign = SignatureUtil.createSign(requestMap, Constant.key);
		log.info("签名:"+sign);
 
			if("0000".equals(orderStatus)){
				resultCode="0000";
				message="交易成功";  
				}else{
					resultCode="fail";
	    			message=request.getParameter("RESP_DESC");
				}
 
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
				transWater.setTransAmount(new BigDecimal(amt));
				transWater.setTransTime( request.getParameter("PAYCH_TIME"));
				log.info("异步transWater"+transWater.getRespCode());
				if ("0000".equals(transWater.getRespCode())){
					log.info("该订单已处理："+outTradeNo);
					ResponseUtil.responseJson(response, "SUCCESS");
					return;
				}
				String transType = transWater.getTrans_type();
				//0:纯消费，1：还款消费，2：快速还款，3：普通还款,4:充值，5,：提现 6 境外交易
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
					callBackService.costNew(resultCode,message,transWater);
					response.getWriter().println("SUCCESS");
				}else if("6".equals(transType)){
					callBackService.jwpay(resultCode, message, transWater);
				}
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

	public String getParam(HttpServletRequest request){
		
		InputStream inputStream = null;
		try {
			 // 读取参数
	        StringBuffer sb = new StringBuffer();
	        inputStream = request.getInputStream();
	        String s;
	        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	        while ((s = in.readLine()) != null) {
	            sb.append(s);
	        }
	        in.close();
	        inputStream.close();
	        String reqStr = sb.toString();
	        log.info("参数串："+reqStr);
	        JSONObject newj =  JSON.parseObject(reqStr);
		    for(java.util.Map.Entry<String,Object> entry:newj.entrySet()){  
		    	log.info("参数"+entry.getKey()+"："+entry.getValue());
	        }  
				return reqStr;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (Exception e) {}
			}
		}

		return null;
	}
	
	@RequestMapping(value="/purchasepg")
	public void purchasebg(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("回调开始");
		Map<String, String[]> params = request.getParameterMap();
		Map<String,String> requestMap = new HashMap<>();
		for (String key : params.keySet()) {
		log.info("参数11:"+key+": "+request.getParameter(key));  
		if(StringUtils.isNotBlank(request.getParameter(key))){
		requestMap.put(key, request.getParameter(key));
		}
		}
		String outTradeNo = request.getParameter("ORDER_ID");
		String orderStatus = request.getParameter("RESP_CODE");
		String amt = request.getParameter("PAY_AMOUNT");
		if(StringUtils.isEmpty(amt)){
			amt="0.00";
		}
		String message = "";
		String resultCode ="";
		requestMap.remove("signData");
		String sign = SignatureUtil.createSign(requestMap, Constant.key);
		log.info("签名:"+sign);
 
			if("0000".equals(orderStatus)){
				resultCode="0000";
				message="交易成功";  
				}else{
					resultCode="fail";
	    			message=request.getParameter("RESP_DESC");
				}
 
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
				transWater.setTransAmount(new BigDecimal(amt));
				transWater.setTransTime( request.getParameter("PAYCH_TIME"));
				log.info("异步transWater"+transWater.getRespCode());
				if ("0000".equals(transWater.getRespCode())){
					log.info("该订单已处理："+outTradeNo);
					ResponseUtil.responseJson(response, "SUCCESS");
					return;
				}
				String transType = transWater.getTrans_type();
				//0:纯消费，1：还款消费，2：快速还款，3：普通还款,4:充值，5,：提现 6 境外交易
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
					callBackService.costNew(resultCode,message,transWater);
					response.getWriter().println("SUCCESS");
				}else if("6".equals(transType)){
					callBackService.jwpay(resultCode, message, transWater);
				}
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
