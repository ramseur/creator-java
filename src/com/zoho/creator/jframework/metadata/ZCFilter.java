// $Id$

package com.zoho.creator.jframework.metadata;

import java.util.ArrayList;
import java.util.List;

public class ZCFilter {
	private String filterName = null;
	private String filterLinkName = null;
	private List<ZCFilterValue> values = new ArrayList<ZCFilterValue>();
	private boolean valuesAdded = false;
	
	ZCFilter(String filterLinkName, String filterName) {
		this.setFilterLinkName(filterLinkName);
		this.setFilterName(filterName);
	}
	
	public String toString() {
		return "Filter Link Name: " + filterLinkName + " - Filter Name: " + filterName + " - Values: " + values;  //No I18N
	}

	public String getFilterName() {
		return filterName;
	}

	void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterLinkName() {
		return filterLinkName;
	}

	void setFilterLinkName(String filterLinkName) {
		this.filterLinkName = filterLinkName;
	}

	public List<ZCFilterValue> getValues() {
		final List<ZCFilterValue> toReturn = new ArrayList<ZCFilterValue>(values);
		return toReturn ;
	}

	void addValues(List<ZCFilterValue> values) {
		if(!valuesAdded) {
			this.values.addAll(values);
			valuesAdded = true;
		} else {
			throw new RuntimeException ("You cannot add more than once"); //No I18N
		}
	}
	

}
