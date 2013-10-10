// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.List;

public class ZCForm extends ZCComponent {

	private boolean hasAddOnLoad = false;
	private boolean hasEditOnLoad = false;
	private String successMessage = "";
	private String dateFormat = "";
	private List<ZCField> fields = new ArrayList<ZCField>();
	private List<ZCButton> buttons = new ArrayList<ZCButton>();
	private boolean isStateLess = false;
	private boolean fieldsAdded = false;
	private boolean buttonsAdded = false;
	private ZCField baseSubFormField = null;
	private ZCField baseLookupField = null;
	private ZCView viewForAdd = null;
	private ZCView viewForEdit = null;
	private ZCView viewForBulkEdit = null;
	private List<ZCRecord> editRecords = new ArrayList<ZCRecord>();
	private List<ZCField> bulkEditFields = new ArrayList<ZCField>();
	private List<String> alertMessages = null; 
	private boolean reLoadForm = false;

	private int formType = ZCForm.FORM_ALONE;

	public static final int FORM_ALONE =1;
	public static final int VIEW_ADD_FORM =2;
	public static final int VIEW_EDIT_FORM =3;
	public static final int VIEW_BULK_EDIT_FORM =4;
	public static final int FORM_LOOKUP_ADD_FORM =5;



	ZCForm(String appOwner, String appLinkName, String componentName, String componentLinkName, int sequenceNumber, boolean hasAddOnLoad, boolean hasEditOnLoad, String successMessage, String dateFormat, boolean isStateLess) {
		super( appOwner,  appLinkName,  ZCComponent.FORM,  componentName,  componentLinkName,  sequenceNumber);

		this.hasAddOnLoad = hasAddOnLoad;
		this.hasEditOnLoad = hasEditOnLoad;
		this.successMessage = successMessage;
		this.dateFormat = dateFormat;
		this.isStateLess = isStateLess;

	}

	public String toString() {
		return super.toString() + " - hasAddOnLoad:" + hasAddOnLoad + " - hasEditOnLoad:" + hasEditOnLoad + " - successMessage:" + successMessage + //No I18N
				" - " + fields + " - Buttons : " + buttons ; //No I18N
	}

	boolean hasOnLoad() { // unifying all the checks here itself. Otherwise client will unnecessarily check all these..
		if(formType == FORM_ALONE || formType == VIEW_ADD_FORM || formType == FORM_LOOKUP_ADD_FORM) {
			return hasAddOnLoad;
		} else if(formType == VIEW_EDIT_FORM) {
			return hasEditOnLoad;
		}
		return false;
	}


	public String getSuccessMessage() {
		return successMessage;
	}
	public String getDateFormat()
	{
		return dateFormat;
	}

	public List<ZCField> getFields() {
		final List<ZCField> toReturn = new ArrayList<ZCField>(fields);
		return toReturn ;
	}

	ZCField getField(String fieldLinkName) {
		for(int i=0; i<fields.size(); i++) {//No I18N
			ZCField field = fields.get(i);
			if(field.getFieldName().equals(fieldLinkName)) {//No I18N
				return field;
			}
		}
		return null;
	}

	public List<ZCButton> getButtons() {
		final List<ZCButton> toReturn = new ArrayList<ZCButton>(buttons);
		return toReturn ;
	}


	void addButtons(List<ZCButton> buttons) {
		if(!buttonsAdded) {
			this.buttons.addAll(buttons);
			buttonsAdded = true;
			for(int i=0; i<buttons.size(); i++) {
				buttons.get(i).setForm(this);
			}
		} else {
			throw new RuntimeException ("You cannot add more than once"); //No I18N
		}
	}


	void addFields(List<ZCField> fields) {
		if(!fieldsAdded) {
			this.fields.addAll(fields);
			fieldsAdded = true;
			for(int i=0; i<fields.size(); i++) {
				fields.get(i).setBaseForm(this);
			}
		} else {
			throw new RuntimeException ("You cannot add more than once"); //No I18N
		}
	}


