// $Id$
package com.zoho.creator.jframework;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;




public class ZCButton implements Comparable<ZCButton>{

	private String name = null;
	private String linkName = null;
	private int sequenceNumber = -1;
	private ZCButtonType buttonType = null;
	private ZCForm zcForm = null;
	private boolean formIsSet = false;
	private boolean onClickExists = false;



	public ZCButton(String name, String linkName, ZCButtonType buttonType) {
		this.name = name;
		this.linkName = linkName;
		this.buttonType = buttonType;
	}

	public String toString() {
		return "Name: " + name + " - Link Name"  + linkName + " - Button Type " + buttonType;//No I18N
	}

	public String getName() {
		return name;
	}

	public String getLinkName() {
		return linkName;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public ZCButtonType getButtonType() {
		return buttonType;
	}

	ZCForm getForm() {
		return zcForm;
	}

	public int compareTo(ZCButton arg0) {
		ZCButton toCompare = (ZCButton)arg0;
		return getSequenceNumber()>toCompare.getSequenceNumber() ? 1 
				: toCompare.getSequenceNumber() > getSequenceNumber() ? -1 
						: 0;
	}

	void setForm(ZCForm zcForm) {
//		if(!formIsSet) {
			this.zcForm = zcForm;
//			formIsSet = true;
//		} else {
//			throw new RuntimeException ("You cannot set more than once"); //No I18N
//		}
	}

	void setOnClickExists(boolean onClickExists)
	{
		this.onClickExists = onClickExists;
	}

	public boolean isOnClickExists()
	{
		return onClickExists;
	}

	public ZCResponse click() throws ZCException{
		return ZOHOCreator.submitForm(this);
	}

}
