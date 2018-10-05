package com.dhk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.sunnada.kernel.util.StringUtil;

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
		String temp = "TT";
		if (len>prefixLen){
			temp += getRandomString(len-prefixLen);
		}
		return temp+prefix+nextId;
	}
	
    private static String getRandomString(int length) { //length表示生成字符串的长度  
		
	    String baseChar= UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();//"ABCDEFGHIJKLMNOPQRSTUVWXYZ";   
	    String baseNum="0123456789";
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
				
		str = OrderNoUtil.genOrderNo(str, 19);
		
		return str;
    }
	
	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String dateString = "2017/01/01 00:00:00";
		Date date = null;
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long s=date.getTime();
		System.out.println(s);
    	
		System.out.println(System.currentTimeMillis());

		System.out.println(System.currentTimeMillis()-s);
	}
}
