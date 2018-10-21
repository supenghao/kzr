package com.dhk.api.tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.xdream.kernel.dao.jdbc.Column;
import com.xdream.kernel.dao.jdbc.Table;
import com.xdream.kernel.entity.Entity;

public class M {

	public static Logger logger = Logger.getLogger(M.class);

	/**
	 * yyyyMMdd
	 */
	public static SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMdd");
	/**
	 * HHmmss
	 */
	public static SimpleDateFormat tformat = new SimpleDateFormat("HHmmss");

	/**
	 * 当前是否调试模式
	 */
	public static boolean debug = true;

	/**
	 * 当前是否调试模式
	 */
	public static boolean printDebug = false;

	public static String createUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * 动态生成某个对象的sql更新语句,只有对象相应字段不为null时才更新
	 * 
	 * @param o
	 * <br>
	 *            实体对象
	 * @param whereCase
	 * <br>
	 *            null:对象应当设置id(主键)由id决定插入对象<br>
	 *            not null:where 由判断语句决定插入对象<br>
	 *            空:不添加where子句
	 * @return
	 */
	public static <T extends Entity> String getUpdateSqlWhenFilesIsNotNull(T o,
			String whereCase) {
		Class<? extends Entity> c = (Class<? extends Entity>) o.getClass();
		String tableName = null;
		StringBuilder sql = new StringBuilder();
		// 取得表名-通过注解获取
		if (c.isAnnotationPresent(Table.class)) {
			Table table = (Table) c.getAnnotation(Table.class);
			tableName = table.name();
		}
		Field[] fields = c.getDeclaredFields();
		sql.append(" update " + tableName + " set ");
		Object id = ":id";
		for (int i = 0; fields != null && i < fields.length; i++) {
			fields[i].setAccessible(true); // 暴力反射
			String column = fields[i].getName();
			Object ov = null;
			try {
				ov = fields[i].get(o);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("传入对象不匹配!");
			}
			if (column != null && ov != null) {
				sql.append(column).append("=").append(":" + column).append(",");
			}
			if (ov != null && column.equals("id")) { // id 代表主键
				id = ov;
				continue;
			}
		}
		sql = sql.deleteCharAt(sql.length() - 1);
		if (whereCase != null && !whereCase.equals("")) {
			sql.append(" where ");
			sql.append(whereCase);
		} else if (whereCase == null) {
			sql.append(" where ");
			sql.append("id=" + id);
		}
		sql.append(" ");
		String ret = sql.toString();
		logger.debug("sql:" + ret);
		return ret;
	}

	/**
	 * 获得Sql插入语句,当传入的对象相应字段不为null时设值,<b> 不设id(主键)字段(自增长) </b>
	 * 
	 * @param c
	 * @return
	 */
	public static <T extends Entity> String getInsertSqlWhenFilesIsNotNull(T o) {
		Class<? extends Entity> c = (Class<? extends Entity>) o.getClass();
		String tableName = null;
		StringBuilder sql = new StringBuilder();
		StringBuilder value = new StringBuilder();
		// 取得表名-通过注解获取
		if (c.isAnnotationPresent(Table.class)) {
			Table table = (Table) c.getAnnotation(Table.class);
			tableName = table.name();
		}
		Field[] fields = c.getDeclaredFields();
		sql.append(" insert into " + tableName);
		sql.append("(");
		for (int i = 0; fields != null && i < fields.length; i++) {
			fields[i].setAccessible(true); // 暴力反射
			String column = fields[i].getName();
			Object ov = null;
			try {
				ov = fields[i].get(o);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("传入对象不匹配!");
			}
			if (column != null && ov != null) {
				sql.append(column).append(",");
				value.append(":" + column).append(",");
			}

		}
		value.deleteCharAt(value.length() - 1);
		sql = sql.deleteCharAt(sql.length() - 1);
		sql.append(") values (");
		sql.append(value.toString());
		sql.append(")");
		String ret = sql.toString();
		logger.debug("create sql:" + ret);
		return ret;
	}

	/**
	 * 获得Sql插入语句,当传入的对象相应字段不为null&& 有@Column注解时设值,<b> 不设id(主键)字段(自增长) </b>
	 * 
	 * @param c
	 * @return
	 */
	public static <T extends Entity> String getInsertSqlWhenFilesIsNotNull21(T o) {
		Class<? extends Entity> c = (Class<? extends Entity>) o.getClass();
		String tableName = null;
		StringBuilder sql = new StringBuilder();
		StringBuilder value = new StringBuilder();
		// 取得表名-通过注解获取
		if (c.isAnnotationPresent(Table.class)) {
			Table table = (Table) c.getAnnotation(Table.class);
			tableName = table.name();
		}
		Field[] fields = c.getDeclaredFields();
		sql.append(" insert into " + tableName);
		sql.append("(");
		boolean needCut = false;
		for (int i = 0; fields != null && i < fields.length; i++) {
			fields[i].setAccessible(true); // 暴力反射
			Column annColumn = getColumn(fields[i]);
			if (annColumn != null) {
				String column = annColumn.name();
				Object ov = null;
				try {
					ov = fields[i].get(o);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("传入对象不匹配!");
				}
				if (column != null && ov != null) {
					sql.append(column).append(",");
					value.append(":" + column).append(",");
					needCut = true;
				}
			}
		}
		if (needCut) {
			value.deleteCharAt(value.length() - 1);
			sql = sql.deleteCharAt(sql.length() - 1);
		}
		sql.append(") values (");
		sql.append(value.toString());
		sql.append(")");
		String ret = sql.toString();
		logger.debug("create sql:" + ret);
		return ret;
	}

	private static Column getColumn(Field field) {
		Column column = null;
		if (field.isAnnotationPresent(Column.class)) {
			column = (Column) field.getAnnotation(Column.class);
		}
		return column;
	}

	public static String getTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}
}
