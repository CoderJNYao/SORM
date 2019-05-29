package com.yjn.sorm.bean;
/**
 * ��װ�����ļ���Ϣ
 * @author JustinNeil
 *
 */
public class Configuration {
	/**
	 * ������
	 */
	private String driver;
	/**
	 * jdbc��URL
	 */
	private String URL;
	/**
	 * �û���
	 */
	private String User;
	/**
	 * ����
	 */
	private String Password;
	/**
	 * ����ʹ�õ����ݿ�
	 */
	private String usingDB;
	/**
	 * ��Ŀ��Դ��·��
	 */
	private String srcPath;
	/**
	 * ɨ������java��İ�
	 */
	private String poPackage;
	/**
	 * ��ǰʹ�õĲ�ѯ��
	 */
	private String queryClass;
	public Configuration() {
		super();
	}
	public Configuration(String driver, String mysqlURL, String mysqlUser, String mysqlPassword, String usingDB,
			String srcPath, String poPackage,String queryClass) {
		super();
		this.driver = driver;
		this.URL = mysqlURL;
		this.User = mysqlUser;
		this.Password = mysqlPassword;
		this.usingDB = usingDB;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
		this.queryClass = queryClass;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String mysqlURL) {
		this.URL = mysqlURL;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String mysqlUser) {
		this.User = mysqlUser;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String mysqlPassword) {
		this.Password = mysqlPassword;
	}
	public String getUsingDB() {
		return usingDB;
	}
	public void setUsingDB(String usingDB) {
		this.usingDB = usingDB;
	}
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public String getPoPackage() {
		return poPackage;
	}
	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}
	public String getQueryClass() {
		return queryClass;
	}
	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}
	
	
}
