// $Id$
package com.zoho.creator.jframework;


import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.zoho.creator.jframework.BasicUserStorage;


public class ZOHOUser {

	private String authToken = null;
	private static ZOHOUser userCredential = null;
	private static UserStorage userStorage = new BasicUserStorage();

	
	public static void setUserStorage(UserStorage userStorage) {
		if(userStorage != null) {
			ZOHOUser.userStorage = userStorage;
		}
	}
	

	static ZOHOUser getUserObject() {
	    if(userCredential == null && userStorage != null) {
    		String loadedAuthToken = userStorage.loadAuthToken();
    		if(loadedAuthToken != null) {
    			userCredential = new ZOHOUser(loadedAuthToken);
    		}
	    }
	    return userCredential;
	}
	
	
	
	private ZOHOUser(String authToken) {
		this.authToken = authToken;
		userCredential = this;
	}
	
    private ZOHOUser(String uname, String password) throws ZCException {
        Properties  props = new Properties();
        int status = 105;
        String cause = null;
        String result = null;
            String loginURL = null;
			try {
				loginURL = ZCURL.getAuthTokenURL(java.net.URLEncoder.encode(uname, "UTF-8"), java.net.URLEncoder.encode(password, "UTF-8"));//No I18N
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //No I18N
            //System.out.println("loginURL : " + loginURL);
 
            String response = ZOHOCreator.postURL(loginURL, null);
            //System.out.println("response : " + response);
            try {
				props.load(new StringReader(response));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	static ZOHOUser getUserObject(String authToken)  {
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
			String delAuthTokenURL = ZCURL.deleteAuthToken(authToken);
			
			try {
				String response = ZOHOCreator.postURL(delAuthTokenURL, null);
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
