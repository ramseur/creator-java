// $Id$

package com.zoho.creator.jframework.metadata;

import java.util.List;

import org.w3c.dom.Document;

public interface ZCFormEvent {

	public void setResponseDocument(Document rootDocument);
	public void hideField(ZCField field);
	public void showField(ZCField field);
	public void enableField(ZCField field);
	public void disableField(ZCField field);
	public void setValue(ZCField field, String value);
	public void appendItems(ZCField field, List<String> values);
	public void selectItems(ZCField field,List<String>values);
	public void selectAllItems(ZCField field);
	public void deSelectItem(ZCField field, List<String> value);
	public void deSelectAllItems(ZCField field);
	public void clearItems(ZCField field);
	public void alert(String message);
	public void reloadForm();
	
	
}
