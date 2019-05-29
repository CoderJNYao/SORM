package com.yjn.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.PreparedStatement;

import com.alibaba.druid.pool.DruidDataSource;
import com.yjn.sorm.bean.Configuration;
/**
 * 根据配置信息，维持连接对象的管理
 * @author JustinNeil
 *
 */
public class DBManager {
	private static Configuration conf;
	private static DruidDataSource dds = new DruidDataSource();
	static { 
		Properties properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		conf = new Configuration();
		conf.setUsingDB(properties.getProperty("usingDB"));
		if(conf.getUsingDB().equals("mysql")){
			conf.setDriver(properties.getProperty("mysqlDriver"));
			conf.setURL(properties.getProperty("mysqlURL"));
			conf.setUser(properties.getProperty("mysqlUser"));
			conf.setPassword(properties.getProperty("mysqlPassword"));
		}else if(conf.getUsingDB().equals("oracle")){
			conf.setDriver(properties.getProperty("oracleDriver"));
			conf.setURL(properties.getProperty("oracleURL"));
			conf.setUser(properties.getProperty("oracleUser"));
			conf.setPassword(properties.getProperty("oraclePassword"));
		}
		conf.setSrcPath(properties.getProperty("srcPath"));
		conf.setPoPackage(properties.getProperty("poPackage"));
		conf.setQueryClass((properties.getProperty("queryClass")));
		dds.setDriverClassName(conf.getDriver());
		dds.setUrl(conf.getURL());
		dds.setUsername(conf.getUser());
		dds.setPassword(conf.getPassword());
		dds.setInitialSize(5);
		dds.setMaxActive(20);
	}
	
	public static Connection getConnection(){
		try {
			return dds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void closeSourse(ResultSet rs,PreparedStatement ps,Connection con){
			try {
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					if(ps != null){
						ps.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		
	}
	
	public static void closeSourse(PreparedStatement ps,Connection con){
			try {
				if(ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
}
	
	public static Configuration getConfiguration(){
		return conf;
	}
}
