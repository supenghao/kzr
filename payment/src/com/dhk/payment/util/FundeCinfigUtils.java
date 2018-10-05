package com.dhk.payment.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FundeCinfigUtils {
	private static String propPath = "fundeConfig.properties";
    private static Properties prop     = null;

    static {        
        try {
        	prop = new Properties();
            Resource resource = new ClassPathResource(propPath);
        	InputStream in = resource.getInputStream();//ClassLoader.getSystemResourceAsStream(propPath);
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String keyName) {
        return prop.getProperty(keyName);
    }

    public static String getProperty(String keyName, String defaultValue) {
        return prop.getProperty(keyName, defaultValue);
    }
}
