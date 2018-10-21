package com.dhk.api.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * dao,service代码生成器
 * 
 * @author bian
 * 
 */
public class GenEntityMysql2 {

	private String entityPackageOutPath = "com.xdream.api.entity";// 指定实体生成所在包的路径
	private String daoInterfacePackageOutPath = "com.xdream.api.dao";// 指定Dao接口生成所在包的路径
	private String daoInterfaceImpPackageOutPath = "com.xdream.api.dao.impl";// 指定Dao接口实现类生成所在包的路径
	private String serviceInterfacePackageOutPath = "com.xdream.api.service";// 指定Service接口实现类生成所在包的路径
	private String serviceInterfaceImpPackageOutPath = "com.xdream.api.service.impl";// 指定Service接口实现类生成所在包的路径
	private String sqlPath = "";
	private String authorName = "qch";// 作者名字

	private String tablename = "t_s_user";// 表名

	private String className = "Users";// 类名

	private String[] colnames; // 列名数组

	private String[] colTypes; // 列名类型数组

	private int[] colSizes; // 列名大小数组

	private boolean f_util = true; // 是否需要导入包java.util.*

	private boolean f_sql = false; // 是否需要导入包java.sql.*
	private String BASEURL = "";

	// 数据库连接
	private static final String URL = "jdbc:mysql://localhost:3306/ioka";
	private static final String NAME = "root";
	private static final String PASS = "root";
	private static final String DRIVER = "com.mysql.jdbc.Driver";

	/*
	 * 构造函数
	 */
	public GenEntityMysql2() {
		File directory = new File("");
		String outputPath = "";
		System.out.println("start");
		// entity class
		String content = entity();
		outputPath = directory.getAbsolutePath() + "/src/" + BASEURL
				+ this.entityPackageOutPath.replace(".", "/") + "/"
				+ initcap(className) + ".java";
		genJavaFile(content, outputPath);

		// dao interface
		content = daoInterface();
		outputPath = directory.getAbsolutePath() + "/src/" + BASEURL
				+ this.daoInterfacePackageOutPath.replace(".", "/") + "/I"
				+ initcap(className) + "Dao.java";
		genJavaFile(content, outputPath);

		// dao interface imp
		content = daoInterfaceImp();
		outputPath = directory.getAbsolutePath() + "/src/" + BASEURL
				+ this.daoInterfaceImpPackageOutPath.replace(".", "/") + "/"
				+ initcap(className) + "Dao.java";
		genJavaFile(content, outputPath);

		// service interface
		content = serviceInterface();
		outputPath = directory.getAbsolutePath() + "/src/" + BASEURL
				+ this.serviceInterfacePackageOutPath.replace(".", "/") + "/I"
				+ initcap(className) + "Service.java";
		genJavaFile(content, outputPath);

		content = serviceInterfaceImp();
		outputPath = directory.getAbsolutePath() + "/src/" + BASEURL
				+ this.serviceInterfaceImpPackageOutPath.replace(".", "/")
				+ "/" + initcap(className) + "Service.java";
		genJavaFile(content, outputPath);

	}

