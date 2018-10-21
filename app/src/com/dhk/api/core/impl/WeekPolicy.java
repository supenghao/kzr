package com.dhk.api.core.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.dhk.api.entity.CostPolicy;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.tool.M;

public class WeekPolicy extends CostTimePolicy {

	private String weeks;

	public WeekPolicy(ParamFee fee, CostPolicy dto) {
		super(fee, dto);
		String weeks = dto.getRepeat_detail();
		if (weeks.isEmpty()) {
			throw new RuntimeException(
					"weeks detail is null! example:1,2,7-> 每周1,每周2和每周天");
		}
		this.weeks = weeks;
	}

	@Override
	public List<String> detailDays() throws ParseException {
		String week[] = weeks.split(",");
		for (String w : week) {
			int dayidle = Integer.parseInt(w);
			if (dayidle > 7 || dayidle <= 0) {
				throw new RuntimeException("days detail must <= 7 and > 0");
			}
		}
		Calendar castart = Calendar.getInstance();
		Calendar caend = Calendar.getInstance();
		Date startd = M.dformat.parse(start);
		Date starte = M.dformat.parse(end);
		List<String> ret = new LinkedList<String>();
		caend.setTime(starte);
		castart.setTime(startd);
		if (castart.compareTo(caend) <= 0) {
			for (String sw : week) {
				castart.setTime(startd);
				int iw = Integer.parseInt(sw);
				int cw = castart.get(Calendar.DAY_OF_WEEK);// 星期天是1
				if (iw != cw) {
					int i = 0;
					while (true) {
						i++;
						cw = ++cw == 8 ? 1 : cw;
						if (iw == cw)
							break;
					}
					castart.add(Calendar.DAY_OF_MONTH, i);
				}
				while (castart.compareTo(caend) <= 0) {
					ret.add(M.dformat.format(castart.getTime()));
					castart.add(Calendar.DAY_OF_MONTH, 7);
				}
			}
		} else {
			throw new ParseException("开始时间不能大于结束时间", 0);
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * 实现方式二
	 * 
	 * @throws ParseException
	 */
	protected void detailDays2() throws ParseException {
		String week[] = weeks.split(",");
		for (String w : week) {
			int dayidle = Integer.parseInt(w);
			if (dayidle > 7 || dayidle <= 0) {
				throw new RuntimeException("days detail must <= 7 and > 0");
			}
		}
		Calendar castart = Calendar.getInstance();
		Calendar caend = Calendar.getInstance();
		Date startd = M.dformat.parse(start);
		Date starte = M.dformat.parse(end);
		castart.setTime(startd);
		caend.setTime(starte);
		List<String> ret = new LinkedList<String>();
		if (castart.compareTo(caend) <= 0) {
			while (castart.compareTo(caend) <= 0) {
				int startw = castart.get(Calendar.DAY_OF_WEEK);// 星期天是1
				if (weeks.contains(startw + "")) {
					ret.add(M.dformat.format(castart.getTime()));
				}
				castart.add(Calendar.DAY_OF_MONTH, 1);
			}
		} else {
			throw new ParseException("开始时间不能大于结束时间", 0);
		}
		// return ret;
	}
}
