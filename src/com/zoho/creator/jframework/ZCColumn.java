// $Id$
package com.zoho.creator.jframework;

public class ZCColumn extends ZCField{
	private boolean isSortOrderForGroupByAscending = true;
	private boolean isSortOrderForSortByAscending = true;
	private boolean isHidden = false;
	private ZCCondition zcCondition = null;


	ZCColumn(String fieldName, FieldType type, String displayName) {
		super(fieldName, type, displayName);
		// TODO Auto-generated constructor stub
	}

	public void setCondition(ZCCondition zcCondition ) {
		this.zcCondition = zcCondition;
	}

	public ZCCondition getCondition() {
		return zcCondition;
	}

	public void setSortOrderForGroupByAscending(boolean isSortOrderForGroupByAscending) {
		this.isSortOrderForGroupByAscending = isSortOrderForGroupByAscending; 
	}

	public boolean isSortOrderForGroupByAscending() {
		return isSortOrderForGroupByAscending;
	}

	public boolean isSortOrderForSortByAscending() {
		return isSortOrderForSortByAscending;
	}

	public void setSortOrderForSortByAscending(boolean isSortOrderForSortByAscending) {
		this.isSortOrderForSortByAscending = isSortOrderForSortByAscending;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	 
}

