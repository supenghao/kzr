package com.dhk.api.core.impl;

import java.math.BigDecimal;

import com.dhk.api.entity.ParamFee;

public class BankFeeCule {

	private ParamFee paraf;
	private String amount;

	public BankFeeCule(String amount, ParamFee fees) {
		this.amount = amount;
		this.paraf = fees;
	}

	private BigDecimal getItemFee(BigDecimal amount) {
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

	/**
	 * 按笔计算银行支付的费率
	 * 
	 * @return
	 */
	public double bankPayFee() {// 按笔计算,按费率计算失败
		return getItemFee(new BigDecimal(amount)).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
