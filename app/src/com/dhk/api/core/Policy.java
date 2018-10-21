package com.dhk.api.core;

import java.text.ParseException;
import java.util.List;

public interface Policy {

	/**
	 * 根据消费策略生成详细消费的具体日期
	 * 
	 * @return
	 * @throws ParseException
	 */
	List<String> detailDays() throws ParseException;

}
