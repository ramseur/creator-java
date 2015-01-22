// $Id$
package com.zoho.creator.jframework;

import java.util.HashMap;
import java.util.Map;

public enum ExternalField{
	ZOHO_CRM ("ZOHOCRM") , SALES_FORCE ("SALEFORCE") , UNKNOWN("");//No I18N
	private static Map<String, ExternalField> typeMap;

	static {
		typeMap = new HashMap<String, ExternalField>();
		for (ExternalField type : values()) {
			typeMap.put(type.externalFieldType, type);
		}
	}

	private String externalFieldType;
	private ExternalField(String externalFieldType) {
		this.externalFieldType = externalFieldType;
	}

//	public String toString() {
//		switch (externalFieldType) {
//		case 1:
//			return "ZOHOCRM";//No I18N
//		case 4:
//			return "SALESFORCE";//No I18N
//		default	:
//			return "";//No I18N
//		}
//	}

	static ExternalField getExternalFieldType(int type) {
		ExternalField toReturn = typeMap.get(type);
		if(toReturn == null) {
			toReturn = ExternalField.UNKNOWN;
		}
		return toReturn;
	}

	static ExternalField getExternalFieldType(String type) {
		ExternalField toReturn = typeMap.get(type);
		if(toReturn == null) {
			toReturn = ExternalField.UNKNOWN;
		}
		return toReturn;
	}

	public static String getCrmModuleType(int moduleType)
	{
		switch(moduleType)
		{
		case 1:
			return "Leads";//No I18N
		case 2:
			return "Accounts";//No I18N
		case 3:
			return "Potentials";//No I18N
		case 4:
			return "Contacts";//No I18N
		case 5:
			return "Campaingns";//No I18N
		case 8:
			return "Cases";//No I18N
		case 9:
			return "Solutions";//No I18N
		case 10:
			return "Products";//No I18N
		case 11:
			return "PriceBooks";//No I18N
		case 12:
			return "Quotes";//No I18N
		case 13:
			return "Vendors";//No I18N
		case 14:
			return "Purchaes Orders";//No I18N
		case 15:
			return "Sales Orders";//No I18N
		case 16:
			return "Invoices";//No I18N
		case 17:
			return "Users";//No I18N

		}
		return null;
	}

}
