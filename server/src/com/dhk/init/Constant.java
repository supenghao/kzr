package com.dhk.init;

import java.util.HashMap;
import java.util.Map;

import com.dhk.entity.FeeParam;

/**
 * 
	* @ClassName: Constant 
	* @Description: TODO
	* @author ZZL
	* @date 2018年1月29日 下午2:38:31 
	*
 */
public class Constant {
    /**
     * 费率配置表
     */
    public static  Map<String,FeeParam> feeMaps = new HashMap<>();
    
    /**
     * 消费报户参数
     */
    
    public static String pmerNo;//服务商编码
    public static String key;//密钥
    public static String postUrl;//报户请求地址
    public static Map<String,String> bankCode = new  HashMap<>();//银行编码
    
    /**
     * 汇享报户
     */
    public static String appIddh;
    public static String appId;//
    public static String transType;//
    public static String requestUrl;//报户请求地址
    
    
    /**
     * 易佰联报户
     */
    public static String yblmerNo;
    public static String yblkey;//密钥
    public static String yblpostUrl;//报户请求地址
    
    
    public static String tdtype;//通道类型
    public static String costtype;//通道类型
    /**
     * 地区配置表
     */
    public static  Map<String,String> areaMaps = new HashMap<>();
    
 
    
    static{
    	bankCode.put("工商", "102");
    	bankCode.put("农业", "103");
    	bankCode.put("中国", "104");
    	bankCode.put("建设", "105");
    	bankCode.put("交通", "301");
    	bankCode.put("中信", "302");
    	bankCode.put("光大", "303");
    	bankCode.put("华夏", "304");
    	bankCode.put("民生", "305");
    	bankCode.put("广发", "306");
    	bankCode.put("平安", "307");
    	bankCode.put("招商", "308");
    	bankCode.put("兴业", "309");
    	bankCode.put("浦发", "310");
    	bankCode.put("浦东", "310");
    	bankCode.put("北京", "314");
    	bankCode.put("上海", "325");
    	bankCode.put("邮储", "403");
    	bankCode.put("邮政", "403");
    }
}
