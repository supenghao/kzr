package com.dhk.api.core.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import com.dhk.api.core.ICostPolicy;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.entity.CostPlan;
import com.dhk.api.entity.CostPolicy;

/**
 * 消费计划策略
 * 
 */
public abstract class CostTimePolicy implements ICostPolicy {

	/**
	 * 手续费率
	 */
	// private BigDecimal rate;// = new BigDecimal((RATE / 100));
	// private BigDecimal external;// = new BigDecimal((RATE / 100));
	protected double expense = 0;
	protected ParamFee fee;
	protected String carNO, costAmount, start, end;
	protected String policyId = null;
	protected String user_id = null;

	private List<CostPlan> ps = null;

	protected CostTimePolicy(ParamFee fee, CostPolicy dto) {
		this.fee = fee;
		user_id = dto.getUser_id();
		policyId = dto.getId() + "";
		if (policyId == null) {
			throw new RuntimeException("CostPolicy id can not be null");
		}
		carNO = dto.getCard_no();
		if (carNO == null) {
			throw new RuntimeException("CostPolicy cardno can not be null");
		}
		costAmount = dto.getTrans_amount();
		double t = Double.parseDouble(costAmount);
		if (t <= 0 || costAmount == null) {
			throw new RuntimeException(
					"CostPolicy costAmount can not be null or below zero");
		}
		start = dto.getRepeat_bengin_date();
		end = dto.getRepeat_end_date();
	}

	@Override
	public abstract List<String> detailDays() throws ParseException;

	@Override
	public List<CostPlan> genDetailPolicy() throws ParseException {
		if (ps != null)
			return ps;
		List<String> days = detailDays();
		List<CostPlan> ret = new LinkedList<CostPlan>();
		for (String day : days) {
			CostPlan c = new CostPlan();
			c.setCard_no(carNO);
			c.setCost_amount(costAmount);
			c.setCost_datetime(day);
			c.setUser_id(user_id);
			c.setCost_policy_id(policyId);
			c.setStatus("0");
			ret.add(c);
		}
		ps = ret;
		return ps;
	}

	/**
	 * 生成计划所需要的手续费
	 * 
	 * @return
	 * @throws ParseException
	 */
	public double genExpense() throws ParseException {
		if (expense != 0)
			return expense;
		List<CostPlan> ps = genDetailPolicy();
		return genExpense(ps);
	}

	/**
	 * 生成计划所需要的手续费
	 * 
	 * @return
	 * @throws ParseException
	 */
	public double genExpense(List<CostPlan> ps) {
		if (expense != 0)
			return expense;
		BigDecimal di = new BigDecimal(0l);
		for (CostPlan p : ps) {
			BigDecimal s = new BigDecimal(p.getCost_amount());
			di = di.add(getItemFee(s));
		}
		expense = di.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
		return expense;
	}

	private BigDecimal getItemFee(BigDecimal amount) {
		if ("0".equals(fee.getFeetype())) {// 按百分比算费率
			BigDecimal tfee = amount
					.multiply(new BigDecimal(fee.getFee() / 100));
			if (fee.getLowerlimit() != 0
					&& tfee.doubleValue() < fee.getLowerlimit()) {// 小于下限
				tfee = new BigDecimal(fee.getLowerlimit());
			}
			if (fee.getUplimit() != 0 && tfee.doubleValue() > fee.getUplimit()) {// 大于上限
				tfee = new BigDecimal(fee.getUplimit());
			}
			tfee = tfee.add(new BigDecimal(fee.getExternal()));
			return tfee;
		} else {// 按笔计算费率
			BigDecimal tfee = new BigDecimal(fee.getFee());
			tfee = tfee.add(new BigDecimal(fee.getExternal()));
			return tfee;
		}
	}
}
