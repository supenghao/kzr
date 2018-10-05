package com.dhk.controller;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.entity.TransWater;
import com.dhk.init.Constant;
import com.dhk.payment.PayCallbackResult;
import com.dhk.service.ICallBackService;
import com.dhk.service.ITransWaterService;
import com.dhk.utils.LockUtil;
import com.dhk.utils.MD5Util;
import com.dhk.utils.ResponseUtils;
import com.dhk.utils.RsaDataEncryptUtil;
import com.dhk.utils.Tool;
import com.sunnada.kernel.controller.BaseController;
import com.sunnada.kernel.util.ResponseUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * 易百联回调
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/yblCallBack")
public class YBLCallBackController extends BaseController {
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
		String result = request.getParameter("REQ_MESSAGE");
		log.info("参数："+result);
		JSONObject resultJosn = JSONObject.parseObject(result);
		JSONObject reqBody = JSONObject.parseObject(resultJosn.get("REQ_BODY") != null ? resultJosn.get("REQ_BODY").toString() : "");
	/*	Map<String, Object> reqMapBody = new HashMap<String, Object>();//
    	reqMapBody.put("repCode", reqBody.get("repCode")); 
    	reqMapBody.put("repMesg", reqBody.get("repMesg")); 
    	reqMapBody.put("sysPayNo", reqBody.get("sysPayNo")); 
		String sign = MD5Util.MD5Encode(JSON.toJSONString(reqMapBody)+Constant.yblkey);*/
		String message = "";
		String resultCode ="";
		//通道方说可以不验签 在通道账户里他还是没钱，资金安全的
		//	log.info("签名成功");
			String outTradeNo = reqBody.get("sysPayNo") != null ? reqBody.get("sysPayNo").toString() : "";
			if("000000".equals(reqBody.get("repCode") != null ? reqBody.get("repCode").toString() : "")){
			resultCode="0000";
			message="交易成功";  
			}else{
				resultCode="fail";
    			message="交易失败-"+reqBody.get("repMesg") != null ? reqBody.get("repMesg").toString() : "";
			}
		Jedis jedis = null;
		try{
/*			jedis = jedisPool.getResource();
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
					boolean back =callBackService.repayDate(resultCode,message,transWater); //资金不过夜模式
                   if(back){
                	   ResponseUtils.render(response, "success");
                   }
				}else if("1".equals(transType)){
					//callBackService.repayCost(resultCode,message,transWater);
					boolean back =callBackService.repayCostDate(resultCode,message,transWater); //资金不过夜模式
					 if(back){
	                	   ResponseUtils.render(response, "success");
	                   }
				}else if("0".equals(transType)){
					callBackService.cost(resultCode,message,transWater);
				}
//				response.getWriter().println("success");
				LockUtil.unlock(outTradeNo, jedis); //解锁
			}else{
				log.info("等待下次回调："+outTradeNo);
				response.getWriter().println("fail");
			}*/
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

	@RequestMapping(value="/replan")
	public void replan(HttpServletRequest request,HttpServletResponse response, PayCallbackResult payResult) throws Exception{
		log.info("回调开始");
		log.info("data:"+JSON.toJSONString(payResult));
		String result = getParam(request);
		payResult = JSONObject.parseObject(result, PayCallbackResult.class);
		String outTradeNo = payResult.getClient_trans_id();
		String message = "";
		String resultCode ="";
		StringBuffer sb = new StringBuffer();
		sb.append("client_trans_id=");
		sb.append(payResult.getClient_trans_id());
		sb.append("|resp_code=");
		sb.append(payResult.getResp_code());
		Boolean b = RsaDataEncryptUtil.rsaDataEncryptPub.verify(sb.toString().getBytes("UTF-8"), payResult.getSign());
		if(b){
			log.info("签名成功");
			if("WITHDRAWALS_SUCCESS".equals(payResult.getResp_code())){
			resultCode="0000";
			message="还款成功";  
			}else if("WITHDRAWALS_REQUEST".equals(payResult.getResp_code())||"WITHDRAWALS_SUBMIT".equals(payResult.getResp_code())) {
        		resultCode="9997";
				message="还款处理中";
        	}else{
				resultCode="fail";
    			message="还款失败-"+payResult.getErr_msg();
			}
		}else{
			log.info("签名失败");
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
//				response.getWriter().println("success");
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

	
	@RequestMapping(value="/proxyPayBack")
	public void proxyPayBack(HttpServletRequest request,HttpServletResponse response, PayCallbackResult payResult) throws Exception{
		log.info("回调开始");
		log.info("data:"+JSON.toJSONString(payResult));
		String result = getParam(request);
		payResult = JSONObject.parseObject(result, PayCallbackResult.class);
		String outTradeNo = payResult.getClient_trans_id();
		String message = "";
		String resultCode ="";
		StringBuffer sb = new StringBuffer();
		sb.append("client_trans_id=");
		sb.append(payResult.getClient_trans_id());
		sb.append("|resp_code=");
		sb.append(payResult.getResp_code());
		Boolean b = RsaDataEncryptUtil.rsaDataEncryptPub.verify(sb.toString().getBytes("UTF-8"), payResult.getSign());
		if(b){
			log.info("签名成功");
			if("PAY_SUCCESS".equals(payResult.getResp_code())){
			resultCode="0000";
			message="还款成功";  
			}else{
				resultCode="fail";
    			message="还款失败-"+payResult.getErr_msg();
			}
		}else{
			log.info("签名失败");
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
	        
	        JSONObject newj =  JSON.parseObject(reqStr);
		    for(java.util.Map.Entry<String,Object> entry:newj.entrySet()){  
		    	log.info("参数"+entry.getKey()+"："+entry.getValue());
		    	System.out.println("json参数"+entry.getKey()+"："+entry.getValue());
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
	
}
