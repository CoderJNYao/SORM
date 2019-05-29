package com.yjn.sorm.utils;


/**
 * 封装常用字符串操作
 * @author JustinNeil
 *
 */
public class StringUtils {

	/**
	 * 将目标字符串的首字母转为大写
	 * @param str 原字符串
	 * @return 首字母大写的字符串
	 */
	public static String firstCharToUpper(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	
	/**
	 * 将目标字符串的转为小写
	 * @param str 原字符串
	 * @return 小写的字符串
	 */
	public static String toLower(String str){
		return str.toLowerCase();
	}
}
