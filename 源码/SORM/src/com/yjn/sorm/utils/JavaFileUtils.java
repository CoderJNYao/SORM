package com.yjn.sorm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.yjn.po.*;
import com.yjn.sorm.bean.ColumnInfo;
import com.yjn.sorm.bean.JavaFieldGetSet;
import com.yjn.sorm.bean.TableInfo;
import com.yjn.sorm.core.DBManager;
import com.yjn.sorm.core.MySQLQuery;
import com.yjn.sorm.core.Query;
import com.yjn.sorm.core.TypeConvertor;

/**
 * 封装了生成Java源码的操作
 * @author JustinNeil
 *
 */
public class JavaFileUtils {

	/**
	 * 根据字段信息生成Java源码
	 * @param columnInfo 字段信息
	 * @param typeConvertor 类型转换器
	 * @return Java属性和get，set方法源码
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo columnInfo,TypeConvertor typeConvertor){
		JavaFieldGetSet jfgs = new JavaFieldGetSet();
		String javaFieldType = typeConvertor.databaseTypeToJavaType(columnInfo.getDataType());
		jfgs.setFieldInfo("\tprivate "+javaFieldType+" "+columnInfo.getName()+";\n");
		
		StringBuilder getSrc = new StringBuilder();
		getSrc.append("\tpublic "+javaFieldType+" get"+StringUtils.firstCharToUpper(columnInfo.getName())+"(){\n"+
		"\t\treturn "+ columnInfo.getName()+
		";\n\t}\n");
		jfgs.setGetInfo(getSrc.toString());
		
		StringBuilder setSrc = new StringBuilder();
		setSrc.append("\tpublic set"+StringUtils.firstCharToUpper(columnInfo.getName())+"("+javaFieldType+" "+columnInfo.getName()+"){\n"+
		"\t\tthis."+columnInfo.getName()+"="+columnInfo.getName()+
		";\n\t}\n");
		jfgs.setSetInfo(setSrc.toString());
		return jfgs;
	}
	
	public static String createJavaSrc(TableInfo tableInfo,TypeConvertor typeConvertor){
		StringBuilder sb = new StringBuilder();
		String tableName = tableInfo.getTname();
		List<ColumnInfo> colList = new ArrayList<ColumnInfo>();
		Map<String,ColumnInfo> colMap = tableInfo.getColnums();
		Set<String> kSet = colMap.keySet();
		for (String string : kSet) {
			colList.add(colMap.get(string));
		}
		//包名
		sb.append("package "+DBManager.getConfiguration().getPoPackage()+";\n\n");
		//import语句
		sb.append("import java.sql.*;\n");
		sb.append("import java.util.*;\n\n");
		//类定义
		sb.append("public class "+StringUtils.firstCharToUpper(tableName)+"{\n\n");
		//字段定义
		for (ColumnInfo c : colList) {
			sb.append("\tprivate "+typeConvertor.databaseTypeToJavaType(c.getDataType())+" "+c.getName()+";\n");
		}
		sb.append("\n");
		//get,set方法定义
		for (ColumnInfo c : colList) {
			sb.append("\tpublic "+typeConvertor.databaseTypeToJavaType(c.getDataType())+" get"+StringUtils.firstCharToUpper(c.getName())+
					"(){\n"+"\t\treturn "+ c.getName()+";\n\t}\n");
			sb.append("\tpublic void set"+StringUtils.firstCharToUpper(c.getName())+"("+typeConvertor.databaseTypeToJavaType(c.getDataType())+
					" "+c.getName()+"){\n"+"\t\tthis."+c.getName()+"="+c.getName()+";\n\t}\n");
		}
		//类体结束
		sb.append("}\n");
		return sb.toString();
	}
	
	public static void createJavaVoFile(TableInfo tableInfo,TypeConvertor typeConvertor){
		String src = createJavaSrc(tableInfo, typeConvertor);
		BufferedWriter bw = null;
		String srcPath = DBManager.getConfiguration().getSrcPath()+"\\"+DBManager.getConfiguration().getPoPackage().replaceAll("\\.", "\\\\");
		File file = new File(srcPath+"\\"+StringUtils.firstCharToUpper(tableInfo.getTname())+".java");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(src);
			System.out.println("建立表"+tableInfo.getTname()+"对应的Java类："+file.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		 Emp emp = new Emp();
		 emp.setEmpno(9899);
		 emp.setComm(888.0);
		 Query query = new MySQLQuery();
		 String[] strings = new String[]{"comm"};
		 query.update(emp,strings);
	}	
}
