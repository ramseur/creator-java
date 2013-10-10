// $Id$

package com.zoho.creator.jframework;

import java.util.List;

public class ZCAppList {
	
	public static final int PERSONAL_APPS = 1;
	public static final int SHARED_APPS = 2;
	public static final int WORKSPACE_APPS = 3;
	
	
	private int type = PERSONAL_APPS;
	private List<ZCApplication> apps = null;
	private ZCSharedGroup sharedGroup = null;
	private String workspaceAppOwner = null;
	
	
	ZCAppList(int type, List<ZCApplication> apps) {
		this.apps = apps;
		this.type = type;
	}


	public ZCSharedGroup getSharedGroup() {
		return sharedGroup;
	}
	

	void setSharedGroup(ZCSharedGroup sharedGroup) {
		this.sharedGroup = sharedGroup;
	}


	public String getWorkspaceAppOwner() {
		return workspaceAppOwner;
	}


	void setWorkspaceAppOwner(String workspaceAppOwner) {
		this.workspaceAppOwner = workspaceAppOwner;
	}


	public List<ZCApplication> getApps() {
		return apps;
	}

	
	public int getType() {
		return type;
	}

	public String toString() {
		return apps.toString();
	}

}
