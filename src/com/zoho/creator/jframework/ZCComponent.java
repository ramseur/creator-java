// $Id$
package com.zoho.creator.jframework;

import android.os.Parcel;
import android.os.Parcelable;



public class ZCComponent  implements Parcelable{

	public static final String FORM = "form"; //No I18N
	public static final String REPORT = "view"; //No I18N
	public static final String CALENDAR = "calendar"; //No I18N
	public static final String PAGE = "html"; //No I18N
	public static final String SUMMARY = "summary";//No I18N
	public static final String GRID = "grid";//No I18N
	public static final String SPREADSHEET = "spreadsheet";//No I18N
	public static final String PIVOT_CHART = "pivotchart";//No I18N
	public static final String PIVOT_TABLE = "pivottable";//No I18N


	private String appOwner = null;
	private String appLinkName = null;
	private String type = null;
	private String componentName = null;
	private String componentLinkName = null;
	private int sequenceNumber = -1;

	private String queryString = null;

	ZCComponent(String appOwner, String appLinkName, String type, String componentName, String componentLinkName, int sequenceNumber) {
		this.appOwner = appOwner;	
		this.appLinkName = appLinkName;
		this.type = type;
		this.componentName = componentName;
		this.componentLinkName = componentLinkName;
		this.sequenceNumber = sequenceNumber;
	}


	public ZCComponent(Parcel in) {
		// TODO Auto-generated constructor stub
		appOwner = in.readString();	
		appLinkName = in.readString();
		type = in.readString();
		componentName = in.readString();
		componentLinkName = in.readString();
		sequenceNumber = in.readInt();
	}

	public String toString() {
		return componentName;  //No I18N
	}

	public String getType() {
		return type;
	}


	public String getComponentName() {
		return componentName;
	}


	public String getComponentLinkName() {
		return componentLinkName;
	}


	public int getSequenceNumber() {
		return sequenceNumber;
	}


	public String getAppOwner() {
		return appOwner;
	}


	public String getAppLinkName() {
		return appLinkName;
	}


	String getQueryString() {
		return queryString;
	}

	void setQueryString(String queryString)
	{
		this.queryString = queryString;
	}

	public static final Parcelable.Creator<ZCComponent> CREATOR = new Creator<ZCComponent>() {  
		public ZCComponent createFromParcel(Parcel in) {  
			return new ZCComponent(in);  
		}  
		public ZCComponent[] newArray(int size) {  
			return new ZCComponent[size];  
		}  
	}; 

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		// TODO Auto-generated method stu
		parcel.writeString(appOwner);
		parcel.writeString(appLinkName);
		parcel.writeString(type);
		parcel.writeString(componentName);
		parcel.writeString(componentLinkName);
		parcel.writeInt(sequenceNumber);

	}


	static boolean isCompTypeSupported(String type)
	{
		if(type.equals(ZCComponent.FORM)||type.equals(ZCComponent.REPORT)||type.equals(ZCComponent.CALENDAR)||type.equals(ZCComponent.PAGE)||type.equals(ZCComponent.SUMMARY)||type.equals(ZCComponent.GRID)||type.equals(ZCComponent.SPREADSHEET)||type.equals(ZCComponent.PIVOT_TABLE)||type.equals(ZCComponent.PIVOT_CHART))
		{
            return true;
		}
		return false;
	}
}
