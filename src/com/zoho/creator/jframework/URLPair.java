// $Id$

package com.zoho.creator.jframework;

import java.util.List;

import org.apache.http.NameValuePair;

class URLPair {

	private String url = null;
	private List<NameValuePair> nvPair = null;
	
	URLPair(String url, List<NameValuePair> nvPair) {
		this.url = url;
		this.nvPair = nvPair;
	}

	String getUrl() {
		return url;
	}


	List<NameValuePair> getNvPair() {
		return nvPair;
	}

}
