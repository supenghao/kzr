package com.dhk.api.third;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public static ThirdResponseObj http2Nvp(String url,List<BasicNameValuePair> nvps) throws Exception{
		HttpClient httpClient = new SSLClient();
		HttpPost postMethod = new HttpPost(url);
		
		postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		//HttpResponse resp = null;
		HttpResponse resp = httpClient.execute(postMethod);
        
        int statusCode = resp.getStatusLine().getStatusCode();
        
        ThirdResponseObj responseObj = new ThirdResponseObj();
        if (200 == statusCode) {
        	responseObj.setCode("success");
        	String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
        	responseObj.setResponseEntity(str);
        }else{
        	responseObj.setCode(statusCode+"");
        }
        
        return responseObj;
	}
	public static ThirdResponseObj http2Se(String url,StringEntity entity) throws Exception{
		HttpClient httpClient = new SSLClient();
		HttpPost postMethod = new HttpPost(url);
		
		postMethod.setEntity(entity);
		//HttpResponse resp = null;
		HttpResponse resp = httpClient.execute(postMethod);
        
        int statusCode = resp.getStatusLine().getStatusCode();
        
        ThirdResponseObj responseObj = new ThirdResponseObj();
        if (200 == statusCode) {
        	responseObj.setCode("success");
        	String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
        	responseObj.setResponseEntity(str);
        }else{
        	responseObj.setCode(statusCode+"");
        }
        
        return responseObj;
	}
}
