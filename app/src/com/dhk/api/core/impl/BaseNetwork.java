package com.dhk.api.core.impl;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dhk.api.core.NetworkInterface;
import com.dhk.api.tool.IOUtils;

public class BaseNetwork implements NetworkInterface {

	private URL murl = null;

	private String charset = "utf-8";

	public BaseNetwork(String baseurl) {
		try {
			murl = new URL(baseurl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("提供的url无效");
		}
	}

	private Map<String, String> cookies = new HashMap<String, String>();

	private String requestMethod = "POST";

	public void setRequestMethod(String method) {
		requestMethod = method;
	}

	public URL getUrl() {
		return murl;
	}

	public void setUrl(URL murl) {
		this.murl = murl;
	}

	/**
	 * 
	 * @return
	 */
	public String getEncode() {
		return charset;
	}

	public void setEncode(String encode) {
		this.charset = encode;
	}

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

	@Override
	public InputStream getInputstream(NetworkParams p) {
		StringBuffer buffer = new StringBuffer();
		OutputStream outputStream = null;
		InputStream inputStream = null;
		int responseCode = 99999;
		Map<String, String> params = p.getMap();
		try {
			if (!params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					buffer.append(URLEncoder.encode(entry.getKey(), charset))
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), charset))
							.append("&");
				}
			} else {
				throw new RuntimeException("参数不能为空");
			}
			buffer.deleteCharAt(buffer.length() - 1);
			HttpURLConnection connection = (HttpURLConnection) murl
					.openConnection();
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			connection.setRequestMethod(requestMethod);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			byte[] mydate = buffer.toString().getBytes(charset);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "zh-cn");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Cookie", getCokies());
			connection.setRequestProperty("Content-Length",
					String.valueOf(mydate.length));
			outputStream = connection.getOutputStream();
			outputStream.write(mydate, 0, mydate.length);
			outputStream.close();
			responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				String headerFild = connection.getHeaderField("Set-Cookie");
				handleCookies(headerFild);
				inputStream = connection.getInputStream();
				return inputStream;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			IOUtils.closeIt(inputStream);
		}
		return null;
	}

	@Override
	public String getResultStr(NetworkParams p) throws SocketTimeoutException{
		InputStream inputStream = null;
		ByteArrayOutputStream baos =null;
		OutputStream outputStream = null;
		Map<String, String> params = p.getMap();
		try {
			StringBuffer buffer = new StringBuffer();
			if (!params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					buffer.append(URLEncoder.encode(entry.getKey(), charset))
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), charset))
							.append("&");
				}
			} else {
				throw new RuntimeException("参数不能为空");
			}
			buffer.deleteCharAt(buffer.length() - 1);
			HttpURLConnection connection = (HttpURLConnection) murl
					.openConnection();
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			connection.setRequestMethod(requestMethod);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			byte[] mydate = buffer.toString().getBytes(charset);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "zh-cn");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Cookie", getCokies());
			connection.setRequestProperty("Content-Length",
					String.valueOf(mydate.length));
			outputStream = connection.getOutputStream();
			outputStream.write(mydate, 0, mydate.length);
			outputStream.close();
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				//得到输入流
				InputStream is = connection.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] date = new byte[1014 * 1024];
				int len = 0;
				while (-1 != (len = is.read(date))) {
					baos.write(date, 0, len);
					baos.flush();
				}
				return baos.toString(charset);

			} else {
				return null;
			}
		}   catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException("链接超时");
		}  catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeIt(inputStream);
			IOUtils.closeIt(baos);
		}
		return null;
	}

}