	private void genJavaFile(String content, String outPath) {
		try {
			String path = this.getClass().getResource("").getPath();
			System.out.println(path);
			FileWriter fw = new FileWriter(outPath);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能：生成实体类主体代码
	 * 
	 * @param colnames
	 * @param colTypes
	 * @param colSizes
	 * @return
	 */
	private String entity() {

		StringBuffer sb = new StringBuffer();
		try {
			// 创建连接
			Connection con;
			// 查要生成实体类的表
			String sql = "select * from " + tablename;
			PreparedStatement pStemt = null;
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, NAME, PASS);
			pStemt = con.prepareStatement(sql);
			ResultSetMetaData rsmd = pStemt.getMetaData();
			int size = rsmd.getColumnCount(); // 统计列
			colnames = new String[size];
			colTypes = new String[size];
			colSizes = new int[size];
			for (int i = 0; i < size; i++) {
				colnames[i] = rsmd.getColumnName(i + 1);
				colTypes[i] = rsmd.getColumnTypeName(i + 1);
				if (colTypes[i].equalsIgnoreCase("datetime")) {
					f_util = true;
				}
				if (colTypes[i].equalsIgnoreCase("image")
						|| colTypes[i].equalsIgnoreCase("text")) {
					f_sql = true;
				}
				colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
			}

			sb.append("package " + this.entityPackageOutPath + ";\r\n");

			sb.append("import com.xdream.kernel.entity.Entity;\r\n");
			sb.append("import com.xdream.kernel.dao.jdbc.Table;\r\n");
			sb.append("import com.xdream.kernel.dao.jdbc.Column;\r\n");
			// 判断是否导入工具包
			if (f_util) {
				sb.append("import java.util.Date;\r\n");
			}
			if (f_sql) {
				sb.append("import java.sql.*;\r\n");
			}
			sb.append("\r\n");
			// 注释部分
			sb.append("   /**\r\n");
			sb.append("    * " + tablename + " 实体类<br/>\r\n");
			sb.append("    * "
					+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
							.format(new Date()) + " " + this.authorName
					+ "\r\n");
			sb.append("    */ \r\n");
			// 注解

			sb.append("@SuppressWarnings(\"serial\")\r\n");
			sb.append("@Table(name=\"" + tablename + "\")\r\n");
			// 实体部分

			sb.append("public class " + initcap(className)
					+ "  extends Entity {\r\n");

			processAllAttrs(sb);// 属性
			processAllMethod(sb);// get set方法
			sb.append("}\r\n");

		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * 
	 * 功能：Dao接口代码
	 * 
	 * @return
	 */
	private String daoInterface() {
		StringBuffer sb = new StringBuffer();

		sb.append("package " + daoInterfacePackageOutPath + " ;\r\n");
		sb.append("import com.xdream.kernel.dao.IBaseDao;\r\n");
		sb.append("import " + entityPackageOutPath + "." + initcap(className)
				+ ";\r\n");

		sb.append("\r\n");
		// 注释部分
		sb.append("   /**\r\n");
		sb.append("    * " + tablename + " DAO 接口<br/>\r\n");
		sb.append("    * "
				+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.format(new Date()) + " " + this.authorName + "\r\n");
		sb.append("    */ \r\n");
		// 实体部分
		sb.append("public interface I" + initcap(className)
				+ "Dao extends IBaseDao<" + initcap(className) + ", Long>{\r\n");
		sb.append("}\r\n");

		// System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * 
	 * 功能：Dao接口实现类代码
	 * 
	 * @return
	 */
	private String daoInterfaceImp() {
		StringBuffer sb = new StringBuffer();

		sb.append("package " + daoInterfaceImpPackageOutPath + " ;\r\n");

		sb.append("import org.springframework.stereotype.Repository;\r\n");
		sb.append("import com.xdream.kernel.dao.jdbc.JdbcBaseDaoSupport;\r\n");
		sb.append("import " + entityPackageOutPath + "." + initcap(className)
				+ ";\r\n");
		sb.append("import " + daoInterfacePackageOutPath + ".I"
				+ initcap(className) + "Dao;\r\n");

		sb.append("\r\n");

		// 注释部分
		sb.append("   /**\r\n");
		sb.append("    * " + tablename + " DAO 接口实现类<br/>\r\n");
		sb.append("    * "
				+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.format(new Date()) + " " + this.authorName + "\r\n");
		sb.append("    */ \r\n");

		// sb.append("@Repository(\""+className+"Dao\")\r\n");

		// 实体部分
		sb.append("@Repository(\"" + className + "Dao\")\r\n");
		sb.append("public class " + initcap(className)
				+ "Dao extends JdbcBaseDaoSupport<" + initcap(className)
				+ ", Long> implements I" + initcap(className) + "Dao {\r\n");
		sb.append("}\r\n");

		// System.out.println(sb.toString());
		return sb.toString();
	}

	private String serviceInterface() {
		StringBuffer sb = new StringBuffer();

		sb.append("package " + serviceInterfacePackageOutPath + ";\r\n");
		sb.append("import " + entityPackageOutPath + "." + initcap(className)
				+ ";\r\n");

		sb.append("\r\n");
		// 注释部分
		sb.append("   /**\r\n");
		sb.append("    * " + tablename + " service 接口<br/>\r\n");
		sb.append("    * "
				+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.format(new Date()) + " " + this.authorName + "\r\n");
		sb.append("    */ \r\n");
		// 实体部分
		sb.append("public interface I" + initcap(className) + "Service {\r\n");

		sb.append("}\r\n");

		// System.out.println(sb.toString());
		return sb.toString();
	}

	private String serviceInterfaceImp() {
		StringBuffer sb = new StringBuffer();

		sb.append("package " + serviceInterfaceImpPackageOutPath + ";\r\n");
		sb.append("import javax.annotation.Resource;\r\n");
		sb.append("import org.springframework.stereotype.Service;\r\n");
		sb.append("import " + daoInterfacePackageOutPath + ".I"
				+ initcap(className) + "Dao;\r\n");
		sb.append("import " + serviceInterfacePackageOutPath + ".I"
				+ initcap(className) + "Service;\r\n");
		sb.append("import " + entityPackageOutPath + "." + initcap(className)
				+ ";\r\n");

		sb.append("@Service(\"" + className + "Service\")\r\n");
		sb.append("public class " + initcap(className) + "Service implements I"
				+ initcap(className) + "Service {\r\n");
		sb.append("\t@Resource(name = \"" + className + "Dao\") \r\n");
		sb.append("\tprivate I" + initcap(className) + "Dao " + className
				+ "Dao;\r\n");

		sb.append("}\r\n");

		// System.out.println(sb.toString());
		return sb.toString();
	}

	/* 
	 *   
	 * 
	 */

	/**
	 * 功能：生成所有属性
	 * 
	 * @param sb
	 */
	private void processAllAttrs(StringBuffer sb) {

		for (int i = 0; i < colnames.length; i++) {
			// colnames[i]=colnames[i].toLowerCase();
			// colnames[i].replace(colnames[i].inde, newChar)
			if (colnames[i].equals("id") || colnames[i].equals("is_del")
					|| colnames[i].equals("add_date"))
				;
			else {
				sb.append("\t@Column(name=\"" + colnames[i] + "\")\r\n");
				String attrName = getAttributeName(colnames[i]);
				// colnames[i]=colnames[i].replaceAll("_", "");
				sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " "
						+ attrName + ";\r\n\r\n");
			}
		}

	}

	/**
	 * 功能：生成所有方法
	 * 
	 * @param sb
	 */
	private void processAllMethod(StringBuffer sb) {

		for (int i = 0; i < colnames.length; i++) {

			if (colnames[i].equals("id") || colnames[i].equals("is_del")
					|| colnames[i].equals("add_date"))
				;
			else {

				String paramName = getAttributeName(colnames[i]);

				String methodName = getMethodeName(paramName);

				sb.append("\tpublic void set" + initcap(methodName) + "("
						+ sqlType2JavaType(colTypes[i]) + " " + paramName
						+ "){\r\n");
				sb.append("\t\tthis." + paramName + "=" + paramName + ";\r\n");
				sb.append("\t}\r\n");
				sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get"
						+ initcap(methodName) + "(){\r\n");
				sb.append("\t\treturn " + paramName + ";\r\n");
				sb.append("\t}\r\n");
			}
		}

	}

	private String getAttributeName(String sqlColName) {
		sqlColName = sqlColName.toLowerCase();
		StringBuilder csb = new StringBuilder(sqlColName);
		int p = -1;
		int from = 0;
		int len = csb.length();
		while ((p = csb.indexOf("_", from)) != -1) {
			from = p + 1;
			if (from == len) {
				break;
			}
			char c = csb.charAt(from);
			if (from == len) {
				break;
			}
			csb.replace(p, from + 1, String.valueOf((char) (c - 32)));
		}

		return csb.toString();
	}

	private String getMethodeName(String attrName) {
		StringBuilder csb = new StringBuilder(attrName);
		char c = csb.charAt(0);

		csb.replace(0, 1, String.valueOf((char) (c - 32)));
		return csb.toString();
	}

	/**
	 * 功能：将输入字符串的首字母改成大写
	 * 
	 * @param str
	 * @return
	 */
	private String initcap(String str) {

		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}

		return new String(ch);
	}

	/**
	 * 功能：获得列的数据类型
	 * 
	 * @param sqlType
	 * @return
	 */
	private String sqlType2JavaType(String sqlType) {

		if (sqlType.equalsIgnoreCase("bit")) {
			return "boolean";
		} else if (sqlType.equalsIgnoreCase("tinyint")) {
			return "byte";
		} else if (sqlType.equalsIgnoreCase("smallint")) {
			return "short";
		} else if (sqlType.equalsIgnoreCase("int")
				|| sqlType.equalsIgnoreCase("integer")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("bigint")) {
			return "Long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "Float";
		} else if (sqlType.equalsIgnoreCase("decimal")
				|| sqlType.equalsIgnoreCase("numeric")
				|| sqlType.equalsIgnoreCase("real")
				|| sqlType.equalsIgnoreCase("money")
				|| sqlType.equalsIgnoreCase("smallmoney")) {
			return "Double";
		} else if (sqlType.equalsIgnoreCase("varchar")
				|| sqlType.equalsIgnoreCase("char")
				|| sqlType.equalsIgnoreCase("nvarchar")
				|| sqlType.equalsIgnoreCase("nchar")
				|| sqlType.equalsIgnoreCase("text")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			return "Blod";
		} else if (sqlType.equalsIgnoreCase("timestamp")) {
			return "Date";
		}

		return null;
	}

	/**
	 * 出口 TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		new GenEntityMysql2();
		System.out.println("finish");
	}

}