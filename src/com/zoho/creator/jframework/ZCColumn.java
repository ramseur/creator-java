// $Id$
package com.zoho.creator.jframework;


public class ZCColumn extends ZCField {

	private ZCCondition zcCondition = null;
	private boolean isSortOrderForGroupByAscending = true;
	private boolean isSortOrderForSortByAscending = true;
	private boolean isHidden = false;
	
	
	ZCColumn(String fieldName, FieldType type, String displayName) {
		super(fieldName, type, displayName, -1);
		// TODO Auto-generated constructor stub
	}
	
	public void setCondition(ZCCondition zcCondition ) {
		this.zcCondition = zcCondition;
	}
	
	public ZCCondition getCondition() {
		return zcCondition;
	}

	public void setSortOrderForGroupByAscending(boolean isSortOrderForGroupByAscending) {
		this.isSortOrderForGroupByAscending= isSortOrderForGroupByAscending; 
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
