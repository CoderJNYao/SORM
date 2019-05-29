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
 * �����ѯ(�����ṩ����ĺ�����)
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
	 * ִ��һ��DML���
	 * @param sql
	 * @param params
	 * @return ����DML�����µ���������
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
	 * ��һ������洢�����ݿ���
	 * @param bean Ҫ�洢��ʵ�������
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
	 * ɾ��ָ�����Ӧ����ָ��id�ļ�¼
	 * @param clazz �����Ӧ��Class�����
	 * @param id ����id
	 */
	public void delete(Class clazz, Object id) {
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		String tableName = tableInfo.getTname();
		String priKeyName = tableInfo.getPriKeys().get(0).getName();
		String sql = "DELETE FROM "+tableName+" WHERE "+priKeyName+"=?";
		executeDML(sql, new Object[]{id});
	}
	/**
	 * ���ݶ���ɾ�����ݿ��еĶ�Ӧ��¼(���ݶ�������ȡ��Ӧ�����ݶ��������id��ȡ��Ӧ��¼)
	 * @param o ��Ҫɾ����¼�Ķ�Ӧ����
	 */
	public void delete(Object o) {
		Class<?> c = o.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		String priKeyName = tableInfo.getPriKeys().get(0).getName();
		Object priKey = ReflectUtils.invokeGet(priKeyName, o);
		delete(c, priKey);
	}
	
	/**
	 * ���¶����Ӧ�������ݵ�ֵ������ֻ����ָ���ֶε�ֵ
	 * @param o ��Ҫ���µĶ���
	 * @param fieldNames ���µ������б�
	 * @return Ӱ�����������
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
	 * ��ѯ���м�¼������ÿ�м�¼��װ��Clazzָ������Ķ����� 
	 * @param sql ��ѯ���
	 * @param clazz ��װ���ݵ�Javabean���Class����
	 * @param params sql�Ĳ���
	 * @return ��ѯ���Ľ��
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
	 * ��ѯһ�м�¼������װ��Clazzָ������Ķ����� 
	 * @param sql ��ѯ���
	 * @param clazz ��װ���ݵ�Javabean���Class����
	 * @param params sql�Ĳ���
	 * @return ��ѯ���Ľ��
	 */
	public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return list==null?null:list.size()==0?null:list.get(1);
	}
	/**
	 * ��ѯһ��ֵ(���ݿ��һ��һ��)�������ظ�ֵ 
	 * @param sql ��ѯ���
	 * @param params sql�Ĳ���
	 * @return ��ѯ����ֵ
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
	 * ��ѯһ�����֣������ظ����� 
	 * @param sql ��ѯ���
	 * @param params sql�Ĳ���
	 * @return ��ѯ����ֵ
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number)queryValue(sql, params);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
}
