package com.yjn.sorm.utils;


/**
 * ��װ�����ַ�������
 * @author JustinNeil
 *
 */
public class StringUtils {

	/**
	 * ��Ŀ���ַ���������ĸתΪ��д
	 * @param str ԭ�ַ���
	 * @return ����ĸ��д���ַ���
	 */
	public static String firstCharToUpper(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	
	/**
	 * ��Ŀ���ַ�����תΪСд
	 * @param str ԭ�ַ���
	 * @return Сд���ַ���
	 */
	public static String toLower(String str){
		return str.toLowerCase();
	}
}