	String getXMLStringForSubmit() {
		StringBuffer buff = new StringBuffer();
		StringBuffer criteriaBuff = new StringBuffer();

		String listTag = "formlist";//No I18N
		String compTag = "form";//No I18N
		String compLinkNameValue = getComponentLinkName(); 
		String actionTag = "add";//No I18N
		String newValuesOpenTag = "";//No I18N
		String newValuesCloseTag = "";//No I18N

		if(viewForAdd != null || viewForEdit != null || viewForBulkEdit != null) {
			listTag = "viewlist"; //No I18N
			compTag = "view"; //No I18N
		}

		if(viewForEdit != null || viewForBulkEdit != null) {
			actionTag = "update"; //No I18N

			newValuesOpenTag = "<newvalues>"; //No I18N
			newValuesCloseTag = "</newvalues>"; //No I18N

			criteriaBuff.append("<criteria>");//No I18N
			criteriaBuff.append("<![CDATA[");//No I18N
			criteriaBuff.append("(");
			for(int i=0; i<editRecords.size(); i++) {
				ZCRecord editRecord = editRecords.get(i);
				criteriaBuff.append("ID == \"" + editRecord.getRecordId() + "\"");//No I18N
				if(i != editRecords.size()-1) {
					criteriaBuff.append(" || ");//No I18N
				}
			}
			criteriaBuff.append(")");//No I18N
			criteriaBuff.append("]]>");//No I18N
			criteriaBuff.append("</criteria>");//No I18N
		}


		if(viewForAdd != null) {
			compLinkNameValue = viewForAdd.getComponentLinkName(); 
		} else if(viewForEdit != null) {
			compLinkNameValue = viewForEdit.getComponentLinkName();
		} else if(viewForBulkEdit != null) {
			compLinkNameValue = viewForBulkEdit.getComponentLinkName();
		}


		buff.append("<ZohoCreator>");//No I18N
		buff.append("<applicationlist>");//No I18N
		buff.append("<application name='" + getAppLinkName() + "'>");//No I18N
		buff.append("<" + listTag + ">");//No I18N
		buff.append("<" + compTag + " name='" + compLinkNameValue + "'>");//No I18N
		buff.append("<" + actionTag + ">");//No I18N
		buff.append(criteriaBuff);
		buff.append(newValuesOpenTag);
		buff.append(getXMLStringForFields());
		buff.append(newValuesCloseTag);
		buff.append("</" + actionTag + ">");//No I18N
		buff.append("</" + compTag + ">");//No I18N
		buff.append("</" + listTag + ">");//No I18N
		buff.append("</application>");//No I18N
		buff.append("</applicationlist>");//No I18N
		buff.append("</ZohoCreator>");//No I18N
		return buff.toString();
	}

	private String getXMLStringForFields() {
		StringBuffer buff = new StringBuffer();
		List<ZCField> fieldsToIterate = fields;
		if(viewForBulkEdit != null) {
			fieldsToIterate = bulkEditFields;
		}
		for(int i=0; i<fieldsToIterate.size(); i++) {//No I18N
			ZCField field = fieldsToIterate.get(i);
			//ZCRecordValue recordValue = field.getEnteredValue();
			ZCRecordValue recordValue = field.getRecordValue();

			if(recordValue != null) 
			{
				if(FieldType.isMultiChoiceField(field.getType())) 
				{
					List<String> values = recordValue.getValues();
					//System.out.println("values"+values.size()+values.get(0));

					if(values.size()!=0)
					{
						if(!values.get(0).equals(""))
						{
							buff.append("<field name='" + field.getFieldName() + "'>");//No I18N
							buff.append("<options>");//No I18N
							for(int j=0; j<values.size(); j++) {
								buff.append("<option>");//No I18N
								buff.append("<![CDATA[");//No I18N
								buff.append(values.get(j));
								buff.append("]]>");//No I18N
								buff.append("</option>");//No I18N
							}
							buff.append("</options>");//No I18N
							buff.append("</field>");//No I18N
						}

					}

				} else if(field.getType().equals(FieldType.SUB_FORM)) {
					buff.append("<field name='" + field.getFieldName() + "'>");//No I18N
					buff.append(getXMLStringForSubFormEntries(field.getAddedSubFormEntries(), "add"));//No I18N
					buff.append(getXMLStringForSubFormEntries(field.getUpdatedSubFormEntries(), "update"));//No I18N
					buff.append(getXMLStringForSubFormEntries(field.getRemovedSubFormEntries(), "delete"));//No I18N
					buff.append("</field>");//No I18N
				} else if( !FieldType.isPhotoField(field.getType()) && !field.getType().equals(FieldType.FORMULA) && !field.getType().equals(FieldType.NOTES)) {
						if(!(recordValue.getValue().equals("")))
						{
							buff.append("<field name='" + field.getFieldName() + "'>");//No I18N
							buff.append("<value>");//No I18N
							buff.append("<![CDATA[");//No I18N
							buff.append(recordValue.getValue());
							buff.append("]]>");//No I18N
							buff.append("</value>");//No I18N
							buff.append("</field>");//No I18N
						}
		
						
					
				}
			}
		}
		//System.out.println("response stringgg"+buff.toString());
		return buff.toString();
	}

