package com.yjn.sorm.core;
/**
 * ����java�������ͺ����ݿ��������͵Ļ���ת��
 * @author JustinNeil
 */
public interface TypeConvertor {
	
	/**
	 * �����ݿ�����ת����java����
	 * @param columnType ���ݿ��ֶε���������
	 * @return java����������
	 */
	public String databaseTypeToJavaType(String columnType);
	
	/**
	 * ��java����ת�������ݿ�����
	 * @param javaDataType java����
	 * @return ���ݿ��ֶε���������
	 */
	public String javaTypeToDatabaseType(String javaDataType);
}
