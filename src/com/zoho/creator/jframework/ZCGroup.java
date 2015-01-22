// $Id$

package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.List;


public class ZCGroup{

	private List<String> groupHeaderValues = new ArrayList<String>();
	private List<ZCRecord> groupRecords = new ArrayList<ZCRecord>();
	
	ZCGroup(List<String> groupHeaderValues) {
		this.groupHeaderValues = groupHeaderValues;
	}

	public String toString() {
		return groupHeaderValues + " --> " + groupRecords;
 	}
	
	void addRecord(ZCRecord record) {
		groupRecords.add(record);
	}

	public List<String> getGroupHeaderValues() {
		return groupHeaderValues;
	}
	
	public List<ZCRecord> getGroupRecords() {
		return groupRecords;
	}
}
