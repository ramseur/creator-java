// $Id$
package com.zoho.creator.jframework.metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZCSection {

	private String appOwner = null;
	private String appLinkName = null;
	private String sectionName = null;
	private String sectionLinkName = null;
	private boolean isHidden = false;
	private Long sectionID = null;
	private Date modifiedTime = null;
	

	private List<ZCComponent> components = new ArrayList<ZCComponent>();
	private boolean componentsAdded = false;
	
	ZCSection(String appOwner, String appLinkName,  String sectionName, String sectionLinkName, boolean isHidden, Long sectionID, Date modifiedTime) {
		this.appLinkName = appLinkName;
		this.sectionName = sectionName;
		this.appOwner = appOwner;
		this.sectionLinkName = sectionLinkName;
		this.isHidden = isHidden;
		this.sectionID = sectionID;
		this.modifiedTime = modifiedTime;
	}

	public String toString() {
		return sectionName + " : " + appOwner + " : " + sectionLinkName + " : " + isHidden + " : " + sectionID + " : " + modifiedTime + " : " + components; //No I18N	
	}
	
	public String getSectionName() {
		return sectionName;
	}

	public String getAppOwner() {
		return appOwner;
	}

	public String getSectionLinkName() {
		return sectionLinkName;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public Long getSectionID() {
		return sectionID;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public final List<ZCComponent> getComponents() {
		final List<ZCComponent> toReturn = new ArrayList<ZCComponent>(components);
		return toReturn;
	}

	void addComponents(List<ZCComponent> zcComponents) {
		if(!componentsAdded) {
			components.addAll(zcComponents);
			componentsAdded = true;
		} else {
			throw new RuntimeException("You cannot add more than once"); //No I18N
		}
	}

	public String getAppLinkName() {
		return appLinkName;
	}

	
}
