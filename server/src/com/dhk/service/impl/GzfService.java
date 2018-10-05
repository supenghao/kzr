package com.dhk.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.sunnada.kernel.DreamConf;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service("gzfService")
public class GzfService {
	static Logger logger = Logger.getLogger(GzfService.class);


	public static final String url = DreamConf.getPropertie("GZF_URL");
	public static final String merNo = DreamConf.getPropertie("GZF_MERNO");
	public static final String key = DreamConf.getPropertie("GZF_KEY");

	public JSONObject findOrder(String orderDate,String orderNo) throws Exception {
		JSONObject retJson = new JSONObject();
		
		boolean flag = true; //不调用接口模拟
		if(flag) {
			retJson.put("code","0000");
			retJson.put("message","成功");
			return retJson;
		}
		
		SortedMap<String, String> map = new TreeMap<String, String>();
		addMap(map, "merNo", merNo);
		addMap(map, "orderDate", orderDate);
		addMap(map, "orderNo", orderNo);
		String sign = getSign(map, key); //签名的报文

		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("merNo", merNo));
		nvps.add(new BasicNameValuePair("orderDate", orderDate));
		nvps.add(new BasicNameValuePair("orderNo", orderNo));
		nvps.add(new BasicNameValuePair("sign", sign));
		logger.info("提交的报文为:" + nvps);
		JSONObject jsonRet = sendForGzf(url+"/findOrder", nvps);
		if (jsonRet == null) {
			return null;
		}
		logger.info("返回的报文为:" + jsonRet);
		map.clear();
		addMap(map, "merNo", jsonRet.getString("merNo"));
		addMap(map, "orderDate", jsonRet.getString("orderDate"));
		addMap(map, "orderNo", jsonRet.getString("orderNo"));
		addMap(map, "transAmt", jsonRet.getString("transAmt"));
		addMap(map, "respCode",  jsonRet.getString("respCode"));
		String  respType = jsonRet.getString("respType");
		addMap(map, "respType", respType);
		addMap(map, "respMsg", jsonRet.getString("respMsg"));
		addMap(map, "oriRespCode", jsonRet.getString("oriRespCode"));
		String  oriRespType = jsonRet.getString("oriRespType");
		addMap(map, "oriRespType", oriRespType);
		addMap(map, "oriRespMsg", jsonRet.getString("oriRespMsg"));
		String signReturn = jsonRet.getString("sign"); //返回签名值

		if("E".equals(oriRespType)){  //返回e不去验证签名
			retJson.put("code","Fail");
			retJson.put("message",jsonRet.getString("oriRespMsg"));
			return   retJson;
		}
		sign = getSign(map,key);

		if(!signReturn.equals(sign)){
			logger.info("签名失败，signReturn:" + signReturn+",sign:"+sign);
			retJson.put("code","9997");
			return   retJson;
		}
		if("S".equals(respType)){
			if("S".equals(oriRespType)){
				retJson.put("code","0000");
				retJson.put("message","成功");
			}else if("R".equals(oriRespType)){
				retJson.put("code","9997");
			}else if("E".equals(oriRespType)){
				retJson.put("code","Fail");
				retJson.put("message",jsonRet.getString("oriRespMsg"));
			}else{
				retJson.put("code","9997");
			}

		}else{
			retJson.put("code","9997");
		}

		return  retJson;
		
	}


	public JSONObject findWithdraw(String orderDate,String orderNo) throws Exception {
		JSONObject retJson = new JSONObject();
		
		boolean flag = true; //不调用接口模拟
		if(flag) {
			retJson.put("code","0000");
			retJson.put("message","成功");
		}
		
		
		SortedMap<String, String> map = new TreeMap<String, String>();
		addMap(map, "merNo", merNo);
		addMap(map, "orderDate", orderDate);
		addMap(map, "orderNo", orderNo);
		String sign = getSign(map, key); //签名的报文

		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("merNo", merNo));
		nvps.add(new BasicNameValuePair("orderDate", orderDate));
		nvps.add(new BasicNameValuePair("orderNo", orderNo));
		nvps.add(new BasicNameValuePair("sign", sign));
		logger.info("提交的报文为:" + nvps);
		JSONObject jsonRet = sendForGzf(url+"/findWithdraw", nvps);
		if (jsonRet == null) {
			return null;
		}
		logger.info("返回的报文为:" + jsonRet);
		map.clear();
		addMap(map, "merNo", jsonRet.getString("merNo"));
		addMap(map, "orderDate", jsonRet.getString("orderDate"));
		addMap(map, "orderNo", jsonRet.getString("orderNo"));
		addMap(map, "transAmt", jsonRet.getString("transAmt"));
		addMap(map, "respCode",  jsonRet.getString("respCode"));
		String  respType = jsonRet.getString("respType");
		addMap(map, "respType", respType);
		addMap(map, "respMsg", jsonRet.getString("respMsg"));
		addMap(map, "oriRespCode", jsonRet.getString("oriRespCode"));
		String  oriRespType = jsonRet.getString("oriRespType");
		addMap(map, "oriRespType", oriRespType);
		addMap(map, "oriRespMsg", jsonRet.getString("oriRespMsg"));
		
		String signReturn = jsonRet.getString("sign"); //返回签名值
		if("E".equals(oriRespType)){  //返回e不去验证签名
			retJson.put("code","Fail");
			retJson.put("message",jsonRet.getString("oriRespMsg"));
			return   retJson;
		}

		sign = getSign(map,key);
		if(!signReturn.equals(sign)){
			logger.info("签名失败，signReturn:" + signReturn+",sign:"+sign);
			retJson.put("code","9997");
			return   retJson;
		}
		if("S".equals(respType)){
			if("S".equals(oriRespType)){
				retJson.put("code","0000");
				retJson.put("message","成功");
			}else if("R".equals(oriRespType)){
				retJson.put("code","9997");
			}else if("E".equals(oriRespType)){
				retJson.put("code","Fail");
				retJson.put("message",jsonRet.getString("oriRespMsg"));
			}else{
				retJson.put("code","9997");
			}

		}else{
			retJson.put("code","9997");
		}

		return  retJson;

	}



	public  String getSign(SortedMap<String, String> packageParams, String merKey) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (StringUtils.isNotEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(v);
			}
		}
		sb.append(merKey);
		logger.info("源串为:"+sb.toString());
		String sign =getSHA256StrJava(sb.toString()).toLowerCase();
		logger.info("加密后:" + sign);
		return sign;
	}

	public  String getSHA256StrJava(String str){
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}
	private  String byte2Hex(byte[] bytes){
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i=0;i<bytes.length;i++){
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length()==1){
				//1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
	private  void addMap(SortedMap<String, String> map,String key,String value){
		if(map!=null&&StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(value))
			map.put(key, value);
	}

	public  JSONObject sendForGzf(String url, List<BasicNameValuePair> nvps) {
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(10000).setConnectionRequestTimeout(2000)
					.setSocketTimeout(10000).build();
			post.setConfig(requestConfig);
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			resp = client.execute(post);
			int statusCode = resp.getStatusLine().getStatusCode();
			if (200 == statusCode) {
				String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
				JSONObject jsonRet = JSONObject.parseObject(str);
				return jsonRet;
			}else{
				logger.error("http请求错误："+statusCode);
			}

		} catch (SocketTimeoutException e){
			logger.error(e.getMessage());
		}catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (client != null)
					client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (resp != null)
					resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("merNo", merNo));
		GzfService gzf = new GzfService();
		gzf.sendForGzf("http://121.204.92.48:8080/server/hlbCallBack/backCallPay",nvps);
	}

}

