package com.yjn.sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装常用JDBC操作
 * @author JustinNeil
 *
 */
public class JDBCUtils {
	
	/**
	 * SQL语句参数补全
	 * @param ps 存放SQL语句的PreparedStatement对象
	 * @param objects 参数数组
	 */
	public static void handleParams(PreparedStatement ps,Object[] objects){
		for(int i=0;i<objects.length;i++){
			try {
				ps.setObject(i+1, objects[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
