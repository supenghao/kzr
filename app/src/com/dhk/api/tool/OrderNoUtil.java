package com.dhk.api.tool;

import java.util.Random;
import java.util.UUID;

import com.xdream.kernel.util.StringUtil;

public class OrderNoUtil {

	public static String genOrderNo(String prefix,int len){
		
		IdWorker worker2 = new IdWorker(2);
		long nextId = worker2.nextId();
		int nextIdLen = Long.toString(nextId).length();
		if (len<nextIdLen)
			return nextId+"";
		
		len = len - nextIdLen -2;
		//prefix = StringUtil.makeLengthWith0InFront(prefix, len);
		int prefixLen = prefix.length();
		String temp = "PL";
		if (len>prefixLen){
			temp += getRandomString(len-prefixLen);
		}
		return temp+prefix+nextId;
	}
	
    private static String getRandomString(int length) { //length表示生成字符串的长度  
		
	    String baseChar= UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();//"ABCDEFGHIJKLMNOPQRSTUVWXYZ";   
	    String baseNum = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();;
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
    
    public static String genOrderNo(long userId,String phone){
    	String str = userId+phone.substring(phone.length()-3,phone.length());
		String time = StringUtil.getCurrentDateTime("HHmmssS");
		
		str += time.substring(time.length()-2,time.length());
				
		str = OrderNoUtil.genOrderNo(str, 32);
		
		return str;
    }
	
	public static void main(String[] args) {
//		String phoneNum = "13850198582";
//		String id = "11112";
//		for (int i=1;i<30;i++){
//			String str = id+phoneNum.substring(phoneNum.length()-3,phoneNum.length());
//			String time = StringUtil.getCurrentDateTime("HHmmssS");
//			
//			str += time.substring(time.length()-4,time.length());
//					
//			str = OrderNoUtil.genOrderNo(str, 32);
//		System.out.println("OrderNoUtil:"+str);
//		}
		String time = StringUtil.getCurrentDateTime("HHmmssS");
		System.out.println("dddddddddddd:"+OrderNoUtil.genOrderNo(time, 32));
	}
}
