package com.yjn.sorm.bean;

import java.util.List;
import java.util.Map;

/**
 * ��װ��ṹ����Ϣ
 * @author JustinNeil
 *
 */
public class TableInfo {

	/**
	 * ����
	 */
	private String tname;
	
	/**
	 * �����ֶ���Ϣ
	 */
	private Map<String,ColumnInfo> columns;
	
	/**
	 * ���������������������洢
	 */
	private List<ColumnInfo> priKeys;

	public TableInfo() {
		super();
	}
	public TableInfo(String tname, Map<String, ColumnInfo> columns, List<ColumnInfo> priKeys) {
		super();
		this.tname = tname;
		this.columns = columns;
		this.priKeys = priKeys;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public Map<String, ColumnInfo> getColnums() {
		return columns;
	}
	public void setColnums(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}

	public List<ColumnInfo> getPriKeys() {
		return priKeys;
	}

	public void setPriKeys(List<ColumnInfo> priKeys) {
		this.priKeys = priKeys;
	}
	
}
