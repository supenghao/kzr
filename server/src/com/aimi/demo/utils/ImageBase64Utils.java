package com.aimi.demo.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageBase64Utils {

	// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	public static String getImageStr(String imgFile) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		// 返回Base64编码过的字节数组字符串
		return Base64.encodeBase64String(data);
	}

}
