package com.ailide.apartmentsabc.model;

import java.io.Serializable;

/**
 * Created by liwenguo on 2017/7/4 0004.
 */

public class UpdateBean implements Serializable {
	
	/**
	 * id : 2
	 * appId : 103
	 * appName : insurance-api
	 * appVersion : 1.0.0
	 * force : false
	 * url : http://m.17bxw.com
	 * describe : 提报发布系统，内测中
	 * platform : Android
	 */
	
	private int id;
	private int appId;
	private String appName;
	private String appVersion;
	private boolean force;
	private String url;
	private String describe;
	private String platform;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getAppId() {
		return appId;
	}
	
	public void setAppId(int appId) {
		this.appId = appId;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public boolean isForce() {
		return force;
	}
	
	public void setForce(boolean force) {
		this.force = force;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDescribe() {
		return describe;
	}
	
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
