package com.dhk.api.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author 丘春华
 * @date 2015-2-20
 */
public class HttpConnection {
	
	private URL url = null;

	/**
	 * @author 丘春华
	 * @param url
	 *            连接的Url
	 */
	public HttpConnection(URL url) {
		this.url = url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	private Map<String, String> cookies = new HashMap<String, String>();

	/**
	 * 保存Cookies
	 * 
	 * @param headerFild
	 */
	private void handleCookies(String headerFild) {
		if (headerFild == null)
			return;
		String c[] = headerFild.split(";");
		for (String kv : c) {
			String ks[] = kv.trim().split("=");
			cookies.put(ks[0], ks[1]);
		}
	}

	/**
	 * 获取Cookies
	 * 
	 * @return
	 */
	private String getCokies() {
		if (cookies.size() == 0) {
			return "";
		}
		StringBuilder build = new StringBuilder(10 + cookies.size() * 10);
		Set<String> set = cookies.keySet();
		for (String key : set) {
			build.append(key);
			build.append("=");
			build.append(cookies.get(key));
			build.append("; ");
		}
		build.delete(build.length() - 2, build.length() - 1);
		return build.toString();
	}

	/**
	 * 获得网络连接的输入流,可以传文件
	 * 
	 * @param params
	 *            符合规则的map请求数据
	 * @return 网络连接的输入流
	 */
	public InputStream doRequest(Map<String, String> params) {
		String ecode = "utf-8";
		StringBuffer buffer = new StringBuffer();
		OutputStream outputStream = null;
		InputStream inputStream = null;
		int responseCode = 99999;
		try {
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					buffer.append(URLEncoder.encode(entry.getKey(), ecode)).append("=")
							.append(URLEncoder.encode(entry.getValue(), ecode)).append("&");
				}
			}
			buffer.deleteCharAt(buffer.length() - 1);
			//System.out.println("--doRequest->" + buffer.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(4000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			byte[] mydate = buffer.toString().getBytes(ecode);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "zh-cn");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Cookie", getCokies());
			// System.out.println("request Cookie--->" + getCokies());
			connection.setRequestProperty("Content-Length", String.valueOf(mydate.length));
			outputStream = connection.getOutputStream();
			outputStream.write(mydate, 0, mydate.length);
			outputStream.close();
			responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				String headerFild = connection.getHeaderField("Set-Cookie");
				// System.out.println("Set-Cookie:" + headerFild);
				handleCookies(headerFild);
				inputStream = connection.getInputStream();
				return inputStream;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (inputStream != null) {
				try {
					inputStream.close();// 关闭输入流
					inputStream = null;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 将输入流转化为String
	 * 
	 * @param inputStream
	 * @return 错误时返回null
	 */
	public String changeToString(InputStream inputStream) {
		String encode = "utf-8";
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		byte[] date = new byte[1014 * 1024];
		int len = 0;
		String result = null;
		if (inputStream != null) {
			try {
				len = inputStream.read(date);
				while (len != -1) {
					arrayOutputStream.write(date, 0, len);
					len = inputStream.read(date);
				}
				inputStream.close();
				result = new String(arrayOutputStream.toByteArray(), encode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
