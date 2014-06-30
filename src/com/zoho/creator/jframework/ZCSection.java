// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ZCSection implements Parcelable{

	private String appOwner = null;
	private String appLinkName = null;
	private String sectionName = null;
	private String sectionLinkName = null;
	private boolean isHidden = false;
	private Long sectionID = null;
	private Date modifiedTime = null;


	private List<ZCComponent> components = new ArrayList<ZCComponent>();
	private boolean componentsAdded = false;

	ZCSection(String appOwner, String appLinkName,  String sectionName, String sectionLinkName, boolean isHidden, Long sectionID, Date modifiedTime) {
		this.appLinkName = appLinkName;
		this.sectionName = sectionName;
		this.appOwner = appOwner;
		this.sectionLinkName = sectionLinkName;
		this.isHidden = isHidden;
		this.sectionID = sectionID;
		this.modifiedTime = modifiedTime;
	}


	public ZCSection(Parcel in) {
		// TODO Auto-generated constructor stub
		appLinkName = in.readString();
		sectionName = in.readString();;
		appOwner = in.readString();
		sectionLinkName = in.readString();
		isHidden = in.readByte() != 0;
		sectionID = in.readLong();
		modifiedTime = new Date(in.readLong());
		in.readList(components, ZCComponent.class.getClassLoader());
	}
	public String toString() {
		return sectionName + " : " + appOwner + " : " + sectionLinkName + " : " + isHidden + " : " + sectionID + " : " + modifiedTime + " : " + components; //No I18N	
	}

	public String getSectionName() {
		return sectionName;
	}

	public String getAppOwner() {
		return appOwner;
	}

	public String getSectionLinkName() {
		return sectionLinkName;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public Long getSectionID() {
		return sectionID;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public final List<ZCComponent> getComponents() {
		final List<ZCComponent> toReturn = new ArrayList<ZCComponent>(components);
		return toReturn;
	}

	void addComponents(List<ZCComponent> zcComponents) {
		if(!componentsAdded) {
			components.addAll(zcComponents);
			componentsAdded = true;
		} else {
			throw new RuntimeException("You cannot add more than once"); //No I18N
		}
	}

	public String getAppLinkName() {
		return appLinkName;
	}


	public static final Parcelable.Creator<ZCSection> CREATOR = new Creator<ZCSection>() {  
		public ZCSection createFromParcel(Parcel in) {  
			return new ZCSection(in);  
		}  
		public ZCSection[] newArray(int size) {  
			return new ZCSection[size];  
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
		parcel.writeString(appLinkName);
		parcel.writeString(sectionName);
		parcel.writeString(appOwner);
		parcel.writeString(sectionLinkName);
		parcel.writeByte((byte) (isHidden ? 1 : 0));
		parcel.writeLong(sectionID);
		parcel.writeLong(modifiedTime.getTime());
		parcel.writeList(components);
	}
}
