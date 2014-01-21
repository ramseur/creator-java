//$Id$
package com.zoho.creator.jframework;


public class ZCResponse {
	
	private long successRecordID = -1l;
	private ZCChoice successLookUpChoiceValue = null;
	private boolean error = false;
	private String mainErrorMessage = "";
	private String successMessage = "";
	private String openUrlType = "";
	private String openUrlValue = "";
	private String status = "";
	
	void setStatus(String status)
	{
		this.status = status;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	void setOpenUrlType(String openUrlType)
	{
		this.openUrlType = openUrlType;
	}
	
	public String getOpenUrlType()
	{
		return openUrlType;
	}
	
	void setOpenUrlValue(String openUrlValue)
	{
		this.openUrlValue = openUrlValue;
	}
	
	public String getOpenUrlValue()
	{
		return openUrlValue;
	}

	public long getSuccessRecordID() {
		return successRecordID;
	}
	void setSuccessRecordID(long successRecordID) {
		this.successRecordID = successRecordID;
	}

	public ZCChoice getSuccessLookUpChoiceValue() {
		return successLookUpChoiceValue;
	}


	void setSuccessLookUpChoiceValue(ZCChoice successLookUpChoiceValue) {
		this.successLookUpChoiceValue = successLookUpChoiceValue;
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

	public void setOpenUrlValueForCustomAction(String openURLValue) {
		// TODO Auto-generated method stub
		this.openUrlValue = openURLValue;
	}

	public String getOpenURLValueForCustomAction() {
		return openUrlValue;
	}
}
