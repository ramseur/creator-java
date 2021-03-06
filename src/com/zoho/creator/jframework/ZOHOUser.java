// $Id$
package com.zoho.creator.jframework;


import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class ZOHOUser implements Parcelable{

	private String authToken = null;
	private static ZOHOUser userCredential = null;
	private static UserStorage userStorage = new BasicUserStorage();
	private String country;
	private String language;
	private String timeZone;
	private List<String> eMailAddresses = new ArrayList<String>();
	private int gender;
	private String displayName;
	private String id;
	private String fullName;
	private Bitmap bitmap = null;
	private HashMap<String,Object> userObject = new HashMap<String, Object>();


	public void setObject(String key,Object object){
		userObject.put(key, object);
	}

	public Object getObject(String key){
		return userObject.get(key);
	}
	
	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap()
	{
		return bitmap;
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
	
	public static void setUserStorage(UserStorage userStorage) {
		if(userStorage != null) {
			ZOHOUser.userStorage = userStorage;
		}
	}


	static ZOHOUser getUserObject() throws ZCException{
		
		if((userCredential == null && userStorage != null)) {
			String loadedAuthToken = userStorage.loadAuthToken();
			if(loadedAuthToken != null) {
				try {
					userCredential = new ZOHOUser(loadedAuthToken);
				} catch (ZCException e) {
					// TODO Auto-generated catch block
					throw e;
				}
			}
		}
		return userCredential;
	}

	private ZOHOUser(String authToken) throws ZCException {
		this.authToken = authToken;
		userCredential = this;
		
		Document rootDocument = ZOHOCreator.getUserDocument();
		if(rootDocument!=null)
		{
			NodeList nl = rootDocument.getChildNodes();
			for(int i=0; i<nl.getLength(); i++) {
				Node responseNode = nl.item(i);
				if(responseNode.getNodeName().equals("response")) {
					NodeList responseNodes = responseNode.getChildNodes();
					for(int j=0; j<responseNodes.getLength(); j++) {
						Node resultNode = responseNodes.item(j);
						if(resultNode.getNodeName().equals("result")) {
							NodeList resultNodes = resultNode.getChildNodes();		
							for(int k=0; k<resultNodes.getLength(); k++) {
								Node resultNodeChild = resultNodes.item(k);
								if(resultNodeChild.getNodeName().equals("DISPLAY_NAME")) {
									displayName = XMLParser.getStringValue(resultNodeChild, ""); //No I18N
								} else if(resultNodeChild.getNodeName().equals("ZUID")) {
									id = XMLParser.getStringValue(resultNodeChild, ""); //No I18N
								} else if(resultNodeChild.getNodeName().equals("FULL_NAME")) {
									fullName = XMLParser.getStringValue(resultNodeChild, ""); //No I18N
								} else if(resultNodeChild.getNodeName().equals("GENDER")) {
									gender = XMLParser.getIntValue(resultNodeChild, 0);
								} else if(resultNodeChild.getNodeName().equals("EMAIL_ID")) {
									String eMailList = XMLParser.getStringValue(resultNodeChild, ""); //No I18N
									String[] tokens = eMailList.split(",");
									for(int m =0 ;m<tokens.length;m++){
										eMailAddresses.add(tokens[m]);
									}
								} else if(resultNodeChild.getNodeName().equals("LOCALE_INFO")) {
									String localeInfo = XMLParser.getStringValue(resultNodeChild, ""); //No I18N
									String[] tokens = localeInfo.split("|");
									language = tokens[1];
									country = tokens[0];
									timeZone = tokens[2];
								}
							}
						}
					}
				}
			}
		}
	}


	//private ZOHOUser(String authToken)
	//	{
	//		this.authToken = authToken;
	//		userCredential = this;
	//		//String response = ZOHOCreator.getUserDocument();
	//		JSONArray jArray = new JSONArray(response);
	//	}

	private ZOHOUser(String uname, String password) throws ZCException {
		Properties  props = new Properties();
		int status = 105;
		String cause = null;
		String result = null;
		try {
			uname = java.net.URLEncoder.encode(uname, "UTF-8");//No I18N
			password = java.net.URLEncoder.encode(password, "UTF-8");//No I18N
			String response = ZOHOCreator.getAuthTokenResponse(uname, password);
			try {
				props.load(new StringReader(response));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		authToken = props.getProperty("AUTHTOKEN");
		result = props.getProperty("RESULT");
		if (authToken != null && result != null)
		{
			status = 200;
			userCredential = this;
			//   ZOHOUser.userStorage.saveAuthToken(authToken);
			//String filePath = "./Login"; //No I18N
			//EncodeObject.encode(filePath, userCredential);

		}
		else
		{
			cause = props.getProperty("CAUSE");


			if (cause != null)
			{
				if (cause.equals("USER_NOT_ACTIVE"))          
				{          
					status = 101;          
				}          
				else if (cause.equals("INVALID_PASSWORD"))          
				{          
					status = 102;          
				}          
				else if (cause.equals("NO_SUCH_USER"))          
				{          
					status = 103;          
				}          
				else if (cause.equals("IP_NOT_ALLOWED"))          
				{          
					status = 104;          
				}          
				else if (cause.equals("REMOTE_SERVER_ERROR"))          
				{          
					status = 105;          
				}
				else if (cause.equals("ACCOUNT_REGISTRATION_NOT_CONFIRMED")) 
				{
					status = 106;
				}
			}
		}                
	}


	static ZOHOUser getUserObject(String uname, String password) throws ZCException {
		ZOHOUser currentUser = getUserObject();

		if(currentUser != null) {
			currentUser.logout(); // logout and remove the files.
		}
		if(uname == null || "".equals(uname) || password == null || "".equals(password)) {
			return null;
		}
		ZOHOUser user = new ZOHOUser(uname, password);
		return user;
	}

	static ZOHOUser getUserObject(String authToken) throws ZCException {
		ZOHOUser currentUser = getUserObject();

		if(currentUser != null) {
			currentUser.logout(); // logout and remove the files.
		}
		ZOHOUser user = new ZOHOUser(authToken);
		ZOHOUser.userStorage.saveAuthToken(authToken);
		return user;
	}

	void logout() {
		if(authToken != null) {
			ZOHOUser.userStorage.removeAuthToken();
			userCredential = null;
		}
	}

	public String getAuthToken() {
		return authToken;
	}

	
	public static final Parcelable.Creator<ZOHOUser> CREATOR = new Creator<ZOHOUser>() {  
		public ZOHOUser createFromParcel(Parcel in) {  
			return new ZOHOUser(in);  
		}  
		public ZOHOUser[] newArray(int size) {  
			return new ZOHOUser[size];  
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
		parcel.writeString(displayName);
		parcel.writeList(eMailAddresses);
		parcel.writeParcelable(bitmap, arg1);	 
	}
	
	public ZOHOUser(Parcel in)
	{
		displayName = in.readString();
		in.readList(eMailAddresses, String.class.getClassLoader());
		bitmap = in.readParcelable(Bitmap.class.getClassLoader());
	}
}
