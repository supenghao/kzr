package com.dhk.payment.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Util {

	static Logger logger = Logger.getLogger(Util.class);
	
	/**
	 * 微信生成签名算法
	 * @return
	 */
	public static String getSign(ArrayList<String> list) {
		if(list==null||list.size()==0)
			return null;
		StringBuilder sb = new StringBuilder();
		for(String tmp:list){
			if(StringUtils.isNotEmpty(tmp))
				sb.append(tmp);
		}
		logger.info("源串为:"+sb.toString());
		return getSHA256StrJava(sb.toString());
}
	
	/**
     *  利用java原生的摘要实现SHA256加密
     * @param str 加密后的报文
     * @return
     */
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

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
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
    
    public static void main(String[] args) {
		String a ="356839003097207235068119860421201001100元移动充值卡柯炳寿3321221820080817768http://kbs.foodmall.comhttps://api.gzfpay.com2017052486730995714440806413696874537未知错误R200798750d89399cfc0b44b3292f85ff561edf300";
		a = getSHA256StrJava(a);
		System.out.println(a);
		//a = byte2Hex(a.getBytes());
		//System.out.println(a);
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
	
	public static String getSignForHashMap(Map<String, Object> packageParams, String merKey) {
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
}
