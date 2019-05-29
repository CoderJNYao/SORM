package com.yjn.sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ��װ����JDBC����
 * @author JustinNeil
 *
 */
public class JDBCUtils {
	
	/**
	 * SQL��������ȫ
	 * @param ps ���SQL����PreparedStatement����
	 * @param objects ��������
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
