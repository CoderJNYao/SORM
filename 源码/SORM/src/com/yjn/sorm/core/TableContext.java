package com.yjn.sorm.core;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.yjn.sorm.bean.ColumnInfo;
import com.yjn.sorm.bean.TableInfo;
import com.yjn.sorm.utils.JavaFileUtils;
import com.yjn.sorm.utils.StringUtils;

/**
 * 负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构
 * @author JustinNeil
 *
 */
public class TableContext {
	
	/**
	 * 表名为key,表信息对象为value;
	 */
	public static Map<String, TableInfo> tables = new HashMap<String,TableInfo>();
	
	/**
	 * 将po的Class对象与表信息对象关联，便于重用
	 */
	public static Map<Class<?>, TableInfo> poClassTableMap = new HashMap<Class<?>,TableInfo>();
	
	static{
		try {
			Connection con = DBManager.getConnection();
			DatabaseMetaData dbmd = con.getMetaData();
			ResultSet rs = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
			while (rs.next()) {
				String tableName = (String)rs.getObject("TABLE_NAME");
				TableInfo ti = new TableInfo(tableName, new HashMap<String,ColumnInfo>(), new ArrayList<ColumnInfo>());
				tables.put(tableName, ti);
				ResultSet set = dbmd.getColumns(null, "%", tableName,"%");
				while(set.next()){
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),set.getString("TYPE_NAME") , 0);
					ti.getColnums().put(set.getString("COLUMN_NAME"), ci);
				}
				ResultSet keySet = dbmd.getPrimaryKeys(null, "%", tableName);
				while (keySet.next()) {
					ColumnInfo keyCI = (ColumnInfo)ti.getColnums().get(keySet.getString("COLUMN_NAME"));
					keyCI.setKeyType(1);
					ti.getPriKeys().add(keyCI);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		updateJavaVoFile();
		loadVoTable();
	}
	
	/**
	 * 根据表结构，更新po包中的java类
	 */
	public static void updateJavaVoFile(){
		Map<String,TableInfo> tableMap  = TableContext.tables;
		for (String string : tableMap.keySet()) {
			TableInfo tableInfo = tableMap.get(string);
			JavaFileUtils.createJavaVoFile(tableInfo, new MySQLTypeConvertor());
		}
	}
	
	/**
	 * 加载po包下的类，与表一一对应
	 */
	public static void loadVoTable(){
		for(TableInfo tableInfo:tables.values()){
			Class<?> c;
			try {
				c = Class.forName(DBManager.getConfiguration().getPoPackage()+"."+
						StringUtils.firstCharToUpper(tableInfo.getTname()));
				poClassTableMap.put(c, tableInfo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
