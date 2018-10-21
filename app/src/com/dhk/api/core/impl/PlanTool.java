package com.dhk.api.core.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.dhk.api.entity.RepayPlan;
import com.dhk.api.entity.RepayPlanTem;

public class PlanTool {
	protected static Logger logger = Logger.getLogger(PlanTool.class);
	public static String genRandomTime(String beginTime, String endTime)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date bginDate = sdf.parse(beginTime);
		Date endDate = sdf.parse(endTime);
		long timeStamp = ThreadLocalRandom.current().nextLong(bginDate.getTime(), endDate.getTime()+1);
		return sdf.format(timeStamp);
	}

	public static String getCurrentDateTime(String format) {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}
	
	public static String fullTime(String time){
		if (StringUtils.isBlank(time)){
			return "";
		}
		String hour = time.substring(0,2);
		String minite = time.substring(2,4);
		String second = time.substring(4,6);
		return hour + ":" + minite + ":" + second;
	}
	
	public static String paresTime(String time){
		if (StringUtils.isBlank(time)){
			return "";
		}
		return time.replaceAll(":","");
	}
	
	public static String getTime(String time,int hour) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currendDate = getCurrentDateTime("yyyy-MM-dd");
		Date date = format.parse(currendDate+" " +time);
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
        cal.add(Calendar.HOUR_OF_DAY, hour);//24小时制
        date = cal.getTime(); 
        
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        return f.format(date); 
	}
	public static String getTimeNoSplit(String time,int hour) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currendDate = getCurrentDateTime("yyyy-MM-dd");
		Date date = format.parse(currendDate+" " +time);
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
        cal.add(Calendar.HOUR_OF_DAY, hour);//24小时制
        date = cal.getTime(); 
        
        SimpleDateFormat f = new SimpleDateFormat("HHmmss");
        return f.format(date); 
	}
	
	public static int compareTime(String time1,String time2){
	   String time1T = paresTime(time1);
	   String time2T = paresTime(time2);
	   return compareNoSpiltTime(time1T,time2T);
	}
	public static int compareNoSpiltTime(String time1,String time2){
		   int compare = 0;
		   Long time1L = 0l;
		   Long time2L = 0l;
		   if (!time1.equals("")){
			   time1L = Long.parseLong(time1);
		   }
		   if (!time2.equals("")){
			   time2L = Long.parseLong(time2);
		   }
		   if (time1L==time2L){
			   compare = 0;
		   }else if (time1L>time2L){
			   compare = 1;
		   }else if (time1L<time2L){
			   compare = -1;
		   }
		   return compare;
	}
	/**
	 * 检查插入的计划后是否会超过一天笔数的限制
	 * 
	 * @param ilist
	 *            准备插入的计划
	 * @param listd
	 *            已经存在的计划
	 * @param maxDayCount
	 * @return
	 */
	public static String checkDaysMaxCount(List<RepayPlan> ilist,
			List<RepayPlanTem> listd, int maxDayCount) {
		if (ilist == null || ilist.isEmpty())
			return "true";
		LinkedList<RepayPlan> tem = new LinkedList<RepayPlan>();
		tem.addAll(ilist);
		for (RepayPlanTem t : listd) {// 插入已经存在的列表
			RepayPlan e = new RepayPlan();
			e.setRepay_day(t.getRepay_day());
			e.setPolicy_type(t.getPolicy_type());
			tem.add(e);
		}
		for (int i = 0; i < tem.size(); i++) {// 剔除普通还款
			RepayPlan tr = tem.get(i);
			if (!"F".equals(tr.getPolicy_type())) {
				tem.remove(i);
				i--;
			}
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (RepayPlan tr : tem) {
			String date = tr.getRepay_day();
			Integer tei = map.get(date);
			if (tei == null) {
				map.put(date, 1);
			} else {
				map.put(date, ++tei);
			}
		}
		//System.out.println("已有:" + listd);
		//System.out.println("map:" + map);
		Set<String> keys = map.keySet();
		LinkedList<String> days = new LinkedList<String>();
		for (String key : keys) {
			int num = map.get(key);
			if (num > maxDayCount) {
				days.add(key);
			}
		}
		if (days.isEmpty())
			return "true";
		else {
			String rets = "";
			for (String day : days) {
				rets += day.substring(6, 8) + ",";
			}
			if (!rets.isEmpty()) {
				rets = rets.substring(0, rets.length() - 1);
			}
			if (listd.isEmpty()) {
				return "添加失败!快速还款计划一天不得超过" + maxDayCount + "条,请尝试增加天数或减少笔数";
			} else {
				return "添加失败! " + rets + "号快速还款计划已超过一天" + maxDayCount + "条的限制";
			}
		}
	}

	/**
	 * 计算时间
	 * @param time HH:mm:ss
	 * @param second
	 * @return
	 */
	public static String  computeTime(String time,long second)  {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			Date date  = sdf.parse(time);
			long l=date.getTime()+second*1000;
			return  sdf.format(l);
		}catch (ParseException e){
			e.printStackTrace();
			return  time;
		}
	}

	/**
	 *
	 * @param time
	 * @param pattern
	 * @param ms  需要加减的毫秒数
	 * @return
	 */
	public static long getTimeStamp(String time,String pattern,long ms){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Date date  = sdf.parse(time);
			return  date.getTime()+ms;
		}catch (ParseException e){
			e.printStackTrace();
			return  0;
		}

	}


	public static String getTime2Minute(String time,int minute) throws RuntimeException {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currendDate = getCurrentDateTime("yyyy-MM-dd");
			Date date = format.parse(currendDate+" " +time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, minute);//24小时制
			date = cal.getTime();

			SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
			return f.format(date);
		}catch (ParseException e){
			throw  new RuntimeException (e.getMessage());
		}

	}

	/**
	 * 检查新增加计划中的时间是否含有上一批次的时间(存在计划的时间)
	 * 
	 * @param newps
	 * @param had
	 * @return
	 */
	public static String checkSetPlanDays(List<RepayPlan> newps,
			List<RepayPlan> had) {
		//System.out.println("newps:" + newps);
		//System.out.println("had:" + had);
		if (had.isEmpty())// 不存在任何计划
			return "true";
		LinkedList<RepayPlan> tem = new LinkedList<RepayPlan>();
		tem.addAll(newps);
		Set<String> set = new HashSet<String>();
		for (RepayPlan re : had) {
			set.add(re.getRepay_day());
		}
		Set<String> days = new HashSet<String>();
		for (RepayPlan re : tem) {
			if (set.contains(re.getRepay_day())) {
				days.add(re.getRepay_day());
			}
		}
		if (days.isEmpty())
			return "true";
		else {
			String rets = "";
			for (String day : days) {
				rets += day.substring(6, 8) + ",";
			}
			if (rets.length() > 1) {
				rets = rets.substring(0, rets.length() - 1);
			}
			return "添加失败! 本期" + rets + "号已有计划正在执行,不得重复设置";
		}
	}
	
	public static int randomMinute(int begin,int end) {
		Random random = new Random();
		return random.nextInt(end + 1 - begin)+ begin;
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
	public static void main(String[] args) throws Exception{
		int interval = PlanTool.randomMinute(5, 15);
		String tempStart = PlanTool.getTime2Minute("13:07:00", -interval);
		System.out.println("interval:"+interval+"+="+tempStart);
	}
}
