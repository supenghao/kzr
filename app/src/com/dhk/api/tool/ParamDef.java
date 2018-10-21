package com.dhk.api.tool;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ParamDef {



	protected static Properties systemConf = null;




	
	public static String findConfName(String key) {
		String value = null;
		try{
			if (systemConf==null){
				//载入
				systemConf = new Properties();
				Resource resource = new ClassPathResource("systemConf.properties");
				InputStream in = resource.getInputStream();
				systemConf.load(in);
			}
			value = systemConf.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}
}