	private String getXMLStringForSubFormEntries(List<ZCRecord> subFormEntries, String actionTag) {
		StringBuffer buff = new StringBuffer();
		for(int j=0; j<subFormEntries.size(); j++) {
			ZCRecord subFormRecord = subFormEntries.get(j);
			buff.append("<");//No I18N
			buff.append(actionTag);
			if(actionTag.equals("update") || actionTag.equals("delete")) {//No I18N
				buff.append(" ID=\"" + subFormRecord.getRecordId() + "\"");//No I18N
			}
			buff.append(">");//No I18N
			if(!actionTag.equals("delete")) {//No I18N
				List<ZCRecordValue> subFormRecordValues = subFormRecord.getValues();
				for(int k=0; k<subFormRecordValues.size(); k++) {
					ZCRecordValue subFormRecordValue = subFormRecordValues.get(k);
					ZCField subFormField = subFormRecordValue.getField();
					buff.append("<field name='" + subFormField.getFieldName() + "'>");//No I18N
					if(FieldType.isMultiChoiceField(subFormField.getType())) {
						List<String> subFormValues = subFormRecordValue.getValues();
						buff.append("<options>");//No I18N
						for(int l=0; l<subFormValues.size(); l++) {
							buff.append("<option>");//No I18N
							buff.append("<![CDATA[");//No I18N
							buff.append(subFormValues.get(l));
							buff.append("]]>");//No I18N
							buff.append("</option>");//No I18N
						}
						buff.append("</options>");//No I18N
					} else if(!FieldType.isPhotoField(subFormField.getType())) {
						buff.append("<value>");//No I18N
						buff.append("<![CDATA[");//No I18N
						buff.append(subFormRecordValue.getValue());
						buff.append("]]>");//No I18N	
						buff.append("</value>");//No I18N
					}
					buff.append("</field>");//No I18N
				}
			}
			buff.append("</");//No I18N
			buff.append(actionTag);
			buff.append(">");//No I18N
		}
		return buff.toString();
	}


	String getXMLStringForDeluge() {
		StringBuffer buff = new StringBuffer();
		buff.append("<fields>");//No I18N
		buff.append(getXMLStringForFields());
		buff.append("</fields>");//No I18N
		//System.out.println("form"+buff.toString());
		return buff.toString();
	}


	public void onAddRowForSubForm(ZCField field) throws ZCException{
		ZOHOCreator.callSubFormAddRow(this, field.getFieldName());
	}

	public void onDeleteRowForSubForm(ZCField field) throws ZCException{
		ZOHOCreator.callSubFormDeleteRow(this, field.getFieldName());
	}

	public void onUserInputForSubFormField(ZCField subFormField, ZCField onUserInputField) throws ZCException{
		ZOHOCreator.callSubFormFieldOnUser(this, subFormField.getFieldName() , onUserInputField.getFieldName());
	}

	public ZCField getBaseSubFormField() {
		return baseSubFormField;
	}

	void setBaseSubFormField(ZCField baseSubFormField) {
		this.baseSubFormField = baseSubFormField;
	}

	public ZCView getViewForAdd() {
		return viewForAdd;
	}

	void setViewForAdd(ZCView viewForAdd) {
		this.viewForAdd = viewForAdd;
	}

	public ZCView getViewForEdit() {
		return viewForEdit;
	}

	void setViewForEdit(ZCView viewForEdit) {
		this.viewForEdit = viewForEdit;
	}

	public ZCView getViewForBulkEdit() {
		return viewForBulkEdit;
	}

	void setViewForBulkEdit(ZCView viewForBulkEdit) {
		this.viewForBulkEdit = viewForBulkEdit;
	}

	public List<ZCField> getBulkEditFields() {
		final List<ZCField> toReturn = new ArrayList<ZCField>(bulkEditFields);
		return toReturn ;
	}

	public void addBulkEditField(ZCField field) {
		if(fields.contains(field) && !bulkEditFields.contains(field)) {
			bulkEditFields.add(field);
		}
	}

	public void removeBulkEditField(ZCField field) {
		bulkEditFields.remove(field);
	}

	void addEditRecord(ZCRecord record) {
		editRecords.add(record);
	}

	public int getFormType() {
		return formType;
	}

	void setFormType(int formType) {
		this.formType = formType;
	}

	void setBaseLookupField(ZCField baseLookupField ) {
		this.baseLookupField = baseLookupField ;
	}

	public ZCField getBaseLookupField() {
		return baseLookupField ;
	}

	public List<String> getAlertMessages() {
		return alertMessages;
	}

	public void setAlertMessages(List<String> alertMessages) {
		this.alertMessages = alertMessages;
	}

	public boolean isReLoadForm() {
		return reLoadForm;
	}

	public void setReLoadForm(boolean reLoadForm) {
		this.reLoadForm = reLoadForm;
	}

	public boolean isStateLess() {
		return isStateLess;
	}

}
