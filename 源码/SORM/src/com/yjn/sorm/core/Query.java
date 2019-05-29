package com.yjn.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yjn.sorm.bean.ColumnInfo;
import com.yjn.sorm.bean.TableInfo;
import com.yjn.sorm.utils.JDBCUtils;
import com.yjn.sorm.utils.ReflectUtils;

/**
 * 负责查询(对外提供服务的核心类)
 * @author JustinNeil
 *
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable{
	
	public  Object executeDMLTemplement(String sql, Object[] params,Class clazz,CallBack callBack){
		Connection con = DBManager.getConnection();
		PreparedStatement ps;
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);		
		try {
			ps = con.prepareStatement(sql);
			JDBCUtils.handleParams(ps, params);
			ResultSet rs = ps.executeQuery();
			return callBack.doExecute(con, ps, rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 执行一条DML语句
	 * @param sql
	 * @param params
	 * @return 返回DML语句更新的数据条数
	 */
	public int executeDML(String sql, Object[] params) {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i+1, params[i]);
			}
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.closeSourse(ps, con);
		}
		return 0;
	}
	
	/**
	 * 讲一个对象存储到数据库中
	 * @param bean 要存储的实体类对象
	 */
	public void insert(Object obj) {
		Class<?> c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		String tableName = tableInfo.getTname();
		StringBuilder sql = new StringBuilder();
		List<Object> values = new ArrayList<Object>();
		int valueCount = 0;
		sql.append("INSERT INTO "+tableName+" (");
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
			if(fieldValue != null){
				sql.append(fieldName+",");
				values.add(fieldValue);
				valueCount++;
			}
		}
		sql.setCharAt(sql.length()-1, ')');
		sql.append(" VALUES(");
		for(int i=0;i<valueCount;i++){
			sql.append("?,");
		}
		sql.setCharAt(sql.length()-1, ')');
		executeDML(sql.toString(), values.toArray());
	}
	/**
	 * 删除指定类对应表中指定id的记录
	 * @param clazz 跟表对应的Class类对象
	 * @param id 主键id
	 */
	public void delete(Class clazz, Object id) {
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		String tableName = tableInfo.getTname();
		String priKeyName = tableInfo.getPriKeys().get(0).getName();
		String sql = "DELETE FROM "+tableName+" WHERE "+priKeyName+"=?";
		executeDML(sql, new Object[]{id});
	}
	/**
	 * 根据对象删除数据库中的对应记录(根据对象的类获取对应表，根据对象的主键id获取对应记录)
	 * @param o 需要删除记录的对应对象
	 */
	public void delete(Object o) {
		Class<?> c = o.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		String priKeyName = tableInfo.getPriKeys().get(0).getName();
		Object priKey = ReflectUtils.invokeGet(priKeyName, o);
		delete(c, priKey);
	}
	
	/**
	 * 更新对象对应表中数据的值，并且只更新指定字段的值
	 * @param o 需要更新的对象
	 * @param fieldNames 更新的属性列表
	 * @return 影响的数据行数
	 */
	public int update(Object o, String[] fieldNames) {
		Class<?> c = o.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		String tableName = tableInfo.getTname();
		List<Object> values = new ArrayList<Object>();
		ColumnInfo columnInfo = tableInfo.getPriKeys().get(0);
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE "+tableName+" SET ");
		for (String fieldName : fieldNames) {
			sql.append(fieldName+"=?,");
			values.add(ReflectUtils.invokeGet(fieldName, o));
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append("WHERE ");
		sql.append(columnInfo.getName()+"=?");
		values.add(ReflectUtils.invokeGet(columnInfo.getName(), o));
		executeDML(sql.toString(), values.toArray());
		return 0;
	}
	/**
	 * 查询多行记录，并将每行记录封装到Clazz指定的类的对象中 
	 * @param sql 查询语句
	 * @param clazz 封装数据的Javabean类的Class对象
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public List queryRows(final String sql,final Class clazz,final Object[] params) {
		List<Object> list = new ArrayList<Object>();
		executeDMLTemplement(sql, params, clazz, new CallBack() {
			@Override
			public Object doExecute(Connection con, PreparedStatement ps, ResultSet rs) {
				ResultSetMetaData metaData;
				try {
					metaData = rs.getMetaData();
					while(rs.next()){
						Object obj = clazz.newInstance();
						for(int i=0;i<metaData.getColumnCount();i++){
							String fieldName = metaData.getColumnLabel(i+1);
							Object value = rs.getObject(i+1);
							ReflectUtils.invokeSet(fieldName, obj, value);
						}
						list.add(obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
	}

	/**
	 * 查询一行记录，并封装到Clazz指定的类的对象中 
	 * @param sql 查询语句
	 * @param clazz 封装数据的Javabean类的Class对象
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return list==null?null:list.size()==0?null:list.get(1);
	}
	/**
	 * 查询一个值(数据库的一行一列)，并返回该值 
	 * @param sql 查询语句
	 * @param params sql的参数
	 * @return 查询到的值
	 */
	public Object queryValue(String sql, Object[] params) {
		Object object = null;
		object = executeDMLTemplement(sql, params, null, new CallBack() {
			@Override
			public Object doExecute(Connection con, PreparedStatement ps, ResultSet rs) {
				try {
					if(rs.next()){
						return rs.getObject(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return object;
	}
	/**
	 * 查询一个数字，并返回该数字 
	 * @param sql 查询语句
	 * @param params sql的参数
	 * @return 查询到的值
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number)queryValue(sql, params);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
}
