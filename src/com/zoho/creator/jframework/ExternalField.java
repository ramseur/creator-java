package com.zoho.creator.jframework;

import java.util.HashMap;
import java.util.Map;

public enum ExternalField{
	ZOHO_CRM ("ZOHOCRM") , SALES_FORCE ("SALEFORCE") , UNKNOWN("");
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
			return "Leads";
		case 2:
			return "Accounts";
		case 3:
			return "Potentials";
		case 4:
			return "Contacts";
		case 5:
			return "Campaingns";
		case 8:
			return "Cases";
		case 9:
			return "Solutions";
		case 10:
			return "Products";
		case 11:
			return "PriceBooks";
		case 12:
			return "Quotes";
		case 13:
			return "Vendors";
		case 14:
			return "Purchaes Orders";
		case 15:
			return "Sales Orders";
		case 16:
			return "Invoices";

		}
		return null;
	}

}
