package com.dhk.api.core.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.dhk.api.entity.CostPolicy;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.tool.M;

public class DaysPolicy extends CostTimePolicy {

	private String dayss;

	public DaysPolicy(ParamFee fee, CostPolicy dto) {
		super(fee, dto);
		String dayss = dto.getRepeat_detail();
		if (dayss.isEmpty()) {
			throw new RuntimeException(
					"days detail is null! example: 2 --> 每两天");
		}
		this.dayss = dayss;
	}

	@Override
	public List<String> detailDays() throws ParseException {
		int dayidle = Integer.parseInt(dayss);
		if (dayidle > 6) {
			if (dayidle > 7 || dayidle <= 0) {
				throw new RuntimeException("days detail must < 7 and > 0");
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
				ret.add(M.dformat.format(castart.getTime()));
				castart.add(Calendar.DAY_OF_MONTH, dayidle);
			}
		} else {
			throw new ParseException("开始时间不能大于结束时间", 0);
		}
		return ret;
	}
}
