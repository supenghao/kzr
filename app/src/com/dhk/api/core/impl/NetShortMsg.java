package com.dhk.api.core.impl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import com.dhk.api.tool.M;
import com.dhk.api.core.ShortMsg;
import com.dhk.api.tool.HttpConnection;
import org.junit.Assert;
import org.junit.Test;
import com.alibaba.fastjson.JSONObject;

/**
 * 使用网络发送手机验证码接口
 * 
 * @author Jerry
 * 
 */
public class NetShortMsg implements ShortMsg {
	private final Pattern p = Pattern.compile("^\\d{11}");

	@Override
	public String sendCheckCode(String phone, String url) {
		if (url == null || phone == null)
			return null;
		if (!p.matcher(phone).matches()) {
			return null;
		}
		URL urls = null;
		try {
			urls = new URL(url);
		} catch (MalformedURLException e) {
			M.logger.debug("验证码接口无效!!!!!");
			e.printStackTrace();
			return null;
		}
		HttpConnection conn = new HttpConnection(urls);
		String r = getRandomInt() + "";
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		params.put("checkCode", r);
		InputStream in = conn.doRequest(params);
		String ret = conn.changeToString(in);
		try {
			M.logger.debug("checkCode:" + r);
			M.logger.debug("sendCheckCode:" + new String(ret.getBytes(), "GBK"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (ret != null && !ret.isEmpty()) {
			JSONObject j = JSONObject.parseObject(ret);
			if ("0000".equals(j.getString("code"))) {
				return r;
			}
		}
		return null;
	}

	@Override
	public boolean sendMsg(String phone, String msg) {
		return false;
	}

	private final Random r = new Random();

	private final int BASENUMBER = 100000;

	/**
	 * 获取一个6位随机数
	 * 
	 * @return
	 */
	public int getRandomInt() {
		int tem = BASENUMBER;
		int i = r.nextInt(900000);
		int res = tem + i;
		return res;
	}

	/**
	 * 随机数测试
	 */
	// @Test
	public void Randomtest() {
		int i = 10000000;
		int r = 0;
		while (i-- > 0) {
			int f = getRandomInt();
			if (f < 100000 || f > 999999) {
				System.out.println("E:" + f);
				r++;
			} else {
				System.out.println("O:" + f);
			}
		}
		System.out.println("R:" + r);
	}

	@Test
	public void sendTest() {
		M.debug = false;
		String r = sendCheckCode("130557223471", "");
		// String phone = "130722347877";
		// Pattern p = Pattern.compile("^\\d{11}");
		// if (!p.matcher(phone).matches()) {
		// System.out.println(false);
		// return;
		// }
		Assert.assertNotNull(r);
		M.debug = true;
	}
}
