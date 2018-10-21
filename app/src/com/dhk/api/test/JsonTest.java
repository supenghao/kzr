package com.dhk.api.test;

import java.util.Calendar;

import org.apache.log4j.Logger;

public class JsonTest {
	private static Logger logger = Logger.getLogger(JsonTest.class);

	public static void main(String[] args) {
		for (int i = 1; i < 28; i++) {
			jsonTest2(i);
		}
	}

	public static void jsonTest(int now) {
		int bday = 1;
		int pday = 28;
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.DAY_OF_MONTH, now);
		Calendar cnow = Calendar.getInstance();
		cnow.setTime(c1.getTime());
		if (bday < pday) {// 同月
			if (bday < now && now < pday) {
				
				System.out.println("yes");
				return;
			}
		} else {// 不同月
			Calendar cbdate = Calendar.getInstance();
			cbdate.setTime(c1.getTime());
			Calendar cpdate = Calendar.getInstance();
			cpdate.setTime(c1.getTime());
			if (now < pday) {// 上个月
				cbdate.set(Calendar.DAY_OF_MONTH, bday);
				cbdate.add(Calendar.MONTH, -1);// 加一个月
				cpdate.set(Calendar.DAY_OF_MONTH, pday);
			} else {
				cbdate.set(Calendar.DAY_OF_MONTH, bday);
				cpdate.set(Calendar.DAY_OF_MONTH, pday);
				cpdate.add(Calendar.MONTH, 1);// 加一个月
			}
			if (cnow.after(cbdate) && cnow.before(cpdate)) {
				System.out.println("Yes");
				return;
			}
		}
	}

	public static void jsonTest2(int now) {
		int bday = 20;
		int rday = 10;
		if (now == 1)
			System.out.println(bday + "-" + rday);
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.DAY_OF_MONTH, now);
		Calendar cnow = Calendar.getInstance();
		cnow.setTime(c1.getTime());
		if (bday < rday) {// 同月
			if (bday < now && now < rday) {
				System.out.println(now + ":yes");
				return;
			}
		} else {// 不同月
			if ((now > rday && now > bday) || (now < rday && now < bday)) {
				System.out.println(now + ":yes");
				return;
			}
		}
		System.out.println("No");
	}
}
