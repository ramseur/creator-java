package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZCUserInfo {
	
	private String country;
	private String language;
	private String timeZone;
	private List<String> eMailAddresses = new ArrayList<String>();
	private int gender;
	private String displayName;
	private String id;
	private String fullName;
	private HashMap<String,Object> userObject = new HashMap<String, Object>();
	
	ZCUserInfo(String displayName ,String fullName,String id ,String country , String language , String timeZone , int gender ,  List<String> eMailAddresses) {
		
		this.displayName = displayName;
		this.id = id;
		this.fullName = fullName;
		this.country = country;
		this.gender = gender;
		this.timeZone = timeZone;
		this.eMailAddresses = eMailAddresses;
		this.language = language;
	}
	
	
	public void setUserObject(String key,Object object){
		userObject.put(key, object);
	}
	
	public Object getUserObject(String key){
		return userObject.get(key);
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
	
	public List<String> getEmailAddresses(){
		return eMailAddresses;
	}
	
	
}
