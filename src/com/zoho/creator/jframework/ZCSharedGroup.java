// $Id$

package com.zoho.creator.jframework;

public class ZCSharedGroup {

	private long groupId = -1l;
	private String groupName = null;

	ZCSharedGroup(String groupName, long groupId) {
		this.groupName = groupName;
		this.groupId = groupId;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public long getGroupId() {
		return groupId;
	}
	
}
