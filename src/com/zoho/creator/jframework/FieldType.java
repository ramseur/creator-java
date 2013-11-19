// $Id$

package com.zoho.creator.jframework;

import java.util.HashMap;
import java.util.Map;


public enum FieldType   {
	
	SINGLE_LINE (1), MULTI_LINE (2), EMAIL (3), NUMBER(5), CURRENCY(8), PERCENTAGE(7), DECISION_CHECK_BOX(16), DATE(10), NOTES(24), FORMULA(20), FILE_UPLOAD(19), DECIMAL(6), IMAGE(18), URL(17), DATE_TIME(11), SUB_FORM(21), RICH_TEXT(4), AUTO_NUMBER(9), DROPDOWN(12), RADIO(13) , MULTISELECT(14), CHECKBOXES(15), NEW_PICKLIST(1100), NEW_RADIO(1101),ZOHO_CRM(22),ZOHO_CRM_LINK(23),UNKNOWN(-1);

	private static Map<Integer, FieldType> typeMap;

	static {
    	typeMap = new HashMap<Integer, FieldType>();
    	for (FieldType type : values()) {
    		typeMap.put(type.fieldType, type);
    	}
    }

    private int fieldType;

    private FieldType(int fieldType) {
        this.fieldType = fieldType;
    }
    
    public String toString() {
		switch (fieldType) {
		case 1:
				return "SINGLE LINE";//No I18N
		case 2:
			return "MULTI LINE";//No I18N
		case 3:
			return "EMAIL";//No I18N
		case 5:
			return "NUMBER";//No I18N
		case 8:
			return "CURRENCY";//No I18N
		case 7:
			return "PERCENTAGE";//No I18N
		case 16:
			return "DECISION CHECK BOX";//No I18N
		case 10:
			return "DATE";//No I18N
		case 24:
			return "NOTES";//No I18N
		case 20:
			return "FORMULA";//No I18N
		case 19:
			return "FILE UPLOAD";//No I18N
		case 6:
			return "DECIMAL";//No I18N
		case 18:
			return "IMAGE";//No I18N
		case 17:
			return "URL";//No I18N
		case 11:
			return "DATE TIME";//No I18N
		case 21:
			return "SUB FORM";//No I18N
		case 4:
			return "RICH TEXT";//No I18N
		case 9:
			return "AUTO NUMBER";//No I18N
		case 12:
			return "DROP DOWN";//No I18N
		case 13:
			return "RADIO";//No I18N
		case 14:
			return "MULTI SELECT";//No I18N
		case 15:
			return "CHECKBOXES";//No I18N
		case 1100:
			return "DROP DOWN(NEM)";//No I18N
		case 1101:
			return "RADIO(NEM)";//No I18N
		default	:
			return "";//No I18N
		}
    }
     
    static FieldType getFieldType(int type) {
    	FieldType toReturn = typeMap.get(type);
    	if(toReturn == null) {
    		toReturn = FieldType.UNKNOWN;
    	}
    	return toReturn;
    }
    
    
    public static boolean isChoiceField(FieldType ftype) {
    	return 	ftype.equals(FieldType.MULTISELECT) || 
    			ftype.equals(FieldType.RADIO) || 
    			ftype.equals(FieldType.DROPDOWN) || 
    			ftype.equals(FieldType.CHECKBOXES) ||
    			ftype.equals(FieldType.NEW_PICKLIST) ||
    			ftype.equals(FieldType.NEW_RADIO);
    }

    public static boolean isSingleChoiceField(FieldType ftype) {
    	return 	ftype.equals(FieldType.RADIO) || 
    			ftype.equals(FieldType.DROPDOWN) || 
    			ftype.equals(FieldType.NEW_PICKLIST) ||
    			ftype.equals(FieldType.NEW_RADIO);
    }

    public static boolean isMultiChoiceField(FieldType ftype) {
    	return 	ftype.equals(FieldType.MULTISELECT) || 
    			ftype.equals(FieldType.CHECKBOXES);
    }
    
    public static boolean isPhotoField(FieldType ftype) {
    	return 	ftype.equals(FieldType.FILE_UPLOAD) || 
    			ftype.equals(FieldType.IMAGE);
    }
    
    public static boolean isNumberField(FieldType ftype) {
    	return 	ftype.equals(FieldType.NUMBER) || 
    			ftype.equals(FieldType.PERCENTAGE) ||
    			ftype.equals(FieldType.CURRENCY) ||
    			ftype.equals(FieldType.DECIMAL);
    }
    
    public static boolean isPrimaryFieldType(FieldType ftype) {
    	return !(ftype.equals(FieldType.FILE_UPLOAD) || 
    			ftype.equals(FieldType.IMAGE) || 
    			ftype.equals(FieldType.URL) || 
    			ftype.equals(FieldType.AUTO_NUMBER) || 
    			ftype.equals(FieldType.UNKNOWN) || 
    			ftype.equals(FieldType.RICH_TEXT));
    }

    public static boolean isSecondaryFieldType(FieldType ftype) {
    	return !(ftype.equals(FieldType.FILE_UPLOAD) || 
    			ftype.equals(FieldType.IMAGE) || 
    			ftype.equals(FieldType.URL) || 
    			ftype.equals(FieldType.UNKNOWN) || 
    			ftype.equals(FieldType.AUTO_NUMBER) || 
    			ftype.equals(FieldType.RICH_TEXT));
    }

}

