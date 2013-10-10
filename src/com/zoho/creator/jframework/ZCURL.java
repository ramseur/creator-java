// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;



public class ZCURL {
	
	private static List<NameValuePair> getParamsWithOwner(String appOwner) {
		if(appOwner == null) {
			throw new RuntimeException("App Owner Cannot be null"); //No I18N
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("zc_ownername", appOwner));//No I18N
		params.addAll(getDefaultParams());
		return params;
	}
	
	private static List<NameValuePair> getDefaultParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("authtoken", ZOHOCreator.getZohoUser().getAuthToken()));//No I18N
		params.add(new BasicNameValuePair("scope", "creatorapi"));//No I18N
		return params;
	}
	
	private static List<NameValuePair> getParamsWithOwnerAndXMLString(String appOwner, String xmlString) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("xmlString", xmlString));//No I18N
		return params;
	}
	
	public static String getImageURL(String filepath, String appOwner, String appLinkName, String viewLinkName) {
		return serverURL() + "/DownloadFileFromMig.do?filepath=/" + filepath + "&sharedBy=" + appOwner + "&appLinkName=" + appLinkName + "&viewLinkName=" + viewLinkName + "&authtoken=" + ZOHOCreator.getZohoUser().getAuthToken() + "&scope=creatorapi";  //No I18N
	}

	
	static URLPair customActionURL(String appLinkName, String viewLinkName, long customActionId, String appOwner, List<Long> recordIDs) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		for(int i=0; i<recordIDs.size(); i++) {
			Long recId = recordIDs.get(i);
			params.add(new BasicNameValuePair("functiongroup", recId+"")); //No I18N
		}
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + viewLinkName + "/OnExecute/" + customActionId + "/", params); //No I18N
	}
	
	static URLPair xmlWriteURL(String appOwner, String xmlString) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("XMLString", xmlString));//No I18N
		return new URLPair(serverURL() + "/api/xml/write", params); //No I18N
	}
	
	static URLPair navigationListURL() {
		return new URLPair(serverURL() + "/api/xml/allappscategory/", getDefaultParams()); //No I18N
	}
	
	static URLPair appListURL() {
		return new URLPair(serverURL() + "/api/xml/applications/", getDefaultParams()); //No I18N
	}
	
	static URLPair sharedAppListURL(Long groupid) {
		List<NameValuePair> params = getDefaultParams();
		if(groupid != null) {
			params.add(new BasicNameValuePair("groupid", groupid+""));//No I18N
		}
		return new URLPair(serverURL() + "/api/xml/sharedapplications/", params); //No I18N
	}
	
	static URLPair workSpaceAppListURL(String workSpaceOwner) {
		if(workSpaceOwner == null) {
			throw new RuntimeException("Workspace Owner Cannot be null"); //No I18N
		}
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("workSpaceOwner", workSpaceOwner));//No I18N
		return new URLPair(serverURL() + "/api/xml/workspaceapplications/", params); //No I18N
	}

	static URLPair sectionMetaURL(String appLinkName, String appOwner) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/sections/", getParamsWithOwner(appOwner)); //No I18N
	}

	static URLPair viewURL(String appLinkName, String viewLinkName, String appOwner) {
		return new URLPair (serverURL() + "/api/mobile/xml/" + appLinkName + "/view/" + viewLinkName + "/", //No I18N
				getParamsWithOwner(appOwner)); //No I18N
	}

	static URLPair htmlViewURL(String appLinkName, String viewLinkName, String appOwner) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("viewLinkName", viewLinkName));//No I18N
		return new URLPair (serverURL() + "/showHtmlViewApi.do", params); //No I18N
	}
	
	static URLPair fileUploadURL(String appOwner) {
		return new URLPair(serverURL() + "/api/xml/fileupload/", getParamsWithOwner(appOwner));//No I18N

	}

	static URLPair formMetaURL(String appLinkName, String formLinkName, String appOwner, String viewLinkName, Long recordLinkId, int formType, String refAppLinkName, String refFormLinkName, String refFieldName, Date calSelectedStartDate,Date calSelectedEndDate) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("complete", "true"));//No I18N
		params.add(new BasicNameValuePair("combinedLookup", "true"));//No I18N
		if(viewLinkName != null && recordLinkId != null) {
			params.add(new BasicNameValuePair("viewLinkName", viewLinkName));//No I18N
			if(recordLinkId != null) {
				params.add(new BasicNameValuePair("recLinkID", String.valueOf(recordLinkId)));//No I18N
			}
		}
		if(refAppLinkName != null && refFormLinkName != null && refFieldName != null) {
			params.add(new BasicNameValuePair("refAppLinkName", refAppLinkName));//No I18N
			params.add(new BasicNameValuePair("refFormLinkName", refFormLinkName));//No I18N
			params.add(new BasicNameValuePair("refFieldName", refFieldName));//No I18N
		}
		if(calSelectedStartDate != null) {			
			//System.out.println(calSelectedStartDate+"start"+calSelectedEndDate+"end");
			Calendar startDateCal = Calendar.getInstance();
			startDateCal.setTime(calSelectedStartDate);
			Calendar endDateCal = Calendar.getInstance();
			endDateCal.setTime(calSelectedEndDate);
			params.add(new BasicNameValuePair("dateJsonObject", "{\"startDate\":{\"day\":" + startDateCal.get(Calendar.DAY_OF_MONTH) + ",\"month\":" + startDateCal.get(Calendar.MONTH) + ",\"year\":" + startDateCal.get(Calendar.YEAR) + ",\"hours\":"+ startDateCal.get(Calendar.HOUR_OF_DAY)  +",\"minutes\":"+ startDateCal.get(Calendar.MINUTE) +",\"seconds\":" + startDateCal.get(Calendar.SECOND) +"}," +  //No I18N
																"\"endDate\":{\"day\":" +endDateCal.get(Calendar.DAY_OF_MONTH) + ",\"month\":" + endDateCal.get(Calendar.MONTH) + ",\"year\":" + endDateCal.get(Calendar.YEAR) + ",\"hours\":"+ endDateCal.get(Calendar.HOUR_OF_DAY)  +",\"minutes\":"+ endDateCal.get(Calendar.MINUTE) +",\"seconds\":" + endDateCal.get(Calendar.SECOND) + "}};"));//No I18N
		}
		params.add(new BasicNameValuePair("formAccessType", String.valueOf(formType)));//No I18N
		
		return new URLPair(serverURL() + "/api/xml/" + appLinkName + "/" + formLinkName + "/fields/", params);//No I18N

	}
	
	

	static URLPair formOnLoad(String appLinkName, String formLinkName, String appOwner, String xmlString) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnLoad/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair formEditOnLoad(String appLinkName, String formLinkName, String appOwner, String xmlString) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnEditLoad/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair subFormOnUser(String appLinkName, String formLinkName, String subFormFieldLinkName, String fieldLinkName, String appOwner, String xmlString) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/SubOnUser/" + subFormFieldLinkName + "/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair fieldOnUser(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, String xmlString) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnUser/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair fieldOnUserForFormula(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, String xmlString) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnUserFormula/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}
	static URLPair buttonOnClick(String appLinkName, String formLinkName, String buttonName, String appOwner, String xmlString) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnClick/" + buttonName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair subFormAddRow(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, String xmlString) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnAddRow/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}

	static URLPair subFormDeleteRow(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, String xmlString) {
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/" + formLinkName + "/OnDeleteRow/" + fieldLinkName + "/", getParamsWithOwnerAndXMLString(appOwner, xmlString)); //No I18N
	}
	
	static URLPair recordCount(String appOwner, String appLinkName, String viewLinkName) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/recordcount/" + viewLinkName, params); //No I18N
	}

	static String getAuthTokenURL(String userName, String password) {
		return "https://" + ZOHOCreator.getAccountsURL() + "/apiauthtoken/nb/create?SCOPE=" + ZOHOCreator.getServiceName() + "/creatorapi&EMAIL_ID=" + userName + "&PASSWORD=" + password; //No I18N
	}

	static String deleteAuthToken(String authToken) {
		return "https://" + ZOHOCreator.getAccountsURL() + "/apiauthtoken/delete?AUTHTOKEN=" + authToken; //No I18N
	}

	private static String serverURL() {
		return "https://" + ZOHOCreator.getCreatorURL(); //"https://icreator.localzoho.com"; //No I18N
	}

	public static URLPair licenseCheckURL() {
		return new URLPair(serverURL() + "/api/xml/license/mobile/", getDefaultParams()); //No I18N
	}
	
}
