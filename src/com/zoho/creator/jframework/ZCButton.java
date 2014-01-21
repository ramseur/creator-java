// $Id$
package com.zoho.creator.jframework;

import java.io.File;
import java.util.ArrayList;
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
	private boolean onClickExists = false;



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

	void setOnClickExists(boolean onClickExists)
	{
		this.onClickExists = onClickExists;
	}

	public boolean isOnClickExists()
	{
		return onClickExists;
	}

	public ZCResponse click() throws ZCException{
		ZCResponse response =  null;
		if(!buttonType.equals(ZCButtonType.RESET)) {
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
				} 
				ZCField baseLookupField = zcForm.getBaseLookupField();
				if(baseLookupField != null) {
					ZCForm baseForm = baseLookupField.getBaseForm();
					params.add(new BasicNameValuePair("childAppLinkName" , baseForm.getAppLinkName()));//No I18N
					if(baseForm.getComponentLinkName()==null)
					{
						params.add(new BasicNameValuePair("childFormLinkName" , baseForm.getBaseSubFormField().getBaseForm().getComponentLinkName()));//No I18N
					}
					else
					{
						params.add(new BasicNameValuePair("childFormLinkName" , baseForm.getComponentLinkName()));//No I18N
					}
					params.add(new BasicNameValuePair("childFieldLabelName" , baseLookupField.getFieldName()));//No I18N
				}
				params.addAll(ZOHOCreator.getAdditionalParamsForForm(zcForm, baseLookupField));
				response =  ZOHOCreator.postXMLString(zcForm.getAppOwner(), xmlString, action, params);		
			} else {
				URLPair urlPair = ZCURL.buttonOnClick(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), linkName, zcForm.getAppOwner(), zcForm.getFieldParamValues(null,-1));
				response = ZOHOCreator.parseResponseDocumentForJSONString(urlPair, zcForm);
			}
			if(response.isError()) {
				return response;
			}
			List<ZCField> fields = zcForm.getFields();
			for(int i=0; i<fields.size(); i++) {
				ZCField field = fields.get(i);
				if(FieldType.isPhotoField(field.getType())) {
					ZCRecordValue recValue = field.getRecordValue();
					File fileToUpload = recValue.getFileValue();

					int imageType = field.getImageType();
					if(field.isFileReUploaded() && imageType != 1 )
					{
						constructImageUrl(field,response,fileToUpload,action);	
					}
				}
				if(field.getType()==FieldType.SUB_FORM)
				{
					List<ZCRecord> zcRecords = field.getAddedSubFormEntries();
					for(int j=0;j<zcRecords.size();j++)
					{
						ZCRecord zcRecord =zcRecords.get(j);
						List<ZCRecordValue> zcRecordValues = zcRecord.getValues();
						for(int k=0;k<zcRecordValues.size();k++)
						{
							ZCRecordValue recordValue = zcRecordValues.get(k);
							if (FieldType.isPhotoField(recordValue.getField().getType()))
							{
								constructImageUrl(field,response,recordValue.getFileValue(),action);
							}
						}
					}	
				}
			}
		}
		return response;
	}

	private void constructImageUrl(ZCField field,ZCResponse response,File fileToUpload,String action) throws ZCException
	{

		URLPair urlPair = ZCURL.fileUploadURL(zcForm.getAppOwner());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.addAll(urlPair.getNvPair());
		params.add(new BasicNameValuePair("applinkname", zcForm.getAppLinkName()));//No I18N
		params.add(new BasicNameValuePair("formname", zcForm.getComponentLinkName()));//No I18N
		params.add(new BasicNameValuePair("fieldname", field.getFieldName()));//No I18N
		ZCForm form = field.getBaseForm();
		int formType = form.getFormType();
		if(!(formType==ZCForm.FORM_ALONE))
		{
			params.add(new BasicNameValuePair("formAccessType",formType+""));//No I18N
		}
		if(action == "update")
		{
			params.add(new BasicNameValuePair("recordId", ZOHOCreator.getCurrentEditRecord().getRecordId() + ""));//No I18N
			if(ZOHOCreator.getCurrentView()!=null && fileToUpload!=null)
			{
			params.add(new BasicNameValuePair("viewLinkName", ZOHOCreator.getCurrentView().getComponentLinkName()));//No I18N
			params.add(new BasicNameValuePair("operation","update"));
			}	
		}
		else
		{
			params.add(new BasicNameValuePair("recordId", response.getSuccessRecordID() + ""));//No I18N
		}
		if(fileToUpload!=null)
		{
			params.add(new BasicNameValuePair("filename", fileToUpload.getName()));//No I18N
			ZOHOCreator.postFile(urlPair.getUrl(), fileToUpload, params);	
		}
		else
		{
			params.add(new BasicNameValuePair("operation", "delete"));//No I18N
			if(ZOHOCreator.getCurrentView()!=null)
			{
			params.add(new BasicNameValuePair("viewLinkName", ZOHOCreator.getCurrentView().getComponentLinkName()));//No I18N
			}
			ZOHOCreator.postFile(urlPair.getUrl(), null, params);
		}

	}

}
