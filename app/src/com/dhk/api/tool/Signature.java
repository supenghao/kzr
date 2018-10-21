package com.dhk.api.tool;

import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;

public class Signature {
	static Logger logger = Logger.getLogger(Signature.class);
	public static String createSign(Map<String,String> paramMap,String key){
		try {
			String resukltContent = StringUtils.getSignContent(paramMap);
//			  resukltContent = URLEncoder.encode(resukltContent, "UTF-8");
			  resukltContent = resukltContent+"&key="+key;
			  logger.info("签名原串："+resukltContent);
			  return MD5Util.MD5Encode(resukltContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}//编码
		return null;
	}
}
