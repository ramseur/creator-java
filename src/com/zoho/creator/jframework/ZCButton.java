// $Id$
package com.zoho.creator.jframework;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class ZCButton implements Comparable<ZCButton>{

	private String name = null;
	private String linkName = null;
	private int sequenceNumber = -1;
	private ZCButtonType buttonType = null;
	private ZCForm zcForm = null;
	private boolean formIsSet = false;



	public ZCButton(String name, String linkName, ZCButtonType buttonType) {
		this.name = name;
		this.linkName = linkName;
		this.buttonType = buttonType;
	}

	public String toString() {
		return "Name: " + name + " - Link Name"  + linkName + " - Button Type " + buttonType;//No I18N
	}

	public String getName() {
		return name;
	}

	public String getLinkName() {
		return linkName;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public ZCButtonType getButtonType() {
		return buttonType;
	}

	public int compareTo(ZCButton arg0) {
		ZCButton toCompare = (ZCButton)arg0;
		return getSequenceNumber()>toCompare.getSequenceNumber() ? 1 
				: toCompare.getSequenceNumber() > getSequenceNumber() ? -1 
						: 0;
	}

	void setForm(ZCForm zcForm) {
		if(!formIsSet) {
			this.zcForm = zcForm;
			formIsSet = true;
		} else {
			throw new RuntimeException ("You cannot set more than once"); //No I18N
		}
	}

	public ZCResponse click() throws ZCException {
		ZCResponse response =  null;
		////System.out.println("inside click");
		if(!buttonType.equals(ZCButtonType.RESET)) {
			////System.out.println("inside click");
			String action = "add"; //No I18N
			String xmlString = zcForm.getXMLStringForSubmit();
			if(!zcForm .isStateLess()) 
			{
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("zcRefValue", true+""));
				params.add(new BasicNameValuePair("formAccessType", String.valueOf(zcForm.getFormType())));//No I18N
				params.add(new BasicNameValuePair("errorLog" , "true"));//No I18N

				if(zcForm.getViewForBulkEdit() != null || zcForm.getViewForEdit() != null) {
					action = "update"; //No I18N
					if(zcForm.getViewForBulkEdit() != null) {
						params.add(new BasicNameValuePair("viewLinkName" , zcForm.getViewForBulkEdit().getComponentLinkName()));//No I18N
					} else if (zcForm.getViewForEdit() != null) {
						params.add(new BasicNameValuePair("viewLinkName" , zcForm.getViewForEdit().getComponentLinkName()));//No I18N
					}
				}
				ZCField baseLookupField = zcForm.getBaseLookupField();

				if(baseLookupField != null) {
					ZCForm baseForm = baseLookupField.getBaseForm();
					params.add(new BasicNameValuePair("childAppLinkName" , baseForm.getAppLinkName()));//No I18N
					params.add(new BasicNameValuePair("childFormLinkName" , baseForm.getComponentLinkName()));//No I18N

					params.add(new BasicNameValuePair("childFieldLabelName" , baseLookupField.getFieldName()));//No I18N
				}


				List<List<String>> fieldList = new ArrayList<List<String>>(); 
				while(baseLookupField != null) 
				{
					List<String> fieldRowList = new ArrayList<String>();
					ZCForm baseForm = baseLookupField.getBaseForm();
					fieldRowList.add(baseForm.getAppLinkName());
					fieldRowList.add(baseForm.getComponentLinkName());
					fieldRowList.add(baseLookupField.getFieldName());
					fieldList.add(fieldRowList);
					baseLookupField = baseForm.getBaseLookupField();
				}
				if(fieldList.size()>0)
				{
					params.add(new BasicNameValuePair("zc_lookupCount", fieldList.size() + ""));//No I18N
					int fieldListSize = fieldList.size();
					for(int i=0; i<fieldListSize; i++) {
						List<String> fieldRowList = fieldList.get(i);
						params.add(new BasicNameValuePair("zc_childappname_" + (fieldListSize -i) , fieldRowList.get(0) + ""));//No I18N
						params.add(new BasicNameValuePair("zc_childformname_" + (fieldListSize -i) , fieldRowList.get(1) + ""));//No I18N
						params.add(new BasicNameValuePair("zc_childlabelname_" + (fieldListSize -i) , fieldRowList.get(2) + ""));//No I18N
					}
				}
				
				response =  ZOHOCreator.postXMLString(zcForm.getAppOwner(), xmlString, action, params, zcForm);
				//System.out.println("inside responseee"+response.getErrorMessagesTable().size());
				
			} else {
				//URLPair urlPair = ZCURL.buttonOnClick(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), linkName, zcForm.getAppOwner(), xmlString);
				URLPair urlPair = ZCURL.buttonOnClick(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), linkName, zcForm.getAppOwner(), zcForm.getFieldParamValues(null));
				response = ZOHOCreator.parseResponseDocumentForXMLString(ZOHOCreator.postURLXML(urlPair.getUrl(), urlPair.getNvPair()), action, zcForm);
				//System.out.println("inside cli");
			}
			////System.out.println("inside cli");
			Hashtable<ZCField, String> errorMessagesTable = response.getErrorMessagesTable();
			if(response.isError()) {
				////System.out.println("inside responsesss");
				return response;
			}

			List<ZCField> fields = zcForm.getFields();
			for(int i=0; i<fields.size(); i++) {
				ZCField field = fields.get(i);
				if(FieldType.isPhotoField(field.getType())) {
					////System.out.println("lkhfff");
					ZCRecordValue recValue = field.getRecordValue();
					File fileToUpload = recValue.getFileValue();
					if(fileToUpload!=null)
					{
					////System.out.println("lkhjjjhjhk"+ Integer.parseInt(String.valueOf(fileToUpload.length()/1024)));
					URLPair urlPair = ZCURL.fileUploadURL(zcForm.getAppOwner());
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.addAll(urlPair.getNvPair());
					params.add(new BasicNameValuePair("applinkname", zcForm.getAppLinkName()));//No I18N
					params.add(new BasicNameValuePair("formname", zcForm.getComponentLinkName()));//No I18N
					params.add(new BasicNameValuePair("fieldname", field.getFieldName()));//No I18N
					params.add(new BasicNameValuePair("recordId", response.getSuccessRecordID() + ""));//No I18N
					params.add(new BasicNameValuePair("filename", fileToUpload.getName()));//No I18N
					ZOHOCreator.postFile(urlPair.getUrl(), fileToUpload, params);
					}
				}
			}
		}
		return response;
	}



}
