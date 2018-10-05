package com.sunnada.kernel.dao.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 表名
 * @author Administrator
 *
 */
@Target(ElementType.TYPE)//用于类
@Retention(RetentionPolicy.RUNTIME) 
public @interface Table {

	String name();
}
