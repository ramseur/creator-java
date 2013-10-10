// $Id$
package com.zoho.creator.jframework;

import java.util.Date;

public class ZCApplication  {

	private String appOwner = null;
	private String appName = null;
	private String appLinkName = null;
	private boolean isPrivate = true;
	private Date createdTime = null;
	
	
	ZCApplication(String appOwner, String appName, String appLinkName, boolean isPrivate, Date createdTime) {
		this.appOwner = appOwner;
		this.appName = appName;
		this.appLinkName = appLinkName;
		this.isPrivate = isPrivate;
		this.createdTime = createdTime;
	}
	
	public String getAppOwner() {
		return appOwner;
	}

	public String getAppLinkName() {
		return appLinkName;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public String getAppName() {
		return appName;
	}

	public String toString() {
		return appName + " : " + appLinkName + " : " + appOwner + " : " + createdTime + " : " + isPrivate; //No I18N
	}
	
}
