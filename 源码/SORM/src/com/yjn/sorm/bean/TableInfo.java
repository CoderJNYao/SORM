package com.yjn.sorm.bean;

import java.util.List;
import java.util.Map;

/**
 * 封装表结构的信息
 * @author JustinNeil
 *
 */
public class TableInfo {

	/**
	 * 表名
	 */
	private String tname;
	
	/**
	 * 所有字段信息
	 */
	private Map<String,ColumnInfo> columns;
	
	/**
	 * 如果有联合主键，在这里存储
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
