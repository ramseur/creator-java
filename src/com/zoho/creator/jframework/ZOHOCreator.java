package com.zoho.creator.jframework;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

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
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class ZOHOCreator {

	private static final String NAV_LIST_FILE = "/navList.xml";//No I18N
	private static final String EDIT_SUPPORT_FILE = "/editSupportURL.txt";//No I18N
	private static final String PERSONAL_APPLIST_FILE = "/personalAppList.json";//No I18N
	private static final String SHARED_APPLIST_FILE = "/sharedAppsList.json";//No I18N

	private static ZCApplication zcApp = null;
	private static List<ZCSection> sectionList = null;
	private static ZCComponent comp = null;
	private static ZCForm form = null;
	private static ZCView view = null;
	private static ZCHtmlView htmlView = null;
	private static List<ZCRecord> bulkEditRecords = null;

	private static ZCAppList appList = null;
	private static ZCNavList navigationListForApps = null;
	private static ZCForm subform = null;
	private static String accountsURL = "accounts.zoho.com";//No I18N
	private static String serviceName = "ZohoCreator";//No I18N1
	private static String authDescription = "ZohoCreator Android";//No I18N
	private static String creatorURL = "creator.zoho.com";//No I18N
	private static String prefix = "https";//No I18N
	private static Properties props = new Properties();
	private static ZCFileHelper fileHelper = null;
	private static String appDisplayName = null;
	private static String appLinkName = null;
	private static String appOwner = null;
	private static String layout = null;
	private static Boolean accessedComponents = false;
	private static boolean isGettingUserDetails = false;


	private static ResourceBundle resourceString = ResourceBundle.getBundle("ResourceString", Locale.getDefault());

	private static boolean cacheResponse = true;
	private static boolean readResponseFromFileForAPI = false;

	private static String portal = null;


	public static String getLoginURL() {
		return ZCURL.getURLString(ZCURL.getLoginUrl());
	}

	public static String getPortalLoginURL() {
		return ZCURL.getURLString(ZCURL.getPortalLoginUrl());
	}

	public static String getCreatorUpgradeURL() {
		return ZCURL.getURLString(ZCURL.getCreatorUpgradeUrl());
	}

	public static String getPortalValue() {
		return portal;
	}

	public static void setPortal(String portal) {

		ZOHOCreator.portal = portal;
	}


	public static String getAppDisplayName()
	{
		return appDisplayName;
	}

	public static void setAppDisplayName(String appDisplayName)
	{
		ZOHOCreator.appDisplayName = appDisplayName;
	}

	public static String getAppLinkName()
	{
		return appLinkName;
	}

	public static void setAppLinkName(String appLinkName)
	{
		ZOHOCreator.appLinkName = appLinkName;
	}
	public static String getAppOwner()
	{
		return appOwner;
	}
	public static void setLayout(String layout)
	{
		ZOHOCreator.layout = layout;
	}
	public static String getLayout()
	{
		return layout;
	}


	public static void setAppOwner(String appOwner)
	{
		ZOHOCreator.appOwner = appOwner;
	}
	public static String getPersonalPhotoURL() throws ZCException{
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

	public static void setAccessedComponents(Boolean state){
		accessedComponents = state;
	}

	public static Boolean getAccessedComponents(){
		return accessedComponents;
	}

	public static void setGettingUserDetails(boolean isGettingUserDetails)
	{
		ZOHOCreator.isGettingUserDetails = isGettingUserDetails;
	}

	public static boolean isGettingUserDetails()
	{
		return isGettingUserDetails;
	}


	static String getFilesDir() {
		String str = ZOHOCreator.getUserProperty("FILES_DIR_PATH");
		str = "/data/data/com.zoho.creator.a/files";
		if(str == null){
			str = "/";
		}
		return str;
	}


	public static void setUserProperty(String key, String value) {
		props.setProperty(key, value);
	}

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

	public static String getAuthDescription()
	{
		return authDescription;
	}

	public static void setAuthDescription(String authDescription)
	{
		ZOHOCreator.authDescription = authDescription;
	}


	public static void setFileHelper(ZCFileHelper fileHelper) {
		ZOHOCreator.fileHelper = fileHelper;
	}

	public static ZCFileHelper getFileHelper() {
		return fileHelper;
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
		ZCComponent component = ZOHOCreator.getCurrentComponent();
		ZCComponent toReturn = new ZCComponent (appOwner, appLinkName, type, componentName, componentLinkName, -1);
		if(queryString == null && component != null){
			toReturn.setQueryString(component.getQueryString());
		}else{
			toReturn.setQueryString(queryString);
		}
		return toReturn;

	}

	public static List<ZCSection> loadSelectedApplication(ZCApplication zcApp,List<NameValuePair> additionalParams)  throws ZCException {
		List<ZCSection> sections = getSectionList(zcApp, additionalParams);
		setCurrentSectionList(sections);
		return sections;
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
		ZCComponent comp = getCurrentComponent();
		List<NameValuePair> params = getQueryStringParams(comp.getQueryString());
		URLPair formMetaURLPair = ZCURL.formMetaURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner(), ZCForm.FORM_ALONE, params);
		//		Document rootDocument = ZOHOCreator.postURLXML(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		//		ZCForm zcForm = XMLParser.parseForForm(rootDocument, appLinkName, appOwner, null, false);
		String response = ZOHOCreator.postURL(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		ZCForm zcForm = JSONParser.parseForForm(response, comp.getAppLinkName(), comp.getAppOwner(), comp.getQueryString(),false);
		if(zcForm == null) {

			throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, resourceString.getString("unable_to_get")+ getURLStringForException(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair())); //No I18N

		}
		zcForm.setFormType(ZCForm.FORM_ALONE);
		setCurrentForm(zcForm);
		if (zcForm.hasOnLoad()) {
			ZOHOCreator.callFormOnAddOnLoad(zcForm,ZCForm.FORM_ALONE);
		}	
	}

	public static ZCForm loadFormForAddRecord(Date calSelectedStartDate , Date calSelectedEndDate) throws ZCException {
		ZCView currentView = getCurrentView();
		String formLinkName = currentView.getBaseFormLinkName();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(calSelectedStartDate != null) {			
			Calendar startDateCal = Calendar.getInstance();
			startDateCal.setTime(calSelectedStartDate);
			Calendar endDateCal = Calendar.getInstance();
			endDateCal.setTime(calSelectedEndDate);
			params.add(new BasicNameValuePair("dateJsonObject", "{\"startDate\":{\"day\":" + startDateCal.get(Calendar.DAY_OF_MONTH) + ",\"month\":" + startDateCal.get(Calendar.MONTH) + ",\"year\":" + startDateCal.get(Calendar.YEAR) + ",\"hours\":"+ startDateCal.get(Calendar.HOUR_OF_DAY)  +",\"minutes\":"+ startDateCal.get(Calendar.MINUTE) +",\"seconds\":" + startDateCal.get(Calendar.SECOND) +"}," +  //No I18N
					"\"endDate\":{\"day\":" +endDateCal.get(Calendar.DAY_OF_MONTH) + ",\"month\":" + endDateCal.get(Calendar.MONTH) + ",\"year\":" + endDateCal.get(Calendar.YEAR) + ",\"hours\":"+ endDateCal.get(Calendar.HOUR_OF_DAY)  +",\"minutes\":"+ endDateCal.get(Calendar.MINUTE) +",\"seconds\":" + endDateCal.get(Calendar.SECOND) + "}};"));//No I18N
		}
		params.add(new BasicNameValuePair("viewLinkName", currentView.getComponentLinkName()));
		params.addAll(getQueryStringParams(currentView.getQueryString()));
		ZCForm form = getForm(currentView.getAppLinkName(), formLinkName, currentView.getAppOwner(), ZCForm.VIEW_ADD_FORM, params);
		setCurrentForm(form);
		form.setViewForAdd(getCurrentView());
		if (form.hasOnLoad()) {
			ZOHOCreator.callFormOnAddOnLoad(form, ZCForm.VIEW_ADD_FORM);
		}
		return form;
	}

	static List<NameValuePair> getQueryStringParams(String queryString)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(queryString!=null)
		{
			String[] stringValues = queryString.split("&");
			for(int p=0;p<stringValues.length;p++)
			{
				String[] fieldNameAndValueString = stringValues[p].split("=");
				if(fieldNameAndValueString.length==2)
				{
					params.add(new BasicNameValuePair(fieldNameAndValueString[0],fieldNameAndValueString[1]));//No I18N
				}
				else if(fieldNameAndValueString.length==1){
					params.add(new BasicNameValuePair(fieldNameAndValueString[0],""));//No I18N
				}
			}
		}
		return params;
	}

	public static void loadFormForAddToLookup(ZCField lookupField) throws ZCException {
		ZCComponent refComponent = lookupField.getRefFormComponent();
		ZCForm zcForm = lookupField.getBaseForm();
		List<NameValuePair> params = getAdditionalParamsForForm(zcForm, lookupField);
		ZCForm lookupForm =  getForm(refComponent.getAppLinkName(), refComponent.getComponentLinkName(), refComponent.getAppOwner(), ZCForm.FORM_LOOKUP_ADD_FORM, params);
		lookupField.setLookupForm(lookupForm);
		if (lookupForm.hasOnLoad()) {
			ZOHOCreator.callFormOnAddOnLoad(lookupForm,ZCForm.FORM_LOOKUP_ADD_FORM);
		}
	}


	private static ZCForm getForm(String appLinkName, String formLinkName, String appOwner, int formType, List<NameValuePair> params) throws ZCException {

		URLPair formMetaURLPair = ZCURL.formMetaURL(appLinkName, formLinkName, appOwner,  formType, params);

		String response = ZOHOCreator.postURL(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		ZCForm toReturn = JSONParser.parseForForm(response, appLinkName, appOwner, null,false);
		//Document rootDocument = ZOHOCreator.postURLXML(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		//ZCForm toReturn = XMLParser.parseForForm(rootDocument, appLinkName, appOwner, null, false);
		if(toReturn == null) {

			throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, resourceString.getString("unable_to_get") + getURLStringForException(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair())); //No I18N

		}
		toReturn.setFormType(formType);
		return toReturn;
	}

	public static ZCForm loadFormForEditRecord(ZCRecord record) throws ZCException {
		ZCView currentView = getCurrentView();
		String appLinkName = currentView.getAppLinkName();
		String appOwner = currentView.getAppOwner();
		String viewLinkName = currentView.getComponentLinkName();
		Long recordLinkId = record.getRecordId();
		URLPair formMetaURLPair = ZCURL.editFormMetaURL(appLinkName, appOwner, viewLinkName, recordLinkId);


		//Document rootDocument = ZOHOCreator.postURLXML(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		//ZCForm editRecordForm = XMLParser.parseForForm(rootDocument, appLinkName, appOwner, queryString, isEditForm)
		String response = ZOHOCreator.postURL(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		ZCForm editRecordForm = JSONParser.parseForForm(response, appLinkName, appOwner,null,true);
		if(editRecordForm == null) {

			throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, resourceString.getString("unable_to_get") + getURLStringForException(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair())); //No I18N
		}
		editRecordForm.setFormType(ZCForm.VIEW_EDIT_FORM);
		setCurrentForm(editRecordForm);		
		editRecordForm.setViewForEdit(getCurrentView());
		editRecordForm.addEditRecord(record);
		if (editRecordForm.hasOnLoad()) {
			ZOHOCreator.callFormEditOnAddOnLoad(editRecordForm, record.getRecordId(),ZCForm.VIEW_EDIT_FORM);
		}
		return editRecordForm;
	}


	public static ZCForm loadFormForBulkEditRecords() throws ZCException {
		List<ZCRecord> records = getCurrentBulkEditRecords();
		ZCView currentView = getCurrentView();
		String appLinkName = currentView.getAppLinkName();
		String appOwner = currentView.getAppOwner();
		String viewLinkName = currentView.getComponentLinkName();
		URLPair formMetaURLPair = ZCURL.bulkEditFormMetaURL(appLinkName, appOwner, viewLinkName);
		//		Document rootDocument = ZOHOCreator.postURLXML(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		String response = ZOHOCreator.postURL(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		ZCForm bulkEditForm = JSONParser.parseForForm(response, appLinkName, appOwner,null,false);
		if(bulkEditForm == null) {

			throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, resourceString.getString("unable_to_get") + getURLStringForException(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair())); //No I18N

		}
		bulkEditForm.setFormType(ZCForm.VIEW_BULK_EDIT_FORM);
		setCurrentForm(bulkEditForm);
		bulkEditForm.setViewForBulkEdit(getCurrentView());
		for(int i=0; i<records.size(); i++) {
			bulkEditForm.addEditRecord(records.get(i));
		}
		return bulkEditForm;
	}



	static List<NameValuePair> getAdditionalParamsForForm(ZCForm zcForm, ZCField baseLookupField) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		ZCForm baseForm = null;
		//		ZCForm baseSubForm = zcForm;
		//		while(baseSubForm.getBaseLookupField()!=null)
		//		{
		//			ZCField tempLookUpField = baseSubForm.getBaseLookupField();
		//			baseSubForm = tempLookUpField.getBaseForm();
		//			if(baseSubForm.getBaseLookupField()==null && baseSubForm.getBaseSubFormField()!=null)
		//			{
		//				params.add(new BasicNameValuePair("zc_childsubformfield_1", tempLookUpField.getFieldName()));
		//				break;  
		//			}
		//		}
		if(baseLookupField == null) {
			baseForm = zcForm;
		} else 
		{
			List<List<String>> fieldList = new ArrayList<List<String>>(); 
			while(baseLookupField != null) {
				List<String> fieldRowList = new ArrayList<String>();
				baseForm = baseLookupField.getBaseForm();
				fieldRowList.add(baseForm.getAppLinkName());
				if(baseForm.getComponentLinkName()==null) {
					fieldRowList.add(baseForm.getBaseSubFormField().getBaseForm().getComponentLinkName());
				} else {
					fieldRowList.add(baseForm.getComponentLinkName());
				}
				fieldRowList.add(baseLookupField.getFieldName());
				fieldList.add(fieldRowList);
				baseLookupField = baseForm.getBaseLookupField();
				//				if(baseLookupField==null)
				//				{
				//					if(baseForm.getBaseSubFormField() != null) {
				//
				//					}
				//				}
			}
			if(fieldList.size()>0) {
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
		if(baseForm.getBaseSubFormField() != null) {
			baseForm = baseForm.getBaseSubFormField().getBaseForm();
		}
		ZCForm form = ZOHOCreator.getCurrentForm();
		if(form!=null)
		{
			params.add(new BasicNameValuePair("childFormAccessType", form.getFormType()+""));//NoI18N
		}
		ZCView viewForAdd = baseForm.getViewForAdd();
		ZCView viewForEdit = baseForm.getViewForEdit(); 

		if(viewForAdd != null) {
			params.add(new BasicNameValuePair("viewLinkName" , viewForAdd.getComponentLinkName()));//No I18N
		} else if(viewForEdit != null) {
			params.add(new BasicNameValuePair("viewLinkName" , viewForEdit.getComponentLinkName()));
		} else if(zcForm.getViewForBulkEdit() != null) {
			params.add(new BasicNameValuePair("viewLinkName" , zcForm.getViewForBulkEdit().getComponentLinkName()));//No I18N
		} 
		return params;
	}

	static ZCResponse submitForm(ZCButton button) throws ZCException {
		ZCResponse response =  null;
		ZCButtonType buttonType = button.getButtonType();
		ZCForm zcForm = button.getForm();
		if(!buttonType.equals(ZCButtonType.RESET)) {
			String action = "add"; //No I18N
			String xmlString = zcForm.getXMLStringForSubmit();
			if(zcForm .isStateLess()) {
				URLPair urlPair = ZCURL.buttonOnClick(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), button.getLinkName(), zcForm.getAppOwner(), zcForm.getFieldParamValues());
				response = parseResponseDocumentForJSONString(urlPair, zcForm);
			} else {
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
					if(baseForm.getComponentLinkName() == null) {
						params.add(new BasicNameValuePair("childFormLinkName" , baseForm.getBaseSubFormField().getBaseForm().getComponentLinkName()));//No I18N
					} else {
						params.add(new BasicNameValuePair("childFormLinkName" , baseForm.getComponentLinkName()));//No I18N
					}
					params.add(new BasicNameValuePair("childFieldLabelName" , baseLookupField.getFieldName()));//No I18N
				}
				params.addAll(getAdditionalParamsForForm(zcForm, baseLookupField));
				response =  postXMLString(zcForm.getAppOwner(), xmlString, action, params,zcForm);		
			}
			if(response.isError()) {
				return response;
			}
			List<ZCField> fields = zcForm.getFields();
			long recordId = response.getSuccessRecordID();
			for(int i=0; i<fields.size(); i++) {
				ZCField field = fields.get(i);
				if(FieldType.isPhotoField(field.getType())||FieldType.SIGNATURE==field.getType()) {
					ZCRecordValue recValue = field.getRecordValue();
					Object bitmap = recValue.getFileValue();
					int imageType = field.getImageType();

					if(recValue.isFileReUploaded() && imageType != ZCField.IMAGE_LINK ) {

						postImage(zcForm, field, recordId, bitmap, action,null,-1l,recValue.getFileName());	
					}
				}
				if(FieldType.SUB_FORM == field.getType())
				{

					ZCForm subForm = field.getSubForm();
					List<ZCRecord> subformRecords = field.getUpdatedSubFormEntries();
					subformRecords.addAll(field.getAddedSubFormEntries());
					List<ZCField> subformFields = subForm.getFields();
					for(int j=0;j<subformFields.size();j++)
					{
						ZCField subformField = subformFields.get(j);
						if(FieldType.isPhotoField(subformField.getType()))
						{
							List<Long> subFormRecordIds = field.getSubFormRecordIds();
							for(int k=0;k<subformRecords.size();k++)
							{
								ZCRecord subformRecord = subformRecords.get(k);	
								List<ZCRecordValue> recordValues = subformRecord.getValues();

								for(int l=0;l<recordValues.size();l++)
								{
									ZCRecordValue subformRecordValue = recordValues.get(l);
									String subFormFieldName = subformField.getFieldName();
									if(subFormFieldName.equals(subformRecordValue.getField().getFieldName()))
									{

										postImage(zcForm, field, recordId, subformRecordValue.getFileValue(), action,subFormFieldName,subFormRecordIds.get(k),subformRecordValue.getFileName());	
									}
								}

							}

						}
					}
					field.setSubFormRecordIds(new ArrayList<Long>());
				}
			}
		}
		return response;		
	}

	private static void postImage(ZCForm zcForm, ZCField field, long recordId, Object bitmap, String action,String subFormFieldName,long subFormRecId,String fileName) throws ZCException	{
		URLPair urlPair = ZCURL.fileUploadURL(zcForm.getAppOwner());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.addAll(urlPair.getNvPair());
		params.add(new BasicNameValuePair("applinkname", zcForm.getAppLinkName()));//No I18N
		params.add(new BasicNameValuePair("formname", zcForm.getComponentLinkName()));//No I18N
		params.add(new BasicNameValuePair("fieldname", field.getFieldName()));//No I18N
		params.add(new BasicNameValuePair("recordId", recordId + ""));//No I18N
		if(subFormFieldName!=null)
		{
			params.add(new BasicNameValuePair("subformFieldName", subFormFieldName));
			params.add(new BasicNameValuePair("subformRecId",subFormRecId +""));
		}
		if(field.getType().equals(FieldType.SIGNATURE))
		{
			params.add(new BasicNameValuePair("isNativeMobileApp", "true"));
		}
		ZCForm form = field.getBaseForm();
		int formType = form.getFormType();
		if(!(formType==ZCForm.FORM_ALONE)&& !(formType==ZCForm.FORM_LOOKUP_ADD_FORM)) {
			params.add(new BasicNameValuePair("formAccessType",formType+""));//No I18N
			params.add(new BasicNameValuePair("viewLinkName", getCurrentView().getComponentLinkName()));//No I18N
		}
		if(action == "update") {
			if(getCurrentView()!=null && bitmap!=null) {
				params.add(new BasicNameValuePair("operation","update"));
			}	
		}

		if(bitmap!=null) {

			if(fileName==null||(fileName!=null&&fileName.equals("")))

			{
				fileName = "image" + System.currentTimeMillis()+".jpg";
			}
			params.add(new BasicNameValuePair("filename", fileName));//No I18N
			if(field.getType().equals(FieldType.FILE_UPLOAD))
			{
				postFile(urlPair.getUrl(), bitmap, fileName, params,true);	
			}else
			{
				postFile(urlPair.getUrl(), bitmap, fileName, params,false);
			}
		} else {
			params.add(new BasicNameValuePair("operation", "delete"));//No I18N
			if(field.getType().equals(FieldType.FILE_UPLOAD))
			{
				postFile(urlPair.getUrl(), null, "", params,true);
			}else
			{
				postFile(urlPair.getUrl(), null, "", params,false);
			}
		}
	}


	static ZCResponse duplicateRecords(ZCView zcView, List<Long> recordIDs) throws ZCException {
		return postXMLString(zcView.getAppOwner(), zcView.getRecordIDXMLString(recordIDs, "duplicate"), "duplicate", null,null);//No I18N		
	}

	static ZCResponse deleteRecords(ZCView zcView, List<Long> recordIDs) throws ZCException {
		return postXMLString(zcView.getAppOwner(), zcView.getRecordIDXMLString(recordIDs, "delete"), "delete", null,null);//No I18N		
	}


	public static void setCurrentForm(ZCForm form) {
		ZOHOCreator.form = form;
		setCurrentSubForm(null);
	}


	public static void setCurrentView(ZCView view) {
		ZOHOCreator.view = view;
		setCurrentBulkEditRecords(null);
	}

	public static void setCurrentHtmlView(ZCHtmlView htmlView) {
		ZOHOCreator.htmlView = htmlView;
	}

	public static void setCurrentSubForm(ZCForm subform) {
		ZOHOCreator.subform=subform;
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

	public static ZOHOUser getZohoUser() throws ZCException {
		return ZOHOUser.getUserObject();
	}

	public static void logout(){
		ZOHOUser user = null;
		try {
			user = ZOHOCreator.getZohoUser();
		} catch (ZCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(user != null) {
			URLPair delAuthTokenURL = ZCURL.deleteAuthToken(user.getAuthToken());
			try {
				ZOHOCreator.postURL(delAuthTokenURL.getUrl(), delAuthTokenURL.getNvPair());
			} catch (ZCException e) {
				e.printStackTrace();
			}
			user.logout();
		}
	}

	public static ZCNavList getNavigationListForApps() throws ZCException {
		// check for file

		File f = new File(getFilesDir()+NAV_LIST_FILE);
		Document rootDocument = null;
		if(cacheResponse)  {
			rootDocument = stringToDocument(readResponseFromFile(f));
		}
		if(rootDocument == null){
			URLPair navigationListURL = ZCURL.navigationListURL();
			rootDocument = ZOHOCreator.postURLXML(navigationListURL.getUrl(), navigationListURL.getNvPair());
			if(cacheResponse) {
				writeResponseToFile(getString(rootDocument),f);
			}
		}
		return XMLParser.parseForNavigationListForApps(rootDocument);
	}

	public static ZCAppList getPersonalApplicationList(List<NameValuePair> additionalParams) throws ZCException {

		Document rootDocument = null;
		File f1 = new File(getFilesDir()+EDIT_SUPPORT_FILE);
		String response = null;

		if(f1.exists()){
			List<ZCApplication> zcApps = new ArrayList<ZCApplication>();
			BufferedReader br = null;
			try {

				br = new BufferedReader(new FileReader(f1));
				//String line = br.readLine();
				while (true) {
					String url = br.readLine();
					if(url == null){
						break;
					}
					String[] token = url.split("/");
					ZCApplication zcApp = new ZCApplication(token[3], token[4], token[4], false, new Date());
					zcApps.add(zcApp);
				}
				br.close();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ZCAppList(ZCAppList.PERSONAL_APPS, zcApps);
		}else{
			File f = new File(getFilesDir()+PERSONAL_APPLIST_FILE);
			//			rootDocument = stringToDocument(readResponseFromFile(f));
			if(cacheResponse){
				response = readResponseFromFile(f);
			}
			if(response == null){
				URLPair appListURLPair = ZCURL.appListURL();
				if(additionalParams == null) {
					additionalParams = new ArrayList<NameValuePair>();
				}
				additionalParams.addAll(appListURLPair.getNvPair());
				//				rootDocument = ZOHOCreator.postURLXML(appListURLPair.getUrl(), additionalParams);
				//				writeResponseToFile(getString(rootDocument), f);
				response = ZOHOCreator.postURL(appListURLPair.getUrl(), additionalParams);
				if(cacheResponse){
					writeResponseToFile(response, f);
				}
			}
			return new ZCAppList(ZCAppList.PERSONAL_APPS, JSONParser.parseForApplicationList(response, JSONParser.PERSONAL_APPS));
		}
	}

	public static ZCAppList getSharedApplicationList() throws ZCException {
		return getSharedApplicationList(null);
	}

	public static ZCAppList getSharedApplicationList(ZCSharedGroup sharedGroup) throws ZCException {


		File f = new File(getFilesDir()+SHARED_APPLIST_FILE);

		if(sharedGroup != null){
			f = new File(getFilesDir()+"/sharedApps_"+sharedGroup.getGroupId()+"List.json");
		}
		//		Document rootDocument = stringToDocument(readResponseFromFile(f));
		String response = null;
		if(cacheResponse){
			response = readResponseFromFile(f);
		}
		if(response == null){
			URLPair appListURLPair = ZCURL.sharedAppListURL(null); 
			if(sharedGroup != null) {
				appListURLPair = ZCURL.sharedAppListURL(sharedGroup.getGroupId()); 
			}
			//			rootDocument = ZOHOCreator.postURLXML(appListURLPair.getUrl(), appListURLPair.getNvPair());
			//			writeResponseToFile(getString(rootDocument), f);

			response = ZOHOCreator.postURL(appListURLPair.getUrl(), appListURLPair.getNvPair());
			if(cacheResponse){
				writeResponseToFile(response, f);
			}
		}
		List<ZCApplication> apps = JSONParser.parseForApplicationList(response, JSONParser.SHARED_APPS);
		ZCAppList toReturn = new ZCAppList(ZCAppList.SHARED_APPS, apps);
		toReturn.setSharedGroup(sharedGroup);
		return toReturn;
	}

	public static ZCAppList getWorkspaceApplicationList(String workspaceAppOwner, List<NameValuePair> additionalParams) throws ZCException {
		File f = new File(getFilesDir()+"/workspaceApps_"+workspaceAppOwner+"List.json");
		//		Document rootDocument = stringToDocument(readResponseFromFile(f));
		String response = null;
		if(cacheResponse){
			response = readResponseFromFile(f);
		}
		if(response == null){
			URLPair appListURLPair = ZCURL.workSpaceAppListURL(workspaceAppOwner); 
			if(additionalParams == null) {
				additionalParams = new ArrayList<NameValuePair>();
			}
			additionalParams.addAll(appListURLPair.getNvPair());
			//			rootDocument = ZOHOCreator.postURLXML(appListURLPair.getUrl(), additionalParams);
			//			writeResponseToFile(getString(rootDocument), f);

			response = ZOHOCreator.postURL(appListURLPair.getUrl(), additionalParams);
			if(cacheResponse){
				writeResponseToFile(response, f);
			}
		}
		ZCAppList toReturn = new ZCAppList(ZCAppList.WORKSPACE_APPS, JSONParser.parseForApplicationList(response, JSONParser.WORKSPACE_APPS));
		toReturn.setWorkspaceAppOwner(workspaceAppOwner);
		return toReturn;
	}

	public static List<ZCSection> getSectionList(String appLinkName, String appOwner, List<NameValuePair> additionalParams) throws ZCException {


		File f = new File(getFilesDir()+"/sections_"+appOwner+"_"+appLinkName+"List.xml");
		Document rootDocument = null;
		if(cacheResponse){
			rootDocument = stringToDocument(readResponseFromFile(f));
		}
		if(rootDocument == null){

			URLPair sectionListURLPair = ZCURL.sectionMetaURL(appLinkName, appOwner);
			if(additionalParams == null) {
				additionalParams = new ArrayList<NameValuePair>();
			}
			additionalParams.addAll(sectionListURLPair.getNvPair());

			rootDocument = ZOHOCreator.postURLXML(sectionListURLPair.getUrl(), additionalParams);

			if(cacheResponse){
				writeResponseToFile(getString(rootDocument),f);
			}

		}

		return XMLParser.parseForSectionList(rootDocument, appLinkName, appOwner);
	}

	public static List<ZCSection> getSectionList(ZCApplication zcApp, List<NameValuePair> additionalParams)  throws ZCException {
		return getSectionList(zcApp.getAppLinkName(), zcApp.getAppOwner(), additionalParams);
	}

	public static ZCForm getFeedBackForm(String preFilledLogMessage,boolean isGeneralErrorOccured) {
		ZCForm toReturn  = new ZCForm("zoho1", "support", "Feedback", "Feedback", 1, false, false, resourceString.getString("thank_you_for_your_feedback"), "dd-MMM-yyyy", false,"","");//No I18N
		ZCField editAccessField   = new ZCField("edit_access_field",FieldType.DROPDOWN,resourceString.getString("allow_edit_access_to_support"));
		ZCField platformField = new ZCField("Platform", FieldType.DROPDOWN, "Platform");//No I18N
		List<ZCChoice> choices = new ArrayList<ZCChoice>();
		//choices.add(new ZCChoice("iOS", "iOS"));//No I18N
		ZCChoice choice = new ZCChoice("Android", "Android");
		choices.add(choice);//No I18N
		ZCRecordValue recValue = new ZCRecordValue(platformField, choice);
		recValue.addChoices(choices);
		platformField.setRecordValue(recValue);//No I18N
		platformField.setHidden(true);
		platformField.setRebuildRequired(true);
		editAccessField.setLookup(true); 
		List<ZCApplication> zcApps = new ArrayList<ZCApplication>();
		if(cacheResponse){
			if(readResponseFromFile(new File(getFilesDir()+PERSONAL_APPLIST_FILE))!=null)
			{
				if(!(readResponseFromFile(new File(getFilesDir()+PERSONAL_APPLIST_FILE)).equals("")))
				{
					try {
						zcApps = getPersonalApplicationList(null).getApps();
					} catch (ZCException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		List<ZCChoice> zcChoices = new ArrayList<ZCChoice>();
		if(zcApps!=null)
		{
			for(int i=0;i<zcApps.size();i++)
			{
				ZCApplication zcApp = zcApps.get(i);
				ZCChoice zcAppChoice = new ZCChoice(zcApp.getAppLinkName(),zcApp.getAppName());
				zcChoices.add(zcAppChoice);		
			}
		}
		ZCChoice zcChoice = null;
		ZCField titleField = new ZCField("Title", FieldType.SINGLE_LINE, "Title");//No I18N
		ZCField messageField = new ZCField("Message", FieldType.MULTI_LINE, "Message");//No I18N
		messageField.setRecordValue(new ZCRecordValue(messageField, preFilledLogMessage));
		List<ZCField> fields = new ArrayList<ZCField>();
		String title = "";
		if(isGeneralErrorOccured)
		{
			String personalAppsOwner = "";
			title = title + resourceString.getString("error_occured");
			ZCComponent comp = ZOHOCreator.getCurrentComponent();
			ZCApplication zcapp = ZOHOCreator.getCurrentApplication();
			if(comp!=null)
			{
				title = title + " - "+comp.getComponentName();
			}
			if(zcapp!=null)
			{
				title = title + " - "+zcapp.getAppName();
			}

			if(zcApps!=null&&zcApps.size()>0)
			{
				personalAppsOwner = zcApps.get(0).getAppOwner();	
				if(ZOHOCreator.getCurrentApplication()!=null)
				{
					String currentApplictionOwner =ZOHOCreator.getCurrentApplication().getAppOwner();
					if(currentApplictionOwner.equals(personalAppsOwner))
					{
						title = "[owner] " + title;
						zcChoice = new ZCChoice(ZOHOCreator.getCurrentApplication().getAppLinkName(),ZOHOCreator.getCurrentApplication().getAppName());

					}
				}
			}
		}
		titleField.setRecordValue(new ZCRecordValue(titleField, title));
		editAccessField.setRecordValue(new ZCRecordValue(editAccessField, zcChoice));
		editAccessField.getRecordValue().addChoices(zcChoices);
		editAccessField.getRecordValue().setLastReachedForChoices(true);
		fields.add(platformField);	
		fields.add(titleField);//No I18N
		fields.add(messageField);//No I18N
		if(zcChoices.size()>0)
		{
			fields.add(editAccessField);
		}
		List<ZCButton> buttons = new ArrayList<ZCButton>();
		buttons.add(new ZCButton(resourceString.getString("submit"), "submit", ZCButtonType.SUBMIT));//No I18N
		buttons.add(new ZCButton(resourceString.getString("reset"), "reset", ZCButtonType.RESET));//No I18N
		toReturn.addFields(fields);
		toReturn.addButtons(buttons);
		return toReturn;
	}



	public static void giveEditAccessToTheApplication(String appOwner,String appLinkName) throws ZCException
	{
		URLPair editAccessUrl  = ZCURL.editAccessURL(appOwner, appLinkName);
		ZOHOCreator.postURLXML(editAccessUrl.getUrl(), editAccessUrl.getNvPair());
	}

	private static void callFormOnAddOnLoad(ZCForm zcForm,int formAccessType) throws ZCException{
		List<NameValuePair> params = zcForm.getFieldParamValues();
		params.addAll(getAdditionalParamsForForm(zcForm, null));

		URLPair formOnAddOnLoadURL = ZCURL.formOnLoad(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), zcForm.getAppOwner(), params ,formAccessType);

		String response = ZOHOCreator.postURL(formOnAddOnLoadURL.getUrl(), formOnAddOnLoadURL.getNvPair());

		JSONParser.parseAndCallFormEvents(response, zcForm,false);

	}

	static void callFormEditOnAddOnLoad(ZCForm zcForm,Long recordLinkId,int formAccessType) throws ZCException{
		List<NameValuePair> params = zcForm.getFieldParamValues();
		params.addAll(getAdditionalParamsForForm(zcForm, null));
		URLPair formEditOnAddOnLoadURL = ZCURL.formEditOnLoad(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), zcForm.getAppOwner(), params,recordLinkId,formAccessType);
		String response = ZOHOCreator.postURL(formEditOnAddOnLoadURL.getUrl(), formEditOnAddOnLoadURL.getNvPair());
		JSONParser.parseAndCallFormEvents(response, zcForm,false);
	}

	private static void callDelugeEvents(ZCForm zcForm, URLPair urlPair,boolean doesOnUserInputRetriggered) throws ZCException{
		String response = ZOHOCreator.postURL(urlPair.getUrl(), urlPair.getNvPair());

		JSONParser.parseAndCallFormEvents(response,zcForm,doesOnUserInputRetriggered);
	}

	static ZCResponse parseResponseDocumentForJSONString(URLPair urlPair, ZCForm zcForm) throws ZCException {
		String response = ZOHOCreator.postURL(urlPair.getUrl(), urlPair.getNvPair());
		return JSONParser.parseAndCallFormEvents(response, zcForm,false);
	}


	static void callFieldOnUser(ZCForm zcForm, String fieldLinkName, boolean isFormula, ZCForm currentShownForm,boolean doesOnUserInputRetriggered) throws ZCException {
		List<NameValuePair> params = zcForm.getFieldParamValues();
		params.addAll(getAdditionalParamsForForm(zcForm, null));

		URLPair urlPair = ZCURL.fieldOnUser(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), fieldLinkName, zcForm.getAppOwner(),params, isFormula,zcForm.getFormType());
		if(currentShownForm!=null)
		{
			zcForm = currentShownForm;
		}

		callDelugeEvents(zcForm, urlPair,doesOnUserInputRetriggered);
	}

	static void callSubFormFieldOnUser(String subFormFieldLinkName, ZCForm currentShownForm, boolean isFormula,int entryPosition,long id) throws ZCException{
		//ZCForm zcForm = ZOHOCreator.getCurrentForm();
		ZCForm zcForm = currentShownForm.getBaseSubFormField().getBaseForm();
		List<NameValuePair> params = zcForm.getFieldParamValues();
		params.addAll(getAdditionalParamsForForm(zcForm, null));
		callDelugeEvents(currentShownForm, ZCURL.subFormOnUser(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, currentShownForm.getBaseSubFormField().getFieldName() , zcForm.getAppOwner(),params,isFormula,entryPosition,id),false);
	}

	public static void callSubFormAddRow(ZCForm zcForm, String subFormFieldLinkName,ZCForm currentShownForm, int rowPosition) throws ZCException{
		List<NameValuePair> params = zcForm.getFieldParamValues();
		params.addAll(getAdditionalParamsForForm(zcForm, null));
		callDelugeEvents(currentShownForm, ZCURL.subFormAddRow(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, zcForm.getAppOwner(), params,rowPosition),false);
	}

	public static void callSubFormDeleteRow(ZCForm zcForm, String subFormFieldLinkName,long id,int position) throws ZCException{
		List<NameValuePair> params = zcForm.getFieldParamValues();
		params.addAll(getAdditionalParamsForForm(zcForm, null));
		callDelugeEvents(zcForm, ZCURL.subFormDeleteRow(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, zcForm.getAppOwner(), params,id,position),false);
	}

	public static ZCView getListReport(String appLinkName, String viewLinkName, String appOwner) throws ZCException{
		return getView(new ZCComponent(appOwner, appLinkName, ZCView.REPORT, viewLinkName, viewLinkName, -1));
	}

	public static ZCView getCalendarReport(String appLinkName, String viewLinkName, String appOwner) throws ZCException{
		return getView(new ZCComponent(appOwner, appLinkName, ZCView.CALENDAR, viewLinkName, viewLinkName, -1));
	}

	public static ZCView getView(ZCComponent comp) throws ZCException{
		if(comp.getType().equals(ZCComponent.REPORT) || comp.getType().equals(ZCComponent.CALENDAR) ||comp.getType().equals(ZCComponent.SUMMARY) || comp.getType().equals(ZCComponent.GRID)||comp.getType().equals(ZCComponent.SPREADSHEET)) {
			List<NameValuePair> params = new ArrayList<NameValuePair>(); //URLConstructor.getDefaultParams(comp.getAppOwner());
			if(comp.getType().equals(ZCComponent.REPORT) || comp.getType().equals(ZCComponent.SUMMARY) ||comp.getType().equals(ZCComponent.GRID)||comp.getType().equals(ZCComponent.SPREADSHEET)) {
				params.add(new BasicNameValuePair("startIndex", "1"));//No I18N
				params.add(new BasicNameValuePair("pageSize", ZCView.PAGE_SIZE+"")); //No I18N
				params.addAll(getQueryStringParams(comp.getQueryString()));
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
				if(getString(rootDocument).contains("here is no such view"))
				{
					throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.LINK_NAME_ERROR, resourceString.getString("unable_to_get") + getURLStringForException((ZCURL.viewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner())).getUrl(), params)); //No I18N
				}
				else
				{
					throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, resourceString.getString("unable_to_get") + getURLStringForException((ZCURL.viewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner())).getUrl(), params)); //No I18N
				}
			}
			return toReturn;
		}
		throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, resourceString.getString("trying_to_fetch_view_details_but_not_a_view") + comp); //No I18N
	}


	private static ZCHtmlView getHtmlView(ZCComponent comp) throws ZCException {
		if(comp.getType().equals(ZCComponent.PAGE)) {
			List<NameValuePair> params = getQueryStringParams(comp.getQueryString());
			URLPair htmlViewURLPair = ZCURL.htmlViewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner(),params);
			//params.addAll(viewURLPair.getNvPair());
			String htmlString = ZOHOCreator.postURL(htmlViewURLPair.getUrl(), htmlViewURLPair.getNvPair());
			if(htmlString.contains("There is no such view in"))
			{
				throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.LINK_NAME_ERROR, resourceString.getString("unable_to_get") + getURLStringForException((ZCURL.viewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner())).getUrl(), params)); //No I18N
			}
			return new ZCHtmlView( comp.getAppOwner(), comp.getAppLinkName(), comp.getType(), comp.getComponentName(), comp.getComponentLinkName(), htmlString);
			//return ZOHOCreator.postURLXML(viewURLPair.getUrl(), params);
		}		
		throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, resourceString.getString("trying_to_fetch_view_details_but_not_a_view")); //No I18N
	}


	static List<ZCChoice> loadMoreChoices(ZCRecordValue zcRecordValue) throws ZCException {

		ZCField field = zcRecordValue.getField();
		ZCField subFormField = field.getBaseForm().getBaseSubFormField();
		ZCForm baseForm = field.getBaseForm();
		String fieldName = field.getFieldName();
		String subformComponent = null;
		int formAccessType = 0;
		int size = field.getRecordValue().getChoices().size();
		String searchForChoices = field.getRecordValue().getSearchForChoices();

		if(subFormField != null) {
			baseForm = subFormField.getBaseForm();
			subformComponent = field.getFieldName();
			fieldName = subFormField.getFieldName();
		}
		
		if(field.getType()==FieldType.EXTERNAL_FIELD)
		{

			String moduleType = field.getModuleType();
			
			URLPair crmLookupChoicesUrl = ZCURL.crmLookupChoices(size,moduleType);
			String response = ZOHOCreator.postURL(crmLookupChoicesUrl.getUrl(),crmLookupChoicesUrl.getNvPair());
			return JSONParser.parseCrmLookupChoices(response,zcRecordValue);
		}else
		{
			if(field.getBaseForm().getFormType()==ZCForm.FORM_LOOKUP_ADD_FORM) {
				formAccessType = ZCForm.FORM_LOOKUP_ADD_FORM;
			} else if(getCurrentForm().getViewForAdd()!=null) {
				formAccessType = ZCForm.VIEW_ADD_FORM;
			} else if(getCurrentForm().getViewForEdit()!=null) {
				formAccessType = ZCForm.VIEW_EDIT_FORM;
			} else if(getCurrentForm().getViewForBulkEdit()!=null) {
				formAccessType = ZCForm.VIEW_BULK_EDIT_FORM;
			} else {
				formAccessType = ZCForm.FORM_ALONE;
			}
			if(baseForm.getBaseLookupField()!=null)
			{
				field=baseForm.getBaseLookupField();
			}
			URLPair lookupChoicesUrl = ZCURL.lookupChoices(baseForm.getAppLinkName(), baseForm.getComponentLinkName(), baseForm.getAppOwner(), fieldName, size, searchForChoices, subformComponent,formAccessType,getAdditionalParamsForForm(baseForm,field));

			Document rootDocument = ZOHOCreator.postURLXML(lookupChoicesUrl.getUrl(), lookupChoicesUrl.getNvPair());
			return XMLParser.parseLookUpChoices(rootDocument);
		}
	}

	static void loadRecords(ZCView zcView) throws ZCException 
	{  // Search, Filter & Group By

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
				if(! ZCCondition.isDateFieldWithoutValues(condition.getOperator()))
				{
					params.add(new BasicNameValuePair(fieldName, condition.getValue()));//No I18N
				}
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

	public static String getPivotViewURL() throws ZCException
	{
		ZCComponent zccomp = getCurrentComponent();
		URLPair viewURLPair = ZCURL.pivotViewURL(zccomp.getAppLinkName(), zccomp.getComponentLinkName(), zccomp.getAppOwner());
		Document toReturn = ZOHOCreator.postURLXML(viewURLPair.getUrl(), viewURLPair.getNvPair());	
		if(getString(toReturn).contains("here is no such view"))
		{
			throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.LINK_NAME_ERROR, resourceString.getString("unable_to_get") + getURLStringForException((ZCURL.viewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner())).getUrl(), new ArrayList<NameValuePair>())); //No I18N
		}
		return XMLParser.parsePivotViewURL(toReturn);
	}

	private static Document getViewXMLDocument(ZCComponent comp, List<NameValuePair> params) throws ZCException{
		URLPair viewURLPair = ZCURL.viewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner());
		params.addAll(viewURLPair.getNvPair());
		Document toReturn = ZOHOCreator.postURLXML(viewURLPair.getUrl(), params);		
		return toReturn;
	}

	static ZCResponse postCustomAction(ZCView zcView, long customActionId, List<Long> recordIDs) throws ZCException {
		String appLinkName =	zcView.getAppLinkName();
		String viewLinkName = zcView.getComponentLinkName();
		String appOwner = zcView.getAppOwner(); 
		URLPair customActionURLPair = ZCURL.customActionURL(appLinkName, viewLinkName, customActionId, appOwner, recordIDs);
		String strResponse = ZOHOCreator.postURL(customActionURLPair.getUrl(), customActionURLPair.getNvPair());
		//		int index = strResponse.indexOf("GenerateJS>");
		//		if(index != -1){
		//			while(strResponse.charAt(index) != '>'){
		//				index++;
		//			}
		//			int startIndex = ++index;
		//			index++;
		//			while(strResponse.charAt(index) != '<'){
		//				index++;
		//			}
		//			int endIndex = index;
		//			strResponse = strResponse.replace(strResponse.substring(startIndex, endIndex), "");
		//			index = strResponse.indexOf("]");
		//			startIndex = index;
		//			while(strResponse.charAt(index) != '>'){
		//				index++;
		//			}
		//			endIndex = ++index;
		//			strResponse = strResponse.replace(strResponse.substring(startIndex, endIndex), "");
		//		}

		Document rootDocument = stringToDocument(strResponse);
		NodeList nl = rootDocument.getChildNodes();
		ZCResponse toReturn = new ZCResponse();
		for(int i=0; i<nl.getLength(); i++) {
			Node resultNode = nl.item(i);
			NodeList customActionNodes = resultNode.getChildNodes();
			for(int j =0;j<customActionNodes.getLength();j++) {
				Node customActionNode = customActionNodes.item(j);
				if(customActionNode.getNodeName().equals("SuccessMessage")) {
					toReturn.setSuccessMessage(customActionNode.getFirstChild().getNodeValue());
				}if(customActionNode.getNodeName().equals("GenerateJS")) {
					NodeList GenerateJSNodeList = customActionNode.getChildNodes();
					for(int k =0;k<GenerateJSNodeList.getLength();k++) {
						Node tasksNode = GenerateJSNodeList.item(k);
						NodeList tasksNodeList = tasksNode.getChildNodes();
						for(int m =0;m<tasksNodeList.getLength();m++) {
							Node taskNode = tasksNodeList.item(m);
							NodeList taskNodeList = taskNode.getChildNodes();
							for(int n =0;n<taskNodeList.getLength();n++) {
								Node urlNode = taskNodeList.item(n);
								if(urlNode.getNodeName().equals("url")) {
									toReturn.setOpenUrlValueForCustomAction(urlNode.getFirstChild().getNodeValue());
								}
							}
						}
					}
				}				
			}
		}
		return toReturn;
	}

	static Document getUserDocument() throws ZCException {
		URLPair userPersonalInfoURL = ZCURL.userPersonalInfoURL();

		return ZOHOCreator.postURLXML(userPersonalInfoURL.getUrl(), userPersonalInfoURL.getNvPair());
	}

	static String getAuthTokenResponse(String uname, String password) throws ZCException {
		URLPair loginURL = ZCURL.getAuthTokenURL(uname, password);//No I18N
		return ZOHOCreator.postURL(loginURL.getUrl(), loginURL.getNvPair());

	}


	private static ZCResponse postXMLString(String appOwner, String xmlString, String action, List<NameValuePair> params,ZCForm zcForm) throws ZCException{

		URLPair xmlWriteURLPair = ZCURL.xmlWriteURL(appOwner, xmlString);
		if(params == null) {
			params = new ArrayList<NameValuePair>();
		}
		params.addAll(xmlWriteURLPair.getNvPair());

		Document rootDocument = ZOHOCreator.postURLXML(xmlWriteURLPair.getUrl(), params);

		XPath xPath = XPathFactory.newInstance().newXPath();
		ZCResponse toReturn = new ZCResponse();
		try {
			String status = xPath.compile("/response/result/form/" + action + "/status").evaluate(rootDocument);//No I18N
			String openUrlValue = xPath.compile("/response/result/form/" + action + "/openurl/url").evaluate(rootDocument);//No I18N
			String openUrlType = xPath.compile("/response/result/form/" + action + "/openurl/type").evaluate(rootDocument);//No I18N
			toReturn.setStatus(status);
			toReturn.setOpenUrlValue(openUrlValue);
			toReturn.setOpenUrlType(openUrlType);
			if(status.startsWith("Failure")) {
				toReturn.setError(true);
			} else  if(status.startsWith("Success")) { //No I18N
				xPath = XPathFactory.newInstance().newXPath();
				String idValue = xPath.compile("/response/result/form/add/values/field[@name=\"ID\"]").evaluate(rootDocument);//No I18N
				if(idValue.length()==0)
				{
					idValue = xPath.compile("/response/result/form/update/criteria").evaluate(rootDocument);//No I18N
					if(idValue.length()>10)
					{
						idValue = idValue.substring(8,idValue.length()-2);
					}
				}
				if(idValue.length()!=0) {
					if(!(idValue.contains("ID")))
					{
						toReturn.setSuccessRecordID(Long.parseLong(idValue));
						String lookUpValue = xPath.compile("/response/result/form/" + action + "/combinedlookupvalue").evaluate(rootDocument);//No I18N
						toReturn.setSuccessLookUpChoiceValue(new ZCChoice(idValue, lookUpValue));
					}
				}
				if(form!=null)
				{
					List<ZCField> zcFields = form.getFields();
					for(int i=0;i<zcFields.size();i++)
					{

						ZCField zcField = zcFields.get(i);
						if(zcField.getType().equals(FieldType.SUB_FORM))
						{
							List<ZCField> subFormFields = zcField.getSubForm().getFields();
							for(int j=0;j<subFormFields.size();j++)
							{
								ZCField subFormField = subFormFields.get(j);
								if(FieldType.isPhotoField(subFormField.getType()))
								{
									NodeList nodes = rootDocument.getElementsByTagName("field");
									for(int k=0;k<nodes.getLength();k++)
									{

										Node node = nodes.item(k);
										if(node.getAttributes().getNamedItem("name").getNodeValue().equals(zcField.getFieldName()))
										{
											NodeList childNodes = node.getChildNodes();
											List<Long> subFormRecordIds = new ArrayList<Long>();
											for(int l=0;l<childNodes.getLength();l++)
											{
												Node childNode = childNodes.item(l);
												if(childNode.getNodeName().equals("update"))
												{
													subFormRecordIds.add(Long.parseLong(childNode.getAttributes().getNamedItem("ID").getNodeValue()));
												}
												else if(childNode.getNodeName().equals("add"))
												{
													subFormRecordIds.add(Long.parseLong(childNode.getAttributes().getNamedItem("ID").getNodeValue()));
												}
											}
											zcField.setSubFormRecordIds(subFormRecordIds);
										}
									}
								}
							}
						}
					}
				}
			} 
			else {
				String errorCodeMessage = xPath.compile("/response/errorlist/error/message").evaluate(rootDocument);//No I18N
				if(errorCodeMessage != null && !errorCodeMessage.equals("")) {
					toReturn.setError(true);
					toReturn.setMainErrorMessage(errorCodeMessage);
				}
			}
		}
		catch (XPathExpressionException e) {
			throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, getTrace(e));//No I18N
		}
		return toReturn;
	}

	public static String postURL(final String url, final List<NameValuePair> params) throws ZCException {

		
		if(readResponseFromFileForAPI) {
			return (getResponseString(getURLString(url, params)));
		}

		

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
			throw new ZCException(resourceString.getString("no_network_connection"), ZCException.NETWORK_ERROR);//No I18N
		} catch(HttpHostConnectException uhe) {
			throw new ZCException(resourceString.getString("no_network_connection"), ZCException.NETWORK_ERROR);//No I18N
		} catch (ClientProtocolException e) {
			throw new ZCException(resourceString.getString("unable_to_connect_with")+appDisplayName, ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
		} catch (IOException e) {
			throw new ZCException(resourceString.getString("network_error"),ZCException.NETWORK_ERROR);//No I18N
		} catch (URISyntaxException e) {
			throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
		} catch (RuntimeException ee) {
			throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, getTraceWithURL(ee, url, params));//No I18N
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

	private static Document postURLXML(String url, List<NameValuePair> params) throws ZCException {
		

		if(readResponseFromFileForAPI)
		{
			return getResponseDocument(getURLString(url, params));
		}

		

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

				throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N

			}
			HttpEntity entity = null;
			try {
				entity = client.execute(request).getEntity();
			} catch (ClientProtocolException e) {
				throw new ZCException(resourceString.getString("unable_to_connect_with")+ appDisplayName, ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N
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

					throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N

				} catch (SAXException e) {

					e.printStackTrace();
					if(isGettingUserDetails)
					{
						isGettingUserDetails = false;
						return null;
					}else
					{
						throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, getTraceWithURL(e, url, params));//No I18N

					}
				}  
				finally {
					if(is != null) {
						is.close();
					}
				}
			}

		} catch(UnknownHostException uhe) {
			throw new ZCException(resourceString.getString("no_network_connection"), ZCException.NETWORK_ERROR);//No I18N
		} catch(HttpHostConnectException uhe) {
			throw new ZCException(resourceString.getString("no_network_connection"), ZCException.NETWORK_ERROR);//No I18N
		} catch (IOException e) {

			throw new ZCException(resourceString.getString("network_error"), ZCException.NETWORK_ERROR);//No I18N
		}
		throw new ZCException(resourceString.getString("an_error_has_occured"), ZCException.GENERAL_ERROR, getURLStringForException(url, params)); //No I18N
	}

	private static String getTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();		
	}

	private static String getTraceWithURL(Exception ex, String url, List<NameValuePair> params) {
		return getURLStringForException(url, params) + "\n\n" + getTrace(ex); //No I18N
	}

	static void postFile(String urlParam, Object bitMap, String fileName, List<NameValuePair> paramsList,boolean isFileUploadField) throws ZCException {
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

		if(bitMap!=null)
		{
			httppost.addHeader("enctype", "multipart/form-data"); //No I18N

			byte[] byteArray=null;
			if(isFileUploadField)
			{

				int len;
				int size = 1024;
				byte[] buf;
				try 
				{
					InputStream is = (InputStream)bitMap;
					if (is instanceof ByteArrayInputStream) {

						size = is.available();

						buf = new byte[size];
						len = is.read(buf, 0, size);
					} else {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						buf = new byte[size];
						while ((len = is.read(buf, 0, size)) != -1)
							bos.write(buf, 0, len);
						byteArray = bos.toByteArray();
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else
			{
				byteArray = fileHelper.getBytes(bitMap);
			}
			ContentBody cbFile = new ByteArrayBody(byteArray, fileName);			
			MultipartEntity mpEntity = new MultipartEntity();
			try {
				mpEntity.addPart("filename", new StringBody(fileName));//No I18N
			} catch (UnsupportedEncodingException e) {
				throw new ZCException(resourceString.getString("unable_to_upload_file"), ZCException.GENERAL_ERROR, getTraceWithURL(e, urlParam, paramsList));//No I18N
			} //No I18N
			mpEntity.addPart("file", cbFile); //No I18N
			httppost.setEntity(mpEntity);
		}
		try {
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			String responseBody = httpclient.execute(httppost, responseHandler);
			
			//HttpResponse httpResponse = httpclient.execute(httppost);

		} catch(UnknownHostException uhe) {
			throw new ZCException(resourceString.getString("no_network_connection"), ZCException.NETWORK_ERROR);//No I18N
		} catch(HttpHostConnectException uhe) {
			throw new ZCException(resourceString.getString("no_network_connection"), ZCException.NETWORK_ERROR);//No I18N
		} catch (IOException e) {
			throw new ZCException(resourceString.getString("network_error"),ZCException.NETWORK_ERROR);//No I18N
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

	private static Document stringToDocument(final String xmlSource)   
	{  
		if(xmlSource != null){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				return builder.parse(new InputSource(new StringReader(xmlSource)));
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
		}
		return null;
	}  

	private static String readResponseFromFile(File file){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			return sb.toString();
		} 

		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(br != null){
					br.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null; 
	}

	private static void writeResponseToFile( String content , File file){
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Methods for APITest

	public static void setCacheResponse(boolean cacheResponse){
		ZOHOCreator.cacheResponse = cacheResponse;
	}
	public static void setReadResponseFromFileForAPI(boolean readResponseFromFileForAPI){
		ZOHOCreator.readResponseFromFileForAPI = readResponseFromFileForAPI;
	}

	private static Document getResponseDocument(String urlString) {
		Properties responseProp = new Properties();
		try {
			FileReader reader = new FileReader("property/Response.properties");
			responseProp.load(reader);
			String respFileName = responseProp.getProperty(urlString);
			return stringToDocument(readResponseFromFile(new File(respFileName)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String getResponseString(String urlString){
		Properties responseProp = new Properties();
		try {
			FileReader reader = new FileReader("property/Response.properties");
			responseProp.load(reader);
			String respFileName = responseProp.getProperty(urlString);
			return readResponseFromFile(new File(respFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
