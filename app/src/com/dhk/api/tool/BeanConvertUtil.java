package com.dhk.api.tool;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;



/**
 * JavaBean转换工具
 * @author zzl
 */
public class BeanConvertUtil {
    private static Logger logger = LoggerFactory.getLogger(BeanConvertUtil.class);
    /**
     * 根据filed的set,get方法，将源对象转换为目标对象
     * @param source 源对象
     * @param target 目标对象类型
     * @return
     * @throws InvocationTargetException 
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T beanConvert(Object source,Class<T> target) {
       try {
           if(source == null) {
               return null;
           }
           T instance = target.newInstance();
           BeanUtils.copyProperties(source, instance);
           return instance;
       } catch (Exception e) {
           logger.error("bean转换异常",e);
           return null; 
       }
       
    }
    /**
     * 将源对象列表转换为目标对象列表
     * @param source 源对象列表
     * @param target 目标对象类型
     * @return
     */
    public static <T> List<T> beanListConvert(List<?> source,Class<T> target)  {
        try{
            List<T> targetList = new ArrayList<>();
            if(source == null || source.size() == 0) {
                return targetList; 
            }
            
            for (Object object : source) {
                T instance = target.newInstance();
                BeanUtils.copyProperties(object, instance);
                targetList.add(instance);
            }
            return targetList;
        } catch (Exception e) {
            logger.error("bean转换异常",e);
            return null; 
        }
    }
    /**
     * 将对象转为map
     * @param bean
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String,Object> convertBean2Map(Object bean)  {
        try {
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
            Map<String,Object> returnMap = new HashMap<>();  
            for(int i = 0; i< propertyDescriptors.length; i++) {  
                PropertyDescriptor descriptor = propertyDescriptors[i];  
                String propertyName = descriptor.getName();  
                if (!propertyName.equals("class")) {  
                    Method readMethod = descriptor.getReadMethod();  
                    Object result = readMethod.invoke(bean, new Object[0]);  
                    if (result != null) {  
                        returnMap.put(propertyName, result);  
                    } 
                }  
            }  
            return returnMap;  
        } catch (Exception e) {
            logger.error("bean转换异常",e);
            return null; 
        }
    }
    /**
     * 将Map转Bean对象
     * @param map
     * @param target
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public static <T> T convertMap2Bean(Map<String,Object> map,Class<T> target)  {  
        try {
            T obj = target.newInstance(); 
            // 给 JavaBean 对象的属性赋值  
            PropertyDescriptor[] propertyDescriptors =  BeanUtils.getPropertyDescriptors(target);  
            for (int i = 0; i< propertyDescriptors.length; i++) {  
                PropertyDescriptor descriptor = propertyDescriptors[i];  
                String propertyName = descriptor.getName();  
      
                if (map.containsKey(propertyName)) {  
                    Object[] args = new Object[]{ map.get(propertyName) };  
                    descriptor.getWriteMethod().invoke(obj, args);  
                }  
            }  
            return obj; 
        } catch (Exception e) {
            logger.error("bean转换异常",e);
            return null; 
        }
    }  
}
