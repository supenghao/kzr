package com.dhk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseNetwork implements NetworkInterface{

	private URL murl = null;

	private String charset = "utf-8";

	private static final Logger log= LogManager.getLogger();

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

			connection.setConnectTimeout(5000);
			connection.setReadTimeout(50000);
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
				log.error("responseCode:" + responseCode);
				return null;
			}
		} catch (IOException e) {
			log.error(e.getMessage());
			IOUtils.closeIt(inputStream);
		}
		return null;
	}

	public String getResultStr(NetworkParams p) {
		InputStream in = getInputstream(p);
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		byte[] date = new byte[1014 * 1024];
		int len = 0;
		String result = null;
		if (in != null) {
			try {
				len = in.read(date);
				while (len != -1) {
					arrayOutputStream.write(date, 0, len);
					len = in.read(date);
				}
				result = new String(arrayOutputStream.toByteArray(), charset);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(in!=null){
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

}
