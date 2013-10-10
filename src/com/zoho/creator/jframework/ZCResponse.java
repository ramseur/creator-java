//$Id$
package com.zoho.creator.jframework;

import java.util.Hashtable;

public class ZCResponse {
	
	private Hashtable<ZCField, String> errorMessagesTable = new Hashtable<ZCField, String>();
	private long successRecordID = -1l;
	private String successLookUpValue = null;
	private boolean error = false;
	private String mainErrorMessage = "";
	private String successMessage = "";
	
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

	public String getSuccessLookUpValue() {
		return successLookUpValue;
	}


	void setSuccessLookUpValue(String successLookUpValue) {
		this.successLookUpValue = successLookUpValue;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}


	public String getMainErrorMessage() {
		return mainErrorMessage;
	}

	public void setMainErrorMessage(String mainErrorMessage) {
		this.mainErrorMessage = mainErrorMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage  = successMessage;
	}
	
	public String getSuccessMessage() {
		return successMessage;
	}
}
