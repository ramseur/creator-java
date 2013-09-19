// $Id$
package com.zoho.creator.jframework.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.zoho.creator.jframework.metadata.UserStorage;

public class BasicUserStorage implements UserStorage {

	@Override
	public String loadAuthToken() {
	    Scanner scanner = null;
		try {
		    StringBuilder text = new StringBuilder();
			scanner = new Scanner(new FileInputStream("/login")); //No I18N
		    while (scanner.hasNextLine()){
			     text.append(scanner.nextLine());
			}
		    return text.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
	    	if(scanner != null) {
	    		scanner.close();
	    	}
	    }
	    return null;
	}

	@Override
	public void saveAuthToken(String authToken) {
		PrintStream out = null;
		try {
		    out = new PrintStream(new FileOutputStream("/login"));//No I18N
			out.print(authToken);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
		finally {
		    if (out != null) {
		    	out.close();
		    }
		}
	}

	@Override
	public void removeAuthToken() {
		new File("/login").delete();//No I18N
	}

}
