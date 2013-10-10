// $Id$
package com.zoho.creator.jframework;


public class ZCChoice {

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

}
