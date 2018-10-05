package com.dhk.kernel.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dhk.kernel.DreamConf;
import com.dhk.kernel.dao.jdbc.Ids;

public class StringUtil {

	public static String byte2hex(byte[] b) {
	    String hs = "";
	    String stmp = "";
	    for (int i = 0; i < b.length; i++) {
	      stmp = Integer.toHexString(b[i] & 0xFF);
	      if (stmp.length() == 1)
	        hs += "0" + stmp;
	      else
	        hs += stmp;
	    }
	    return hs.toUpperCase();
	  }
	//编码转换
	public static String unescape (String src){
		        if (isEmpty(src)) return "";
		        
			    StringBuffer tmp = new StringBuffer();
			    tmp.ensureCapacity(src.length());
			    int lastPos=0,pos=0;

			    char ch;

			    while (lastPos<src.length()){

			     pos = src.indexOf("%",lastPos);

			     if (pos == lastPos){

			      if (src.charAt(pos+1)=='u'){
				       ch = (char)Integer.parseInt(src.substring(pos+2,pos+6),16);
				       tmp.append(ch);
				       lastPos = pos+6;
			       }else{
				       ch = (char)Integer.parseInt(src.substring(pos+1,pos+3),16);
				       tmp.append(ch);
				       lastPos = pos+3;
			       }
			    }else{
			      if (pos == -1){
			       tmp.append(src.substring(lastPos));
			       lastPos=src.length();
			      }else{
				       tmp.append(src.substring(lastPos,pos));
				       lastPos=pos;
			       }
			      }
			    }
			    return tmp.toString();
	}
	/**
	 * 判断对象是否Empty(null或元素为0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}
	public static String encoding(String value) throws Exception{
		if (value==null)
			return "";
//		byte[] tmp = value.getBytes("iso8859-1");
//		value = new String(tmp,"utf-8");
//		byte[] tmp = value.getBytes("utf-8");
		byte[] tmp = value.getBytes("iso8859-1");
		//System.out.println("aaa:"+new String(tmp,"iso8859-1"));
		//System.out.println("aaa:"+new String(tmp,"gbk"));
		value = new String(tmp,"gbk");
		return value;
	}
	// 格式化为空的字符串，如果不为空则去空格，为空则值改为"";
	public static String formatNullString(String str) throws Exception {
		if (str == null) {
			str = "";
		} else {
			str = str.trim();
		}
		return str;
	}
	
	public static String formatDate(Date date,String format){
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(format);
		
		return datetimeFormat.format(date);
	}
	
	/**
	 * 返回当前日期时间字符串<br>
	 * 默认格式:yyyy-mm-dd hh:mm:ss
	 * 
	 * @return String 返回当前字符串型日期时间
	 */
	public static String getCurrentTime() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}
	
	
	public static String layYear(String format,int years){
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+years);
		Date date=curr.getTime();
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
	/** 
	* 获得指定日期的前一天 
	* @param specifiedDay 
	* @return 
	* @throws Exception 
	*/ 
	public static String getSpecifiedDayBefore(String specifiedDay,String format){ 
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar c = Calendar.getInstance(); 
		Date date=null; 
		try { 
			date = new SimpleDateFormat(format).parse(specifiedDay); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		c.setTime(date); 
		int day=c.get(Calendar.DATE); 
		c.set(Calendar.DATE,day-1); 
	
		String dayBefore=new SimpleDateFormat(format).format(c.getTime()); 
		return dayBefore; 
	} 
	public static String layMonth(String format,int months){
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+months);
		Date date=curr.getTime();
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
	public static String layWeek(String format,int weeks){
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)+(7*weeks));
		Date date=curr.getTime();
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}
	
	public static String getCurrentDate() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}
	
	public static String getCurrentDateTime(String format) {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}
	
	public static String intToHex(int value,int len){
	      char[] c = new char[len];
	      int tmpInt = value;
	      for (int i=len-1;i>=0;i--){
	              c[i] = (char)(tmpInt % 256);
	              tmpInt/=256;
	      }
	      return new String(c);

	}
	public static int hexToInt(byte b[]) {
        int len = b.length;
        int returnInt = 0;
        int tmpInt = 0;
        tmpInt = b[0];
        if (tmpInt<0){
                tmpInt +=256;
        }
        returnInt += tmpInt ;
        for (int i=1;i<len;i++){
                tmpInt = b[i];
                if (tmpInt<0){
                    tmpInt +=256;
            }
                returnInt = returnInt*256+tmpInt;
        }
        return returnInt;

//	int len = b.length;
//	int[] tmp = new int[len];
//	int returnInt = 0;
//	for (int i=0;i<len;i++){
//		if (b[i]<0){
//			tmp[i] = b[i] + 256;
//		}else{
//			tmp[i] = b[i];
//		}
//
//		int tmpInt = 1;
//		for (int j=i;j<len-1;j++){//len=3
//			tmpInt *= 256;
//		}
//		returnInt += tmp[i]*tmpInt;
//	}
//	return returnInt;//(tmp[0]*256*256 + tmp[1] * 256 + tmp[2]);
	}
	
	/**
     * 对字符串补0取定长，不够前补0，太长截取
     * @author linfux
     * @param value
     * @param len
     * @return
     */
    public static String makeLengthWith0InFront(String value, int len) {
      try {
    	String returnStr = value.trim();
        int i = returnStr.length();
        while (i < len) {
          returnStr = "0" + returnStr;
          i++;
        }
        if(i>len){
        	returnStr = returnStr.substring(0,len);
        } 
        return returnStr;
      }
      catch (Exception e) {
        return null;
      }
    }
	
    public static void debug(String print){
    	boolean isDebug = false;
        if (DreamConf.getPropertie("isDebug")==null || DreamConf.getPropertie("isDebug").trim().equals("")){
        	isDebug = true;
        }else{
	        if (DreamConf.getPropertie("isDebug").trim().equals("yes")){
	        	isDebug = true;
	        }else{
	        	isDebug = false;
	        }
        }
        if (isDebug){
	    	String now = StringUtil.getCurrentDateTime("yyy-MM-dd HH:mm:ss");
			System.out.println("[debug-"+now+"] "+print);
        }
    }
    
    public static List<Ids> parseIds(String str){
    	String[] ids = str.split(",");
    	List<Ids> list = new ArrayList<Ids>();
    	for(String s:ids){
    		//System.out.println("aaaaaaaaaa:"+s);
    		Ids id = new Ids();
    		id.setId(Long.parseLong(s));
    		list.add(id);
    	}
    	return list;
    }
    /**
     * 是否超级管理员
     * @param role_id
     * @return
     */
    public static boolean isAdmin(Long role_id){
    	if (role_id==1)
    		return true;
    	else
    		return false;
    }
	
    
	public static void main(String[] args) throws Exception{
//	   String aa = "test.maps.xml";
//	   int index = aa.indexOf(".");
//	   System.out.println("aaa:"+aa.substring(0,index)+"==="+aa.substring(index,aa.length()));
	   //StringUtil.parseIds("2,3,");
//		int cLen =5;
//		int maxCol = 3;
//		for(int i=0;i < cLen;i++){
//			if(i%maxCol ==0){
//				System.out.println("====tr===");
//			}
//			System.out.println("---------td--------------");
//		}
//		Double test = Double.parseDouble("3");
//		System.out.println("aaaa:"+((test%1000)>0));
//		System.out.println("bbb:"+(0.00000<=0));
		//BigDecimal aa = new BigDecimal("1");
		
		//System.out.println("aaaa:"+StringUtil.getSpecifiedDayBefore("2017-02-22","yyyy-MM-dd"));
		
		String insuredCertificateNo = "350121198110045212";
		String insuredBirthday = insuredCertificateNo.substring(6,10) +"-"+ insuredCertificateNo.substring(10,12) +"-"+ insuredCertificateNo.substring(12,14);
		System.out.println("insuredBirthday:"+insuredBirthday);
		
		if (insuredCertificateNo.length()!=18){
			insuredBirthday = StringUtil.getCurrentDateTime("yyyy-MM-dd");
		}else{
			insuredBirthday = insuredCertificateNo.substring(6,10) +"-"+ insuredCertificateNo.substring(10,12) +"-"+ insuredCertificateNo.substring(12,14);
			
			
		}
		System.out.println("insuredBirthday:"+insuredBirthday);
	}
		
}
