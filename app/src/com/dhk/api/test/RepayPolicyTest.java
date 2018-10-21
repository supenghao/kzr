package com.dhk.api.test;

import java.text.ParseException;
import java.util.List;

import com.dhk.api.core.impl.RepayPolicyDisp;
import com.dhk.api.dto.RepayPlanDto;
import com.dhk.api.entity.CreditCard;
import com.dhk.api.entity.CreditcardBill;
import com.dhk.api.entity.RepayPlan;
import org.junit.Test;

public class RepayPolicyTest {
	@Test
	public void PolicyTest() throws ParseException {

		CreditCard card = new CreditCard();
		card.setId(3l);
		card.setCard_no("carno");
		RepayPlanDto dto = new RepayPlanDto();
		// dto.setPolicy_type("F");
		dto.setPolicy_type("C");
		dto.setRepeat_detail("27,28,1,2");
		dto.setTrans_amount("300");
		dto.setTrans_count("10");
		CreditcardBill bill = new CreditcardBill();
		bill.setBill_day("20170227");
		bill.setRepay_day("20170306");
		RepayPolicyDisp dis = new RepayPolicyDisp(card, dto);
		System.out.println(dis.detailDays());
		List<RepayPlan> dil = dis.genDetailPolicy();
		for (RepayPlan repayPlan : dil) {
			System.out.println(repayPlan);
		}
	}
}
