package com.dhk.api.test;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class MyTest {

	@Test
	public void testRandom() {
		DecimalFormat format = new DecimalFormat(".###");
		for (int i = 100; i < 1000000; i++) {
			double money = i;
			int num = 4;
			double[] mos = getMoney(money, num);
			double total = 0;
			for (double d : mos) {
				total += d;
			}
			total = Double.parseDouble(format.format(total));
			if (money != total) {
				System.out.println(money + "!=" + total);
				System.out.println(Arrays.toString(mos));
			}
		}
	}

	private RandomAmount getRandomAmount(int count, double total) {
		RandomAmount ret = new RandomAmount();
		if (count <= 1) {
			ret.firstamount = total;
			ret.elseamount = 0;
		} else {
			double single = total / count;
			int sinin = (int) single;
			ret.firstamount = total - (sinin * (count - 1));
			ret.elseamount = sinin;
		}
		return ret;
	}

	private class RandomAmount {
		private double firstamount, elseamount;

		@Override
		public String toString() {
			return "RandomAmount [firstamount=" + firstamount + ", elseamount="
					+ elseamount + "]";
		}

	}

	public static double[] getMoney(double money, int num) {
		Random r = new Random();
		DecimalFormat format = new DecimalFormat(".##");
		double middle = Double.parseDouble(format.format(money / num));
		double[] dou = new double[num];
		double redMoney = 0;
		double nextMoney = money;
		double sum = 0;
		int index = 0;
		for (int i = num; i > 0; i--) {
			if (i == 1) {
				dou[index] = nextMoney;
			} else {
				while (true) {
					String str = format.format(r.nextDouble() * nextMoney);
					redMoney = Double.parseDouble(str);
					if (redMoney > 0 && redMoney < middle) {
						break;
					}
				}
				nextMoney = Double.parseDouble(format.format(nextMoney
						- redMoney));
				sum = sum + redMoney;
				dou[index] = redMoney;
				middle = Double.parseDouble(format.format(nextMoney / (i - 1)));
				index++;
			}
		}
		return dou;
	}
}
