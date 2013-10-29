// $Id$

package com.zoho.creator.jframework;

import java.util.HashMap;
import java.util.Map;


public enum FieldType   {
	
	SINGLE_LINE (1), MULTI_LINE (3), EMAIL (4), NUMBER(5), CURRENCY(6), PERCENTAGE(7), DECISION_CHECK_BOX(9), DATE(10), NOTES(14), FORMULA(15), FILE_UPLOAD(18), DECIMAL(19), IMAGE(20), URL(21), DATE_TIME(22), SUB_FORM(23), RICH_TEXT(24), AUTO_NUMBER(31), DROPDOWN(100), RADIO(101) , MULTISELECT(102), CHECKBOXES(103), NEW_PICKLIST(1100), NEW_RADIO(1101),ZOHO_CRM(25),ZOHO_CRM_LINK(26),UNKNOWN(-1);

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
		case 31:
			return "AUTO NUMBER";//No I18N
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
    	if(type == 97 || type == 98 || type == 99) {
    		type = 23;
    	}
    	FieldType toReturn = typeMap.get(type);
    	if(toReturn == null) {
    		toReturn = FieldType.UNKNOWN;
    	}
    	return toReturn;
    }
    static FieldType getFieldTypeNew(int type) {
    	FieldType toReturn = null;
    	System.out.println("newwww");
    	if(type == 1)
    	{
    		toReturn = FieldType.SINGLE_LINE;
    	}
    	else if(type == 2)
    	{
    		toReturn = FieldType.MULTI_LINE;
    	}
    	else if(type == 3)
    	{
    		toReturn = FieldType.EMAIL;
    	}
    	else if(type == 4)
    	{
    		toReturn = FieldType.RICH_TEXT;
    	}
    	else if(type == 5)
    	{
    		toReturn = FieldType.NUMBER;
    	}
    	else if(type == 6)
    	{
    		toReturn = FieldType.DECIMAL;
    	}
    	else if(type == 7)
    	{
    		toReturn = FieldType.PERCENTAGE;
    	}
    	else if(type == 8)
    	{
    		toReturn = FieldType.CURRENCY;
    	}
    	else if(type == 9)
    	{
    		toReturn = FieldType.AUTO_NUMBER;
    	}
    	else if(type == 10)
    	{
    		System.out.println("date....");
    		toReturn = FieldType.DATE;
    	}
    	else if(type == 11)
    	{
    		toReturn = FieldType.DATE_TIME;
    	}
    	else if(type == 12)
    	{
    		System.out.println("drppppp");
    		toReturn = FieldType.DROPDOWN;
    	}
    	else if(type == 13)
    	{
    		toReturn = FieldType.RADIO;
    	}
    	else if(type == 14)
    	{
    		toReturn = FieldType.MULTISELECT;
    	}
    	
    	else if(type == 15)
    	{
    		toReturn = FieldType.CHECKBOXES;
    	}
    	else if(type == 16)
    	{
    		toReturn = FieldType.DECISION_CHECK_BOX;
    	}
    	else if(type == 17)
    	{
    		toReturn = FieldType.URL;
    	}
    	else if(type == 18)
    	{
    		toReturn = FieldType.IMAGE;
    	}
    	else if(type == 19)
    	{
    		toReturn = FieldType.FILE_UPLOAD;
    	}
    	else if(type == 20)
    	{
    		toReturn = FieldType.FORMULA;
    	}
    	else if(type == 21)
    	{
    		toReturn = FieldType.SUB_FORM;
    	}
    	else if(type == 22)
    	{
    		toReturn = FieldType.ZOHO_CRM;
    	}
    	else if(type == 23)
    	{
    		toReturn = FieldType.ZOHO_CRM_LINK;
    	}
    	else if(type == 24)
    	{
    		toReturn = FieldType.NOTES;
    	}
    	else if(type>0&&type<=24)
    	{
    		toReturn = FieldType.UNKNOWN;
    	}
    	System.out.println("torrrrr"+toReturn);
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

