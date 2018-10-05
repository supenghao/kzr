package com.dhk.payment.util;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ParamDef {



	protected static Properties yilian = null;
	protected static Properties fr = null;
	protected static Properties gzf = null;
	protected static Properties hlb = null;
	protected static Properties hxtc = null;
	protected static Properties ybl = null;
	protected static Properties kj = null;


	
	public static String findYiLianByName(String key){
		String value = null;
		try{
			if (yilian==null){
				//载入
				yilian = new Properties();
				Resource resource = new ClassPathResource("yilianConfig.properties");
				InputStream in = resource.getInputStream();
				yilian.load(in);
			}
			value = yilian.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();			
		}finally{
			return value;
		}
	}

	public static String findFrByName(String key){
		String value = null;
		try{
			if (fr==null){
				//载入
				fr = new Properties();
				Resource resource = new ClassPathResource("frConfig.properties");
				InputStream in = resource.getInputStream();
				fr.load(in);
			}
			value = fr.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}

	public static String findGzfByName(String key){
		String value = null;
		try{
			if (gzf==null){
				//载入
				gzf = new Properties();
				Resource resource = new ClassPathResource("gzfConfig.properties");
				InputStream in = resource.getInputStream();
				gzf.load(in);
			}
			value = gzf.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}

	public static String findHlbByName(String key) {
		String value = null;
		try{
			if (hlb==null){
				//载入
				hlb = new Properties();
				Resource resource = new ClassPathResource("hlbConfig.properties");
				InputStream in = resource.getInputStream();
				hlb.load(in);
			}
			value = hlb.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}
	public static String findHXTCByName(String key) {
		String value = null;
		try{
			if (hxtc==null){
				//载入
				hxtc = new Properties();
				Resource resource = new ClassPathResource("hxtcConfig.properties");
				InputStream in = resource.getInputStream();
				hxtc.load(in);
			}
			value = hxtc.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}
	
	public static String findYBLByName(String key) {
		String value = null;
		try{
			if (ybl==null){
				//载入
				ybl = new Properties();
				Resource resource = new ClassPathResource("yblConfig.properties");
				InputStream in = resource.getInputStream();
				ybl.load(in);
			}
			value = ybl.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}
	
	
	public static String findKjByName(String key) {
		String value = null;
		try{
			if (kj==null){
				//载入
				kj = new Properties();
				Resource resource = new ClassPathResource("kjConfig.properties");
				InputStream in = resource.getInputStream();
				kj.load(in);
			}
			value = kj.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}
}
