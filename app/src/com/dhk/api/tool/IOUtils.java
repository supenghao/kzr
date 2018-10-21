package com.dhk.api.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class IOUtils {

	public static volatile int BUFFER_SIZE = 1024 * 4;

	/**
	 * 将数据从输入流流向输出流,流完默认关闭
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void flowTO(InputStream in, OutputStream out)
			throws IOException {
		flowTO(in, out, true);
	}

	/**
	 * 将数据从输入流流向输出流,流完选择关闭
	 * 
	 * @param in
	 * @param out
	 * @param close
	 * @throws IOException
	 */
	public static void flowTO(InputStream in, OutputStream out, boolean close)
			throws IOException {
		if (in == null || out == null) {
			throw new IllegalArgumentException("输入输出流是NULL");
		}
		byte[] buf = new byte[BUFFER_SIZE];
		int c = in.read(buf);
		while (c != -1) {
			out.write(buf, 0, c);
			c = in.read(buf);
		}
		if (close) {
			closeIt(in);
			closeIt(out);
		}
	}

	/**
	 * 获得输入流中的字符串,默认UTF-8编码
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getString(InputStream in) throws IOException {
		return getString(in, "utf-8");
	}

	/**
	 * 获得输入流中的字符串
	 * 
	 * @param in
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String getString(InputStream in, String charset)
			throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		StringBuilder b = new StringBuilder(BUFFER_SIZE);
		int c = in.read(buf);
		while (c != -1) {
			b.append(new String(buf, charset));
			c = in.read(buf);
		}
		closeIt(in);
		return b.toString();
	}

	/**
	 * 关闭输入流
	 * 
	 * @param in
	 */
	public static void closeIt(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭输出流
	 * 
	 * @param out
	 */
	public static void closeIt(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将输入流的数据装换成数组
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream in) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		byte[] ret = new byte[0];
		int c = in.read(buf);
		while (c != -1) {
			int y = ret.length;
			ret = Arrays.copyOf(buf, y + c);
			for (int i = 0; i < c; i++) {
				ret[y + i] = buf[i];
			}
			c = in.read(buf);
		}
		closeIt(in);
		return ret;
	}

	public static void main(String[] args) {
		byte[] buf = new byte[] { 0, 1, 2, 3, 4 };
		byte[] buf2 = new byte[0];
		int y = buf.length;
		byte[] ret = Arrays.copyOf(buf, y + buf2.length);
		for (int i = 0; i < buf2.length; i++) {
			ret[y + i] = buf2[i];
		}
		System.out.println(Arrays.toString(ret));
	}
}
