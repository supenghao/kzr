package com.dhk.api.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

	public static volatile int BUFFER_SIZE = 1024 * 4;

	/**
	 * 删除文件(夹)
	 * 
	 * @param file
	 * 
	 */
	public static final void deleteFiles(File file) {
		if (file == null)
			return;
		if (file.exists()) {
			if (file.isDirectory()) {// 如果是个文件夹,必须删除里面的文件后再删除文件夹
				File fs[] = file.listFiles();
				for (int i = 0; i < fs.length; i++) {
					deleteFiles(fs[i]);// 递归调用
				}
				file.delete();
			} else {
				file.delete();
			}
		}
	}

	/**
	 * 获得带缓冲的文件读对象,线程安全的
	 * 
	 * @param filename
	 * @return 记得close();
	 * @throws FileNotFoundException
	 */
	public final static BufferedReader getBuffReader(String filename) throws FileNotFoundException {
		File file = new File(filename);
		return getBuffReader(file);
	}

	/**
	 * 获得带缓冲的文件读对象,线程安全的
	 * 
	 * @param file
	 * @return 记得close();
	 * @throws FileNotFoundException
	 */
	public final static BufferedReader getBuffReader(File file) throws FileNotFoundException {
		FileReader reader = new FileReader(file);
		BufferedReader reader2 = new BufferedReader(reader);
		return reader2;
	}

	/**
	 * 获得缓冲流写文件对象,线程安全的
	 * 
	 * @param filename
	 * @param append
	 *            是否追加写入文件
	 * @return 记得调用close();close()中会调用flush();
	 * @throws IOException
	 */
	public final static BufferedWriter getBuffWriter(String filename, boolean append) throws IOException {
		File file = new File(filename);
		return getBuffWriter(file, append);
	}

	/**
	 * 获得缓冲流写文件对象,线程安全的
	 * 
	 * @param file
	 * @param append
	 *            是否追加写入文件
	 * @return 记得调用close();close()中会调用flush();
	 * @throws IOException
	 */
	public static final BufferedWriter getBuffWriter(File file, boolean append) throws IOException {
		FileWriter out = new FileWriter(file, append);
		BufferedWriter writer2 = new BufferedWriter(out);
		return writer2;
	}

	/**
	 * 复制文件到destFile
	 * 
	 * @param srcFile
	 * @param destFile
	 * @param overlay
	 * @return
	 */
	public static final boolean copyFile(File srcFile, File destFile, boolean overlay) {
		if (srcFile == null || destFile == null)
			return false;
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			throw new RuntimeException("复制源文件不存在！");
		} else if (!srcFile.isFile()) {
			throw new RuntimeException("复制文件失败，源文件不是一个文件！");
		}
		// 判断目标文件是否存在
		if (destFile.exists()) {
			// 如果目标文件存在并允许覆盖
			if (overlay) {
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件
				destFile.delete();
			}
		} else {
			// 如果目标文件所在目录不存在，则创建目录
			if (!destFile.getParentFile().exists()) {
				// 目标文件所在目录不存在
				if (!destFile.getParentFile().mkdirs()) {
					// 复制文件失败：创建目标文件所在目录失败
					return false;
				}
			}
		}
		// 复制文件
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			IOUtils.flowTO(in, out);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			IOUtils.closeIt(in);
			IOUtils.closeIt(out);
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static final boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
		if (srcFileName == null || destFileName == null || srcFileName.isEmpty() || destFileName.isEmpty())
			throw new RuntimeException("传入文件名称不能为null or Enpty");
		File srcFile = new File(srcFileName);
		File destFile = new File(destFileName);
		return copyFile(srcFile, destFile, overlay);
	}

	/**
	 * 复制整个目录的内容
	 * 
	 * @param srcDirName
	 *            待复制目录的目录名
	 * @param destDirName
	 *            目标目录名
	 * @param overlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static final boolean copyDirectory(String srcDirName, String destDirName, boolean overlay) {
		// 判断源目录是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists()) {
			throw new RuntimeException("复制目录失败：源目录" + srcDirName + "不存在！");
		} else if (!srcDir.isDirectory()) {
			throw new RuntimeException("复制目录失败：" + srcDirName + "不是目录！");
		}

		// 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		File destDir = new File(destDirName);
		// 如果目标文件夹存在
		if (destDir.exists()) {
			// 如果允许覆盖则删除已存在的目标目录
			if (overlay) {
				new File(destDirName).delete();
			} else {
				throw new RuntimeException("复制目录失败：目的目录" + destDirName + "已存在！");
			}
		} else {
			// 创建目的目录
			if (!destDir.mkdirs()) {
				System.out.println("复制目录失败：创建目的目录失败！");
				return false;
			}
		}

		boolean flag = true;
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 复制文件
			if (files[i].isFile()) {
				flag = copyFile(files[i].getAbsolutePath(), destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			} else if (files[i].isDirectory()) {
				flag = copyDirectory(files[i].getAbsolutePath(), destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			}
		}
		if (!flag) {
			System.out.println("复制目录" + srcDirName + "至" + destDirName + "失败！");
			return false;
		} else {
			return true;
		}
	}

}
