// $Id$
package com.zoho.creator.jframework;


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
