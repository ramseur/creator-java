package com.zoho.creator.jframework;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public enum ExternalField{
	ZOHO_CRM (1) , SALES_FORCE (4) , UNKNOWN(-1);
	private static Map<Integer, ExternalField> typeMap;

	static {
    	typeMap = new HashMap<Integer, ExternalField>();
    	for (ExternalField type : values()) {
    		typeMap.put(type.externalFieldType, type);
    	}
    }
	
	 private int externalFieldType;
	    private ExternalField(int externalFieldType) {
	        this.externalFieldType = externalFieldType;
	    }
	    
	    public String toString() {
			switch (externalFieldType) {
			case 1:
				return "ZOHO CRM";//No I18N
			case 4:
				return "SALES FORCE";//No I18N
			default	:
				return "";//No I18N
			}
	    }
	    
	    static ExternalField getExternalFieldType(int type) {
	    	ExternalField toReturn = typeMap.get(type);
	    	if(toReturn == null) {
	    		toReturn = ExternalField.UNKNOWN;
	    	}
	    	return toReturn;
	    }
	    
	   
}
