// $Id$
package com.zoho.creator.jframework.metadata;

import java.util.HashMap;
import java.util.Map;

public enum ZCButtonType {

	BUTTON (61), SUBMIT (62), RESET (63), CANCEL(64);

	private int buttonType = -1;
	
	private static Map<Integer, ZCButtonType> typeMap;

	static {
    	typeMap = new HashMap<Integer, ZCButtonType>();
    	for (ZCButtonType type : values()) {
    		typeMap.put(type.buttonType, type);
    	}
    }

	private ZCButtonType(int buttonType) {
		this.buttonType = buttonType;
	}
	
	
     static ZCButtonType getButtonType(int type) {
    	return typeMap.get(type);
    }
    
    public String toString() {
    	return buttonType + "";
    }
	
}
