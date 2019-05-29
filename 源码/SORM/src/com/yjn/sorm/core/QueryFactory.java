package com.yjn.sorm.core;

import java.util.Properties;

public class QueryFactory {
	
	private static Class c;
	private static Query query;
	
	static {
		try {
			c = Class.forName(DBManager.getConfiguration().getQueryClass());
			query = (Query) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Query createQuery(){
		try {
			return (Query) query.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
