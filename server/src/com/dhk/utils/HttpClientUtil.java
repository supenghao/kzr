package com.dhk.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;



/**
 * @ClassName:HttpClientUtil
 * @Author :Gnaily
 * @CreateDate:2016-3-17下午10:40:16
 * @Description:
 */
public class HttpClientUtil {
	private static CloseableHttpClient httpClient = null;
	private  static final RequestConfig globalConfig = RequestConfig.custom().
			setCookieSpec(CookieSpecs.BEST_MATCH).build();
	private  static final RequestConfig localConfig = RequestConfig.copy(globalConfig)
            .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
            .build();
	
	public static CloseableHttpClient getHttpClient() {
		if (httpClient == null){
			//httpClient = HttpClients.createDefault();
			
			httpClient=HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
			return httpClient;
		}
		return httpClient;
	}
	
	public static HttpPost  getHttpPost(String url){
		HttpPost httpPost=new HttpPost(url);
		httpPost.setConfig(localConfig);
		return httpPost;
	}
	
	public static HttpGet  getHttpGet(String url){
			HttpGet httpGet=new HttpGet(url);
			httpGet.setConfig(localConfig);
			return httpGet;
	}
	/**
	 * 
	 * @param url
	 * @param paramStr
	 */
	public static Object  httpGet(String url,String paramStr,OnResponseListener responseListener){
		CloseableHttpClient http = httpClient=HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
		HttpGet httpGet = HttpClientUtil.getHttpGet(url+"?"+paramStr);
		try {
			HttpResponse response= http.execute(httpGet);
			return responseListener.onResponseReceived(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return responseListener.onError(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return responseListener.onError(e.getMessage());
		} finally {
			try {
				http.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			http = null;
		}
	}				
	
	public interface OnResponseListener{
		Object onResponseReceived(HttpResponse response);
		Object onError(String msg);
	}
}
