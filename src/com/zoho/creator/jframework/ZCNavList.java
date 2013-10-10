// $Id$

package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.List;

public class ZCNavList {
	private List<String> sharedWithWorkSpaceList = new ArrayList<String>();
	private List<ZCSharedGroup> sharedWithGroupList = new ArrayList<ZCSharedGroup>();
	
	ZCNavList(List<ZCSharedGroup> sharedWithGroupList, List<String> sharedWithWorkSpaceList) {
		this.sharedWithGroupList = sharedWithGroupList;
		this.sharedWithWorkSpaceList = sharedWithWorkSpaceList;
	}

	public List<String> getSharedWithWorkSpaceList() {
		return sharedWithWorkSpaceList;
	}

	public List<ZCSharedGroup> getSharedWithGroupList() {
		return sharedWithGroupList;
	}

}
