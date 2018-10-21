package com.dhk.api.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串的工具集
 *
 */
public class StrUtils {

	public static String EMPTY = "";

	/**
	 * 将字符串进行拼接
	 * 
	 * @param sourse
	 * @param js
	 * @return
	 */
	public static final String join(String sourse, Object... js) {
		if (js == null || sourse == null) {
			return sourse;
		}
		String sts[] = new String[js.length];
		for (int i = 0; i < js.length; i++) {
			if (js[i] != null) {
				sts[i] = js[i].toString();
			}
		}
		int le = sourse.length();
		for (String s : sts) {
			if (s != null) {
				le += s.length();
			}
		}
		StringBuilder buil = new StringBuilder(le);
		buil.append(sourse);
		for (String s : sts) {
			if (s != null) {
				buil.append(s);
			}
		}
		return buil.toString();
	}

	/**
	 * 将字符串进行拼接
	 * 
	 * @param js
	 * @return
	 */
	public static final String join(Object... js) {
		return join(EMPTY, js);
	}

	/**
	 * 字符串是否可用,不为空
	 * 
	 * @param test
	 * @return
	 */
	public static final boolean isEmpty(String test) {
		if (test == null || test.isEmpty())
			return true;
		return false;
	}
	
    public static String getMatcher(String regex, String source) {  
    	
        String result = "";  
        if(regex!=null && source!=null){
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(source);  
        while (matcher.find()) {  
            result = matcher.group(0);
        }  
        }
       return result;  
   }  
   
   
   public   static String getStrByRegex(String str,String regex){
//   	  String url = "http://news.163.com/17/1218/15/D5UU8PPK00018AOR_mobile.html";(?<=://)[a-zA-Z\.0-9]+(?=\/)
   	//[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)
      //   String regex = "(?<=://)[a-zA-Z\\.0-9]+(?=\\/)";
   	return getMatcher(regex,str);
   }
   public static void main(String arrg[]){
	String ss=   getStrByRegex("商户号已经存在[merchant_code=A055HX15659978913]", "\\[merchant_code\\=(.*?)\\]") ;
	System.out.println(ss);
   }
}

