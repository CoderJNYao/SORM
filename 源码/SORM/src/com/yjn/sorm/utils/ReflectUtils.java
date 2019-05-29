package com.yjn.sorm.utils;

import java.lang.reflect.Method;

/**
 * ��װ���÷������
 * @author JustinNeil
 *
 */
public class ReflectUtils {
	/**
	 * ��װ����get����
	 * @param fieldName �ֶ���
	 * @param obj ���÷����Ķ���
	 * @return �ֶ�ֵ
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
	 * ��װ����set����
	 * @param fieldName �ֶ���
	 * @param obj ���÷����Ķ���
	 * @param value �ֶ�ֵ
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
