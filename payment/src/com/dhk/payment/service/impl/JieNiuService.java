package com.dhk.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.kernel.util.StringUtil;
import com.dhk.payment.config.JieNiuConfig;
import com.dhk.payment.service.IJieNiuService;
import com.dhk.payment.util.*;
import com.dhk.payment.wpay.*;
import com.dhk.payment.yilian.MD5Util;
import com.dhk.payment.yilian.QuickPay;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 捷牛
 * @author Administrator
 *
 */
@Service("jieNiuService")
public class JieNiuService implements IJieNiuService {

	static Logger logger = Logger.getLogger(JieNiuService.class);

	/**
	 * 快捷支付
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public QuickPay creditPurchase(Map<String, Object> paramMap) throws Exception {
		//获取参数
		Map<String, String> map = new HashMap();
		map.put("linkId",(String)paramMap.get("orderNo"));
		map.put("orderType","10");
		map.put("amount",(String)paramMap.get("transAmt"));
		map.put("bankNo",(String)paramMap.get("cardNo"));
		map.put("bankAccount",(String)paramMap.get("accountName"));
		map.put("bankPhone",(String)paramMap.get("phoneNo"));
		map.put("bankCert",(String)paramMap.get("cerdId"));
		map.put("bankCvv",(String)paramMap.get("cvn2"));
		map.put("bankYxq",(String)paramMap.get("expDate"));
		map.put("notifyUrl",JieNiuConfig.callBackUrl);

		QuickPay quickPay = new QuickPay();
		JSONObject result =requestAction("SdkNocardOrderPayNoSms",map);
		if (result == null||"000000".equals(result.getString("code"))){
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
		}else{
			quickPay.setRespCode("Fail");
			quickPay.setRespDesc(result.getString("msg"));
		}
		return quickPay;
	}


	/**
	 * 代付
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public QuickPay proxyPay(Map<String, Object> paramMap) throws Exception {
		//获取参数
		Map<String, String> map = new HashMap();
		map.put("linkId",(String)paramMap.get("orderNo"));
		map.put("payType","1");
		map.put("orderType","10");
		map.put("amount",(String)paramMap.get("transAmt"));
		map.put("bankNo",(String)paramMap.get("cardNo"));
		map.put("bankAccount",(String)paramMap.get("accountName"));
		map.put("bankPhone",(String)paramMap.get("phoneNo"));
		map.put("bankCert",(String)paramMap.get("cerdId"));
		map.put("notifyUrl",JieNiuConfig.callBackUrl);

		QuickPay quickPay = new QuickPay();
		JSONObject result =requestAction("SdkSettleMcg",map);
		if (result == null||"000000".equals(result.getString("code"))){
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
		}else{
			quickPay.setRespCode("Fail");
			quickPay.setRespDesc(result.getString("msg"));
		}

		return quickPay;
	}


	public JSONObject  requestAction(String action, Map<String, String> data)  {
		try {
			//业务数据加密
			String encryptkey = "sss"+StringUtil.getCurrentDateTime("yyyMMddHHmmss")+"sss";
			String dataStr = AESTool.encrypt(JSON.toJSONString(data), encryptkey);
			System.out.println("dataStr:"+dataStr);
			logger.info("dataStr:"+dataStr);

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("orgNo", JieNiuConfig.orgno);
			parameters.put("merNo", JieNiuConfig.merno);
			parameters.put("action", action);
			parameters.put("data", dataStr);
			//把私钥进行base64加密
			parameters.put("encryptkey", Base64.encode(RSATool.encrypt(encryptkey.getBytes("UTF-8"), JieNiuConfig.privateKey)));
			parameters.put("sign", sign(action,dataStr));

			String requestContent = createRequest(parameters);
			//发送请求
			String resultBuffer = HttpClientUtil.sendForJN(JieNiuConfig.baseUrl, requestContent);

			JSONObject jsonObject = JSON.parseObject(resultBuffer.toString());
			String rtnData = jsonObject.getString("data");
			String rtnEncryptkey = jsonObject.getString("encryptkey");
			if (StringUtils.isEmpty(rtnData) || StringUtils.isEmpty(rtnEncryptkey)) {
				return null;
			}
			byte[] aesKey = RSATool.decrypt(Base64.decode(rtnEncryptkey), JieNiuConfig.privateKey);
			JSONObject result = JSONObject.parseObject(AESTool.decrypt(rtnData, new String(aesKey))) ;
			logger.info("------------result:"+result.toString());
			return result;
		}catch (Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}

	}


	public  String createRequest(Map<String, String> parm) {
		StringBuffer rtn = new StringBuffer();
		for (String s : parm.keySet()) {
			try {
				rtn.append(s).append("=").append(URLEncoder.encode(parm.get(s), "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return rtn.toString();
	}

	/**
	 *
	 * @param action
	 * @param dataStr   aes加密过的数据串
	 * @return
	 */
	private String sign(String action,String dataStr){
		StringBuilder signBuffer = new StringBuilder();
		signBuffer.append(JieNiuConfig.orgno);
		signBuffer.append(JieNiuConfig.merno);
		signBuffer.append(action);
		signBuffer.append(dataStr);
		return  MD5Util.MD5Encode(signBuffer.toString()+JieNiuConfig.key).toLowerCase();
	}



	public static void main(String[]args){
		
	}


}
