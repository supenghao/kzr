package com.dhk.api.core.impl;

import java.text.ParseException;
import java.util.List;

import com.dhk.api.core.ICostPolicy;
import com.dhk.api.entity.CostPlan;
import com.dhk.api.entity.CostPolicy;
import com.dhk.api.entity.ParamFee;

public class CostPolicyDisp implements ICostPolicy {

	private ICostPolicy p = null;

	/**
	 * 消费策略转明细
	 * 
	 * @param fee
	 * @param o
	 */
	public CostPolicyDisp(ParamFee fee, CostPolicy o) {
		String mode = o.getRepeat_mode();
		if ("d".equals(mode)) {
			p = new DaysPolicy(fee, o);
		} else if ("w".equals(mode)) {
			p = new WeekPolicy(fee, o);
		} else if ("m".equals(mode)) {
			p = new MonthPolicy(fee, o);
		} else {
			new RuntimeException("repeat mode error!");
		}
	}

	@Override
	public List<String> detailDays() throws ParseException {
		return p.detailDays();
	}

	@Override
	public List<CostPlan> genDetailPolicy() throws ParseException {
		return p.genDetailPolicy();
	}

	@Override
	public double genExpense() throws ParseException {
		return p.genExpense();
	}

	@Override
	public double genExpense(List<CostPlan> ps) {
		return p.genExpense(ps);
	}
}
