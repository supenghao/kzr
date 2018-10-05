package com.dhk.kernel.dao.jdbc;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.dhk.kernel.util.DES;

public class EncryptablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{

	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		try{
			
			String encrypt = props.getProperty("encrypt");
			//System.out.println("encrypt:"+encrypt);
			if ("true".equals(encrypt)){
				String username = props.getProperty("username");
				String password = props.getProperty("password"); 
				//解密
				username = DES.Decrypt(username);
				password = DES.Decrypt(password);
				//System.out.println("username:"+username);
				props.setProperty("username", username);
				props.setProperty("password", password);
				 
			}
			super.processProperties(beanFactory, props);
		}catch (Exception e) {  
            e.printStackTrace();  
            throw new BeanInitializationException(e.getMessage());  
        }  
	}
}
