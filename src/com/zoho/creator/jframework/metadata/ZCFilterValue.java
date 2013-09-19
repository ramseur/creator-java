// $Id$
package com.zoho.creator.jframework.metadata;


public class ZCFilterValue {

	private String value = null;
	private boolean isSelected = false;

	ZCFilterValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return "value  : " + value + " - " + isSelected;  //No I18N
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getValue() {
		return value;
	}

}
