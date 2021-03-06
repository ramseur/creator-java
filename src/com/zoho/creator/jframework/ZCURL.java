// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
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

	private static List<NameValuePair> getAuthtokenAsParam(List<NameValuePair> params)
	{
		try {
			params.add(new BasicNameValuePair("authtoken", ZOHOCreator.getZohoUser().getAuthToken()));//No I18N
		} catch (ZCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

	private static List<NameValuePair> getDefaultParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		getAuthtokenAsParam(params);
		params.add(new BasicNameValuePair("scope", "creatorapi"));//No I18N
		return params;
	}

	private static List<NameValuePair> getSharedByParamsWithOwner(String appOwner)
	{
		if(appOwner == null) {
			throw new RuntimeException("App Owner Cannot be null"); //No I18N
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.addAll(getDefaultParams());
		return params;
	}


	private static List<NameValuePair> getParamsWithOwnerAndXMLString(String appOwner, String xmlString) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("xmlString", xmlString));//No I18N
		return params;
	}

	static URLPair getFileUploadURL(String filepath, String appOwner, String appLinkName, String viewLinkName) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("filepath","/"+filepath));//No I18N
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("viewLinkName", viewLinkName));//No I18N
		return new URLPair(serverURL() + "/DownloadFileFromMig.do", params);  //No I18N
	}

	static URLPair getImageURL(String filePath) {
		List<NameValuePair> params = getDefaultParams();
		return new URLPair(serverURL() + filePath, params);  
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
		return new URLPair(serverURL() + "/api/json/applications/", getDefaultParams()); //No I18N
	}

	static URLPair sharedAppListURL(Long groupid) {
		List<NameValuePair> params = getDefaultParams();
		if(groupid != null) {
			params.add(new BasicNameValuePair("groupid", groupid+""));//No I18N
		}
		return new URLPair(serverURL() + "/api/json/sharedapplications/", params); //No I18N
	}

	static URLPair workSpaceAppListURL(String workSpaceOwner) {
		if(workSpaceOwner == null) {
			throw new RuntimeException("Workspace Owner Cannot be null"); //No I18N
		}
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("workSpaceOwner", workSpaceOwner));//No I18N
		return new URLPair(serverURL() + "/api/json/workspaceapplications/", params); //No I18N
	}

	static URLPair sectionMetaURL(String appLinkName, String appOwner) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);

		params.add(new BasicNameValuePair("isCrm", "true"));//No I18N
		params.add(new BasicNameValuePair("isRules", "true"));//No I18N

		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/sections/",params); //No I18N
	}

	static URLPair viewURL(String appLinkName, String viewLinkName, String appOwner) {
		return new URLPair (serverURL() + "/api/mobile/xml/" + appLinkName + "/view/" + viewLinkName + "/", //No I18N
				getParamsWithOwner(appOwner)); //No I18N
	}

	static URLPair pivotViewURL(String appLinkName, String viewLinkName, String appOwner) {
		List<NameValuePair> params = getSharedByParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("applinkname", appLinkName));//No I18N
		params.add(new BasicNameValuePair("reportlinkname", viewLinkName));//No I18N
		return new URLPair (serverURL() + "/getReportsUrl.do" , params);//No I18N
	}

	static URLPair htmlViewURL(String appLinkName, String viewLinkName, String appOwner, List<NameValuePair> additionalParams ) {
		List<NameValuePair> params = getSharedByParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("viewLinkName", viewLinkName));//No I18N
		if(additionalParams != null) {
			params.addAll(additionalParams);
		}
		return new URLPair (serverURL() + "/showHtmlViewApi.do", params); //No I18N
	}

	static URLPair fileUploadURL(String appOwner) {
		return new URLPair(serverURL() + "/api/xml/fileupload/", getParamsWithOwner(appOwner));//No I18N
	}

	static URLPair bulkEditFormMetaURL(String appLinkName, String appOwner, String viewLinkName) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("metaData","complete"));//No I18N
		params.add(new BasicNameValuePair("zcRefValue","true"));//No I18N
		params.add(new BasicNameValuePair("formAccessType", String.valueOf(ZCForm.VIEW_BULK_EDIT_FORM)));//No I18N
		return new URLPair(serverURL() + "/api/"+appOwner+"/json/" + appLinkName + "/" +"view/"+ viewLinkName + "/bulkeditfields/", params);//No I18N
	}

	static URLPair editFormMetaURL(String appLinkName, String appOwner, String viewLinkName, Long recordLinkId) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("metaData","complete"));//No I18N
		params.add(new BasicNameValuePair("zcRefValue","true"));//No I18N
		return new URLPair(serverURL() + "/api/"+appOwner+"/json/" + appLinkName + "/view/"+ viewLinkName + "/record/" + recordLinkId + "/edit/", params);//No I18N
	}

	static URLPair formMetaURL(String appLinkName, String formLinkName, String appOwner, int formAccessType, List<NameValuePair> additionalParams) {
		List<NameValuePair> params = getParamsWithOwner(appOwner);
		params.add(new BasicNameValuePair("metaData","complete"));//No I18N
		params.add(new BasicNameValuePair("zcRefValue","true"));//No I18N
		params.add(new BasicNameValuePair("formAccessType", String.valueOf(formAccessType)));//No I18N
		if(additionalParams != null) {
			params.addAll(additionalParams);
		}
		return new URLPair(serverURL() + "/api/"+appOwner+"/json/" + appLinkName + "/" +"form/"+ formLinkName + "/fields/", params);//No I18N
	}

	static URLPair lookupChoices(String appLinkName, String formLinkName, String appOwner,String lookupFieldName, int startIndex, String searchString, String subformComponent,int formAccessType,List<NameValuePair> additionalParams) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("limit", 50 + ""));//No I18N
		params.add(new BasicNameValuePair("appendRows", "true"));//No I18N
		params.add(new BasicNameValuePair("startindex", startIndex + ""));//No I18N
		params.add(new BasicNameValuePair("zcRefValue", true+""));//No I18N
		if(searchString != null && !"".equals(searchString)) {
			params.add(new BasicNameValuePair("searchValue", searchString));//No I18N
		}
		if(subformComponent != null) {
			params.add(new BasicNameValuePair("subformcomponent", subformComponent));//No I18N
		}
		params.add(new BasicNameValuePair("zc_ownername",appOwner));//No I18N
		params.add(new BasicNameValuePair("formAccessType", String.valueOf(formAccessType)));//No I18N
		String urlValToAdd = "lookup";//No I18N
		params.addAll(additionalParams);
		return new URLPair(serverURL() + "/api/"+appOwner+"/xml/" + appLinkName + "/" +"form/"+ formLinkName +"/"+urlValToAdd+"/"+lookupFieldName+ "/options/", params);//No I18N
	}

	static URLPair crmLookupChoiceByID(String crmModuleType,String recordId)
	{
		List<NameValuePair> params = getAuthtokenAsParam(new ArrayList<NameValuePair>());
		params.add(new BasicNameValuePair("scope", "crmapi"));//No I18N
		params.add(new BasicNameValuePair("id", recordId));//No I18N
		if(ZOHOCreator.getCreatorURL().contains("localzoho"))
		{
			return new URLPair(ZOHOCreator.getPrefix() + "://" +"crm.localzoho.com"+"/crm/private/json/"+crmModuleType+"/getRecordById",params);
		}else
		{
			return new URLPair(ZOHOCreator.getPrefix() + "://" +"crm.zoho.com"+"/crm/private/json/"+crmModuleType+"/getRecordById",params);
		}
	}

	static URLPair crmLookupChoices(int startIndex,String crmModuleType,String searchString)
	{
		List<NameValuePair> params = getAuthtokenAsParam(new ArrayList<NameValuePair>());
		params.add(new BasicNameValuePair("scope", "crmapi"));//No I18N
		if(crmModuleType.equalsIgnoreCase("Users"))
		{
			params.add(new BasicNameValuePair("type","ActiveUsers"));//No I18N
			if(ZOHOCreator.getCreatorURL().contains("localzoho"))
			{
				return new URLPair(ZOHOCreator.getPrefix() + "://" +"crm.localzoho.com/crm/private/json/Users/getUsers",params);
			}else
			{
				return new URLPair(ZOHOCreator.getPrefix() + "://" +"crm.zoho.com/crm/private/json/Users/getUsers",params);
			}
		}else
		{
			params.add(new BasicNameValuePair("limit", 50 + ""));//No I18N
			params.add(new BasicNameValuePair("appendRows", "true"));//No I18N
			params.add(new BasicNameValuePair("fromIndex", startIndex + ""));//No I18N
			params.add(new BasicNameValuePair("toIndex", (startIndex+50)+""));//No I18N
			if(searchString !=null && !(searchString.equals("")))
			{
				params.add(new BasicNameValuePair("searchWord",searchString));//No I18N
				if(ZOHOCreator.getCreatorURL().contains("localzoho"))
				{
					return new URLPair(ZOHOCreator.getPrefix() + "://" +"crm.localzoho.com"+"/crm/private/json/"+crmModuleType+"/getGSearchRecords",params);
				}else
				{
					return new URLPair(ZOHOCreator.getPrefix() + "://" +"crm.zoho.com"+"/crm/private/json/"+crmModuleType+"/getGSearchRecords",params);
				}
			}else
			{
				if(ZOHOCreator.getCreatorURL().contains("localzoho"))
				{
					return new URLPair(ZOHOCreator.getPrefix() + "://" +"crm.localzoho.com"+"/crm/private/json/"+crmModuleType+"/getRecords",params);
				}else
				{
					return new URLPair(ZOHOCreator.getPrefix() + "://" +"crm.zoho.com"+"/crm/private/json/"+crmModuleType+"/getRecords",params);
				}
			}
		}

	}

	static URLPair formOnLoad(String appLinkName, String formLinkName, String appOwner, List<NameValuePair> additionalParams,int formAccessType) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("formLinkName", formLinkName));//No I18N
		params.add(new BasicNameValuePair("linkNameBased", "true"));//No I18N
		params.add(new BasicNameValuePair("formAccessType",String.valueOf(formAccessType)));//No I18N
		params.addAll(additionalParams);
		return new URLPair(serverURL() + "/generateJSAPI.do" , params);//No I18N
	}

	static URLPair formEditOnLoad(String appLinkName, String formLinkName, String appOwner,List<NameValuePair> additionalparams,Long recordLinkId,int formAccessType) {
		List<NameValuePair> params=getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("formLinkName", formLinkName));//No I18N
		params.add(new BasicNameValuePair("linkNameBased", "true"));//No I18N
		params.add(new BasicNameValuePair("recordLinkID",String.valueOf(recordLinkId)));//No I18N
		params.add(new BasicNameValuePair("recType",String.valueOf(formAccessType)));//No I18N
		params.add(new BasicNameValuePair("formAccessType",String.valueOf(formAccessType)));//No I18N
		params.addAll(additionalparams);
		return new URLPair(serverURL() + "/generateJSAPI.do" , params);//No I18N
	}

	static URLPair subFormOnUser(String appLinkName, String formLinkName, String subFormFieldLinkName, String fieldLinkName, String appOwner, List<NameValuePair> params,boolean isFormula,int entryPosition,long id) {
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("formLinkName", formLinkName));//No I18N
		params.add(new BasicNameValuePair("linkNameBased", "true"));//No I18N
		params.add(new BasicNameValuePair("fieldName", fieldLinkName));//No I18N
		params.add(new BasicNameValuePair("subformFieldName", subFormFieldLinkName));//No I18N
		if(id==-1l)
		{
			params.add(new BasicNameValuePair("rowseqid","t::row_"+entryPosition));//No I18N
		}
		else
		{
			params.add(new BasicNameValuePair("rowseqid",id+"_"+entryPosition));//No I18N
		}
		if(isFormula)
		{
			params.add(new BasicNameValuePair("isFormula", "true"));//No I18N
			return new URLPair(serverURL() + "/calculateFormulaAPI.do" , params);//No I18N
		}
		else
		{
			return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
		}
	}

	static URLPair subFormDeleteRow(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, List<NameValuePair> params,long id,int position) {
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("formLinkName", formLinkName));//No I18N
		params.add(new BasicNameValuePair("fieldName", fieldLinkName));//No I18N
		params.add(new BasicNameValuePair("linkNameBased", "true"));//No I18N
		params.add(new BasicNameValuePair("rowactiontype","ondeleterow"));//No I18N
		if(id==-1l)
		{
			params.add(new BasicNameValuePair("rowseqid","t::row_"+position));//No I18N
		}
		else
		{
			params.add(new BasicNameValuePair("rowseqid",id+"_"+position));//No I18N
		}
		return new URLPair(serverURL() + "/generateJSAPI.do" , params);//No I18N
	}

	static URLPair fieldOnUser(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, List<NameValuePair> params, boolean isFormula,int formType) {
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("formLinkName", formLinkName));//No I18N
		params.add(new BasicNameValuePair("fieldName", fieldLinkName));//No I18N
		params.add(new BasicNameValuePair("linkNameBased", "true"));//No I18N
		params.add(new BasicNameValuePair("formAccessType",formType+""));//No I18N
		if(isFormula) {
			params.add(new BasicNameValuePair("isFormula", "true"));//No I18N
		}
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
	}



	static URLPair buttonOnClick(String appLinkName, String formLinkName, String buttonName, String appOwner,List<NameValuePair> additionalParams) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("formLinkName", formLinkName));//No I18N
		params.add(new BasicNameValuePair("linkNameBased", "true"));//No I18N
		params.add(new BasicNameValuePair("buttonName",buttonName));//No I18N
		params.addAll(additionalParams);
		return new URLPair(serverURL() + "/generateJSAPI.do" , params); //No I18N
	}

	static URLPair subFormAddRow(String appLinkName, String formLinkName, String fieldLinkName, String appOwner, List<NameValuePair> params,int rowPosition) {
		params.addAll(getDefaultParams());
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		params.add(new BasicNameValuePair("appLinkName", appLinkName));//No I18N
		params.add(new BasicNameValuePair("formLinkName", formLinkName));//No I18N
		params.add(new BasicNameValuePair("fieldName", fieldLinkName));//No I18N
		params.add(new BasicNameValuePair("linkNameBased", "true"));//No I18N
		params.add(new BasicNameValuePair("rowactiontype","onaddrow"));//No I18N
		params.add(new BasicNameValuePair("rowseqid","t::row_"+  rowPosition));//No I18N
		params.add(new BasicNameValuePair("SF("+fieldLinkName+").FD(t::row_"+rowPosition+").SV(record::status)","added"));//No I18N
		return new URLPair(serverURL() + "/generateJSAPI.do" , params);//No I18N
	}

	static URLPair recordCount(String appOwner, String appLinkName, String viewLinkName) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("sharedBy", appOwner));//No I18N
		return new URLPair(serverURL() + "/api/mobile/xml/" + appLinkName + "/recordcount/" + viewLinkName, params);//No I18N
	}

	static URLPair getLoginUrl() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("scope", "creatorapi"));//No I18N		
		params.add(new BasicNameValuePair("hide_signup", "true"));//No I18N
		params.add(new BasicNameValuePair("hide_remember", "true"));//No I18N
		params.add(new BasicNameValuePair("scopes", ZOHOCreator.getServiceName() + "/creatorapi,ZohoContacts/photoapi,ZohoCRM/crmapi"));//No I18N
		params.add(new BasicNameValuePair("appname", ZOHOCreator.getAuthDescription()));//No I18N
		params.add(new BasicNameValuePair("serviceurl", serverURL()));//No I18N
		return new URLPair("https://" + ZOHOCreator.getAccountsURL() + "/login", params); //No I18N
	}

	static URLPair getPortalLoginUrl() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("_sh", "false"));//No I18N		
		params.add(new BasicNameValuePair("hideidp", "true"));//No I18N
		params.add(new BasicNameValuePair("portal", ZOHOCreator.getPortalValue()));//No I18N
		params.add(new BasicNameValuePair("client_portal", "true"));//No I18N
		params.add(new BasicNameValuePair("scopes", ZOHOCreator.getServiceName() + "/creatorapi,ZohoContacts/photoapi"));//No I18N
		params.add(new BasicNameValuePair("servicename", ZOHOCreator.getServiceName()));//No I18N
		params.add(new BasicNameValuePair("appname", ZOHOCreator.getAuthDescription()));//No I18N
		params.add(new BasicNameValuePair("serviceurl", serverURL()));//No I18N
		params.add(new BasicNameValuePair("hide_signup", "true"));//No I18N
		return new URLPair("https://" + ZOHOCreator.getAccountsURL() + "/accounts/signin", params);//No I18N
	}


	static URLPair getCreatorUpgradeUrl() {
		//https://accounts.zoho.com/login?servicename=ZohoCreator&serviceurl=https://creator.zoho.com/dashboard?showpage=upgradeplan&hide_signup=true&LOGIN_ID="+ZOHOCreator.getZohoUser().getEmailAddresses().get(0)
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("servicename", ZOHOCreator.getServiceName()));//No I18N		
		params.add(new BasicNameValuePair("serviceurl", serverURL()+"/dashboard?showpage=upgradeplan&from=Android"));//No I18N
		params.add(new BasicNameValuePair("hide_signup", "true"));//No I18N
		return new URLPair("https://" + ZOHOCreator.getAccountsURL() + "/login", params);//No I18N
	}

	// 		signInUrl = "https://accounts.zoho.com/login?hide_signup=true&hide_remember=true&scopes=ZohoCreator/creatorapi&appname=ZohoCreator&serviceurl=https://creator.zoho.com";  //No I18N


	static URLPair deleteAuthToken(String authToken) {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("AUTHTOKEN", authToken));//No I18N
		return new URLPair("https://" + ZOHOCreator.getAccountsURL() + "/apiauthtoken/delete", params);//No I18N
	}

	static URLPair editAccessURL(String appOwner,String appLinkName)
	{
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("emailid","edit@zohocreator.com"));//No I18N
		return new URLPair(serverURL() + "/api/"+appOwner+"/xml/" + appLinkName + "/developer/add",params);	//No I18N
	}

	private static String serverURL() {
		return ZOHOCreator.getPrefix() + "://" + ZOHOCreator.getCreatorURL();//No I18N
	}


	static URLPair userPersonalInfoURL() {
		return new URLPair(serverURL() + "/api/xml/user/", getDefaultParams());//No I18N
	}

	static URLPair getURLForPersonalPhoto() {
		List<NameValuePair> params = getDefaultParams();
		params.add(new BasicNameValuePair("fs", "thumb"));//No I18N
		params.add(new BasicNameValuePair("t", "user"));//No I18N
		try {
			params.add(new BasicNameValuePair("ID", ZOHOCreator.getZohoUser().getId()));//No I18N
		} catch (ZCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new URLPair("https://contacts.zoho.com/file/download", params);//No I18N
	}

	static URLPair getAuthTokenURL(String userName, String password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("scopes", ZOHOCreator.getServiceName() + "/creatorapi,ZohoContacts/photoapi"));//No I18N
		params.add(new BasicNameValuePair("EMAIL_ID", userName));//No I18N
		params.add(new BasicNameValuePair("PASSWORD", password));//No I18N
		return new URLPair("https://" + ZOHOCreator.getAccountsURL() + "/apiauthtoken/nb/create", params);//No I18N
	}

	public static URLPair externalFieldTokenURL(String accessToken) {
		// TODO Auto-generated method stub
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));//No I18N
		params.add(new BasicNameValuePair("code", accessToken));//No I18N
		params.add(new BasicNameValuePair("client_id", ""));//No I18N
		params.add(new BasicNameValuePair("client_secret", "4406449787548088942"));//No I18N
		params.add(new BasicNameValuePair("redirect_uri", "https://creator.zoho.com"));//No I18N
		return new URLPair("https://login.salesforce.com/services/oauth2/token", params);//No I18N
	}


	static String getURLString(URLPair urlPair) {
		StringBuffer buff = new StringBuffer();
		buff.append(urlPair.getUrl());
		buff.append("?");
		List<NameValuePair> nvPair = urlPair.getNvPair();
		for(int i=0; i<nvPair.size(); i++) {
			if(i != 0) {
				buff.append("&");
			}
			buff.append(nvPair.get(i).getName());
			buff.append("=");
			buff.append(nvPair.get(i).getValue());
		}
		return buff.toString();
	}




}
