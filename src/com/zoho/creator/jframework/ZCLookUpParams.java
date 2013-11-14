package com.zoho.creator.jframework;

public class ZCLookUpParams {
	private String appLinkName;
	private String formLinkName;
	private String fieldLinkName;
	
	
	public ZCLookUpParams(String appLinkName,String formLinkName,String fieldLinkName) {
		this.appLinkName = appLinkName;
		this.formLinkName = formLinkName;
		this.fieldLinkName = fieldLinkName;	
	}
	
	public String getAppLinkName()
	{
		return appLinkName;
	}
	
	public String getFormLinkName()
	{
		return formLinkName;
	}
	
	public String getFieldLinkName()
	{
		return fieldLinkName;
	}

}
