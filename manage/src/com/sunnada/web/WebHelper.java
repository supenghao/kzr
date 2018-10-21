package com.sunnada.web;

import org.apache.log4j.Logger;

import com.sunnada.kernel.sql.SQLConf;

public class WebHelper {
	static Logger logger = Logger.getLogger(WebHelper.class);
	
	public static void start() throws Exception{
		SQLConf.loadSql();
	}
}
