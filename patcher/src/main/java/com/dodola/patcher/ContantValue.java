package com.dodola.patcher;

public interface ContantValue {
	/**
	 * 字符编码
	 */
    String ENCODE = "UTF-8";
	/**
	 * 服务端的的URL
	 */
    String BASE_URL = "http://192.168.173.1:8080/UpApk";
	
	/**
	 * 检查版本号是否为最新
	 */
	String CHECK_VERSION_CODE_URL=BASE_URL + "/CheckVersionServlet";
	
	/**
	 * 获取差异包的URL地址
	 */
	String UPGRADE_URL = BASE_URL +"/UpGradeServlet";
	
}

