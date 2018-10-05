package com.dhk.kernel.dao.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 表字段
 * @author Administrator
 *
 */
@Target(ElementType.FIELD)//用于成员变量  
@Retention(RetentionPolicy.RUNTIME) 
public @interface Column {

	int length() default 0;
	String name() default "";
}
