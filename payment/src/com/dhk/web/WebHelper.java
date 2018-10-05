package com.dhk.web;

import org.apache.log4j.Logger;

import com.dhk.kernel.sql.SQLConf;

public class WebHelper {
	static Logger logger = Logger.getLogger(WebHelper.class);
	
	public static void start() throws Exception{
		SQLConf.loadSql();
	}
}
