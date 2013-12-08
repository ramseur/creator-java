// $Id$
package com.zoho.creator.jframework;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZCRecordValue {
	private ZCField field = null;
	private String value = null;
	private ZCChoice choiceValue = null;
	private List<ZCChoice> choiceValues = null;
	private File fileValue = null;

	
	public ZCRecordValue(ZCField field, String value) {
		this.field = field;		
		setValue(value); 
	}

	public ZCRecordValue(ZCField field, ZCChoice choiceValue) {
		this.field = field;
		setChoiceValue(choiceValue); 
	}
	
	public ZCRecordValue(ZCField field, List<ZCChoice> choiceValues) {
		this.field = field;
		setChoiceValues(choiceValues);
	}

	public ZCRecordValue(ZCField field, File fileValue) {
		this.field = field;
		setFileValue(fileValue);
	}

	public String toString() {
		return field.getDisplayName() + " : " + value;  //No I18N
	}

	public String getValue() {
		if(FieldType.isChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		return value;
	}
	
	public String getDisplayValue() {
		if(FieldType.isMultiChoiceField(field.getType())) {
			StringBuffer buff = new StringBuffer();
			for(int i=0; i<choiceValues.size(); i++) {
				ZCChoice choice = choiceValues.get(i);
				buff.append(choice.getValue());
				if(i != choiceValues.size() -1) {
					buff.append(", ");
				}
			}
			return buff.toString();
		} else if(FieldType.isSingleChoiceField(field.getType())) {
			if(choiceValue==null)
			{
				return "";
			}
			else
				return choiceValue.getValue();
        }
		System.out.println("recordvalue's Value"+value);
        return value;
	}

	public ZCChoice getChoiceValue() {
		if(!FieldType.isSingleChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		return choiceValue;
	}
	
	public List<ZCChoice> getChoiceValues() {
		final List<ZCChoice> toReturn = new ArrayList<ZCChoice>(choiceValues);
		return toReturn;
	}
	
	void addToValues(List<ZCChoice> valuesToAdd) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		for(int i=0;i<valuesToAdd.size();i++) // 3,2
		{
			ZCChoice valueToAdd = valuesToAdd.get(i);
			if(!choiceValues.contains(valueToAdd))
			{
				choiceValues.add(valueToAdd);
			}
		}
	}
	
	void removeFromValues(List<ZCChoice> valuesToRemove) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		for(int i=0;i<choiceValues.size();i++)
		{
			for(int j=0;j<valuesToRemove.size();j++)
			{
				if(choiceValues.get(i).equals(valuesToRemove.get(j)))
				{
					choiceValues.remove(i);
					i--;
					break;
				}
			}
		}
	}

	public void removeValue(ZCChoice value)
	{
		for(int j=0;j<choiceValues.size();j++)
		{
			if(choiceValues.get(j).equals(value))
			{
				choiceValues.remove(j);
			}
		}
	}
	public File getFileValue() {
		return fileValue;
	}
	
	public void setValue(String value) {
		if(FieldType.isChoiceField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.value = value;
	}

	public void setChoiceValue(ZCChoice choiceValue) {
		if(!FieldType.isSingleChoiceField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.choiceValue = choiceValue;
	}

	public void setChoiceValues(List<ZCChoice> choiceValues) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.choiceValues = new ArrayList<ZCChoice>(choiceValues);
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
