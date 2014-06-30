// $Id$
package com.zoho.creator.jframework;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ZCHtmlView extends ZCComponent{

	
	private String htmlContent = "";
	
	ZCHtmlView(String appOwner, String appLinkName, String type, String componentName, String componentLinkName, String htmlContent) {
		super(appOwner, appLinkName, type, componentName, componentLinkName, -1);
		this.htmlContent = htmlContent;
	}

	public String getHtmlContent() {
		return htmlContent;
	}
	

}
