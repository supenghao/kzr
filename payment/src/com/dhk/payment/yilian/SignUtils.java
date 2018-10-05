package com.dhk.payment.yilian;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;

public class SignUtils {

    public static String signData(List<BasicNameValuePair> nvps) throws Exception {
    	Map<String, String> tempMap = new LinkedHashMap<String, String>();
        for (BasicNameValuePair pair : nvps) {
            if (StringUtils.isNotBlank(pair.getValue())) {
                tempMap.put(pair.getName(), pair.getValue());
            }
        }
        StringBuffer buf = new StringBuffer();
        for (String key : tempMap.keySet()) {
            buf.append(key).append("=").append((String) tempMap.get(key)).append("&");
        }
        return buf.substring(0, buf.length() - 1);
    }

    public static String verferSignData(String str) throws UnsupportedEncodingException {
        System.out.println("响应数据：" + str);
        String data[] = str.split("&");
        StringBuffer buf = new StringBuffer();
        String signature = "";
        for (int i = 0; i < data.length; i++) {
            String tmp[] = data[i].split("=", 2);
            if ("signature".equals(tmp[0])) {
                signature = tmp[1];
            } else {
                buf.append(tmp[0]).append("=").append(tmp[1]).append("&");
            }
        }
        String signatureStr = buf.substring(0, buf.length() - 1);
//        String st = URLEncoder.encode(signatureStr, "utf-8");	//编码
        System.out.println("验签数据：" + signatureStr);
        return signatureStr;
        //return RSAUtil.verifyByKeyPath(signatureStr, signature, ConfigUtils.getProperty("public_key_path"), "UTF-8");
    }
    
}
