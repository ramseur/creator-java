// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.List;



public class ZCChoice extends ZCRecord{

	private String key = null;
	private String value = null;
	
	
	ZCChoice(List<ZCRecordValue> recValues,long recordid,String key,String value) {
		super(recordid,recValues,value);
		this.key = key;
		this.value = value;
	}
	
	
	ZCChoice(String key, String value) {
		super(key);
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
