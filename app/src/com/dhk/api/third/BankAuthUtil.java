package com.dhk.api.third;

import com.alibaba.fastjson.JSONObject;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class BankAuthUtil {
	private static Logger logger = Logger.getLogger(BankAuthUtil.class);
	private static String url = "https://api.gzfpay.com/v1.0/auth";

	public static boolean bankCard4Verify(String realName,String idNo,String cardNo,String mobile) {
		
	    boolean flag = true;
	    if(flag)//保护模式无需鉴权
	    	return true;
		String merNo = "820080822455";
		String merKey = "a119ddc220954c809d7f107a250a983e";

		SortedMap<String, String> map = new TreeMap<String, String>();

		addMap(map, "merNo", merNo);
		addMap(map, "customerName", realName);
		addMap(map, "cerdId", idNo);
		addMap(map, "mobileNo", mobile);
		addMap(map, "authType", "4");
		addMap(map, "acctNo", cardNo);
		String sign = getSign(map, merKey); //签名的报文

		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("merNo", merNo));
		nvps.add(new BasicNameValuePair("customerName", realName));
		nvps.add(new BasicNameValuePair("cerdId", idNo));
		nvps.add(new BasicNameValuePair("acctNo", cardNo));
		nvps.add(new BasicNameValuePair("mobileNo", mobile));
		nvps.add(new BasicNameValuePair("authType", "4"));
		nvps.add(new BasicNameValuePair("sign", sign));
		logger.info("提交的报文为:" + nvps);
		JSONObject jsonRet = sendForGzf(url, nvps);
		if (jsonRet == null) {
			return false;
		}
		logger.info("返回的报文为:" + nvps);
		map.clear();
		merNo = jsonRet.getString("merNo"); //商户号
		addMap(map, "merNo", merNo);
		addMap(map, "customerName", jsonRet.getString("customerName"));
		addMap(map, "cerdId", jsonRet.getString("cerdId"));
		addMap(map, "acctNo", jsonRet.getString("acctNo"));
		addMap(map, "authType",  jsonRet.getString("authType"));
		addMap(map, "mobileNo", jsonRet.getString("mobileNo"));
		addMap(map, "respCode", jsonRet.getString("respCode"));
		String respType = jsonRet.getString("respType");//应答状态  S成功，E错误，R不确定（对于不确定的可以在三分钟后发起查询）
		addMap(map, "respType", respType);
		String respMsg = jsonRet.getString("respMsg");//应答描述   可为空
		addMap(map, "respMsg", respMsg);
		String signReturn = jsonRet.getString("sign"); //返回签名值
		sign = getSign(map,merKey);

		if(signReturn.equals(sign)&&"S".equals(respType)){
			return true;
		}else{
			return false;
		}
	}
	public static String getSign(SortedMap<String, String> packageParams, String merKey) {
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

	public static String getSHA256StrJava(String str){
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
	private static String byte2Hex(byte[] bytes){
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
	private static void addMap(SortedMap<String, String> map,String key,String value){
		if(map!=null&&StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(value))
			map.put(key, value);
	}

	public static JSONObject sendForGzf(String url, List<BasicNameValuePair> nvps) {
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
	
}
