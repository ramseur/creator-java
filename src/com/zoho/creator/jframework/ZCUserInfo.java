package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.List;

public class ZCUserInfo {
	
	private String country;
	private String language;
	private String timeZone;
	private List<String> eMailAddress = new ArrayList<String>();
	private int gender;
	private String displayName;
	private String id;
	private String fullName;
	private Object photoBitmap;
	
	ZCUserInfo(String displayName ,String fullName,String id ,String country , String language , String timeZone , int gender ,  List<String> eMailAddress) {
		
		this.displayName = displayName;
		this.id = id;
		this.fullName = fullName;
		this.country = country;
		this.gender = gender;
		this.timeZone = timeZone;
		this.eMailAddress = eMailAddress;
		this.language = language;
	}
	
	
	public void setPersonalPhotoBitmap(Object photoBitmap){
		this.photoBitmap = photoBitmap;
	}
	
	public Object getPersonalPhotoBitmap(){
		return photoBitmap;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public String getFullName(){
		return fullName;
	}
	
	public String getId(){
		return id;
	}
	
	public int getGender(){
		return gender;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getLanguage(){
		return language;
	}
	
	public String getTimeZone(){
		return timeZone;
	}
	
	public List<String> getEmailAddress(){
		return eMailAddress;
	}
	
	
}
