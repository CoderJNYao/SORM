package com.yjn.sorm.bean;

/**
 * 封装了java类的属性和get，set方法
 * @author JustinNeil
 *
 */
public class JavaFieldGetSet {
	/**
	 * 属性的源码信息
	 */
	private String fieldInfo;
	/**
	 * get方法的源码信息
	 */
	private String getInfo;
	/**
	 * set方法的源码信息
	 */
	private String setInfo;
	
	public JavaFieldGetSet() {
		super();
	}
	public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
		super();
		this.fieldInfo = fieldInfo;
		this.getInfo = getInfo;
		this.setInfo = setInfo;
	}
	public String getFieldInfo() {
		return fieldInfo;
	}
	public void setFieldInfo(String fieldInfo) {
		this.fieldInfo = fieldInfo;
	}
	public String getGetInfo() {
		return getInfo;
	}
	public void setGetInfo(String getInfo) {
		this.getInfo = getInfo;
	}
	public String getSetInfo() {
		return setInfo;
	}
	public void setSetInfo(String setInfo) {
		this.setInfo = setInfo;
	}
	@Override
	public String toString() {
		return getFieldInfo()+"\n"+getSetInfo()+"\n"+getGetInfo()+"";
	}
	
	
}
