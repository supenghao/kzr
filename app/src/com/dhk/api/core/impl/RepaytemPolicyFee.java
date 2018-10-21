package com.dhk.api.core.impl;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.entity.RepayPlanTem;

public class RepaytemPolicyFee implements FeeCalculator {

	private JSONArray li;
	private List<ParamFee> fees;

	public RepaytemPolicyFee(JSONArray li, List<ParamFee> fees) {
		this.li = li;
		this.fees = fees;
	}

	@Override
	public double getfees() {
		BigDecimal di = new BigDecimal(0);// 费用
		BigDecimal totalAmount = new BigDecimal(0);// 费用

		int i;
		for( i=0;i<li.size();i++){
			JSONObject repayJson = li.getJSONObject(i);
			BigDecimal amount = new BigDecimal(repayJson.getString("repay_amount"));
			totalAmount=totalAmount.add(amount);
		}
		di = di.add(getItemFee("purchase", totalAmount));// 还款消费(同普通消费)
		di = di.add(getItemFee("quickl_proxy_pay", new BigDecimal("0")).multiply(new BigDecimal(i)));

		return di.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	private BigDecimal getItemFee(String sqlcode, BigDecimal amount) {
		ParamFee paraf = getParamFee(sqlcode);
		if ("0".equals(paraf.getFeetype())) {// 按百分比算费率
			BigDecimal tfee = amount.multiply(new BigDecimal(
					paraf.getFee() / 100));
			// System.out.println("fee:" + tfee);
			if (paraf.getLowerlimit() != 0
					&& tfee.doubleValue() < paraf.getLowerlimit()) {// 小于下限
				tfee = new BigDecimal(paraf.getLowerlimit());
			}
			if (paraf.getUplimit() != 0
					&& tfee.doubleValue() > paraf.getUplimit()) {// 大于上限
				tfee = new BigDecimal(paraf.getUplimit());
			}
			// System.out.println("fee2:" + tfee);
			tfee = tfee.add(new BigDecimal(paraf.getExternal()));
			return tfee;
		} else {// 按笔计算费率
			BigDecimal tfee = new BigDecimal(paraf.getFee());
			tfee = tfee.add(new BigDecimal(paraf.getExternal()));
			return tfee;
		}
	}

	@Override
	public double getTotalAmount() {
		BigDecimal di = new BigDecimal(0l);
		for(int i=0;i<li.size();i++){
			JSONObject repayJson = li.getJSONObject(i);
			BigDecimal s = new BigDecimal(repayJson.getString("repay_amount"));
			di = di.add(s);
		}
		return di.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public double getRequire() {
		double max = 0;

		for(int i=0;i<li.size();i++){
			JSONObject repayJson = li.getJSONObject(i);
			double t =repayJson.getDouble("repay_amount");
			if (t > max) {
				max = t;
			}

		}
		return new BigDecimal(max).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 获取费率
	 * 
	 * @param code
	 * @return
	 */
	ParamFee getParamFee(String code) {
		for (ParamFee fee : fees) {
			if (code.equals(fee.getCode())) {
				return fee;
			}
		}
		return null;
	}

	@Override
	public double getDatefees() {
		BigDecimal di = new BigDecimal(0);// 费用
		BigDecimal totalAmount = new BigDecimal(0);// 费用

		int i;
		for( i=0;i<li.size();i++){
			JSONObject repayJson = li.getJSONObject(i);
			BigDecimal amount = new BigDecimal(repayJson.getString("repay_amount"));
			totalAmount=totalAmount.add(amount);
			JSONArray repayCostList =  repayJson.getJSONArray("repayCostList");
			for(int j=0;j<repayCostList.size();j++) {
				JSONObject repayCost = repayCostList.getJSONObject(j);
				BigDecimal cost_amount = new BigDecimal(repayCost.getString("cost_amount"));
				di = di.add(getItemFee("purchase", cost_amount));// 还款消费(同普通消费)
			}
			
		}
		//di = di.add(getItemFee("purchase", totalAmount));// 还款消费(同普通消费)
		di = di.add(getItemFee("quickl_proxy_pay", new BigDecimal("0")).multiply(new BigDecimal(i)));

		return di.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public double getDateRequire() {
		double max = 0;
		for(int i=0;i<li.size();i++){
			JSONObject repayJson = li.getJSONObject(i);
			double t = 0;
			JSONArray repayCostList =  repayJson.getJSONArray("repayCostList");
			for(int j=0;j<repayCostList.size();j++) {
				JSONObject repayCost = repayCostList.getJSONObject(j);
				double cost_amount = repayCost.getDouble("cost_amount");
				t = t + cost_amount;
			}
			if (t > max) {
				max = t;
			}

		}
		return new BigDecimal(max).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
