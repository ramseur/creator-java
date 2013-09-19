//$Id$
package com.zoho.creator.jframework.metadata;

import java.util.Hashtable;
import java.util.List;

public class ZCResponse {
	
	private Hashtable<ZCField, String> errorMessagesTable = new Hashtable<ZCField, String>();
	private long successRecordID = -1l;
	private List<String> successLookUpValue = null;
	
	void addErrorMessage(ZCField field, String errorMessage) {
		errorMessagesTable.put(field, errorMessage);
	}
	
	public Hashtable<ZCField, String> getErrorMessagesTable() {
		return errorMessagesTable;
	}

	public long getSuccessRecordID() {
		return successRecordID;
	}
	void setSuccessRecordID(long successRecordID) {
		this.successRecordID = successRecordID;
	}

	public List<String> getSuccessLookUpValue() {
		return successLookUpValue;
	}

	void setSuccessLookUpValue(List<String> successLookUpValue) {
		this.successLookUpValue = successLookUpValue;
	}
}
