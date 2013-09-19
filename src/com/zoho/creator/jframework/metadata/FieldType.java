// $Id$

package com.zoho.creator.jframework.metadata;

import java.util.HashMap;
import java.util.Map;


public enum FieldType   {
	
	SINGLE_LINE (1), MULTI_LINE (3), EMAIL (4), NUMBER(5), CURRENCY(6), PERCENTAGE(7), DECISION_CHECK_BOX(9), DATE(10), NOTES(14), FORMULA(15), FILE_UPLOAD(18), DECIMAL(19), IMAGE(20), URL(21), DATE_TIME(22), SUB_FORM(23), RICH_TEXT(24), DROPDOWN(100), RADIO(101) , MULTISELECT(102), CHECKBOXES(103), NEW_PICKLIST(1100), NEW_RADIO(1101);

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
		case 3:
			return "MULTI LINE";//No I18N
		case 4:
			return "EMAIL";//No I18N
		case 5:
			return "NUMBER";//No I18N
		case 6:
			return "CURRENCY";//No I18N
		case 7:
			return "PERCENTAGE";//No I18N
		case 9:
			return "DECISION CHECK BOX";//No I18N
		case 10:
			return "DATE";//No I18N
		case 14:
			return "NOTES";//No I18N
		case 15:
			return "FORMULA";//No I18N
		case 18:
			return "FILE UPLOAD";//No I18N
		case 19:
			return "DECIMAL";//No I18N
		case 20:
			return "IMAGE";//No I18N
		case 21:
			return "URL";//No I18N
		case 22:
			return "DATE TIME";//No I18N
		case 23:
			return "SUB FORM";//No I18N
		case 24:
			return "RICH TEXT";//No I18N
		case 100:
			return "DROP DOWN";//No I18N
		case 101:
			return "RADIO";//No I18N
		case 102:
			return "MULTI SELECT";//No I18N
		case 103:
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
    	return typeMap.get(type);
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
    			ftype.equals(FieldType.RICH_TEXT));
    }

    public static boolean isSecondaryFieldType(FieldType ftype) {
    	return !(ftype.equals(FieldType.FILE_UPLOAD) || 
    			ftype.equals(FieldType.IMAGE) || 
    			ftype.equals(FieldType.RICH_TEXT));
    }

}

