// $Id$
package com.zoho.creator.jframework;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ZCApplication  implements Parcelable{

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
	public ZCApplication(Parcel in) {
		// TODO Auto-generated constructor stub
		appOwner = in.readString();  
		appName = in.readString();  
		appLinkName = in.readString();
		isPrivate = in.readByte() != 0;
		createdTime = new Date(in.readLong());
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

	public static final Parcelable.Creator<ZCApplication> CREATOR = new Creator<ZCApplication>() {  
		public ZCApplication createFromParcel(Parcel in) {  
			return new ZCApplication(in);  
		}  
		public ZCApplication[] newArray(int size) {  
			return new ZCApplication[size];  
		}  
	}; 

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		// TODO Auto-generated method stub
		parcel.writeString(appOwner);
		parcel.writeString(appName);
		parcel.writeString(appLinkName);
		parcel.writeByte((byte) (isPrivate ? 1 : 0));
		parcel.writeLong(createdTime.getTime());
	}

}

