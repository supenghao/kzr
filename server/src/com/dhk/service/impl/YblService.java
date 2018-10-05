package com.dhk.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.xiajinsuo.epay.sdk.HttpUtils;
import org.xiajinsuo.epay.sdk.RRParams;
import org.xiajinsuo.epay.sdk.ResponseDataWrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.init.Constant;
import com.dhk.utils.BeanConvertUtil;
import com.dhk.utils.HXPayBaseReult;
import com.dhk.utils.HttpRequest;
import com.dhk.utils.MD5Util;
import com.dhk.utils.RsaDataEncryptUtil;



/**
 * 易佰联服务
 * @author kebingshou
 *
 */
@Service("yblService")
public class YblService {
	static Logger logger =LogManager.getLogger(YblService.class);

	public JSONObject findOrder(String orderDate,String orderNo) throws Exception {
		JSONObject retJson = new JSONObject();
			  try{
	            	Map<String,Object> reqMapBody = new HashMap<>();
	            	reqMapBody.put("sysPayNo", orderNo);
	            	String sign = MD5Util.MD5Encode(JSON.toJSONString(reqMapBody)+Constant.yblkey);
	    			Map<String,Object> reqMapHead = new HashMap<>();
	    			reqMapHead.put("SIGN", sign);
	    			reqMapHead.put("SYSID",Constant.yblmerNo);
	    			Map<String,Object> reqMapString = new HashMap<>();
	    			reqMapString.put("REQ_BODY", reqMapBody);
	    			reqMapString.put("REQ_HEAD", reqMapHead);
	    			reqMapString.put("ip", "fe80::5fd0:c9db:b821:88b2");
	    			logger.info("易佰联查询提交报文："+ JSON.toJSONString(reqMapString));
	    			String httpResult = HttpRequest.sendPost(Constant.yblpostUrl+"XFBJ002.json", "REQ_MESSAGE="+JSON.toJSONString(reqMapString));
	    			if(httpResult==null){
	    				retJson.put("code","fail");
						retJson.put("message","系统异常:易佰联查询异常，返回空");
						   return retJson;
	    			}
	    			logger.info("易佰联查询返回的报文："+httpResult);
	    			JSONObject resultJosn = JSONObject.parseObject(httpResult);
	    			JSONObject reqbody = JSONObject.parseObject(resultJosn.get("REP_BODY") != null ? resultJosn.get("REP_BODY").toString() : "");
	    			if ("000000".equals(reqbody.get("RSPCOD") != null ? reqbody.get("RSPCOD").toString() : "")) {
	    				retJson.put("code","0000");
		        		retJson.put("message","成功");
	    			} else {
	    				retJson.put("code","fail");
						retJson.put("message",reqbody.get("RSPMSG")+"");
	    			}
	           return retJson;
	        } catch (Exception e) {
	        	logger.error("HlbPayBankPay Exception："+e);
				retJson.put("code","9997");
				retJson.put("message","错误信息:"+e.getMessage());
				return retJson;	      
	        }		
	}


	public JSONObject findWithdraw(String orderDate,String orderNo) throws Exception {
		JSONObject retJson = new JSONObject();
		String orig_tran_id = orderNo;//订单号
		Map<String, String> businessReq = new HashMap();
		businessReq.put("method", "pay_qry");
		businessReq.put("orig_tran_id",orig_tran_id);
		String trans_type = "AGENCY_PAYMENT";
		long trans_timestamp = System.currentTimeMillis();
		try{
			/**设置公用参数并签名*/
			RRParams requestData = RRParams.newBuilder().setAppId(Constant.appId).setClientTransId(generateTransId())
					.setTransTimestamp(trans_timestamp).setTransType(trans_type).build();
			logger.info("提交的报文为:"+businessReq);
			/**请求接口*/
			ResponseDataWrapper rdw = HttpUtils.post(Constant.requestUrl, requestData, businessReq,
					RsaDataEncryptUtil.rsaDataEncryptPri, RsaDataEncryptUtil.rsaDataEncryptPub);
			HXPayBaseReult hxPayBaseReult = BeanConvertUtil.beanConvert(rdw, HXPayBaseReult.class);
			logger.info("返回的报文为:"+JSON.toJSONString(hxPayBaseReult));
			String respCode = rdw.getRespCode();
			if (respCode.equals("000000")) {
				@SuppressWarnings("rawtypes")
				Map responseData = rdw.getResponseData();
				System.out.println(responseData);
				if("PAY_SUCCESS".equals(responseData.get("resp_code")+"")){
					retJson.put("code","0000");
					retJson.put("message","成功");
				}else if("PAY_SUBMIT".equals(responseData.get("resp_code")+"")){
					retJson.put("code","9997");
					retJson.put("message",responseData.get("resp_msg")+"");
				}else{
					retJson.put("code","Fail");
					retJson.put("message",responseData.get("resp_msg")+"");
				}
			} else {
				logger.info("rdw.getRespCode():"+rdw.getRespCode() +"  rdw.getRespMsg():"+rdw.getRespMsg());
				retJson.put("code","fail");
				retJson.put("message",rdw.getRespMsg());
			}
	           return retJson;
	        } catch (Exception e) {e.printStackTrace();
	        	logger.error("HxtcPayBankPay Exception："+e);
				retJson.put("code","9997");
				retJson.put("message","错误信息:"+e.getMessage());
				return retJson;	      
	        }		
	}

	
	static String generateTransId() {
		String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String nanoTime = System.nanoTime() + "";
		return String.format("%s%s", time, nanoTime.substring(nanoTime.length() - 6));
	}

}

