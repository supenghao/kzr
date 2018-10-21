package com.dhk.api.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.dhk.api.core.impl.CostPolicyDisp;
import com.dhk.api.entity.CostPlan;
import com.dhk.api.entity.CreditcardBill;
import com.dhk.api.entity.ParamFee;
import org.junit.Test;

import com.dhk.api.core.impl.RepayPolicyDisp;
import com.dhk.api.dto.RepayPlanDto;
import com.dhk.api.entity.CostPolicy;
import com.dhk.api.entity.CreditCard;

/**
 * 策略转明细测试
 * 
 */
public class PolicyTest {

	// @Test
	public void repayPolicyTest() throws ParseException {
		RepayPlanDto dto = new RepayPlanDto();
		dto.setPolicy_type("D");
		dto.setRepeat_detail("28,12,5,7,8,9,10");
		dto.setTrans_amount("1000");
		CreditcardBill bill = new CreditcardBill();
		bill.setBill_day("20170129");
		bill.setBill_amount("2000");
		CreditCard card = new CreditCard();
		card.setId(12L);
		List<ParamFee> li = new ArrayList<ParamFee>();
		ParamFee f = new ParamFee();
		f.setCode("normal_proxy_pay");
		f.setFee(0.2);
		f.setExternal(1d);
		li.add(f);
		f = new ParamFee();
		f.setCode("quickl_proxy_pay");
		f.setFee(0.3);
		f.setExternal(1d);
		li.add(f);
		f = new ParamFee();
		f.setCode("repay_purchase");
		f.setFee(0.4);
		f.setExternal(1d);
		li.add(f);
		RepayPolicyDisp di = new RepayPolicyDisp(card, dto);
		System.out.println(di.detailDays());
		//System.out.println("手续费:" + di.genExpense());
		//System.out.println("" + di.repayEnough());
	}

	@Test
	public void daysPolicyTest() throws ParseException {
		CostPolicy o = new CostPolicy();
		o.setCard_no("23489978478643486434");
		o.setUser_id("1");
		o.setId(12l);
		o.setTrans_amount("100");
		o.setTrans_count("3");
		o.setPolicy_type("F");
		String s = "20170220", e = "20170224", w = "1";
		o.setRepeat_bengin_date(s);
		o.setRepeat_end_date(e);
		o.setRepeat_mode("d");
		o.setRepeat_detail(w);
		ParamFee fee = new ParamFee();
		fee.setExternal(1D);
		fee.setFeetype("0");
		fee.setFee(0.53D);
		fee.setLowerlimit(1D);
		fee.setUplimit(0D);
		//RepaytemPolicyFee ca = new RepaytemPolicyFee(li, fees);
		CostPolicyDisp d = new CostPolicyDisp(fee, o);
		List<CostPlan> pl = d.genDetailPolicy();
		List<String> li = d.detailDays();
		// for (String p : li) {
		// System.out.println(p);
		// }
		System.out.println(li);
		System.out.println(s + "~" + e + "每" + w + "天:");
		System.out.println("size:" + pl.size());
		System.out.println("手续费:" + d.genExpense());
	}

	// @Test
	public void weekPolicyTest() throws ParseException {
		CostPolicy o = new CostPolicy();
		o.setCard_no("23489978434478686434");
		o.setUser_id("1");
		o.setId(12l);
		o.setTrans_amount("201.32");
		o.setTrans_count("3");
		o.setPolicy_type("p");
		String s = "20170101", e = "20170217", w = "1,3,7";
		o.setRepeat_bengin_date(s);
		o.setRepeat_end_date(e);
		o.setRepeat_mode("w");
		o.setRepeat_detail(w);
		ParamFee fee = new ParamFee();
		fee.setExternal(1D);
		fee.setFeetype("0");
		fee.setFee(0.5D);
		fee.setLowerlimit(0D);
		fee.setUplimit(0D);
		CostPolicyDisp d = new CostPolicyDisp(fee, o);
		List<String> li = d.detailDays();
		// for (String p : li) {
		// System.out.println(p);
		// }
		System.out.println(li);
		List<CostPlan> pl = d.genDetailPolicy();
		System.out.println(s + "~" + e + "每周" + w + ":");
		for (CostPlan p : pl) {
			System.out.println(p);
		}
		System.out.println("费率:0.5%         手续费:" + d.genExpense());
	}

	// @Test
	public void mouthPolicyTest() throws ParseException {
		CostPolicy o = new CostPolicy();
		o.setCard_no("23489978434478686434");
		o.setUser_id("1");
		o.setId(12l);
		o.setTrans_amount("201.32");
		o.setTrans_count("3");
		o.setPolicy_type("p");
		String s = "20170213", e = "20170331", w = "2,4,8,10,12,14,16,23,28";
		o.setRepeat_bengin_date(s);
		o.setRepeat_end_date(e);
		o.setRepeat_mode("m");
		o.setRepeat_detail(w);
		ParamFee fee = new ParamFee();
		fee.setExternal(1D);
		fee.setFeetype("0");
		fee.setFee(0.5D);
		fee.setLowerlimit(0D);
		fee.setUplimit(0D);
		CostPolicyDisp d = new CostPolicyDisp(fee, o);
		List<String> li = d.detailDays();
		// for (String p : li) {
		// System.out.println(p);
		// }
		System.out.println(li);
		List<CostPlan> pl = d.genDetailPolicy();
		System.out.println(s + "~" + e + "每月" + w + ":");
		for (CostPlan p : pl) {
			System.out.println(p.getCost_datetime());
		}
		System.out.println("费率:0.5%         手续费:" + d.genExpense());
	}
}
