package com.dhk.init;

import java.util.HashMap;
import java.util.Map;

import com.dhk.api.entity.ParamFee;

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
    public static  Map<String,ParamFee> feeMaps = new HashMap<>();
    
    public static String customertel="4000034336" ;//客服电话
    
    
    /**
     * 汇享天成
     */
    public static String appId;//
    public static String transType;//
    public static String requestUrl;//
    /**
     * 消费报户参数
     */
    
    public static String pmerNo;//服务商编码
    public static String key;//密钥
    public static String asynNotifyUrl;//异步回调地址
    public static String synNotifyUrl;//同步回调地址
    public static String postUrl;//报户请求地址
    
    
    public static String bktdtype;//通道
    public static String kjmerNo; 
    public static String kjkey; 
    public static String kjpostUrl; 
    
    
   
    public static String USER_ID; 
    public static String AGENT_NO; 
    public static String BG_URL; 
    public static String PAGE_URL; 
    
    public static String GOODS_NAME; 
    public static String GOODS_DESC;
    
    public static String BUS_CODE_JX; 
    public static String BUS_CODE_YX;
    
    public static String YX_USER_ID; 
    public static String JX_USER_ID;
    
    public static String YT_POSTURL;
    
    public static String YTJX_POSTURL;
    public static Map<String,String> bankCode = new  HashMap<>();//银行编码
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
    	bankCode.put("邮储", "404");
    	bankCode.put("邮政", "404");
    }
    
	/**
	 * 获取银行限额
	 * @param bankName
	 * @return
	 */
	public static Map<String,String> getBankLimitMap(String bankName){
		Map<String,String> limit = null;
		Map<String,String> bankLimitMap = new HashMap<String, String>();
		bankLimitMap.put("工商", "20000-20000");
		bankLimitMap.put("中国", "20000-20000");
		bankLimitMap.put("建设", "10000-10000");
		bankLimitMap.put("邮政", "10000-10000");
		bankLimitMap.put("邮储", "10000-10000");
		bankLimitMap.put("光大", "10000-10000");
		bankLimitMap.put("兴业", "20000-50000");
		bankLimitMap.put("浦发", "10000-10000");
		bankLimitMap.put("浦东", "10000-10000");
		bankLimitMap.put("广发", "20000-50000");
		bankLimitMap.put("民生", "10000-10000");
		bankLimitMap.put("平安", "10000-10000");
		bankLimitMap.put("花旗", "20000-20000");
		bankLimitMap.put("北京", "20000-20000");
		bankLimitMap.put("华夏", "20000-50000");
		bankLimitMap.put("上海", "20000-50000");
		bankLimitMap.put("招商", "20000-50000");
		bankLimitMap.put("农业", "20000-50000");
		bankLimitMap.put("交通", "20000-50000");
		bankLimitMap.put("中信", "20000-50000");
		  for (Map.Entry<String, String> entry : bankLimitMap.entrySet()) {
			   if(bankName!=null&&bankName.contains(entry.getKey())){
				   limit = new HashMap<>();
				   limit.put("singleLimit", entry.getValue().split("-")[0]);
				   limit.put("singleDayLimit", entry.getValue().split("-")[1]);
				   break;
			   }
			  }
		return limit;
	}
}
