package com.dhk.controller;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
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
import com.dhk.dao.IAPPUserDao;
import com.dhk.entity.APPUser;
import com.dhk.entity.TransWater;
import com.dhk.init.Constant;
import com.dhk.payment.PayCallbackResult;
import com.dhk.service.ICallBackService;
import com.dhk.service.ITransWaterService;
import com.dhk.utils.LockUtil;
import com.dhk.utils.RsaDataEncryptUtil;
import com.dhk.utils.Tool;
import com.fast.pay.utils.EncryptUtils;
import com.sunnada.kernel.controller.BaseController;
import com.sunnada.kernel.util.ResponseUtil;


/**
 * kj回调
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/kjCallBack")
public class KJCallBackController extends BaseController {
	@Autowired
	JedisPool jedisPool;
	private static final Logger log= LogManager.getLogger();

	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;

	@Resource(name = "callBackService")
	private ICallBackService callBackService;
	@Resource(name = "APPUserDao")
	private IAPPUserDao appUserDao;

	@RequestMapping(value="/purchase")
	public void purchase(HttpServletRequest request,HttpServletResponse response, PayCallbackResult payResult) throws Exception{
		log.info("回调开始");
        Map map=new HashMap();
	 
		 
		Enumeration enu=request.getParameterNames(); 
		
		while(enu.hasMoreElements()){  
		String paraName=(String)enu.nextElement();  
		log.info("参数11:"+paraName+": "+request.getParameter(paraName));
	    map.put(paraName, request.getParameter(paraName));
		}
		if(map.get("app_id")==null){
			return ;
		}
		//临时处理下注册回调（坑爹的文档）
		if("KAZU".equals(map.get("app_id")+"")){
			try {
			String signStr=map.get("data")+"";
			log.info("获取的签名数据为："+signStr);
			log.info("解密签名wei："+EncryptUtils.decrypt(signStr, "7ba93cce852afdaffca595d842a9ea85"));
			JSONObject josnZc=JSONObject.parseObject(EncryptUtils.decrypt(signStr, "7ba93cce852afdaffca595d842a9ea85"));
			//注册失败时候清除报户信息
			if(!"true".equals(josnZc.getString("success"))){
				String merchantid = josnZc.getString("mct_number");
				String sql = "update t_s_user set kj_merno =:kj_merno,kj_key=:kj_key  where kj_merno=:id";
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("kj_merno", "");
				paramap.put("kj_key", "");
				paramap.put("id", merchantid);
				int change = appUserDao.update(sql, paramap);
			}else{
				String merchantid = josnZc.getString("mct_number");
				String key = josnZc.getString("secret_key");
				String sql = "update t_s_user set kj_merno =:kj_merno,kj_key=:kj_key,is_auth =:is_auth where kj_merno=:id";
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("kj_merno",merchantid);
				paramap.put("kj_key", key);
				paramap.put("id", merchantid);
				paramap.put("is_auth", "1");
				int change = appUserDao.update(sql, paramap);
			} 
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			ResponseUtil.responseJson(response, "success");
			return ;
		}
		if(!StringUtils.isEmpty(map.get("app_id")+"")){
			String signStr=map.get("data")+"";
			log.info("获取的签名数据为："+signStr);
			APPUser user = null;
			String sql ="select * from t_s_user where kj_merno=:kj_merno";
			Map<String, Object> mapp = new HashMap<String, Object>();
			mapp.put("kj_merno", map.get("app_id")+"");
			String resultCode ="";
			String message="";
			String outTradeNo ="";
			boolean c=false;
			try {
				user = appUserDao.findBy(sql, mapp);
				log.info("解密签名wei："+EncryptUtils.decrypt(signStr, user.getKjKey()));
				JSONObject josn=JSONObject.parseObject(EncryptUtils.decrypt(signStr, user.getKjKey()));
				if(josn==null){
					return ;
				}
				log.info("快捷回调返回参数："+josn.toString());
				String action=map.get("action")+"";
				outTradeNo=josn.getString("trade_order_code"); 
				//回调收款操作、信用卡代付
				if("RCM_RH5".equals(action) || "CCT_PYI".equals(action) || "CCT_PYO".equals(action)){
						c=true;
						if("true".equals(josn.getString("success"))){
							resultCode="0000";
							message=josn.getString("message");
						}else{
							resultCode="fail";
							message=josn.getString("message");
						}
					}
					if(c){
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
									return ;
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
									callBackService.costNew(resultCode,message,transWater);
								}
//								response.getWriter().println("success");
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
					}
				
				
				
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
		}
		
		 
		ResponseUtil.responseJson(response, "success");
		return ;
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
					callBackService.costNew(resultCode,message,transWater);
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
