// $Id$

package com.zoho.creator.jframework;

import java.util.HashMap;
import java.util.Map;


public enum FieldType   {
	
	SINGLE_LINE (1), MULTI_LINE (2), EMAIL (3), NUMBER(5), CURRENCY(8), PERCENTAGE(7), DECISION_CHECK_BOX(16), DATE(10), NOTES(24), FORMULA(20), FILE_UPLOAD(19), DECIMAL(6), IMAGE(18), URL(17), DATE_TIME(11), SUB_FORM(21), RICH_TEXT(4), AUTO_NUMBER(9), DROPDOWN(12), RADIO(13) , MULTISELECT(14), CHECKBOXES(15), NEW_PICKLIST(1100), NEW_RADIO(1101),EXTERNAL_FIELD(22),EXTERNAL_LINK(23),UNKNOWN(-1);

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
		case 22:
			return "EXTERNAL FIELD";
		case 23:
			return "EXTERNAL LINK";
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
    			ftype.equals(FieldType.EXTERNAL_FIELD) ||
    			ftype.equals(FieldType.NEW_PICKLIST) ||
    			ftype.equals(FieldType.NEW_RADIO);
    }

    public static boolean isSingleChoiceField(FieldType ftype) {
    	return 	ftype.equals(FieldType.RADIO) || 
    			ftype.equals(FieldType.DROPDOWN) || 
    			ftype.equals(FieldType.NEW_PICKLIST) ||
    			ftype.equals(FieldType.EXTERNAL_FIELD) ||
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
    public static boolean isBulkEditUNSupportedField(FieldType ftype)
    {
    	return (ftype.equals(FieldType.SUB_FORM) || 
    			ftype.equals(FieldType.NOTES) || 
    			ftype.equals(FieldType.AUTO_NUMBER) || 
    			isUNSupportedField(ftype) || 
    			ftype.equals(FieldType.FORMULA) ||
    			FieldType.isPhotoField(ftype));
    }
    public static boolean isUNSupportedField(FieldType ftype)
    {
    	return (ftype.equals(FieldType.EXTERNAL_FIELD) || 
    			ftype.equals(FieldType.EXTERNAL_LINK));
    }
    
    
    public static boolean isDisplayAsLinksField(FieldType ftype) {
    	return 	ftype.equals(FieldType.RICH_TEXT) || 
    			ftype.equals(FieldType.URL) || 
    			ftype.equals(FieldType.EXTERNAL_FIELD) || 
    			ftype.equals(FieldType.SUB_FORM);
    }
    
    static FieldType getFieldType(String type) {
    	FieldType fieldType = FieldType.SINGLE_LINE;
    	if(type.equals("MULTI_SELECT")) {
			fieldType = FieldType.MULTISELECT;
		} else if(type.equals("FILE_UPLOAD")) {
			fieldType = FieldType.FILE_UPLOAD;
		} else if(type.equals("IMAGE")) {
			fieldType = FieldType.IMAGE;
		} else if(type.equals("URL")){
			fieldType = FieldType.URL;
		} else if(type.equals("TEXT_AREA")){
			fieldType = FieldType.MULTI_LINE;
		} else if(type.equals("EMAIL_ADDRESS")){
			fieldType = FieldType.EMAIL;
		} else if(type.equals("RICH_TEXT_AREA")){
			fieldType = FieldType.RICH_TEXT;
		} else if(type.equals("DATE")){
			fieldType = FieldType.DATE;
		} else if(type.equals("DATE_TIME")){
			fieldType = FieldType.DATE_TIME;
		} else if(type.equals("INLINE_SINGLE_SELECT")){
			fieldType = FieldType.DROPDOWN;
		} else if(type.equals("INLINE_SINGLE_SELECT")){
			fieldType = FieldType.RADIO;
		} else if(type.equals("NUMBER")){
			fieldType = FieldType.NUMBER;
		} else if(type.equals("PERCENTAGE")){
			fieldType = FieldType.PERCENTAGE;
		} else if(type.equals("CURRENCY")){
			fieldType = FieldType.CURRENCY;
		} else if(type.equals("CHECK_BOX")){
			fieldType = FieldType.DECISION_CHECK_BOX;
		} else if(type.equals("SCRIPT")){
			fieldType = FieldType.FORMULA;
		} else if(type.equals("SUB_FORM")){
			fieldType = FieldType.SUB_FORM;
		} else if(type.equals("EXTERNAL_FIELD")){
			fieldType = EXTERNAL_FIELD;
		} 
    		
		return fieldType;
    }
    	

}

