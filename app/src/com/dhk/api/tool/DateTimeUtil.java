package com.dhk.api.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	/**
	 * 获取当前日期时间字符串
	 * @param pattern
	 * @return
	 */
	public static String getNowDateTime(String pattern) {

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(c.getTime());
	}
	
	public static String getSpecifiedDayAfterNoSplit(String specifiedDay) {  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day + 1);  
  
        String dayAfter = new SimpleDateFormat("yyyyMMdd").format(c.getTime());  
        return dayAfter;  
    }
	public static String getCurrentDateTime(String format) {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}
	public static String getTime2Minute(String time,int minute) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
		String currendDate = getCurrentDateTime("yyyyMMdd");
		Date date = format.parse(currendDate+" " +time);
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
        cal.add(Calendar.MINUTE, minute);//24小时制
        date = cal.getTime(); 
        
        SimpleDateFormat f = new SimpleDateFormat("HHmmss");
        return f.format(date); 
	}
	public static void main(String[] args) throws Exception{
		int interval = 3;
		String tempStart = DateTimeUtil.getTime2Minute("130702", -interval);
		System.out.println("interval:"+interval+"+="+tempStart);
	}
}
