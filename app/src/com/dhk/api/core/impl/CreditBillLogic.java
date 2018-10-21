package com.dhk.api.core.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.dhk.api.entity.CreditCard;
import com.dhk.api.entity.CreditcardBill;
import com.dhk.api.tool.M;

public class CreditBillLogic {

	@Deprecated
	private static CreditcardBill getEmptyBill(CreditCard card) {
		return null;
		// CreditcardBill bill = new CreditcardBill();
		// bill.setBill_amount("0.00");
		// String bill_day = card.getBill_date();// 信用卡表的账单日
		// String repay_day = card.getRepay_date();// 信用卡表的还款日
		// int bday = Integer.parseInt(bill_day);
		// int rday = Integer.parseInt(repay_day);
		// if (bday != rday) {
		// Calendar cb = getNowCalander();
		// Calendar cr = getNowCalander();
		// Calendar now = getNowCalander();
		// if (bday < rday) {// 同月
		// cb.set(Calendar.DAY_OF_MONTH, bday);
		// cr.set(Calendar.DAY_OF_MONTH, rday);
		// bill.setBill_day(M.dformat.format(cb.getTime()));
		// bill.setRepay_day(M.dformat.format(cr.getTime()));
		// return bill;
		// } else {// 下月
		//
		// }
		// } else {
		// throw new RuntimeException("账单日和还款日不能是同一天");
		// }
		// return null;
	}

	/**
	 * 处理信用卡表的账单日与信用卡账单表账单日的关系,归并到信用卡账单对象
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static CreditcardBill disposeBillDate(CreditCard card) {
		String bd = card.getBill_date();// 信用卡表的账单日
		String rd = card.getRepay_date();// 信用卡表的还款日
		CreditcardBill	bill = new CreditcardBill();
		int bday = Integer.parseInt(bd);
		int pday = Integer.parseInt(rd);
		Calendar cnow = Calendar.getInstance();
		int now = cnow.get(Calendar.DAY_OF_MONTH);
		if (bday < pday) {// 同月
			if (bday < now && now < pday) {
				Calendar tem = Calendar.getInstance();
				tem.set(Calendar.DAY_OF_MONTH, bday);
				bill.setBill_day(M.dformat.format(tem.getTime()));
				tem.set(Calendar.DAY_OF_MONTH, pday);
				bill.setRepay_day(M.dformat.format(tem.getTime()));
				return bill;
			} else {
				return null;//不是还款时间
			}
		} else {// 不同月
			Calendar cbdate = Calendar.getInstance();
			cbdate.setTime(cnow.getTime());
			Calendar cpdate = Calendar.getInstance();
			cpdate.setTime(cnow.getTime());
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
				bill.setBill_day(M.dformat.format(cbdate.getTime()));
				bill.setRepay_day(M.dformat.format(cpdate.getTime()));
				return bill;
			} else {
				return null;//不是还款时间
			}
		}
	}

	/**
	 * 获取当前yyyyMMdd格式的时间
	 * 
	 * @return
	 */
	private static Calendar getNowCalander() {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(M.dformat.parse(M.dformat.format(new Date())));
		} catch (ParseException e1) {
			// e1.printStackTrace();
		}
		return c;
	}

	/**
	 * 检查账单日和还款日是否有效
	 * 
	 * @param bill
	 * @return
	 */
	public static boolean checkBill_RepayDay(CreditcardBill bill) {
		if (bill == null) {
			System.out.println("checkBill_RepayDay bill:" + bill);
			return false;
		}
		String billday = bill.getBill_day();
		String repayDay = bill.getRepay_day();
		Calendar cbill = Calendar.getInstance();
		Calendar crepay = Calendar.getInstance();
		try {
			cbill.setTime(M.dformat.parse(billday));
			crepay.setTime(M.dformat.parse(repayDay));
		} catch (ParseException e) {
			e.printStackTrace();
			M.logger.error("账单表日期格式有误");
			return false;
		}
		if (cbill.before(crepay)) {// 账单日必须小于还款日
			Calendar now = Calendar.getInstance();
			if (now.after(cbill) && now.before(crepay)) {// 设置的账单日还没到则设置成前一个月
				return true;
			} else {
				return false;
			}
		} else {
			M.logger.error("账单表日期有误(账单日必须在还款日之前)!");
			return false;
		}
	}
}
