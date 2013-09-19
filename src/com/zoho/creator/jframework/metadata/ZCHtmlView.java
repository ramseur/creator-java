// $Id$
package com.zoho.creator.jframework.metadata;

public class ZCHtmlView extends ZCComponent {

	
	private String htmlContent = "";
	
	ZCHtmlView(String appOwner, String appLinkName, String type, String componentName, String componentLinkName, String htmlContent) {
		super(appOwner, appLinkName, type, componentName, componentLinkName, -1, -1L);
		this.htmlContent = htmlContent;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

}
