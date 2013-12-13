// $Id$

package com.zoho.creator.jframework;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ZOHOCreator {

	private static ZCApplication zcApp = null;
	private static List<ZCSection> sectionList = null;
	private static ZCComponent comp = null;
	private static ZCForm form = null;
	private static ZCView view = null;
	private static ZCHtmlView htmlView = null;
	private static ZCRecord editRecord = null;
	private static List<ZCRecord> bulkEditRecords = null;
	private static ZCField baseLookUpField = null;

	private static ZCAppList appList = null;
	private static ZCNavList navigationListForApps = null;
	private static ZCForm subform = null;
	private static String accountsURL = "accounts.zoho.com";//No I18N
	private static String serviceName = "ZohoCreator";//No I18N
	private static String creatorURL = "creator.zoho.com";//No I18N
	private static String prefix = "https";//No I18N
	private static Properties props = new Properties();
	private static String accessTokenForExternalField;


	public static String getLoginURL() {
		return ZCURL.getURLString(ZCURL.getLoginUrl());
	}

	public static String getPersonalPhotoURL() {
		return ZCURL.getURLString(ZCURL.getURLForPersonalPhoto());
	}

	public static String getFileUploadURL(String filepath, String appOwner, String appLinkName, String viewLinkName) {
		return ZCURL.getURLString(ZCURL.getFileUploadURL(filepath, appOwner, appLinkName, viewLinkName));
	}

	public static String getImageURL(String filePath) {
		return ZCURL.getURLString(ZCURL.getImageURL(filePath));
	}

	public static String getUserProperty(String key) {
		return props.getProperty(key);
	}

	public static void setBaseLookUpField(ZCField baseLookUpField)
	{
		ZOHOCreator.baseLookUpField = baseLookUpField;
	}

	public static ZCField getBaseLookUPField()
	{
		return baseLookUpField;
	}

	public static void setUserProperty(String key, String value) {
		props.setProperty(key, value);
	}

	//	public static void setSubFormRecordValueParams(List<ZCRecordValue> subFormRecordValueParams)
	//	{
	//		ZOHOCreator.subFormRecordValueParams = subFormRecordValueParams;
	//	}
	//	public static List<ZCRecordValue> getSubFormRecordValueParams()
	//	{
	//		return subFormRecordValueParams;
	//	}
	public static String getPrefix() {
		return prefix;
	}

	public static void setPrefix(String prefix) {
		ZOHOCreator.prefix = prefix;
	}

	public static String getAccountsURL() {
		return accountsURL;
	}

	public static void setAccountsURL(String accountsURL) {
		ZOHOCreator.accountsURL = accountsURL;
	}


	public static String getServiceName() {
		return serviceName;
	}

	public static void setServiceName(String serviceName) {
		ZOHOCreator.serviceName = serviceName;
	}

	public static String getCreatorURL() {
		return creatorURL;
	}

	public static void setCreatorURL(String creatorURL) {
		ZOHOCreator.creatorURL = creatorURL;
	}

	public static ZCAppList getCurrentAppList() {
		return appList;
	}

	public static void setCurrentNavigationListForApps(ZCNavList zcNavList) {
		ZOHOCreator.navigationListForApps = zcNavList;
		setCurrentAppList(null);
	}

	public static ZCNavList  getCurrentNavigationListForApps() {
		return navigationListForApps;
	}

	public static ZCApplication getCurrentApplication() {
		return zcApp;
	}

	public static ZCComponent getCurrentComponent() {
		return comp;
	}

	public static ZCForm getCurrentForm() {
		return form;
	}

	public static ZCView getCurrentView() {
		return view;
	}

	public static ZCHtmlView getCurrentHtmlView() {
		return htmlView;
	}


	public static ZCForm getCurrentSubForm() {
		return subform;
	}

	public static ZCRecord getCurrentEditRecord() {
		return editRecord;
	}

	public static List<ZCRecord> getCurrentBulkEditRecords() {
		return bulkEditRecords;
	}


	public static void setCurrentAppList(ZCAppList appList) {
		ZOHOCreator.appList = appList;
		setCurrentApplication(null);
	}

	public static void setCurrentApplication(ZCApplication zcApp) {
		ZOHOCreator.zcApp = zcApp;
		setCurrentComponent(null);
	}

	public static void setCurrentApplication(String appOwner, String appDisplayName, String appLinkName) {
		setCurrentApplication(new ZCApplication(appOwner, appDisplayName, appLinkName, false, null));		
	}

	public static void setCurrentComponent(ZCComponent comp) {
		ZOHOCreator.comp = comp;
		setCurrentForm(null);
		setCurrentView(null);
		setCurrentHtmlView(null);
	}

	public static ZCComponent getComponent(String appOwner, String appLinkName, String type, String componentLinkName,String componentName,String queryString) {
		ZCComponent toReturn = new ZCComponent (appOwner, appLinkName, type, componentName, componentLinkName, -1);
		toReturn.setQueryString(queryString);
		return toReturn;

	}

	public static void loadSelectedApplication(List<NameValuePair> additionalParams)  throws ZCException {
		setCurrentSectionList(getSectionList(getCurrentApplication(), additionalParams));
	}

	public static void setCurrentSectionList(List<ZCSection> sectionList) {
		ZOHOCreator.sectionList = sectionList;
	}

	public static List<ZCSection> getCurrentSectionList() {
		return sectionList;
	}

	public static void loadSelectedHtmlView() throws ZCException {
		setCurrentHtmlView(getHtmlView(getCurrentComponent()));
	}

	public static void loadSelectedView() throws ZCException{
		setCurrentView(getView(getCurrentComponent()));
	}	

	public static void loadSelectedForm() throws ZCException {
		ZCComponent zcComp = getCurrentComponent();
		ZCForm zcForm = getForm(zcComp);
		setCurrentForm(zcForm);
		if (zcForm.hasOnLoad()) {
			ZOHOCreator.callFormOnAddOnLoad(zcForm);
		}	
	}

	public static void loadFormForAddRecord(Date calSelectedStartDate , Date calSelectedEndDate) throws ZCException {
		loadFormForView(null, ZCForm.VIEW_ADD_FORM, calSelectedStartDate , calSelectedEndDate);
		getCurrentForm().setViewForAdd(getCurrentView());
		if (getCurrentForm().hasOnLoad()) {
			ZOHOCreator.callFormOnAddOnLoad(getCurrentForm());

		}


	}

	public static void loadFormForEditRecord() throws ZCException {
		ZCRecord record = getCurrentEditRecord();
		loadFormForView(record.getRecordId(), ZCForm.VIEW_EDIT_FORM, null,null);
		ZCForm currentForm = getCurrentForm();
		currentForm.setViewForEdit(getCurrentView());
		currentForm.addEditRecord(record);
		if (currentForm.hasOnLoad()) {
			ZOHOCreator.callFormEditOnAddOnLoad(currentForm, record.getRecordId());
		}

	}

	public static void loadFormForBulkEditRecords() throws ZCException {
		List<ZCRecord> records = getCurrentBulkEditRecords();
		loadFormForView(null, ZCForm.VIEW_BULK_EDIT_FORM, null,null);
		ZCForm currentForm = getCurrentForm();
		currentForm.setViewForBulkEdit(getCurrentView());
		for(int i=0; i<records.size(); i++) {
			currentForm.addEditRecord(records.get(i));
		}
	}

	public static void loadFormForAddToLookup(ZCField lookupField,List<ZCRecordValue> recordValues,int recordPosition) throws ZCException {
		ZCComponent refComponent = lookupField.getRefFormComponent();
		ZCForm zcForm = lookupField.getBaseForm();
		List<NameValuePair> params = getAdditionalParamsForForm(zcForm, lookupField);
		params.addAll(ZOHOCreator.getCurrentForm().getFieldParamValues(recordValues,recordPosition));
		ZCForm lookupForm =  getForm(refComponent.getAppLinkName(), refComponent.getComponentLinkName(), refComponent.getAppOwner(), null,null, ZCForm.FORM_LOOKUP_ADD_FORM, zcForm.getAppLinkName(), zcForm.getComponentLinkName(), lookupField.getFieldName(), null, null,params,null);
		lookupField.setLookupForm(lookupForm);
		if (lookupForm.hasOnLoad()) {
			ZOHOCreator.callFormOnAddOnLoad(lookupForm);
		}
	}

	private static void loadFormForView(Long recordLinkId, int formType, Date calSelectedStartDate ,Date calSelectedEndDate) throws ZCException {
		ZCView currentView = getCurrentView();
		String formLinkName = currentView.getBaseFormLinkName();
		ZCForm form = getForm(currentView.getAppLinkName(), formLinkName, currentView.getAppOwner(), currentView.getComponentLinkName(), recordLinkId, formType, null, null, null, calSelectedStartDate,calSelectedEndDate,null,null);
		setCurrentForm(form);
	}

	static List<NameValuePair> getAdditionalParamsForForm(ZCForm zcForm, ZCField baseLookupField)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		ZCForm baseForm = null;
		if(baseLookupField == null)
		{
			baseForm = zcForm;
		}
		else 
		{
			List<List<String>> fieldList = new ArrayList<List<String>>(); 
			while(baseLookupField != null) 
			{
				List<String> fieldRowList = new ArrayList<String>();
				baseForm = baseLookupField.getBaseForm();
				fieldRowList.add(baseForm.getAppLinkName());
				if(baseForm.getComponentLinkName()==null)
				{
					fieldRowList.add(baseForm.getBaseSubFormField().getBaseForm().getComponentLinkName());
				}
				else
				{
					fieldRowList.add(baseForm.getComponentLinkName());
				}

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
		}


		ZCView viewForAdd = baseForm.getViewForAdd();
		ZCView viewForEdit = baseForm.getViewForEdit(); 
		if(viewForAdd != null) {
			params.add(new BasicNameValuePair("viewLinkName" , viewForAdd.getComponentLinkName()));//No I18N
		}
		else if(viewForEdit != null) {
			params.add(new BasicNameValuePair("viewLinkName" , viewForEdit.getComponentLinkName()));
		}else if(zcForm.getViewForBulkEdit() != null) {
			params.add(new BasicNameValuePair("viewLinkName" , zcForm.getViewForBulkEdit().getComponentLinkName()));//No I18N
		} 
		return params;
	}


	public static void setCurrentForm(ZCForm form) {
		ZOHOCreator.form = form;
		setCurrentSubForm(null);
	}


	private static void setCurrentView(ZCView view) {
		ZOHOCreator.view = view;
		setCurrentEditRecord(null);
		setCurrentBulkEditRecords(null);
	}

	private static void setCurrentHtmlView(ZCHtmlView htmlView) {
		ZOHOCreator.htmlView = htmlView;
	}

	public static void setCurrentSubForm(ZCForm subform) {
		ZOHOCreator.subform=subform;
	}

	public static void setCurrentEditRecord(ZCRecord editRecord) {
		ZOHOCreator.editRecord = editRecord;
	}

	public static void setCurrentBulkEditRecords(List<ZCRecord> bulkEditRecords) {
		ZOHOCreator.bulkEditRecords = bulkEditRecords;
	}

	public static ZOHOUser login(String authToken) throws ZCException {
		return ZOHOUser.getUserObject(authToken);
	}

	public static ZOHOUser login(String uname, String password) throws ZCException {
		return ZOHOUser.getUserObject(uname, password);
	}

	public static ZOHOUser getZohoUser() {
		return ZOHOUser.getUserObject();
	}

	public static void logout() {
		ZOHOUser user = ZOHOCreator.getZohoUser();
		if(user != null) {
			user.logout();
		}
	}

	public static ZCNavList getNavigationListForApps() throws ZCException {
		URLPair navigationListURL = ZCURL.navigationListURL();
		Document rootDocument = ZOHOCreator.postURLXML(navigationListURL.getUrl(), navigationListURL.getNvPair());
		return XMLParser.parseForNavigationListForApps(rootDocument);
	}



	public static ZCAppList getPersonalApplicationList(List<NameValuePair> additionalParams) throws ZCException {
		URLPair appListURLPair = ZCURL.appListURL();
		if(additionalParams == null) {
			additionalParams = new ArrayList<NameValuePair>();
		}
		additionalParams.addAll(appListURLPair.getNvPair());
		Document rootDocument = ZOHOCreator.postURLXML(appListURLPair.getUrl(), additionalParams);
		return new ZCAppList(ZCAppList.PERSONAL_APPS, XMLParser.parseForApplicationList(rootDocument));
	}

	public static ZCAppList getSharedApplicationList() throws ZCException {
		return getSharedApplicationList(null);
	}

	public static ZCAppList getSharedApplicationList(ZCSharedGroup sharedGroup) throws ZCException {
		URLPair appListURLPair = ZCURL.sharedAppListURL(null); 
		if(sharedGroup != null) {
			appListURLPair = ZCURL.sharedAppListURL(sharedGroup.getGroupId()); 
		}
		Document rootDocument = ZOHOCreator.postURLXML(appListURLPair.getUrl(), appListURLPair.getNvPair());
		List<ZCApplication> apps = XMLParser.parseForApplicationList(rootDocument);
		ZCAppList toReturn = new ZCAppList(ZCAppList.SHARED_APPS, apps);
		toReturn.setSharedGroup(sharedGroup);
		return toReturn;
	}

	public static ZCAppList getWorkspaceApplicationList(String workspaceAppOwner, List<NameValuePair> additionalParams) throws ZCException {
		//checkForLicense(workspaceAppOwner);
		URLPair appListURLPair = ZCURL.workSpaceAppListURL(workspaceAppOwner); 
		if(additionalParams == null) {
			additionalParams = new ArrayList<NameValuePair>();
		}
		additionalParams.addAll(appListURLPair.getNvPair());
		Document rootDocument = ZOHOCreator.postURLXML(appListURLPair.getUrl(), additionalParams);
		ZCAppList toReturn = new ZCAppList(ZCAppList.WORKSPACE_APPS, XMLParser.parseForApplicationList(rootDocument));
		toReturn.setWorkspaceAppOwner(workspaceAppOwner);
		return toReturn;
	}


	public static List<ZCSection> getSectionList(String appLinkName, String appOwner, List<NameValuePair> additionalParams) throws ZCException {
		URLPair sectionListURLPair = ZCURL.sectionMetaURL(appLinkName, appOwner);
		if(additionalParams == null) {
			additionalParams = new ArrayList<NameValuePair>();
		}
		additionalParams.addAll(sectionListURLPair.getNvPair());
		Document rootDocument = ZOHOCreator.postURLXML(sectionListURLPair.getUrl(), additionalParams);
		return XMLParser.parseForSectionList(rootDocument, appLinkName, appOwner);
	}

	public static List<ZCSection> getSectionList(ZCApplication zcApp, List<NameValuePair> additionalParams)  throws ZCException {
		return getSectionList(zcApp.getAppLinkName(), zcApp.getAppOwner(), additionalParams);
	}

	public static ZCForm getForm(ZCComponent comp) throws ZCException{
		if(comp.getType().equals(ZCComponent.FORM)) {
			return getForm(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner(), null, null, ZCForm.FORM_ALONE, null, null, null, null, null,null,comp.getQueryString());
		}
		throw new ZCException("An error has occured.", ZCException.GENERAL_ERROR, "Trying to fetch form details. But not a form. " + comp); //No I18N
	}


	public static ZCForm getFeedBackForm(String preFilledLogMessage) {
		ZCForm toReturn  = new ZCForm("zoho1", "support", "Feedback", "Feedback", 1, false, false, "Thank you for your feedback.", "dd-MMM-yyyy", false,"","");//No I18N

		ZCField platformField = new ZCField("Platform", FieldType.DROPDOWN, "Platform");//No I18N
		List<ZCChoice> choices = new ArrayList<ZCChoice>();
		//choices.add(new ZCChoice("iOS", "iOS"));//No I18N
		ZCChoice choice = new ZCChoice("Android", "Android");
		choices.add(choice);//No I18N
		platformField.setRecordValue(new ZCRecordValue(platformField, choice));//No I18N
		platformField.setHidden(true);
		platformField.setRebuildRequired(true);
		platformField.addChoices(choices);

		ZCField titleField = new ZCField("Title", FieldType.SINGLE_LINE, "Title");//No I18N
		titleField.setRecordValue(new ZCRecordValue(titleField, ""));

		ZCField messageField = new ZCField("Message", FieldType.MULTI_LINE, "Message");//No I18N
		messageField.setRecordValue(new ZCRecordValue(messageField, preFilledLogMessage));

		List<ZCField> fields = new ArrayList<ZCField>();
		fields.add(platformField);	
		fields.add(titleField);//No I18N
		fields.add(messageField);//No I18N

		List<ZCButton> buttons = new ArrayList<ZCButton>();
		buttons.add(new ZCButton("Submit", "submit", ZCButtonType.SUBMIT));//No I18N
		buttons.add(new ZCButton("Reset", "reset", ZCButtonType.RESET));//No I18N
		toReturn.addFields(fields);
		toReturn.addButtons(buttons);
		return toReturn;
	}



	public static ZCForm getForm(String appLinkName, String formLinkName, String appOwner) throws ZCException{
		return getForm(appLinkName, formLinkName, appOwner, null, null, ZCForm.FORM_ALONE, null, null, null, null, null,null,null);
	}

	private static ZCForm getForm(String appLinkName, String formLinkName, String appOwner, String viewLinkName, Long recordLinkId, int formType, String refAppLinkName, String refFormLinkName, String refFieldName, Date calSelectedStartDate,Date calSelectedEndDate,List<NameValuePair> params,String queryString) throws ZCException {
		URLPair formMetaURLPair = ZCURL.formMetaURL(appLinkName, formLinkName, appOwner, viewLinkName, recordLinkId, formType, refAppLinkName, refFormLinkName, refFieldName, calSelectedStartDate,calSelectedEndDate,params);
		System.out.println("formmetaurl"+getURLString(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair()));
		Document rootDocument = ZOHOCreator.postURLXML(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		ZCForm toReturn = XMLParser.parseForForm(rootDocument, appLinkName, appOwner,queryString);
		if(toReturn == null) {
			throw new ZCException("An error has occured.", ZCException.GENERAL_ERROR, "Unable to get " + getURLStringForException(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair())); //No I18N
		}
		toReturn.setFormType(formType);

		if(formType == ZCForm.VIEW_BULK_EDIT_FORM) {
			List<ZCButton> zcButtons = new ArrayList<ZCButton>();
			zcButtons.add(new ZCButton("Submit","Submit",ZCButtonType.SUBMIT));//No I18N
			//toReturn.addButtons(zcButtons);
			//		} else if (toReturn.hasOnLoad()) {
			//			if(formType == ZCForm.FORM_ALONE || formType == ZCForm.VIEW_ADD_FORM || formType == ZCForm.FORM_LOOKUP_ADD_FORM) {
			//				ZOHOCreator.callFormOnAddOnLoad(toReturn);
			//			} else if(formType == ZCForm.VIEW_EDIT_FORM) {
			//				ZOHOCreator.callFormEditOnAddOnLoad(toReturn,recordLinkId);
			//			}
		}
		return toReturn;
	}

	private static void callFormOnAddOnLoad(ZCForm zcForm) throws ZCException{
		List<NameValuePair> params = zcForm.getFieldParamValues(null,-1);
		params.addAll(getAdditionalParamsForForm(zcForm, null));
		URLPair formOnAddOnLoadURL = ZCURL.formOnLoad(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), zcForm.getAppOwner(), params );
		String response = ZOHOCreator.postURL(formOnAddOnLoadURL.getUrl(), formOnAddOnLoadURL.getNvPair());
		JSONParser.parseAndCallFormEvents(response, zcForm,null,null);
	}

	static void callFormEditOnAddOnLoad(ZCForm zcForm,Long recordLinkId) throws ZCException{
		List<NameValuePair> params = zcForm.getFieldParamValues(null,-1);
		params.addAll(getAdditionalParamsForForm(zcForm, null));
		URLPair formEditOnAddOnLoadURL = ZCURL.formEditOnLoad(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), zcForm.getAppOwner(), params,recordLinkId);
		String response = ZOHOCreator.postURL(formEditOnAddOnLoadURL.getUrl(), formEditOnAddOnLoadURL.getNvPair());
		JSONParser.parseAndCallFormEvents(response, zcForm,null,null);
	}

	private static void callDelugeEvents(ZCForm zcForm, URLPair urlPair,List<ZCRecordValue> recordValues,ZCForm currentShownForm) throws ZCException{
		System.out.println("deluge url"+getURLString(urlPair.getUrl(), urlPair.getNvPair()));
		String response = ZOHOCreator.postURL(urlPair.getUrl(), urlPair.getNvPair());
		System.out.println("Response"+response);
		JSONParser.parseAndCallFormEvents(response,zcForm,recordValues,currentShownForm);
	}
	public static void JSONPARSER(String response)
	{
		
		try {
			JSONArray jArray = new JSONArray(response);
			for (int i=0; i < jArray.length(); i++) {
				JSONObject jsonObj = jArray.getJSONObject(i);
				Iterator<?> keys = jsonObj.keys();
				while( keys.hasNext() ){
					String key = (String)keys.next(); 
					if( key.contains("generatedjs")){
						String value = jsonObj.getString(key);
						if(value.contains("openWindowTask("))
						{
							String s = value.substring((value.indexOf("openWindowTask(")+"openWindowTask(".length()));
							String a[]=s.split(",");
							String url = a[0].substring(1,a[0].length()-1);
							String type = a[1].substring(1,a[1].length()-1);
						}

					}
				}	  
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


	}


	static ZCResponse parseResponseDocumentForJSONString(URLPair urlPair, ZCForm zcForm) throws ZCException {
		String response = ZOHOCreator.postURL(urlPair.getUrl(), urlPair.getNvPair());
		return JSONParser.parseAndCallFormEvents(response, zcForm, null,null);
	}


	static void callFieldOnUser(ZCForm zcForm, String fieldLinkName, boolean isFormula,List<ZCRecordValue> subformTempRecordValues) throws ZCException {
		callDelugeEvents(zcForm, ZCURL.fieldOnUser(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), fieldLinkName, zcForm.getAppOwner(), zcForm.getFieldParamValues(subformTempRecordValues,-1), isFormula,getAdditionalParamsForForm(zcForm, null)),null,null);
	}

	static void callSubFormFieldOnUser(ZCForm zcForm, String subFormFieldLinkName, ZCForm currentShownForm,List<ZCRecordValue> tempRecordValues,boolean isFormula,int entryPosition) throws ZCException{
		callDelugeEvents(zcForm, ZCURL.subFormOnUser(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, currentShownForm.getBaseSubFormField().getFieldName() , zcForm.getAppOwner(), zcForm.getFieldParamValues(tempRecordValues,entryPosition),isFormula,getAdditionalParamsForForm(zcForm, null),entryPosition),tempRecordValues,currentShownForm);
	}


	public static void callSubFormAddRow(ZCForm zcForm, String subFormFieldLinkName,List<ZCRecordValue> tempRecordValues,ZCForm currentShownForm) throws ZCException{
		callDelugeEvents(zcForm, ZCURL.subFormAddRow(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, zcForm.getAppOwner(), zcForm.getFieldParamValues(null,-1),getAdditionalParamsForForm(zcForm, null)),tempRecordValues,currentShownForm);
	}

	public static void callSubFormDeleteRow(ZCForm zcForm, String subFormFieldLinkName,List<ZCRecordValue> tempRecordValues) throws ZCException{
		callDelugeEvents(zcForm, ZCURL.subFormDeleteRow(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, zcForm.getAppOwner(), zcForm.getFieldParamValues(null,-1),getAdditionalParamsForForm(zcForm, null)),tempRecordValues,null);
	}

	public static ZCView getListReport(String appLinkName, String viewLinkName, String appOwner) throws ZCException{
		return getView(new ZCComponent(appOwner, appLinkName, ZCView.REPORT, viewLinkName, viewLinkName, -1));
	}

	public static ZCView getCalendarReport(String appLinkName, String viewLinkName, String appOwner) throws ZCException{
		return getView(new ZCComponent(appOwner, appLinkName, ZCView.CALENDAR, viewLinkName, viewLinkName, -1));
	}

	public static ZCView getView(ZCComponent comp) throws ZCException{
		if(comp.getType().equals(ZCComponent.REPORT) || comp.getType().equals(ZCComponent.CALENDAR)) {
			List<NameValuePair> params = new ArrayList<NameValuePair>(); //URLConstructor.getDefaultParams(comp.getAppOwner());
			if(comp.getType().equals(ZCComponent.REPORT)) {
				params.add(new BasicNameValuePair("startIndex", "1"));//No I18N
				params.add(new BasicNameValuePair("pageSize", ZCView.PAGE_SIZE+"")); //No I18N
			} else if(comp.getType().equals(ZCComponent.CALENDAR)) {
				Calendar startCalendar = Calendar.getInstance();   // this takes current date
				startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				startCalendar.set(Calendar.HOUR_OF_DAY, 0);
				startCalendar.set(Calendar.MINUTE, 0);
				startCalendar.set(Calendar.SECOND, 0);
				startCalendar.set(Calendar.MILLISECOND, 0);
				SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd");//No I18N

				Calendar endCalendar = Calendar.getInstance();   // this takes current date
				endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				endCalendar.set(Calendar.HOUR_OF_DAY, 23);
				endCalendar.set(Calendar.MINUTE, 59);
				endCalendar.set(Calendar.SECOND, 59);
				endCalendar.set(Calendar.MILLISECOND, 999);
				SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd");//No I18N

				params.add(new BasicNameValuePair("startDateStr", startDateFormat.format(startCalendar.getTime())));//No I18N
				params.add(new BasicNameValuePair("endDateStr", endDateFormat.format(endCalendar.getTime()))); //No I18N
			}
			
			Document rootDocument = getViewXMLDocument(comp, params);
			Calendar cal = Calendar.getInstance();
			ZCView toReturn =  XMLParser.parseForView(rootDocument, comp.getAppLinkName(), comp.getAppOwner(), comp.getType(), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
			if(toReturn == null) {
				throw new ZCException("An error has occured.", ZCException.GENERAL_ERROR, "Unable to get " + getURLStringForException((ZCURL.viewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner())).getUrl(), params)); //No I18N
			}
			return toReturn;
		}
		throw new ZCException("An error has occured.", ZCException.GENERAL_ERROR, "Trying to fetch view details. But not a view. " + comp); //No I18N
	}


	private static ZCHtmlView getHtmlView(ZCComponent comp) throws ZCException {
		if(comp.getType().equals(ZCComponent.PAGE)) {
			URLPair htmlViewURLPair = ZCURL.htmlViewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner());
			//params.addAll(viewURLPair.getNvPair());
			String htmlString = ZOHOCreator.postURL(htmlViewURLPair.getUrl(), htmlViewURLPair.getNvPair());
			return new ZCHtmlView( comp.getAppOwner(), comp.getAppLinkName(), comp.getType(), comp.getComponentName(), comp.getComponentLinkName(), htmlString);
			//return ZOHOCreator.postURLXML(viewURLPair.getUrl(), params);
		}		
		throw new ZCException("An error has occured.", ZCException.GENERAL_ERROR, "Trying to fetch view details. But not a view"); //No I18N
	}
	
	

	static List<ZCChoice> loadMoreChoices(ZCField field, ZCField subFormField) throws ZCException {
		ZCForm baseForm = field.getBaseForm();
		if(baseForm==ZOHOCreator.getCurrentSubForm())
		{
			baseForm = ZOHOCreator.getCurrentForm();
		}
		String subformComponent = null;
		int formAccessType = 0;
		if(subFormField != null) {
			subformComponent = subFormField.getFieldName();
		}
		if(getCurrentForm().getViewForAdd()!=null)
		{
			formAccessType = ZCForm.VIEW_ADD_FORM;
		}
		else if(getCurrentForm().getViewForEdit()!=null)
		{
			formAccessType = ZCForm.VIEW_EDIT_FORM;
		}
		else if(getCurrentForm().getViewForBulkEdit()!=null)
		{
			formAccessType = ZCForm.VIEW_BULK_EDIT_FORM;
		}
		else
		{
			formAccessType = ZCForm.FORM_ALONE;
		}
		//List<NameValuePair> params = getAdditionalParamsForForm(baseForm,field);
		//params.addAll(getCurrentForm().getFieldParamValues(recordValues));
		URLPair lookupChoicesUrl = ZCURL.lookupChoices(baseForm.getAppLinkName(), baseForm.getComponentLinkName(), baseForm.getAppOwner(), field.getFieldName(), field.getChoices().size(), field.getSearchForChoices(), subformComponent,formAccessType,getAdditionalParamsForForm(baseForm,field),field);
		Document rootDocument = ZOHOCreator.postURLXML(lookupChoicesUrl.getUrl(), lookupChoicesUrl.getNvPair());
		return XMLParser.getLookUpChoices(rootDocument);

	}


	static void loadRecords(ZCView zcView) throws ZCException{  // Search, Filter & Group By

		List<NameValuePair> params = new ArrayList<NameValuePair>(); //URLConstructor.getDefaultParams(zcView.getAppOwner());
		if(zcView.getType().equals(ZCComponent.CALENDAR)) {
			Calendar startCalendar = Calendar.getInstance();   // this takes current date

			startCalendar.set(Calendar.YEAR, zcView.getRecordsMonthYear().getTwo());
			startCalendar.set(Calendar.MONTH, zcView.getRecordsMonthYear().getOne());

			startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			startCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startCalendar.set(Calendar.MINUTE, 0);     
			startCalendar.set(Calendar.SECOND, 0);
			startCalendar.set(Calendar.MILLISECOND, 0);
			SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd");//No I18N

			Calendar endCalendar = Calendar.getInstance();   // this takes current date

			endCalendar.set(Calendar.YEAR, zcView.getRecordsMonthYear().getTwo());
			endCalendar.set(Calendar.MONTH, zcView.getRecordsMonthYear().getOne());

			endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			endCalendar.set(Calendar.HOUR_OF_DAY, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);
			endCalendar.set(Calendar.MILLISECOND, 999);
			SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd");//No I18N

			params.add(new BasicNameValuePair("startDateStr", startDateFormat.format(startCalendar.getTime())));//No I18N
			params.add(new BasicNameValuePair("endDateStr", endDateFormat.format(endCalendar.getTime()))); //No I18N

		} else {
			params.add(new BasicNameValuePair("startIndex", zcView.getRecords().size()+1 + ""));//No I18N
			params.add(new BasicNameValuePair("pageSize", ZCView.PAGE_SIZE+"")); //No I18N
		}
		params.add(new BasicNameValuePair("viewmeta", "false")); //No I18N
		List<ZCColumn> columns = zcView.getColumns();


		StringBuffer filterBuff = new StringBuffer();

		ZCCustomFilter selectedCustomFilter = zcView.getSelectedCustomFilter();
		if(selectedCustomFilter != null) {
			filterBuff.append("CustomFilter:");//No I18N
			filterBuff.append(selectedCustomFilter.getId());
			filterBuff.append(";");
		}

		// FILTERS
		List<ZCFilter> filters = zcView.getFilters();
		if(filters.size() >0) {
			HashMap<String, List<String>> filterMap = new HashMap<String, List<String>>();
			for(int i=0; i<filters.size(); i++) {
				ZCFilter filter = filters.get(i);
				List<ZCFilterValue> filterValues = filter.getValues();
				for(int j=0; j<filterValues.size(); j++) {
					ZCFilterValue filterValue = filterValues.get(j);
					if(filterValue.isSelected()) {
						String filterLinkName = filter.getFilterLinkName();
						List<String> valueList = filterMap.get(filterLinkName);
						if(valueList == null) {
							valueList = new ArrayList<String>();
							filterMap.put(filterLinkName, valueList);
						}
						valueList.add(filterValue.getValue());
					}
				}
			}

			for (Map.Entry<String, List<String>> entry : filterMap.entrySet()) {
				String filterLinkName = entry.getKey();
				List<String> valueList = entry.getValue();
				filterBuff.append(filterLinkName);
				filterBuff.append(":");
				for(int i=0; i<valueList.size(); i++) {
					String value = valueList.get(i);
					filterBuff.append(value);
					if(i != valueList.size()-1) {
						filterBuff.append("@zohocomma@"); //No I18N
					}
				}
				filterBuff.append(";");
			}
		}

		if(filterBuff.length() > 0) {
			params.add(new BasicNameValuePair("filterVal", filterBuff.toString())); //No I18N
		}

		//GROUP BY
		List<ZCColumn> groupByColumns = zcView.getGroupByColumns();
		if(groupByColumns.size()>0) {
			StringBuffer buff = new StringBuffer();
			for(int i=0; i<groupByColumns.size(); i++) {
				ZCColumn zcColumn = groupByColumns.get(i);
				buff.append(zcColumn.getFieldName());
				buff.append(":");
				buff.append(zcColumn.isSortOrderForGroupByAscending());
				buff.append(";");
			}
			params.add(new BasicNameValuePair("groupBy", buff.toString()));//No I18N
		}

		//SORT BY
		List<ZCColumn> sortByColumns = zcView.getSortByColumns();
		if(sortByColumns.size()>0) {
			StringBuffer buff = new StringBuffer();
			for(int i=0; i<sortByColumns.size(); i++) {
				ZCColumn zcColumn = sortByColumns.get(i);
				buff.append(zcColumn.getFieldName());
				buff.append(":");
				buff.append(zcColumn.isSortOrderForSortByAscending());
				buff.append(";");
			}
			params.add(new BasicNameValuePair("sortBy", buff.toString()));//No I18N
		}


		// SEARCH and HIDDEN/SHOWN COLUMNS
		boolean onceForSearch = false;
		boolean onceForHidden = false;
		StringBuffer hiddenColumnsBuff = new StringBuffer();
		StringBuffer shownColumnsBuff = new StringBuffer();

		for(int i=0; i<columns.size(); i++) {
			ZCColumn column = columns.get(i);
			String fieldName = column.getFieldName();

			ZCCondition condition = column.getCondition();
			if(condition != null) {
				if(!onceForSearch) {
					onceForSearch = true;
					params.add(new BasicNameValuePair("searchCrit", "true"));//No I18N
				}
				params.add(new BasicNameValuePair(fieldName, condition.getValue()));//No I18N
				params.add(new BasicNameValuePair(fieldName + "_op", condition.getOperator() + ""));//No I18N
			}

			if(column.isHidden()) {
				onceForHidden = true;
				hiddenColumnsBuff.append(fieldName);
				hiddenColumnsBuff.append(";");
			} else {
				shownColumnsBuff.append(fieldName);
				shownColumnsBuff.append(";");
			}
		}

		if(onceForHidden) {
			params.add(new BasicNameValuePair("hideColumns", hiddenColumnsBuff.toString()));//No I18N
			params.add(new BasicNameValuePair("showColumns", shownColumnsBuff.toString()));//No I18N
		}


		Document rootDocument = getViewXMLDocument(zcView, params);
		XMLParser.parseAndAddRecords(rootDocument, zcView);
	}

	private static Document getViewXMLDocument(ZCComponent comp, List<NameValuePair> params) throws ZCException{
		URLPair viewURLPair = ZCURL.viewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner());
		params.addAll(viewURLPair.getNvPair());
		System.out.println("viewUrl"+getURLString(viewURLPair.getUrl(), params));
		Document toReturn = ZOHOCreator.postURLXML(viewURLPair.getUrl(), params);		
		return toReturn;
	}
	
	public static void getAuthTokenForExternalField(String accessToken) throws ZCException{
		URLPair externalFieldTokenURLPair = ZCURL.externalFieldTokenURL(accessToken);
		String rootDocument = ZOHOCreator.postURL(externalFieldTokenURLPair.getUrl(), externalFieldTokenURLPair.getNvPair());
		ZOHOCreator.setAccessTokenForExternalField(JSONParser.parseForTokenForExternalField(rootDocument));
	}

	private static void setAccessTokenForExternalField(String accessTokenForExternalField) {
		// TODO Auto-generated method stub
		ZOHOCreator.accessTokenForExternalField = accessTokenForExternalField;
	}
	
	public static String getAccessTokenForExternalField(){
		return accessTokenForExternalField;
	}
	static ZCResponse postCustomAction(String appLinkName, String viewLinkName, String appOwner, long customActionId, List<Long> recordIDs) throws ZCException{
		URLPair customActionURLPair = ZCURL.customActionURL(appLinkName, viewLinkName, customActionId, appOwner, recordIDs);
		Document rootDocument = ZOHOCreator.postURLXML(customActionURLPair.getUrl(), customActionURLPair.getNvPair());
		NodeList nl = rootDocument.getChildNodes();
		ZCResponse toReturn = new ZCResponse();
		for(int i=0; i<nl.getLength(); i++) {
			Node resultNode = nl.item(i);
			NodeList customActionNodes = resultNode.getChildNodes();
			for(int j =0;j<customActionNodes.getLength();j++) {
				Node customActionNode = customActionNodes.item(j);
				if(customActionNode.getNodeName().equals("SuccessMessage")) {
					toReturn.setSuccessMessage(customActionNode.getFirstChild().getNodeValue());
				}				
			}
		}
		return toReturn;
	}



	static ZCResponse postXMLString(String appOwner, String xmlString, String action, List<NameValuePair> params, ZCForm form) throws ZCException{
		URLPair xmlWriteURLPair = ZCURL.xmlWriteURL(appOwner, xmlString);
		if(params == null) {
			params = new ArrayList<NameValuePair>();
		}
		params.addAll(xmlWriteURLPair.getNvPair());
		System.out.println("xmlstringforsubmit"+getURLString(xmlWriteURLPair.getUrl(), params));
		Document rootDocument = ZOHOCreator.postURLXML(xmlWriteURLPair.getUrl(), params);

		XPath xPath = XPathFactory.newInstance().newXPath();
		//read a string value
		ZCResponse toReturn = new ZCResponse();
		try {
			String status = xPath.compile("/response/result/form/" + action + "/status").evaluate(rootDocument);//No I18N
			if(status.startsWith("Failure")) {
				toReturn.setError(true);
				String[] failureMessages = status.split(",");
				List<ZCField> fields = form.getFields();
				if(status.contains("*")) {
					toReturn.setMainErrorMessage("Invalid entries found. Please correct and submit again.");//No I18N
					for(int i=1;i<failureMessages.length;i++)
					{
						String fieldName=null;
						String errorMessage=null;
						fieldName = failureMessages[i].substring(1,failureMessages[i].indexOf("*"));
						errorMessage = failureMessages[i].substring(failureMessages[i].indexOf("*")+1);
						for(int j=0;j<fields.size();j++)
						{
							if(fieldName.equals(fields.get(j).getFieldName())) 
							{
								toReturn.addErrorMessage(fields.get(j), errorMessage);
								break;
							}
						}
					}
				} else {
					if(failureMessages.length > 1) {
						toReturn.setMainErrorMessage(status.substring(failureMessages[0].length() + 1));
					} else {
						toReturn.setMainErrorMessage(status);
					}
				}
			} else  if(status.startsWith("Success")) { //No I18N
				if(form != null) {
					xPath = XPathFactory.newInstance().newXPath();
					String idValue = xPath.compile("/response/result/form/add/values/field[@name=\"ID\"]").evaluate(rootDocument);//No I18N
					if(idValue.length()!=0) {
						toReturn.setSuccessRecordID(Long.parseLong(idValue));
					}
					String lookUpValue = xPath.compile("/response/result/form/" + action + "/combinedlookupvalue").evaluate(rootDocument);//No I18N
					toReturn.setSuccessLookUpChoiceValue(new ZCChoice(idValue, lookUpValue));
				}
			} else {
				//String errorCodeStr = xPath.compile("/response/errorlist/error/code/").evaluate(rootDocument);//No I18N
				String errorCodeMessage = xPath.compile("/response/errorlist/error/message").evaluate(rootDocument);//No I18N
				if(errorCodeMessage != null && !errorCodeMessage.equals("")) {

					toReturn.setError(true);
					toReturn.setMainErrorMessage(errorCodeMessage);
				}
			}
		} catch (XPathExpressionException e) {
			throw new ZCException("An error has occured.", ZCException.GENERAL_ERROR, getTrace(e));//No I18N
		}


		return toReturn;
	}


	static String postURL(final String url, final List<NameValuePair> params) throws ZCException {
		try
		{
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			if(params != null) {
				request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));//No I18N
			}
			request.setURI(new URI(url));
			ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
				public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						return EntityUtils.toByteArray(entity);
					} else {
						throw new RuntimeException("Unable to convert response for " + getURLStringForException(url, params));//No I18N
					}
				}
			};
			byte[] response = client.execute(request, handler);
			return new String(response);
		} catch(UnknownHostException uhe) {
			throw new ZCException("No network connection.", ZCException.NETWORK_ERROR);//No I18N
		} catch(HttpHostConnectException uhe) {
			throw new ZCException("No network connection.", ZCException.NETWORK_ERROR);//No I18N
		} catch (ClientProtocolException e) {
			throw new ZCException("Unable to connect with Zoho Creator", ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
		} catch (IOException e) {
			throw new ZCException("Network Error.",ZCException.NETWORK_ERROR);//No I18N
		} catch (URISyntaxException e) {
			throw new ZCException("An error has occured", ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
		} catch (RuntimeException ee) {
			throw new ZCException("An error has occured", ZCException.GENERAL_ERROR, getTraceWithURL(ee, url, params));//No I18N
		}
	}

	// convert InputStream to String

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	private static String getURLStringForException(String url, List<NameValuePair> params) {
		List<NameValuePair> newParams = new ArrayList<NameValuePair>();
		for(int i=0; i<params.size(); i++) {
			NameValuePair nvPair = params.get(i);
			String name = nvPair.getName();
			if(!name.equals("authtoken")) {
				newParams.add(nvPair);
			}
		}
		return getURLString(url, newParams);
	}

	private static String getURLString(String url, List<NameValuePair> params) {
		StringBuffer buff = new StringBuffer(url);
		if(!url.endsWith("/")) {
			buff.append("?");
		}
		if(params != null) {
			for(int i=0; i<params.size(); i++) {
				NameValuePair nvPair = params.get(i);
				buff.append(nvPair.getName());
				buff.append("=");
				buff.append(nvPair.getValue());
				if(i != params.size()-1) {
					buff.append("&");
				}
			}
		}

		return buff.toString();
	}

	static Document postURLXML(String url, List<NameValuePair> params) throws ZCException {
		System.out.println("urleeeee"+getURLString(url, params));
		try
		{
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			if(params != null) {
				request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));//No I18N
			}
			try {
				request.setURI(new URI(url));
			} catch (URISyntaxException e) {
				throw new ZCException("An error has occured", ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
			}
			HttpEntity entity = null;
			try {
				entity = client.execute(request).getEntity();
			} catch (ClientProtocolException e) {
				throw new ZCException("Unable to connect with Zoho Creator", ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
			} 
			if(entity != null) {
				InputStream is = null;
				try {
					is = entity.getContent();
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document toReturn = builder.parse(is);
					return toReturn;

				} catch (ParserConfigurationException e) {
					throw new ZCException("An error has occured", ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
				} catch (SAXException e) {
					throw new ZCException("An error has occured", ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
				}  
				finally {
					if(is != null) {
						is.close();
					}
				}
			}

		} catch(UnknownHostException uhe) {
			throw new ZCException("No network connection.", ZCException.NETWORK_ERROR);//No I18N
		} catch(HttpHostConnectException uhe) {
			throw new ZCException("No network connection.", ZCException.NETWORK_ERROR);//No I18N
		} catch (IOException e) {
			throw new ZCException("Network Error.", ZCException.NETWORK_ERROR);//No I18N
		}
		throw new ZCException("An error has occured.", ZCException.GENERAL_ERROR, getURLStringForException(url, params)); //No I18N
	}

	private static String getTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();		
	}

	private static String getTraceWithURL(Exception ex, String url, List<NameValuePair> params) {
		return getURLStringForException(url, params) + "\n\n" + getTrace(ex); //No I18N
	}

	static void postFile(String urlParam, File fileToUpload, List<NameValuePair> paramsList) throws ZCException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams httpParameters = httpclient.getParams();
		httpParameters.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);


		StringBuffer buff = new StringBuffer(urlParam);
		if(!urlParam.endsWith("/")) {
			buff.append("/");
		}
		if(paramsList != null) {
			for(int i=0; i<paramsList.size(); i++) {
				NameValuePair nvPair = paramsList.get(i);
				buff.append(nvPair.getName());
				buff.append("=");
				buff.append(nvPair.getValue());
				if(i != paramsList.size()-1) {
					buff.append("&");
				}
			}
		}

		String url = buff.toString();


		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("enctype", "multipart/form-data"); //No I18N
		ContentBody cbFile = new FileBody(fileToUpload); 
		MultipartEntity mpEntity = new MultipartEntity();
		try {
			mpEntity.addPart("filename", new StringBody(fileToUpload.getName()));//No I18N
		} catch (UnsupportedEncodingException e) {
			throw new ZCException("Unable to upload file.", ZCException.GENERAL_ERROR, getTraceWithURL(e, urlParam, paramsList));//No I18N
		} //No I18N
		mpEntity.addPart("file", cbFile); //No I18N
		httppost.setEntity(mpEntity);

		try {
			HttpResponse httpResponse = httpclient.execute(httppost);

		} catch(UnknownHostException uhe) {
			throw new ZCException("No network connection.", ZCException.NETWORK_ERROR);//No I18N
		} catch(HttpHostConnectException uhe) {
			throw new ZCException("No network connection.", ZCException.NETWORK_ERROR);//No I18N
		} catch (IOException e) {
			throw new ZCException("Network Error.",ZCException.NETWORK_ERROR);//No I18N
		}
		httpclient.getConnectionManager().shutdown();
	}

	/*
	private static String getString(Node node) {
		try
		{
			DOMSource domSource = new DOMSource(node);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		}
		catch(TransformerException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	 */

	private static String getString(Document doc) {
		try
		{
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		}
		catch(TransformerException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}
