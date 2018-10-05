package com.dhk.payment.util;

import java.util.Random;
import java.util.UUID;

import com.dhk.kernel.util.IdWorker;
import com.dhk.kernel.util.StringUtil;

public class OrderNoUtil {

	public static String genOrderNo(String prefix,int len){
		
		IdWorker worker2 = new IdWorker(2);
		long nextId = worker2.nextId();
		int nextIdLen = Long.toString(nextId).length();
		if (len<nextIdLen)
			return nextId+"";
		
		len = len - nextIdLen;
		//prefix = StringUtil.makeLengthWith0InFront(prefix, len);
		int prefixLen = prefix.length();
		String temp = "";
		if (len>prefixLen){
			temp = getRandomString(len-prefixLen);
		}
		return temp+prefix+nextId;
	}
	
    private static String getRandomString(int length) { //length表示生成字符串的长度  
		
	    String baseChar= UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();//"ABCDEFGHIJKLMNOPQRSTUVWXYZ";   
	    String baseNum= UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {
	    	if(i%2==0){
	    		int number = random.nextInt(baseNum.length());
	    		sb.append(baseNum.charAt(number)); 
	    	}else{
	    		int number = random.nextInt(baseChar.length());  
	    		sb.append(baseChar.charAt(number));
	    	}
	    }     
	    return sb.toString();     
	 }
    
    public static String genRequestNo(String orderNo){
    	String time = StringUtil.getCurrentDateTime("HHmmssS");
		//System.out.println("time:"+time);
		String value = time.substring(time.length()-4,time.length())+orderNo.substring(0,6);
		String transNo =  OrderNoUtil.genOrderNo(value, 30);
		
		return transNo;
    }
	
	public static void main(String[] args) {
		for (int i=0;i<30;i++){
			String orderNo = "2017022412471700001056";
			String time = StringUtil.getCurrentDateTime("HHmmssS");
			System.out.println("time:"+time);
			String value = time.substring(time.length()-4,time.length())+orderNo.substring(0,6);
			System.out.println("OrderNoUtil:"+time.substring(time.length()-4,time.length()));
			String transNo =  OrderNoUtil.genOrderNo(value, 30);//worker2.nextId()+"";
		//System.out.println("OrderNoUtil:"+transNo);
		}
	}
}
