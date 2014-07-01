// $Id$
package com.zoho.creator.jframework;

import android.os.Parcel;
import android.os.Parcelable;


public class ZCChoice{

	private String key = null;
	private String value = null;
	
	ZCChoice(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String toString() {
		return  value; //No I18N
	}

	public String getKey() {
		return key;
	}


	public String getValue() {
		return value;
	}
	
	public boolean equals(Object toCheck) {
        if (this == toCheck) {
        	return true;
        }
        
        if (key == null || toCheck == null || getClass() != toCheck.getClass()) {
        	return false;
        }
        return key.equals(((ZCChoice)toCheck).getKey());
	}
	

}
