// $Id$
package com.zoho.creator.jframework.metadata;


public class ZCCustomAction {
	private String type = null;
	private String name = null;
	private long id = -1l;
	
	ZCCustomAction(String type, String name, long id) {
		this.type = type;
		this.name = name;
		this.id = id;
	}
	
	public String toString() {
		return "Custom Action Type: " + type + " - Name: " + name + " - id: " + id;  //No I18N
	}
	
	public String getType() {
		return type;
	}
	
	void setType(String type) {
		this.type = type;
	}
	
	public long getId() {
		return id;
	}
	
	void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	void setName(String name) {
		this.name = name;
	}

}
