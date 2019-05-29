package com.yjn.sorm.utils;

import java.lang.reflect.Method;

/**
 * 封装常用反射操作
 * @author JustinNeil
 *
 */
public class ReflectUtils {
	/**
	 * 封装调用get方法
	 * @param fieldName 字段名
	 * @param obj 调用方法的对象
	 * @return 字段值
	 */
	public static Object invokeGet(String fieldName,Object obj){
		Class<?> c = obj.getClass();
		try {
			Method m = c.getDeclaredMethod("get"+StringUtils.firstCharToUpper(fieldName));
			return m.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		 return null;
	}
	/**
	 * 封装调用set方法
	 * @param fieldName 字段名
	 * @param obj 调用方法的对象
	 * @param value 字段值
	 */
	public static void invokeSet(String fieldName,Object obj,Object value){
		Class<?> c = obj.getClass();
		try {
			Method m = c.getDeclaredMethod("set"+StringUtils.firstCharToUpper(fieldName),value.getClass());
			m.invoke(obj,value);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
}
