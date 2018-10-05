package com.aimi.demo.utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Signature {
	/**
	 * 签名算法
	 * 
	 * @param o
	 *            要参与签名的数据对象
	 * @return 签名
	 * @throws IllegalAccessException
	 */
	public static String getSign(Map<String, Object> map, String key) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null && !"".equals(entry.getValue().toString())) {
				list.add(entry.getKey() + "=" + entry.getValue().toString() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + key;
		result = MD5.MD5Encode(result).toUpperCase();
		return result;
	}

	/**
	 * 检验数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
	 * 
	 * @return API签名是否合法
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public static boolean checkIsSignValidFromMap(Map<String, Object> map, String key) {
		boolean isCheck = true;
		try {
			map.remove("class");
			String signFromAPIResponse = map.remove("sign").toString();
			if (signFromAPIResponse == "" || signFromAPIResponse == null) {
				isCheck = false;
			}
			// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
			String signForAPIResponse = Signature.getSign(map, key);

			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验不过，表示这个API返回的数据有可能已经被篡改了
				isCheck = false;
			}
		} catch (Exception e) {
			isCheck = false;
			e.printStackTrace();
		}
		return isCheck;
	}

}
