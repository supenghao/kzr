package com.dhk.api.core.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.dhk.api.entity.ParamFee;
import com.dhk.api.tool.M;
import com.dhk.api.entity.CostPolicy;

public class MonthPolicy extends CostTimePolicy {

	private String mouths;

	public MonthPolicy(ParamFee fee, CostPolicy o) {
		super(fee, o);
		String mouths = o.getRepeat_detail();
		if (mouths.isEmpty()) {
			throw new RuntimeException(
					"mouth detail is null! example:1,2,7-> 每月1,每月2和每月7号");
		}
		this.mouths = mouths;
	}

	@Override
	public List<String> detailDays() throws ParseException {
		String mouth[] = mouths.split(",");
		for (String w : mouth) {
			int dayidle = Integer.parseInt(w);
			if (dayidle > 28 || dayidle <= 0) {
				throw new RuntimeException("days detail must <= 28 and > 0");
			}
		}
		Calendar castart = Calendar.getInstance();
		Calendar caend = Calendar.getInstance();
		Date dastart = M.dformat.parse(start);
		Date daend = M.dformat.parse(end);
		castart.setTime(dastart);
		caend.setTime(daend);
		List<String> ret = new LinkedList<String>();
		if (castart.compareTo(caend) <= 0) {
			Calendar mm = Calendar.getInstance();
			for (String sw : mouth) {
				mm.setTime(dastart);
				int iw = Integer.parseInt(sw);
				mm.set(Calendar.DAY_OF_MONTH, iw);
				int i=0;
				while (mm .compareTo(caend)<= 0) {
					if(i>=12){      //跨度不能大于一年
						break;
					}
					if (mm.compareTo(caend) <= 0 && mm.compareTo(castart) >= 0) {
						ret.add(M.dformat.format(mm.getTime()));
					}
					mm.add(Calendar.MONTH, 1);
					i++;
				}
			}
		} else {
			throw new ParseException("开始时间不能大于结束时间", 0);
		}
		Collections.sort(ret);
		return ret;
	}

}
