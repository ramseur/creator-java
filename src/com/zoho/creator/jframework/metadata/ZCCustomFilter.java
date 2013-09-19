// $Id$
package com.zoho.creator.jframework.metadata;

public class ZCCustomFilter {

	private String name = null;
	private long id = -1L;
	
	ZCCustomFilter(String name, long id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}
}
