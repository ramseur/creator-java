// $Id$
package com.zoho.creator.jframework;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZCRecordValue {
	private ZCField field = null;
	private String value = null;
	private List<String> values = null;
	private File fileValue = null;

	
	public ZCRecordValue(ZCField field, String value) {
		this.field = field;
		if(value == null) {
			value = "";
		}
		
		setValue(value); 
	}

	
	public ZCRecordValue(ZCField field, List<String> values) {
		this.field = field;
		
		if(values == null) {
			values = new ArrayList<String>();
		}
		setValues(values);
	}


	public ZCRecordValue(ZCField field, File fileValue) {
		this.field = field;
		setFileValue(fileValue);
	}


	public String toString() {
		return field.getDisplayName() + " : " + value;  //No I18N
	}

	public String getValue() {
		if(FieldType.isMultiChoiceField(field.getType())) {
			String valuesString = values.toString();
			return valuesString.toString().substring(1, valuesString.length() - 1);
		}
		return value;
	}

	public List<String> getValues() {
		final List<String> toReturn = new ArrayList<String>(values);
		return toReturn;
	}
	
	void addToValues(List<String> valuesToAdd) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		values.addAll(valuesToAdd);
	}
	
	void removeFromValues(List<String> valuesToRemove) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		for(int i=0;i<values.size();i++)
		{
			for(int j=0;j<valuesToRemove.size();j++)
			{
				//System.out.println("Values111111"+values.get(i)+valuesToRemove.get(j));
				if(values.get(i).equals(valuesToRemove.get(j)))
				{
					values.remove(i);
					i--;
					break;
				}
			}
		}
	}

	public File getFileValue() {
		return fileValue;
	}
	
	public void setValue(String value) {
		if(FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.value = value;
	}

	public void setValues(List<String> values) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.values = new ArrayList<String>(values);
	}

	public void setFileValue(File fileValue) {

		if(!FieldType.isPhotoField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.fileValue = fileValue;
	}
	

	public ZCField getField() {
		return field;
	}



}
