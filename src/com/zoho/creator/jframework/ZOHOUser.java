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

import com.zoho.creator.jframework.BasicUserStorage;


public class ZOHOUser {

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
	private HashMap<String,Object> userObject = new HashMap<String, Object>();
	
	
	public void setObject(String key,Object object){
		userObject.put(key, object);
	}
	
	public Object getObject(String key){
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
	



	public static void setUserStorage(UserStorage userStorage) {
		if(userStorage != null) {
			ZOHOUser.userStorage = userStorage;
		}
	}
	

	static ZOHOUser getUserObject() {
	    if(userCredential == null && userStorage != null) {
    		String loadedAuthToken = userStorage.loadAuthToken();
    		if(loadedAuthToken != null) {
    			try {
					userCredential = new ZOHOUser(loadedAuthToken);
				} catch (ZCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
	    }
	    return userCredential;
	}
	
	
	
	private ZOHOUser(String authToken) throws ZCException {
		this.authToken = authToken;
		userCredential = this;
		URLPair userPersonalInfoURL = ZCURL.userPersonalInfoURL();
		Document rootDocument = ZOHOCreator.postURLXML(userPersonalInfoURL.getUrl(), userPersonalInfoURL.getNvPair());

		NodeList nl = rootDocument.getChildNodes();
		
		
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			////////System.out.println("******* " + responseNode.getNodeName());
			if(responseNode.getNodeName().equals("response")) {
				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0; j<responseNodes.getLength(); j++) {
					Node resultNode = responseNodes.item(j);
					////////System.out.println(resultNode.getNodeName());
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
								gender = XMLParser.getIntValue(resultNodeChild, 0); //No I18N
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

	
    private ZOHOUser(String uname, String password) throws ZCException {
        Properties  props = new Properties();
        int status = 105;
        String cause = null;
        String result = null;
			try {
				uname = java.net.URLEncoder.encode(uname, "UTF-8");
				password = java.net.URLEncoder.encode(password, "UTF-8");//No I18N
				URLPair loginURL = ZCURL.getAuthTokenURL(uname, password);//No I18N
	            String response = ZOHOCreator.postURL(loginURL.getUrl(), loginURL.getNvPair());
	            try {
					props.load(new StringReader(response));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //No I18N
            //System.out.println("loginURL : " + loginURL);
 
            //System.out.println("response : " + response);
        authToken = props.getProperty("AUTHTOKEN");
        result = props.getProperty("RESULT");
        //System.out.println("authToken : " + authToken);
        //System.out.println("result : " + result);
        if (authToken != null && result != null)
        {
            status = 200;
            userCredential = this;
            ZOHOUser.userStorage.saveAuthToken(authToken);
            //String filePath = "./Login"; //No I18N
            //EncodeObject.encode(filePath, userCredential);
        }
        else
        {
            cause = props.getProperty("CAUSE");
            
            //System.out.println("Cause : " + cause);
 
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
        //System.out.println("currentUser : " + currentUser);
        
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
		//System.out.println("currentUser : " + currentUser);
		
		if(currentUser != null) {
			currentUser.logout(); // logout and remove the files.
		}
		ZOHOUser user = new ZOHOUser(authToken);
		ZOHOUser.userStorage.saveAuthToken(authToken);
		
		return user;
	}
	
	void logout() {
		if(authToken != null) {
			URLPair delAuthTokenURL = ZCURL.deleteAuthToken(authToken);
			try {
				String response = ZOHOCreator.postURL(delAuthTokenURL.getUrl(), delAuthTokenURL.getNvPair());
			} catch (ZCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ZOHOUser.userStorage.removeAuthToken();
			//System.out.println("***** LOGOUT RESPONSE **** " + response);
			userCredential = null;
		}
	}
	
	String getAuthToken() {
		return authToken;
	}

}
