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

import com.dhk.utils.Tool;
import com.sunnada.kernel.controller.BaseController;

import com.sunnada.kernel.util.ResponseUtil;
import com.sunnada.kernel.util.StringUtil;
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
import java.util.Map;
import java.util.TreeMap;


/**
 * 放融回调
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/frCallBack")
public class FRCallBackController extends BaseController {
	@Autowired
	JedisPool jedisPool;
	private static final Logger log= LogManager.getLogger();

	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;

	@Resource(name = "callBackService")
	private ICallBackService callBackService;



	@RequestMapping(value="/purchase")
	public void purchase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("回调开始");
		String accessId = request.getParameter("accessId");
		String encryptData = request.getParameter("data");
		System.out.println("data:"+encryptData);
		//截取key的前16位对data进行解密
		String decryptData = AESUtil.decrypt(encryptData,AimiConfig.AES_KEY);
	
		//解密出来的data转为map对象
		Map<String, Object> result  = JSON.parseObject(decryptData,new TypeReference<TreeMap<String, Object>>() {});
		//验证签名
		if(!Signature.checkIsSignValidFromMap(result,AimiConfig.MD5_KEY)){
			System.out.println("签名验不过!");
			return ;
		}

		JSONObject jsonObject =JSONObject.parseObject(decryptData);
		System.out.println("jsonObject:"+jsonObject);
		String resultCode = jsonObject.getString("resultCode");
		String outTradeNo = jsonObject.getString("outTradeNo");
		String message = "";
		if("1".equals(resultCode)){
			resultCode="0000";
			message="交易成功";
		}else {
			resultCode="fail";
			message="交易失败";
		}
		//{"t0Rate":"0.00480","payType":"UNIONPAYPORT","t0Fee":"100","payTime":"20171207191029","bankType":"0","t1Rate":"0.00480","outTradeNo":"W17120719101800037","resultCode":"1","sign":"76869159264145DBFDB049C417D0BB52","amt":1200,"transactionId":"1512645019336505893","actualAmt":1094}
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			String val = jedis.hget("repayUndefined_"+ StringUtil.getCurrentDateTime("yyyyMMdd"),outTradeNo);
			if(!StringUtil.isEmpty(val)){
				jedis.hdel("repayUndefined_"+ StringUtil.getCurrentDateTime("yyyyMMdd"),outTradeNo);
				log.info("开始处理异常订单："+outTradeNo);
				TransWater transWater =transWaterService.findById(Long.parseLong(val));
				if (!"9997".equals(transWater.getRespCode())){
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
				response.getWriter().println("SUCCESS");
			}else{
				log.info("等待下次回调："+outTradeNo);
				response.getWriter().println("FAIL");
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


	@RequestMapping(value="/proxyPay")
	public void proxyPay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("回调开始");
		String accessId = request.getParameter("accessId");
		String encryptData = request.getParameter("data");
		//{"outTradeNo":"W17120720474500040","resultCode":"0","sign":"6BC40990514250F97E001B8E27AD5283","amt":1000,"time":"20171207204800","transactionId":1801690640}
		//截取key的前16位对data进行解密
		System.out.println("data:"+encryptData);
		String decryptData = AESUtil.decrypt(encryptData,AimiConfig.AES_KEY);
		System.out.println("decryptData:"+decryptData);
		System.out.println("accessId:"+accessId);
		System.out.println(",AimiConfig.MD5_KEY:"+AimiConfig.MD5_KEY);
		System.out.println(",AimiConfig.MD5_KEY:"+AimiConfig.MD5_KEY);
		System.out.println(",AimiConfig.AES_KEY:"+AimiConfig.AES_KEY);
		//解密出来的data转为map对象
		Map<String, Object> result  = JSON.parseObject(decryptData,new TypeReference<TreeMap<String, Object>>() {});
		//验证签名
		if(!Signature.checkIsSignValidFromMap(result,AimiConfig.MD5_KEY)){
			System.out.println("签名验不过!");
			return ;
		}

		JSONObject jsonObject =JSONObject.parseObject(decryptData);
		System.out.println("jsonObject:"+jsonObject);
		String resultCode = jsonObject.getString("resultCode");
		String outTradeNo = jsonObject.getString("outTradeNo");
		String message = "";

		if("0".equals(resultCode)){
			resultCode="0000";
			message="交易成功";
		}else {
			resultCode="fail";
			message="交易失败";
		}
		//{"t0Rate":"0.00480","payType":"UNIONPAYPORT","t0Fee":"100","payTime":"20171207191029","bankType":"0","t1Rate":"0.00480","outTradeNo":"W17120719101800037","resultCode":"1","sign":"76869159264145DBFDB049C417D0BB52","amt":1200,"transactionId":"1512645019336505893","actualAmt":1094}
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			String val = jedis.hget("repayUndefined_"+ StringUtil.getCurrentDateTime("yyyyMMdd"),outTradeNo);
			if(!StringUtil.isEmpty(val)){
				jedis.hdel("repayUndefined_"+ StringUtil.getCurrentDateTime("yyyyMMdd"),outTradeNo);
				log.info("开始处理异常订单："+outTradeNo);
				TransWater transWater =transWaterService.findById(Long.parseLong(val));
				if (!"9997".equals(transWater.getRespCode())){
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
					callBackService.repay(resultCode,message,transWater);
				}else if("1".equals(transType)){
					callBackService.repayCost(resultCode,message,transWater);
				}else if("0".equals(transType)){
					callBackService.cost(resultCode,message,transWater);
				}
				response.getWriter().println("SUCCESS");
			}else{
				log.info("等待下次回调："+outTradeNo);
				response.getWriter().println("FAIL");
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
