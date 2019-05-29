package com.yjn.sorm.core;

/**
 * MySQL数据库类型与java类型的转化器
 * @author JustinNeil
 *
 */
public class MySQLTypeConvertor implements TypeConvertor{

	@Override
	public String databaseTypeToJavaType(String columnType) {
		
		if("varchar".equalsIgnoreCase(columnType)||"char".equalsIgnoreCase(columnType)){
			return "String";
		}else if("int".equalsIgnoreCase(columnType)
				||"tinyint".equalsIgnoreCase(columnType)
				||"smallint".equalsIgnoreCase(columnType)
				||"integer".equalsIgnoreCase(columnType)){
			return "Integer";
		}else if ("bigint".equalsIgnoreCase(columnType)) {
			return "Long";
		}else if("double".equalsIgnoreCase(columnType)||"float".equalsIgnoreCase(columnType)){
			return "Double";
		}else if("clob".equalsIgnoreCase(columnType)){
			return "java.sql.Clob";
		}else if("date".equalsIgnoreCase(columnType)){
			return "java.sql.Date";
		}else if("time".equalsIgnoreCase(columnType)){
			return "java.sql.Time";
		}else if("timestamp".equalsIgnoreCase(columnType)){
			return "java.sql.Timestamp";
		}
		
		return null;
	}

	@Override
	public String javaTypeToDatabaseType(String javaDataType) {
		// TODO Auto-generated method stub
		return null;
	}

}
