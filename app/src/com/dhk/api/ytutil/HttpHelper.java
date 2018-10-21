package com.dhk.api.ytutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
public class HttpHelper {
	public static final String GET = "GET";
	public static final String POST = "POST";

	public static String getNvPairs(List list, String charSet) {
		if (list == null || list.size() == 0) {
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			String[] nvPairStr = (String[]) list.get(i);
			try {
				if (i > 0) {
					stringBuffer.append("&");
				}
				stringBuffer.append(URLEncoder.encode(nvPairStr[0], charSet))
						.append("=").append(
								URLEncoder.encode(nvPairStr[1], charSet));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		return stringBuffer.toString();
	}

	public static String getNvPairs(Map<String, String> map, String charSet) {
		if (map == null || map.size() == 0) {
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer();
		Set<String> key = map.keySet();
		int i=0;
		for (Iterator it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			try {
				if(i>0){
					stringBuffer.append("&");
				}
				i++;
				stringBuffer.append(URLEncoder.encode(s, charSet)).append("=")
						.append(URLEncoder.encode(map.get(s), charSet));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
	/*	for (int i = 0; i < map; i++) {
			String[] nvPairStr = (String[]) list.get(i);
			try {
				if (i > 0) {
					stringBuffer.append("&");
				}
				stringBuffer.append(URLEncoder.encode(nvPairStr[0], charSet))
						.append("=").append(
								URLEncoder.encode(nvPairStr[1], charSet));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}*/
		return stringBuffer.toString();
	}
	public static String doHttp(String urlStr, String method, String charSet,
			String postStr, String timeOut) {
		if (method == null
				|| (!GET.equalsIgnoreCase(method) && !POST
						.equalsIgnoreCase(method))) {
			return null;
		}
		URL url = null;
		try {
			if ("GET".equals(method)){
				if(StringUtils.isBlank(postStr)){
					url = new URL(urlStr);
				}else{
					url = new URL(urlStr+"?"+postStr);
				}
			}
			else 
				url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		if ("https".equalsIgnoreCase(urlStr.substring(0, 5))) {
			SSLContext sslContext = null;
			try {
				sslContext = SSLContext.getInstance("TLS");
				X509TrustManager xtmArray[] = { new HttpX509TrustManager() };
				sslContext.init(null, xtmArray, new SecureRandom());
			} catch (GeneralSecurityException gse) {
				gse.printStackTrace();
			}
			if (sslContext != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
						.getSocketFactory());
			}
			HttpsURLConnection
					.setDefaultHostnameVerifier(new HttpHostnameVerifier());
		}
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		httpURLConnection.setRequestProperty("ContentType","text/xml;charset=utf-8"); 
		httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT //5.1)AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.46 Safari/535.11"); 
		httpURLConnection.setConnectTimeout(Integer.parseInt(timeOut));
        httpURLConnection.setReadTimeout(Integer.parseInt(timeOut));
		try {
			httpURLConnection.setRequestMethod(method.toUpperCase());
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		}
		if (POST.equalsIgnoreCase(method)) {
			httpURLConnection.setDoOutput(true);
			PrintWriter printWriter = null;
			try {
				printWriter = new PrintWriter(new OutputStreamWriter(
						httpURLConnection.getOutputStream(), charSet));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			printWriter.write(postStr);
			printWriter.flush();
		}
		InputStream inputStream = null;
		try {
			
			inputStream = httpURLConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int data = 0;
		try {
			int statusCode = httpURLConnection.getResponseCode();
			if (statusCode < HttpURLConnection.HTTP_OK
					|| statusCode >= HttpURLConnection.HTTP_MULT_CHOICE) {
				return null;
			}
			while ((data = inputStream.read()) != -1) {
				byteArrayOutputStream.write(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String serverCharset = null;
		String contentEncoding = httpURLConnection.getContentEncoding();
		String contentType = httpURLConnection.getContentType();
		if (serverCharset == null && contentEncoding != null) {
			serverCharset = contentEncoding;
		}
		if (serverCharset == null && contentType != null) {
			int pos = contentType.indexOf("; charset=");
			if (-1 != pos) {
				serverCharset = contentType.substring(pos + 10);
			}
		}
		if (serverCharset == null) {
			serverCharset = charSet;
		}
		byte[] returnBytes = byteArrayOutputStream.toByteArray();
		String returnStr = null;
		try {
			returnStr = new String(returnBytes, serverCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return returnStr;
	}
	
	
	
	
	public static HttpResponse doHttpGetResponse(String urlStr, String method,
			String charSet, String postStr, String timeOut) {
		if (method == null
				|| (!GET.equalsIgnoreCase(method) && !POST
						.equalsIgnoreCase(method))) {
			return null;
		}
		String myUrlStr = "";
		if (GET.equalsIgnoreCase(method)) {
			if (-1 == urlStr.indexOf("?")) {
				myUrlStr = urlStr + "?" + postStr;
			} else {
				myUrlStr = urlStr + "&" + postStr;
			}
		} else {
			myUrlStr = urlStr;
		}
		URL url = null;
		try {
			url = new URL(myUrlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		if ("https".equalsIgnoreCase(urlStr.substring(0, 5))) {
			SSLContext sslContext = null;
			try {
				sslContext = SSLContext.getInstance("TLS");
				X509TrustManager xtmArray[] = { new HttpX509TrustManager() };
				sslContext.init(null, xtmArray, new SecureRandom());
			} catch (GeneralSecurityException gse) {
				gse.printStackTrace();
			}
			if (sslContext != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
						.getSocketFactory());
			}
			HttpsURLConnection
					.setDefaultHostnameVerifier(new HttpHostnameVerifier());
		}
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		System.setProperty("sun.net.client.defaultConnectTimeout", timeOut);
		System.setProperty("sun.net.client.defaultReadTimeout", timeOut);
		try {
			httpURLConnection.setRequestMethod(method.toUpperCase());
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		}
		if (POST.equalsIgnoreCase(method)) {
			httpURLConnection.setDoOutput(true);
			PrintWriter printWriter = null;
			try {
				printWriter = new PrintWriter(new OutputStreamWriter(
						httpURLConnection.getOutputStream(), charSet));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			printWriter.write(postStr);
			printWriter.flush();
		}
		InputStream inputStream = null;
		try {
			inputStream = httpURLConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		int statusCode = 0;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			statusCode = httpURLConnection.getResponseCode();
			int data = 0;
			while ((data = inputStream.read()) != -1) {
				byteArrayOutputStream.write(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		byte[] returnBytes = byteArrayOutputStream.toByteArray();
		String returnStr = null;
		try {
			returnStr = new String(returnBytes, charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		// 返回
		HttpResponse httpRes = new HttpResponse();
		httpRes.setResponseCode(statusCode);
		httpRes.setBody(returnStr);
		return httpRes;
	}

}
