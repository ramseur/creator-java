// $Id$

package com.zoho.creator.jframework.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ZOHOCreator {
	
	private static ZCApplication zcApp = null;
	private static List<ZCSection> sectionList = null;
	private static ZCComponent comp = null;
	private static ZCForm form = null;
	private static ZCView view = null;
	private static ZCHtmlView htmlView = null;
	private static ZCRecord editRecord = null;
	private static List<ZCRecord> bulkEditRecords = null;

	private static ZCAppList appList = null;
	private static ZCNavList navigationListForApps = null;
	private static ZCForm subform = null;
	private static String accountsURL = "accounts.zoho.com";//No I18N
	private static String serviceName = "ZohoCreator";//No I18N
	private static String creatorURL = "creator.zoho.com";//No I18N
	
	

	static String getAccountsURL() {
		return accountsURL;
	}

	public static void setAccountsURL(String accountsURL) {
		ZOHOCreator.accountsURL = accountsURL;
	}


	static String getServiceName() {
		return serviceName;
	}

	public static void setServiceName(String serviceName) {
		ZOHOCreator.serviceName = serviceName;
	}

	static String getCreatorURL() {
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

	public static void loadSelectedApplication() {
		setCurrentSectionList(getSectionList(getCurrentApplication()));
	}

	public static void setCurrentSectionList(List<ZCSection> sectionList) {
		ZOHOCreator.sectionList = sectionList;
	}

	public static List<ZCSection> getCurrentSectionList() {
		return sectionList;
	}
	
	public static void loadSelectedHtmlView() {
		setCurrentHtmlView(getHtmlView(getCurrentComponent()));
	}

	public static void loadSelectedView() {
		setCurrentView(getView(getCurrentComponent()));
	}	

	public static void loadSelectedForm() {
		setCurrentForm(getForm(getCurrentComponent()));
	}

	public static void loadFormForAddRecord(Date calSelectedStartDate , Date calSelectedEndDate) {
		loadFormForView(null, ZCForm.VIEW_ADD_FORM, calSelectedStartDate , calSelectedEndDate);
		getCurrentForm().setViewForAdd(getCurrentView());
	}

	public static void loadFormForEditRecord() {
		ZCRecord record = getCurrentEditRecord();
		loadFormForView(record.getRecordId(), ZCForm.VIEW_EDIT_FORM, null,null);
		ZCForm currentForm = getCurrentForm();
		currentForm.setViewForEdit(getCurrentView());
		currentForm.addEditRecord(record);
	}

	public static void loadFormForBulkEditRecords() {
		List<ZCRecord> records = getCurrentBulkEditRecords();
		loadFormForView(null, ZCForm.VIEW_BULK_EDIT_FORM, null,null);
		ZCForm currentForm = getCurrentForm();
		currentForm.setViewForBulkEdit(getCurrentView());
		for(int i=0; i<records.size(); i++) {
			currentForm.addEditRecord(records.get(i));
		}
	}

	public static void loadFormForAddToLookup(ZCForm baseForm, ZCField lookupField) {
		ZCComponent refComponent = lookupField.getRefFormComponent();
		ZCForm lookupForm =  getForm(refComponent.getAppLinkName(), refComponent.getComponentLinkName(), refComponent.getAppOwner(), null, null, ZCForm.FORM_ALONE, baseForm.getAppLinkName(), baseForm.getComponentLinkName(), lookupField.getFieldName(), null, null);
		lookupField.setLookupForm(lookupForm);
	}

	private static void loadFormForView(Long recordLinkId, int formType, Date calSelectedStartDate ,Date calSelectedEndDate) {
		ZCView currentView = getCurrentView();
		String formLinkName = currentView.getBaseFormLinkName();
		ZCForm form = getForm(currentView.getAppLinkName(), formLinkName, currentView.getAppOwner(), currentView.getComponentLinkName(), recordLinkId, formType, null, null, null, calSelectedStartDate,calSelectedEndDate);
		setCurrentForm(form);
	}

	private static void setCurrentForm(ZCForm form) {
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

	public static ZOHOUser login(String authToken) {
		return ZOHOUser.getUserObject(authToken);
	}

	public static ZOHOUser login(String uname, String password) {
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

	public static ZCNavList getNavigationListForApps() {
		URLPair navigationListURL = ZCURL.navigationListURL();
		Document rootDocument = ZOHOCreator.postURLXML(navigationListURL.getUrl(), navigationListURL.getNvPair());
		return XMLParser.parseForNavigationListForApps(rootDocument);

	}

	public static ZCAppList getPersonalApplicationList() {
		URLPair appListURLPair = ZCURL.appListURL();
		Document rootDocument = ZOHOCreator.postURLXML(appListURLPair.getUrl(), appListURLPair.getNvPair());
		return new ZCAppList(ZCAppList.PERSONAL_APPS, XMLParser.parseForApplicationList(rootDocument));
	}

	public static ZCAppList getSharedApplicationList() {
		return getSharedApplicationList(null);
	}

	public static ZCAppList getSharedApplicationList(ZCSharedGroup sharedGroup) {
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
	
	public static ZCAppList getWorkspaceApplicationList(String workspaceAppOwner) {
		URLPair appListURLPair = ZCURL.workSpaceAppListURL(workspaceAppOwner); 
		Document rootDocument = ZOHOCreator.postURLXML(appListURLPair.getUrl(), appListURLPair.getNvPair());
		ZCAppList toReturn = new ZCAppList(ZCAppList.WORKSPACE_APPS, XMLParser.parseForApplicationList(rootDocument));
		toReturn.setWorkspaceAppOwner(workspaceAppOwner);
		return toReturn;
	}
	

	public static List<ZCSection> getSectionList(String appLinkName, String appOwner) {
		URLPair sectionListURLPair = ZCURL.sectionMetaURL(appLinkName, appOwner);
		Document rootDocument = ZOHOCreator.postURLXML(sectionListURLPair.getUrl(), sectionListURLPair.getNvPair());
		return XMLParser.parseForSectionList(rootDocument, appLinkName, appOwner);
	}

	public static List<ZCSection> getSectionList(ZCApplication zcApp) {
		return getSectionList(zcApp.getAppLinkName(), zcApp.getAppOwner());
	}

	public static ZCForm getForm(ZCComponent comp) {
		if(comp.getType().equals(ZCComponent.FORM)) {
			return getForm(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner(), null, null, ZCForm.FORM_ALONE, null, null, null, null, null);
		}
		return null;
	}



	private static ZCForm getForm(String appLinkName, String formLinkName, String appOwner, String viewLinkName, Long recordLinkId, int formType, String refAppLinkName, String refFormLinkName, String refFieldName, Date calSelectedStartDate,Date calSelectedEndDate) {
		URLPair formMetaURLPair = ZCURL.formMetaURL(appLinkName, formLinkName, appOwner, viewLinkName, recordLinkId, formType, refAppLinkName, refFormLinkName, refFieldName, calSelectedStartDate,calSelectedEndDate);
		Document rootDocument = ZOHOCreator.postURLXML(formMetaURLPair.getUrl(), formMetaURLPair.getNvPair());
		ZCForm toReturn = XMLParser.parseForForm(rootDocument, appLinkName, appOwner);
		toReturn.setFormType(formType);
		//System.out.println("form xml"+getString(rootDocument)+formMetaURLPair.getUrl());

		if(formType == ZCForm.VIEW_BULK_EDIT_FORM) {
			List<ZCButton> zcButtons = new ArrayList<ZCButton>();
			zcButtons.add(new ZCButton("Submit","Submit",ZCButtonType.SUBMIT));//No I18N
			toReturn.addButtons(zcButtons);
		}
		return toReturn;
	}


	static void callFormOnAddOnLoad(ZCForm zcForm) {

		URLPair formOnAddOnLoadURL = ZCURL.formOnLoad(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), zcForm.getAppOwner(), zcForm.getXMLStringForDeluge());
		Document rootDocument = ZOHOCreator.postURLXML(formOnAddOnLoadURL.getUrl(), formOnAddOnLoadURL.getNvPair());
		//	System.out.println("onload response " + getString(rootDocument));
		XMLParser.setResponseDocumentForFormEvents(rootDocument, zcForm);
	}

	static void callFormEditOnAddOnLoad(ZCForm zcForm) {

		URLPair formEditOnAddOnLoadURL = ZCURL.formEditOnLoad(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), zcForm.getAppOwner(), zcForm.getXMLStringForDeluge());
		Document rootDocument = ZOHOCreator.postURLXML(formEditOnAddOnLoadURL.getUrl(), formEditOnAddOnLoadURL.getNvPair());
		XMLParser.setResponseDocumentForFormEvents(rootDocument, zcForm);
	}

	private static void callDelugeEvents(ZCForm zcForm, URLPair urlPair) {
		Document rootDocument = ZOHOCreator.postURLXML(urlPair.getUrl(), urlPair.getNvPair());
		XMLParser.setResponseDocumentForFormEvents(rootDocument, zcForm);
	}

	static void callFieldOnUserForFormula(ZCForm zcForm, String fieldLinkName) {
		callDelugeEvents(zcForm, ZCURL.fieldOnUserForFormula(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), fieldLinkName, zcForm.getAppOwner(), zcForm.getXMLStringForDeluge()));
	}

	static void callFieldOnUser(ZCForm zcForm, String fieldLinkName) {
		callDelugeEvents(zcForm, ZCURL.fieldOnUser(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), fieldLinkName, zcForm.getAppOwner(), zcForm.getXMLStringForDeluge()));
	}

	static void callSubFormFieldOnUser(ZCForm zcForm, String subFormFieldLinkName, String fieldLinkName) {
		callDelugeEvents(zcForm, ZCURL.subFormOnUser(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, fieldLinkName, zcForm.getAppOwner(), zcForm.getXMLStringForDeluge()));
	}

	static void callSubFormAddRow(ZCForm zcForm, String subFormFieldLinkName) {
		callDelugeEvents(zcForm, ZCURL.subFormAddRow(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, zcForm.getAppOwner(), zcForm.getXMLStringForDeluge()));
	}

	static void callSubFormDeleteRow(ZCForm zcForm, String subFormFieldLinkName) {
		callDelugeEvents(zcForm, ZCURL.subFormDeleteRow(zcForm.getAppLinkName(), zcForm.getComponentLinkName(), subFormFieldLinkName, zcForm.getAppOwner(), zcForm.getXMLStringForDeluge()));
	}

	public static void callFormEvents(Document rootDocument, ZCForm form) {
		XMLParser.parseAndCallFormEvents(rootDocument, form);
		//System.out.println("rootdoc"+getString(rootDocument));
	}


	public static ZCView getView(ZCComponent comp) {
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
			return toReturn;
		}
		return null;
	}


	private static ZCHtmlView getHtmlView(ZCComponent comp) {
		if(comp.getType().equals(ZCComponent.PAGE)) {
			URLPair htmlViewURLPair = ZCURL.htmlViewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner());
			//params.addAll(viewURLPair.getNvPair());
			String htmlString = ZOHOCreator.postURL(htmlViewURLPair.getUrl(), htmlViewURLPair.getNvPair());
			return new ZCHtmlView( comp.getAppOwner(), comp.getAppLinkName(), comp.getType(), comp.getComponentName(), comp.getComponentLinkName(), htmlString);
			//return ZOHOCreator.postURLXML(viewURLPair.getUrl(), params);
		}		
		return null;
	}

	static void loadRecords(ZCView zcView) {  // Search, Filter & Group By

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


		//long t1 = System.currentTimeMillis();
		Document rootDocument = getViewXMLDocument(zcView, params);
		//long t2 = System.currentTimeMillis();
		XMLParser.parseAndAddRecords(rootDocument, zcView);
		//long t3 = System.currentTimeMillis();
		//System.out.println("network call " + (t2-t1));
		//System.out.println("Parsing " + (t3-t2));
	}

	private static Document getViewXMLDocument(ZCComponent comp, List<NameValuePair> params) {
		URLPair viewURLPair = ZCURL.viewURL(comp.getAppLinkName(), comp.getComponentLinkName(), comp.getAppOwner());
		params.addAll(viewURLPair.getNvPair());
		Document toReturn = ZOHOCreator.postURLXML(viewURLPair.getUrl(), params);		
		return toReturn;
	}

	static String postCustomAction(String appLinkName, String viewLinkName, String appOwner, long customActionId, List<Long> recordIDs) {
		URLPair customActionURLPair = ZCURL.customActionURL(appLinkName, viewLinkName, customActionId, appOwner, recordIDs);
		Document rootDocument = ZOHOCreator.postURLXML(customActionURLPair.getUrl(), customActionURLPair.getNvPair());
		NodeList nl = rootDocument.getChildNodes();
		for(int i=0; i<nl.getLength(); i++) {
			Node resultNode = nl.item(i);
			NodeList customActionNodes = resultNode.getChildNodes();
			for(int j =0;j<customActionNodes.getLength();j++){
				Node customActionNode = customActionNodes.item(j);
				if(customActionNode.getNodeName().equals("SuccessMessage")){
					String toReturnSuccessMessage = customActionNode.getFirstChild().getNodeValue();
					return toReturnSuccessMessage;
				}				
			}
		}
		//System.out.println(getString(rootDocument));
		return "";
	}

	static ZCResponse postXMLString(String appOwner, String xmlString, String action, List<NameValuePair> params, ZCForm form) {
		URLPair xmlWriteURLPair = ZCURL.xmlWriteURL(appOwner, xmlString);
		if(params == null) {
			params = new ArrayList<NameValuePair>();
		}
		params.addAll(xmlWriteURLPair.getNvPair());


		printURL(xmlWriteURLPair.getUrl(), params);
		Document rootDocument = ZOHOCreator.postURLXML(xmlWriteURLPair.getUrl(), params);
		//System.out.println("postXMLString " + getString(rootDocument));

		return parseResponseDocumentForXMLString(rootDocument, action, form);
	}


	static ZCResponse parseResponseDocumentForXMLString(Document rootDocument, String action, ZCForm form) {
		//getString(rootDocument);
		XPath xPath = XPathFactory.newInstance().newXPath();
		//read a string value
		ZCResponse toReturn = new ZCResponse();

		if(form != null) {
			try {
				String status = xPath.compile("/response/result/form/" + action + "/status").evaluate(rootDocument);//No I18N
				//System.out.println("postxml responsestring"+status);
				if(status.startsWith("Failure")) {
					String[] failureMessages = status.split(",");
					List<String> errorMessages = Arrays.asList(failureMessages).subList(1, failureMessages.length);
					List<ZCField> fields = form.getFields();
					for(int i=0; i<errorMessages.size(); i++) {
						String errorMessage = errorMessages.get(i);
						for(int j=0; j<fields.size(); j++) {
							if(fields.get(i).getDisplayName().contains(errorMessage)) {
								toReturn.addErrorMessage(fields.get(i), errorMessage);
							}
						}
					}
				} else  if(status.startsWith("Success")) { //No I18N
					xPath = XPathFactory.newInstance().newXPath();
					String idValue = xPath.compile("/response/result/form/add/values/field[@name=\"ID\"]").evaluate(rootDocument);//No I18N
					toReturn.setSuccessRecordID(Long.parseLong(idValue));
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return toReturn;
	}

	 static String postURL(String url, List<NameValuePair> params) {
		printURL(url, params);
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
						return null;
					}
				}
			};

			byte[] response = client.execute(request, handler);
			return new String(response);
		}
		catch (Exception ee)
		{
			////System.out.println("Exception occured = " + ee.getMessage());
		}
		return "";
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

	private static void printURL(String url, List<NameValuePair> params) {
		StringBuffer buff = new StringBuffer(url + "/");
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
		//System.out.println("Url "+buff.toString());

	}

	static Document postURLXML(String url, List<NameValuePair> params) {
		printURL(url, params);
		try
		{
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			if(params != null) {
				request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));//No I18N
			}
			request.setURI(new URI(url));
			HttpEntity entity = client.execute(request).getEntity();
			if(entity != null) {
				InputStream is = null;
				try {
					is = entity.getContent();
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document toReturn = builder.parse(is);
					return toReturn;
				}  finally {
					if(is != null) {
						is.close();
					}
				}
			}
		}
		catch (Exception ee)
		{
			ee.printStackTrace();
			//System.out.println("Exception occured = " + ee.getMessage());
		}
		return null;
		//throw new RuntimeException("Error Occured");
	}

	static String postFile(String urlParam, File fileToUpload, List<NameValuePair> paramsList) {

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
			mpEntity.addPart("filename", new StringBody(fileToUpload.getName()));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //No I18N
		mpEntity.addPart("file", cbFile); //No I18N
		httppost.setEntity(mpEntity);
		//System.out.println("executing request " + httppost.getRequestLine()); //No I18N
		try {
			//System.out.println("before httpresponse... ");
			HttpResponse httpResponse = httpclient.execute(httppost);
			//System.out.println("after httpresponse... ");
			//HttpEntity resEntity = httpResponse.getEntity();
			//System.out.println(EntityUtils.toString(resEntity));
			//resEntity.consumeContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();


		return "";		 
	}


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
