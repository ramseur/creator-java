// $Id$
package com.zoho.creator.jframework;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;


public class ZCFilterValue{

	private String value = null;
	private boolean isSelected = false;
	private String dispValue = null;

	ZCFilterValue(String value,String dispValue) {
		this.value = value;
		this.dispValue  = dispValue;
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
	
	public String getDisplayValue() {
		return dispValue;
	}
	
	

}
