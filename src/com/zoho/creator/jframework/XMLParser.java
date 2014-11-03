// $Id$
package com.zoho.creator.jframework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


class XMLParser {

	private static ResourceBundle resourceString = ResourceBundle.getBundle("ResourceString", Locale.getDefault());

	static String getStringValue(Node node, String defaultValue) {
		if(node != null && node.getFirstChild() != null) {
			return node.getFirstChild().getNodeValue();
		}
		return defaultValue;
	}

	static int getIntValue(Node node, int defaultValue) {
		if(node != null && node.getFirstChild() != null) {
			return Integer.parseInt(node.getFirstChild().getNodeValue());
		}

		return defaultValue;
	}

	private static long getLongValue(Node node, long defaultValue) {
		if(node != null && node.getFirstChild() != null) {
			return Long.parseLong(node.getFirstChild().getNodeValue());
		}
		return defaultValue;
	}

	static String parsePivotViewURL(Document document)
	{
		String pivotViewUrl = "";
		NodeList nl = document.getChildNodes();
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("response")) {

				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0;j<responseNodes.getLength();j++)
				{
					Node resultNode = responseNodes.item(j);

					if(resultNode.getNodeName().equals("result")) {

						NodeList resultNodes = resultNode.getChildNodes();

						for(int k=0;k<resultNodes.getLength();k++)
						{

							Node reportsUrl = resultNodes.item(k);

							if(reportsUrl.getNodeName().equals("reportsUrl")) {

								pivotViewUrl = getStringValue(reportsUrl, pivotViewUrl);
							}
						}
					}
				}
			}
		}
		return pivotViewUrl;
	}

	private static boolean getBooleanValue(Node node, boolean defaultValue) {
		if(node != null && node.getFirstChild() != null) {
			return Boolean.valueOf(node.getFirstChild().getNodeValue());
		}
		return defaultValue;
	}

	private static Date getDateValue(String dateString, String dateFormat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			return formatter.parse(dateString);
		} catch (java.text.ParseException e) {
			//e.printStackTrace();
		}
		return null;
	}

	private static Date getDateValue(Node node, Date defaultValue, String dateFormat) {
		if(node != null && node.getFirstChild() != null) {
			String dateString = node.getFirstChild().getNodeValue();
			Date toReturn  = getDateValue(dateString, dateFormat);
			if(toReturn != null) {
				return toReturn;
			}
		}
		return defaultValue;
	}



	static ZCNavList parseForNavigationListForApps(Document rootDocument) {

		List<ZCSharedGroup> sharedWithGroupList = new ArrayList<ZCSharedGroup>();
		List<String> sharedWithWorkSpaceList = new ArrayList<String>();
		NodeList nl = rootDocument.getChildNodes();
		for(int i=0; i<nl.getLength(); i++) {
			Node resultNode = nl.item(i);
			if(resultNode.getNodeName().equals("result")) {
				NodeList resultNodes = resultNode.getChildNodes();
				for(int j=0; j<resultNodes.getLength(); j++) {
					Node resultNodeChild = resultNodes.item(j);
					if(resultNodeChild.getNodeName().equals("allworkspaces")) {
						NodeList workspacesNodes = resultNodeChild.getChildNodes(); 
						for(int k=0; k<workspacesNodes.getLength(); k++) {
							Node workspacesNode = workspacesNodes.item(k);
							if(workspacesNode.getNodeName().equals("workspaces")) {
								NodeList workspaceNodes = workspacesNode.getChildNodes();
								for(int l=0; l<workspaceNodes.getLength(); l++) {
									Node workspaceNode = workspaceNodes.item(l);
									if(workspaceNode.getNodeName().equals("workspace")) {
										NodeList workspaceownerNodes = workspaceNode.getChildNodes();
										for(int m=0; m<workspaceownerNodes.getLength(); m++) {
											Node workspaceownerNode = workspaceownerNodes.item(m);
											if(workspaceownerNode.getNodeName().equals("workspaceowner")) {
												String workspaceowner = getStringValue(workspaceownerNode, null);
												sharedWithWorkSpaceList.add(workspaceowner);
											}
										}
									}
								}
							}
						}
					} else if(resultNodeChild.getNodeName().equals("allsharedcategory")) {
						NodeList groupsNodes = resultNodeChild.getChildNodes();
						for(int k=0; k<groupsNodes.getLength(); k++) {
							Node groupsNode = groupsNodes.item(k);
							if(groupsNode.getNodeName().equals("groups")) {
								NodeList groupNodes = groupsNode.getChildNodes();
								for(int l=0; l<groupNodes.getLength(); l++) {
									Node groupNode = groupNodes.item(l);
									if(groupNode.getNodeName().equals("group")) {
										NodeList groupNodeChildNodes = groupNode.getChildNodes();
										String groupName = null;
										Long groupId = -1L;
										for(int m=0; m<groupNodeChildNodes.getLength(); m++) {
											Node groupNodeChildNode = groupNodeChildNodes.item(m);
											if(groupNodeChildNode.getNodeName().equals("groupname")) {
												groupName = getStringValue(groupNodeChildNode, groupName);
											} else if(groupNodeChildNode.getNodeName().equals("groupid")) {
												groupId = getLongValue(groupNodeChildNode, groupId);
											}
										}
										sharedWithGroupList.add(new ZCSharedGroup(groupName, groupId));
									}
								}
							}
						}
					}
				}
			}
		}

		return new ZCNavList(sharedWithGroupList, sharedWithWorkSpaceList);
	}

	//	static ZCNavList parseForNavigationListForAppsJson(String response) {
	//		List<ZCSharedGroup> sharedWithGroupList = new ArrayList<ZCSharedGroup>();
	//		List<String> sharedWithWorkSpaceList = new ArrayList<String>();
	//		JSONArray jArray;
	//		try {
	//			jArray = new JSONArray(response);
	//		} catch (JSONException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		for (int i=0; i < jArray.length(); i++) {
	//		
	//		}
	//		return new ZCNavList(sharedWithGroupList, sharedWithWorkSpaceList);
	//	}

	static List<ZCApplication> parseForApplicationList(Document rootDocument) throws ZCException {
		List<ZCApplication> toReturn = new ArrayList<ZCApplication>();
		NodeList nl = rootDocument.getChildNodes();
		String remainingDays = "";
		boolean licenceEnabled = true;
		String appOwner = "";
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("response")) {
				NodeList responseNodes = responseNode.getChildNodes();

				for(int j=0; j<responseNodes.getLength(); j++) {
					Node resultNode = responseNodes.item(j);
					if(resultNode.getNodeName().equals("result")) {
						NodeList resultNodes = resultNode.getChildNodes();						
						for(int k=0; k<resultNodes.getLength(); k++) {
							Node resultNodeChild = resultNodes.item(k);
							if(resultNodeChild.getNodeName().equals("application_owner")) {
								appOwner = getStringValue(resultNodeChild, ""); //No I18N
							} else if(resultNodeChild.getNodeName().equals("application_list")) {
								NodeList appListNodes = resultNodeChild.getChildNodes();
								for(int l=0; l<appListNodes.getLength(); l++) {
									Node applicationsNode = appListNodes.item(l);
									if(applicationsNode.getNodeName().equals("applications")) {
										NodeList applicationsNodes = applicationsNode.getChildNodes();
										for(int m=0; m<applicationsNodes.getLength(); m++) {
											Node applicationNode = applicationsNodes.item(m);
											if(applicationNode.getNodeName().equals("application")) {
												NodeList applicationNodes = applicationNode.getChildNodes();
												String appName = null;
												String linkName = null;
												Date createdTime = null;
												boolean isPrivate = true;
												for(int n=0; n<applicationNodes.getLength(); n++) {
													Node appPropertyNode = applicationNodes.item(n);
													String nodeName = appPropertyNode.getNodeName();
													if(nodeName.equals("application_name")) {
														appName = getStringValue(appPropertyNode, appName); //No I18N
													} else if(nodeName.equals("link_name")) {
														linkName = getStringValue(appPropertyNode, linkName); //No I18N
													} else if(nodeName.equals("access")) {
														isPrivate = getStringValue(appPropertyNode, "private").equals("private"); //No I18N
													} else if(nodeName.equals("created_time")) {
														// 2013-04-01 14:28:58.0
														createdTime = getDateValue(appPropertyNode, createdTime, "yyyy-MM-dd HH:mm:ss.S"); //No I18N
													} else if(nodeName.equals("sharedBy")) {
														appOwner = getStringValue(appPropertyNode, appOwner);
													}
												}
												ZCApplication zcApp = new ZCApplication(appOwner, appName, linkName, isPrivate, createdTime);
												toReturn.add(zcApp);
											}
										}
									}
								}
							} else if(resultNodeChild.getNodeName().equals("license_enabled")) {
								licenceEnabled = getBooleanValue(resultNodeChild, false);
							} else if(resultNodeChild.getNodeName().equals("evaluationDays")) {
								remainingDays = getStringValue(resultNodeChild, "");	
							}
							ZOHOCreator.setUserProperty("evaluationDays", remainingDays);
						}
					}
				}
			}
		}

		if(!licenceEnabled) {
			throw new ZCException(resourceString.getString("please_subscribe_to_professional_edition_and_get_access"), ZCException.LICENCE_ERROR); //No I18N
		}
		return toReturn;
	}

	static List<ZCSection> parseForSectionList(Document rootDocument, String appLinkName, String appOwner) throws ZCException {
		List<ZCSection> toReturn = new ArrayList<ZCSection>();
		NodeList nl = rootDocument.getChildNodes();
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("Response")) {

				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0; j<responseNodes.getLength(); j++) {
					Node responseNodeChild = responseNodes.item(j);
					if(responseNodeChild.getNodeName().equals("error")) {
						throw new ZCException(getStringValue(responseNodeChild, "error"), ZCException.ERROR_OCCURED,"" );
					}
					else if(responseNodeChild.getNodeName().equals("Sections")) {

						NodeList sectionsNodes = responseNodeChild.getChildNodes();

						for(int k=0; k<sectionsNodes.getLength(); k++) {
							Node sectionNode = sectionsNodes.item(k);
							if(sectionNode.getNodeName().equals("Section")) {
								NodeList sectionNodes = sectionNode.getChildNodes();
								String sectionName = null;
								boolean isHidden = false;
								String sectionLinkName = null;
								long sectionID = -1L;
								Date modifiedTime = null;
								List<ZCComponent> comps = new ArrayList<ZCComponent>();
								for(int l=0; l<sectionNodes.getLength(); l++) {
									Node sectionNodeChild = sectionNodes.item(l);
									String nodeName = sectionNodeChild.getNodeName();
									if(nodeName.equals("Display_Name")) {
										sectionName = getStringValue(sectionNodeChild, ""); //No I18N
									} else if(nodeName.equals("Is_Hidden")) {
										isHidden = getBooleanValue(sectionNodeChild, isHidden); //No I18N
									} else if(nodeName.equals("Link_Name")) {
										sectionLinkName = getStringValue(sectionNodeChild, ""); //No I18N
									} else if(nodeName.equals("Section_ID")) {
										sectionID = getLongValue(sectionNodeChild, sectionID); //No I18N
									} else if(nodeName.equals("Modified_Time")) {
										modifiedTime = getDateValue(sectionNodeChild, modifiedTime, "yyyy-MM-dd HH:mm:ss.S"); //No I18N
										// 2013-04-01 14:28:58.0
									} else if(nodeName.equals("Components")) {
										NodeList ComponentsNodes = sectionNodeChild.getChildNodes();
										for(int m=0; m<ComponentsNodes.getLength(); m++) {
											Node componentNode = ComponentsNodes.item(m);
											if(componentNode.getNodeName().equals("Component")) {
												NamedNodeMap attrMap = componentNode.getAttributes();
												Node typeNode = attrMap.getNamedItem("type");//No I18N
												String type = typeNode.getNodeValue();
												NodeList componentNodes = componentNode.getChildNodes();
												String componentName = null;
												String componentLinkName = null;
												int sequenceNumber = -1;
												long componentID = -1L;
												for(int n=0; n<componentNodes.getLength(); n++) {
													Node componentNodeChild = componentNodes.item(n);
													String componentNodeName = componentNodeChild.getNodeName();
													String nodeValue = getStringValue(componentNodeChild, "");
													if(componentNodeName.equals("Display_Name")) {
														componentName = nodeValue;
													} else if(componentNodeName.equals("Link_Name")) {
														componentLinkName = nodeValue;
													} else if(componentNodeName.equals("Secquence")) {
														sequenceNumber = Integer.parseInt(nodeValue);
													} else if(componentNodeName.equals("Component_ID")) {
														componentID = Long.valueOf(nodeValue);
													}
												}

												if(ZCComponent.isCompTypeSupported(type))
												{

													comps.add(new ZCComponent (appOwner, appLinkName, type, componentName, componentLinkName, sequenceNumber));
												}
											}
										}
									}
								}
								ZCSection zcSection = new ZCSection(appOwner, appLinkName, sectionName,  sectionLinkName,  isHidden,  sectionID,  modifiedTime);
								zcSection.addComponents(comps);
								toReturn.add(zcSection);
							}
						}
					}
				}
			} 
			else if(responseNode.getNodeName().equals("license_enabled")) { //No I18N
				if(!getBooleanValue(responseNode, false)) {
					throw new ZCException(resourceString.getString("please_subscribe_to_professional_edition_and_get_access"), ZCException.LICENCE_ERROR); //No I18N
				}
			} else if(responseNode.getNodeName().equals("evaluationDays")) { //No I18N
				ZOHOCreator.setUserProperty("evaluationDays", getStringValue(responseNode, ""));
			} 
		}
		return toReturn;		
	}

	static ZCForm parseForForm(Document rootDocument, String appLinkName, String appOwner,String queryString,boolean isEditForm) throws ZCException {
		NodeList nl = rootDocument.getChildNodes();
		ZCForm toReturn = null;
		Hashtable<String,String> queryStringTable = new Hashtable<String, String>();
		//		if(queryString!=null)
		//		{
		//			String[] stringValues = queryString.split("&");
		//			for(int p=0;p<stringValues.length;p++)
		//			{
		//				String[] fieldNameAndValueString = stringValues[p].split("=");
		//				queryStringTable.put(fieldNameAndValueString[0],fieldNameAndValueString[1]);
		//			}
		//		}
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("errorlist"))
			{
				NodeList errorListNodes = responseNode.getChildNodes();
				for(int j=0; j<errorListNodes.getLength(); j++) {
					Node errorNode = errorListNodes.item(j);
					if(errorNode.getNodeName().equals("error"))
					{
						NodeList messageNodes = errorNode.getChildNodes();
						for(int k=0;k<messageNodes.getLength();k++)
						{
							Node messageNode = messageNodes.item(k);
							if(messageNode.getNodeName().equals("message"))
							{
								throw new ZCException(getStringValue(messageNode,""), ZCException.ERROR_OCCURED,"" );
							}
						}
					}
				}
			}
			else if(responseNode.getNodeName().equals("response")) {
				boolean hasAddOnLoad = false;
				boolean hasEditOnLoad = false;
				String successMessage = "";
				String dateFormat = "";
				List<ZCField> fields = new ArrayList<ZCField>();
				List<ZCButton> buttons = new ArrayList<ZCButton>();
				boolean isStateLess = false;
				int formLinkId = -1;

				String componentName = null;
				String componentLinkName = null; 
				int sequenceNumber = -1;
				Long componentID = -1L; 
				String openurlType = "";
				String openurlValue = "";

				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0; j<responseNodes.getLength(); j++) {
					Node responseNodeChild = responseNodes.item(j);
					if(responseNodeChild.getNodeName().equals("result"))
					{

						NodeList resultNodes = responseNodeChild.getChildNodes();
						for(int k=0;k<resultNodes.getLength();k++)
						{

							Node resultNodeChild = resultNodes.item(k);
							if(resultNodeChild.getNodeName().equalsIgnoreCase("hasAddOnLoad")) { 
								String nodeValue = getStringValue(resultNodeChild, "");
								hasAddOnLoad = Boolean.valueOf(nodeValue);
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("hasEditOnLoad")) { 
								String nodeValue = getStringValue(resultNodeChild, "");
								hasEditOnLoad = Boolean.valueOf(nodeValue);
							} 
							else if(resultNodeChild.getNodeName().equalsIgnoreCase("captcha")) { 
								throw new ZCException(resourceString.getString("captcha_enabled_forms_are_currently_not_supported"), ZCException.ERROR_OCCURED, "");
							}
							else if(resultNodeChild.getNodeName().equalsIgnoreCase("successMessage")) { 
								successMessage = getStringValue(resultNodeChild, "");
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("dateFormat")) {
								dateFormat = getStringValue(resultNodeChild,"");
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("errorlist")) {
								throw new ZCException(parseErrorMessage(resultNodeChild), ZCException.ACCESS_ERROR);
							} 
							else if(resultNodeChild.getNodeName().equalsIgnoreCase("type")) {
								int type = getIntValue(resultNodeChild, 1);
								if(type==2)
								{
									isStateLess = true;
								}
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("formLinkId")) {
								formLinkId = getIntValue(resultNodeChild, formLinkId);
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("DisplayName")) {
								componentName = getStringValue(resultNodeChild, "");
							}
							else if(resultNodeChild.getNodeName().equalsIgnoreCase("nexturl")) {
								NodeList openurlNodes = resultNodeChild.getChildNodes();
								for(int l=0;l<openurlNodes.getLength();l++)
								{
									Node openurlChildNode = openurlNodes.item(l);
									if(openurlChildNode.getNodeName().equals("value"))
									{
										openurlValue = getStringValue(openurlChildNode,openurlValue);
									}
									else if(openurlChildNode.getNodeName().equals("type"))
									{
										openurlType = getStringValue(openurlChildNode, openurlType);
									}
								}
							}
							else if(resultNodeChild.getNodeName().equalsIgnoreCase("labelname")) {
								componentLinkName = getStringValue(resultNodeChild, "");
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("formid")) {
								componentID = getLongValue(resultNodeChild, componentID);
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("Fields")) {
								NodeList fieldNodes = resultNodeChild.getChildNodes();
								for(int l=0; l<fieldNodes.getLength(); l++) {
									Node fieldNode = fieldNodes.item(l);
									if(fieldNode.getNodeName().equalsIgnoreCase("field")) {
										ZCField field = parseField(fieldNode,appLinkName,componentLinkName, appOwner, false,queryStringTable);
										if(field != null) {
											if(!isEditForm)
											{
												for(int m=0;m<field.getDefaultRows();m++)
												{
													field.addAndGetNewSubFormEntry();
												}
											}
											fields.add(field);
										}
									} 

								}
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("buttons")) {

								NodeList buttonNodes = resultNodeChild.getChildNodes();
								for(int m=0;m<buttonNodes.getLength();m++)
								{
									Node buttonNode = buttonNodes.item(m);
									if(buttonNode.getNodeName().equals("button"))
									{
										NodeList buttonPropetyNodes = buttonNode.getChildNodes();
										String buttonDisplayName = null;
										String buttonLinkName = null;
										ZCButtonType buttonType = null;
										int buttonSequenceNumber = -1;
										int actiontype = 1;
										boolean isOnClickExists = false;
										for(int l=0; l<buttonPropetyNodes.getLength(); l++) {
											Node buttonPropetyNode = buttonPropetyNodes.item(l);
											if(buttonPropetyNode.getNodeName().equalsIgnoreCase("labelname")) {
												buttonLinkName = getStringValue(buttonPropetyNode, buttonLinkName);
											} else if(buttonPropetyNode.getNodeName().equalsIgnoreCase("sequencenumber")) {
												buttonSequenceNumber = getIntValue(buttonPropetyNode, buttonSequenceNumber);
											} else if(buttonPropetyNode.getNodeName().equalsIgnoreCase("actiontype")) {
												actiontype = getIntValue(buttonPropetyNode, actiontype);
											} else if(buttonPropetyNode.getNodeName().equalsIgnoreCase("type")) {
												buttonType = ZCButtonType.getButtonType(getIntValue(buttonPropetyNode, 62));
											} else if(buttonPropetyNode.getNodeName().equalsIgnoreCase("displayname")) {
												buttonDisplayName = getStringValue(buttonPropetyNode, buttonDisplayName);
											} else if(buttonPropetyNode.getNodeName().equalsIgnoreCase("onclickexists")){
												isOnClickExists = getBooleanValue(buttonPropetyNode, isOnClickExists);
											}
										}

										ZCButton button = new ZCButton(buttonDisplayName, buttonLinkName, buttonType);
										if(isStateLess)
										{
											button.setOnClickExists(isOnClickExists);
										}
										buttons.add(button);
									}
								}
							}
						}
						Collections.sort(buttons);

						// String appOwner, String appLinkName, String type, String componentName, String componentLinkName, int sequenceNumber, Long componentID, boolean hasAddOnLoad, boolean hasEditOnLoad, String successMessage, String tableName, int formLinkId
						toReturn = new ZCForm(appOwner, appLinkName, componentName, componentLinkName, sequenceNumber,  hasAddOnLoad, hasEditOnLoad, successMessage, dateFormat, isStateLess,openurlType,openurlValue);
						toReturn.addFields(fields);
						if(buttons.size()>0) {
							toReturn.addButtons(buttons);	
						}
					}
				}
			}
		}
		return toReturn;
	}

	private static String parseErrorMessage(Node errorlistNode) {
		NodeList resultNodes = errorlistNode.getChildNodes();
		for(int k=0; k<resultNodes.getLength(); k++) {
			Node resultNodeChild = resultNodes.item(k);
			if(resultNodeChild.getNodeName().equals("error")) {
				NodeList resultNodeChildNodes = resultNodeChild.getChildNodes();
				for(int l=0; l<resultNodeChildNodes.getLength(); l++) {
					Node resultNodeChildNode = resultNodeChildNodes.item(l);
					if(resultNodeChildNode.getNodeName().equals("message")) {
						return getStringValue(resultNodeChildNode, "");
					}
				}
			}
		}
		return "";
	}


	private static ZCField parseField(Node resultNodeChild,String applinkName,String formlinkName, String appOwner,boolean isParentSubForm,Hashtable<String, String> queryStringhashTable) throws ZCException {
		NodeList fieldPropetyNodes = resultNodeChild.getChildNodes();

		String fieldName = null;
		FieldType fieldType = FieldType.SINGLE_LINE;
		ExternalField externalFieldType = ExternalField.UNKNOWN;
		String displayName = "";
		String delugeType = "";
		String appLinkName = applinkName;
		String formLinkName = formlinkName;

		String initialValue = "";
		List<String> initialChoiceValues = new ArrayList<String>(); 		
		List<String> keys = new ArrayList<String>();	

		boolean isUnique = false;
		boolean isRequired = false;
		boolean isNewEntriesAllowed = false;
		boolean isHidden = false;
		boolean allowOtherChoice = false;

		int maxChar = 255;
		int defaultRows = 0;
		int maximumRows = 0;
		int decimalLength = 0;

		boolean urlTitleReq = false;
		boolean urlLinkNameReq = false;

		boolean fromZohoDoc = false;
		boolean fromGoogleDoc = false;
		boolean fromLocalComputer = true;

		boolean imgLinkReq = false;
		boolean imgTitleReq = false;
		boolean altTxtReq = false;

		boolean isLookup = false;
		boolean hasOnUserInput = false;
		boolean hasOnUserInputForFormula = false;
		boolean hasSubForm = false;
		boolean onAddRowExists = false;
		boolean onDeleteRowExists = false;
		boolean isFilterApplied = false;

		String refFormLinkName = null;
		String refFormDisplayName = null;
		String refAppLinkName = null;
		String refFieldLinkName = null;
		String currencyType = "USD";//No I18N
		String urlTitleValue = "";
		String urlLinkNameValue = "";
		String urlValue = "";
		int imageType = 3;


		List<ZCChoice> choices  = new ArrayList<ZCChoice>(); 
		List<ZCField> subFormFields = new ArrayList<ZCField>();
		List<ZCField> subFormEditFields = new ArrayList<ZCField>();
		List<ZCRecord> subFormEntries = new ArrayList<ZCRecord>();
		ZCRecord defaultSubFormEntry = null;
		Node fieldNode = resultNodeChild.getAttributes().getNamedItem("ishidden");
		if(fieldNode!=null)
		{
			isHidden = Boolean.valueOf(fieldNode.getNodeValue());	
		}
		for(int l=0; l<fieldPropetyNodes.getLength(); l++) {
			Node fieldPropetyNode = fieldPropetyNodes.item(l);
			if(fieldPropetyNode.getNodeName().equalsIgnoreCase("altTxtReq")) {
				altTxtReq = getBooleanValue(fieldPropetyNode, altTxtReq);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("delugeType")) {
				delugeType = getStringValue(fieldPropetyNode, delugeType);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("DisplayName")) {
				displayName = getStringValue(fieldPropetyNode, displayName);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("FieldName")) {
				fieldName = getStringValue(fieldPropetyNode, fieldName);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("Text")) {
				initialValue = getStringValue(fieldPropetyNode, "");
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("fromLocalComputer")) {
				fromLocalComputer = getBooleanValue(fieldPropetyNode, fromLocalComputer);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("fromGoogleDoc")) {
				fromGoogleDoc = getBooleanValue(fieldPropetyNode, fromGoogleDoc);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("fromZohoDoc")) {
				fromZohoDoc = getBooleanValue(fieldPropetyNode, fromZohoDoc);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("imgLinkReq")) {
				imgLinkReq	= getBooleanValue(fieldPropetyNode, imgLinkReq);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("imgTitleReq")) {
				imgTitleReq = getBooleanValue(fieldPropetyNode, imgTitleReq);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("Initial") || fieldPropetyNode.getNodeName().equals("value")) {

				NodeList urlTagsList = fieldPropetyNode.getChildNodes();
				boolean isImage = false;
				for(int q=0;q<urlTagsList.getLength();q++)
				{
					Node childNode = urlTagsList.item(q);
					if(childNode.getNodeName().equals("linkname")) {
						urlLinkNameValue = getStringValue(childNode, urlLinkNameValue);
					}
					else if(childNode.getNodeName().equals("url"))
					{
						urlValue = getStringValue(childNode, urlValue);
					}
					else if(childNode.getNodeName().equals("title"))
					{
						urlTitleValue = getStringValue(childNode, urlTitleValue);
					}else if(childNode.getNodeName().equals("src"))
					{
						isImage = true;
						initialValue = getStringValue(childNode, "");
					}
				}

				if(subFormFields.size()>0) {
					NodeList recordsList = fieldPropetyNode.getChildNodes();
					for(int k=0; k<recordsList.getLength(); k++) {
						Node recordNode = recordsList.item(k);
						if(recordNode.getNodeName().equals("record")) {
							ZCRecord zcRecord = parseAndSetRecord(null, recordNode, subFormFields);
							subFormEntries.add(zcRecord);
						}
					}
				}
				else {
					String key ="";
					Node node = fieldPropetyNode.getAttributes().getNamedItem("value");
					if(node!=null)
					{
						key = node.getNodeValue();
					}
					if(!(key.equals("")))
					{
						keys.add(key);
					}
					if(!isImage)
					{
						initialValue = getStringValue(fieldPropetyNode, initialValue);
						initialChoiceValues.add(initialValue);
					}	
				}

			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("maxChar")) {
				maxChar = getIntValue(fieldPropetyNode, maxChar);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("decimallength")) {
				decimalLength = getIntValue(fieldPropetyNode, decimalLength);
			}else if(fieldPropetyNode.getNodeName().equals("ref_formdispname")) {
				refFormDisplayName = getStringValue(fieldPropetyNode, "");
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("refform")) {
				refFormLinkName = getStringValue(fieldPropetyNode, "");
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("refapplication")) {
				refAppLinkName = getStringValue(fieldPropetyNode, "");
			} else if(fieldPropetyNode.getNodeName().equals("ref_fieldname")) {
				refFieldLinkName = getStringValue(fieldPropetyNode, "");
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("required")) {
				isRequired = getBooleanValue(fieldPropetyNode, isRequired);
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("allowotherchoice")) {
				allowOtherChoice = getBooleanValue(fieldPropetyNode, allowOtherChoice);
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("inputtype")) {
				imageType = getIntValue(fieldPropetyNode, imageType);
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("allownewentries")) {
				isNewEntriesAllowed = getBooleanValue(fieldPropetyNode, isNewEntriesAllowed);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("Type")) {
				int type = getIntValue(fieldPropetyNode, 1);
				fieldType = FieldType.getFieldType(type);
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("serviceType")) {
				int type = getIntValue(fieldPropetyNode, 1);
				externalFieldType = ExternalField.getExternalFieldType(type);
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("islookupfield"))
			{
				isLookup =  getBooleanValue(fieldPropetyNode, isLookup);
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("Unique")) {
				isUnique = getBooleanValue(fieldPropetyNode, isUnique);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("onaddrowexists")) {
				onAddRowExists = getBooleanValue(fieldPropetyNode,onAddRowExists);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("ondeleterowexists")) {
				onDeleteRowExists = getBooleanValue(fieldPropetyNode,onDeleteRowExists);
			} 

			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("TitleReq")) {

				urlTitleReq = getBooleanValue(fieldPropetyNode, urlTitleReq);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("filter")) {

				isFilterApplied = getBooleanValue(fieldPropetyNode, isFilterApplied);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("currencydisp")){
				currencyType = getStringValue(fieldPropetyNode, "");
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("LinkNameReq")) {
				urlLinkNameReq = getBooleanValue(fieldPropetyNode, urlLinkNameReq);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("onChangeExists")) {
				hasOnUserInput = getBooleanValue(fieldPropetyNode, hasOnUserInput);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("formulaExists")) {
				hasOnUserInputForFormula = getBooleanValue(fieldPropetyNode, hasOnUserInputForFormula);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("dynamicpicklistexists")) {
				hasOnUserInputForFormula = getBooleanValue(fieldPropetyNode, hasOnUserInputForFormula);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("defaultrows"))
			{
				defaultRows = getIntValue(fieldPropetyNode, defaultRows);
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("maximumrows"))
			{
				maximumRows = getIntValue(fieldPropetyNode, maximumRows);
				if(maximumRows<0)
				{
					maximumRows = 10000;
				}
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("subformrecords"))
			{

				NodeList subFormRecordsNodeList = fieldPropetyNode.getChildNodes();
				for(int m=0; m<subFormRecordsNodeList.getLength(); m++) {
					Node subFormRecordsNode = subFormRecordsNodeList.item(m);

					ZCRecord record = parseAndSetRecord(null, subFormRecordsNode, subFormFields);
					subFormEntries.add(record);
				}

			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("SubFormFields")) {
				NodeList subFormFieldNodes = fieldPropetyNode.getChildNodes();
				hasSubForm = true;
				List<ZCRecordValue> defaultSubFormEntryValues = new ArrayList<ZCRecordValue>();
				defaultSubFormEntry = new ZCRecord(defaultSubFormEntryValues);
				for(int m=0; m<subFormFieldNodes.getLength(); m++) {
					Node subFormFieldNode = subFormFieldNodes.item(m);
					if(subFormFieldNode.getNodeName().equalsIgnoreCase("Field")) {
						ZCField subFormField = parseField(subFormFieldNode,appLinkName,formLinkName, appOwner, true,new Hashtable<String, String>());
						if(subFormField != null)
						{
							subFormFields.add(subFormField);
							//subFormEditFields.add(parseField(subFormFieldNode,appLinkName,formLinkName, appOwner, true,new Hashtable<String, String>()));
							defaultSubFormEntryValues.add(subFormField.getRecordValue().getNewRecordValue());
						}
					}
				}
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("Choices")) {
				choices.addAll(parseLookUpChoices(fieldPropetyNode));
			}
		}
		if(isParentSubForm) {
			if(fieldType == FieldType.SUB_FORM || fieldType == FieldType.UNKNOWN  || fieldType ==FieldType.NOTES ) {
				return null;
			}
		}	
		if(fieldType.equals(FieldType.EXTERNAL_FIELD) || fieldType.equals(FieldType.EXTERNAL_LINK)) {
			throw new ZCException(resourceString.getString("this_form_contains_zoho_crm_field_which_is_currently_not_supported"), ZCException.ERROR_OCCURED, "");
		}
		if(isParentSubForm && FieldType.isPhotoField(fieldType))
		{
			throw new ZCException(resourceString.getString("this_form_contains_subform_field_which_contains")+fieldType+" " + resourceString.getString("field_which_is_currently_not_supported"), ZCException.ERROR_OCCURED,"" );
		}

		ZCField zcField = new ZCField(fieldName, fieldType, displayName);
		//		for(String key:queryStringhashTable.keySet())
		//		{
		//			if(key.equals(fieldName))
		//			{
		//				if(FieldType.isMultiChoiceField(fieldType)){
		//					initialChoiceValues = setFieldInitialValues(queryStringhashTable.get(key));
		//				}
		//				else
		//				{
		//					initialValue = queryStringhashTable.get(key);
		//				}
		//			}
		//		}

		if(FieldType.isMultiChoiceField(fieldType)) {
			List<ZCChoice> selectedChoices = new ArrayList<ZCChoice>();// THis is used in Edit record for multichoice field. But in form builder there is no way to set initial value
			if(!isLookup)
			{
				if(keys.size()>0)
				{
					initialChoiceValues = keys;
				}
				if(initialChoiceValues.size()==1)
				{
					String keyValue = initialChoiceValues.get(0);
					if(keyValue.contains(","))
					{
						String[] choiceValues =keyValue.split(",");
						for(int i=0;i<choiceValues.length;i++)
						{
							initialChoiceValues.add(choiceValues[i]);
						}
					}
				}
				for(int j=0; j<initialChoiceValues.size(); j++) {
					String initValue = initialChoiceValues.get(j);
					ZCChoice toAdd = null;
					for(int i=0; i<choices.size(); i++) {
						ZCChoice choice = choices.get(i);
						if(choice.getKey().equals(initValue)) {
							toAdd = choice;
							break;
						}
					}
					if(toAdd != null) {
						selectedChoices.add(toAdd);
					}
				}
				zcField.setRecordValue(new ZCRecordValue(zcField, selectedChoices)); 
			}
			else
			{

				for(int i=0;i<keys.size();i++)
				{
					selectedChoices.add(new ZCChoice(keys.get(i), initialChoiceValues.get(i)));
				}
				zcField.setRecordValue(new ZCRecordValue(zcField,selectedChoices));

			}
		} else if(FieldType.isSingleChoiceField(fieldType)) {
			ZCChoice toAdd = null;
			if(!isLookup)
			{	
				if(keys.size()>0)
				{
					initialValue = keys.get(0);
				}
				for(int i=0; i<choices.size(); i++) {
					ZCChoice choice = choices.get(i);
					if(choice.getKey().equals(initialValue)) {
						toAdd = choice;
						break;
					}
				}
				zcField.setRecordValue(new ZCRecordValue(zcField, toAdd));
			}
			else
			{   
				if(keys.size()>0)
				{
					toAdd = new ZCChoice(keys.get(0), initialValue);
				}else
				{
					if(initialValue.length()>0)
					{
						toAdd = new ZCChoice(initialValue, initialValue);
					}
				}
				zcField.setRecordValue(new ZCRecordValue(zcField,toAdd));
			}
		} 
		//		 else if(FieldType.isPhotoField(fieldType)) {
		//			File file = null;
		//			zcField.setRecordValue(new ZCRecordValue(zcField, file));}
		else {
			ZCRecordValue recordValue = null;
			if(zcField.getType()==FieldType.URL)
			{
				recordValue = new ZCRecordValue(zcField, urlValue,urlTitleValue,urlLinkNameValue);
			}
			else
			{
				recordValue = new ZCRecordValue(zcField, initialValue);
			}
			zcField.setRecordValue(recordValue);
		}
		zcField.setExternalFieldType(externalFieldType);
		zcField.setHidden(isHidden);
		zcField.setDefaultRows(defaultRows);
		zcField.setMaximumRows(maximumRows);
		zcField.setFilterApplied(isFilterApplied);
		zcField.setDecimalLength(decimalLength);
		if(isFilterApplied || (!isLookup))
		{
			zcField.getRecordValue().addChoices(choices);
			zcField.getRecordValue().setLastReachedForChoices(true);
		}
		zcField.setOnAddRowExists(onAddRowExists);
		zcField.setOnDeleteRowExists(onDeleteRowExists);
		zcField.setLookup(isLookup);
		zcField.setRequired(isRequired);
		zcField.setUnique(isUnique);
		zcField.setHasOnUserInput(hasOnUserInput);
		zcField.setHasOnUserInputForFormula(hasOnUserInputForFormula);
		zcField.setMaxChar(maxChar);
		zcField.setCurrencyType(currencyType);
		zcField.setUrlLinkNameReq(urlLinkNameReq);
		zcField.setUrlTitleReq(urlTitleReq);
		zcField.setImageType(imageType);
		zcField.setNewEntriesAllowed(isNewEntriesAllowed);

		if(refFormLinkName != null && refAppLinkName != null ) {
			zcField.setRefFormComponent(new ZCComponent(appOwner, refAppLinkName, ZCComponent.FORM, "", refFormLinkName, -1));
			zcField.setRefFieldLinkName(refFieldLinkName);
			if(fieldType != FieldType.SUB_FORM) {
				zcField.setNewEntriesAllowed(true);
			}
		}
		// String appOwner, String appLinkName, String type, String componentName, String componentLinkName, int sequenceNumber, Long componentID, boolean hasAddOnLoad, boolean hasEditOnLoad, String successMessage, String tableName, int formLinkId
		if(hasSubForm) { // type/sequenceNumber/componentID/formLinkId -1, hasAddOnLoad/hasEditOnLoad false,  successMessage/tableName empty string, 
			/*
			ZCForm subForm = new ZCForm(appOwner, refAppLinkName,displayName, refFormLinkName, -1, false, false, "", "", false,"","");
			subForm.addFields(subFormFields);
			zcField.setSubForm(subForm);
			 */
			zcField.setDefaultSubFormEntry(defaultSubFormEntry);
			for(int i=0; i<subFormEntries.size(); i++) {

				zcField.addSubFormEntry(subFormEntries.get(i));
			}
			ZCForm subForm = new ZCForm(appOwner, refAppLinkName, displayName, refFormLinkName, -1, false, false, "", "", false,"","");
			subForm.addFields(subFormFields);
			zcField.setSubForm(subForm);
		}

		return zcField;
	}

	private static List<String> setFieldInitialValues(String queryString)
	{
		String[] choicevalues = null;
		List<String> initialValues = new ArrayList<String>();
		if(queryString.contains(","))
		{
			choicevalues = queryString.split(",");
			for(int i=0;i<choicevalues.length;i++)
			{
				initialValues.add(choicevalues[i]);
			}
		}
		else
		{
			initialValues.add(queryString);
		}
		return initialValues;
	}

	private static List<ZCChoice> parseLookUpChoices(Node fieldPropetyNode) {
		List<ZCChoice> choices = new ArrayList<ZCChoice>();
		NodeList choiceNodes = fieldPropetyNode.getChildNodes();
		for(int m=0; m<choiceNodes.getLength(); m++) {
			Node choiceNode = choiceNodes.item(m);
			String key = choiceNode.getAttributes().getNamedItem("value").getNodeValue();
			String value = getStringValue(choiceNode, "");
			choices.add(new ZCChoice(key, value));
		}

		return choices;
	}

	static List<ZCChoice> parseLookUpChoices(Document rootDocument)
	{

		NodeList nl = rootDocument.getChildNodes();
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("response")) {
				NodeList responseNodeList = responseNode.getChildNodes();
				for(int j=0; j<responseNodeList.getLength(); j++) {
					Node resultNode = responseNodeList.item(j);
					if(resultNode.getNodeName().equals("result")) {
						NodeList resultNodeList = resultNode.getChildNodes();
						for(int k=0; k<resultNodeList.getLength(); k++) {
							Node fieldNode = resultNodeList.item(k);
							if(fieldNode.getNodeName().equals("field")) {
								NodeList fieldNodeList = fieldNode.getChildNodes();
								for(int l=0;l<fieldNodeList.getLength();l++)
								{
									Node choicesNode = fieldNodeList.item(l);
									if(choicesNode.getNodeName().equals("choices"))
									{
										return parseLookUpChoices(choicesNode);
									}
								}
							}
						}
					}
				}
			}

		}
		return new ArrayList<ZCChoice>();
	}

	private static ZCChoice parseSingleSelectValue(String value, List<ZCChoice> choices) {
		if(choices != null) {
			for(int i=0; i<choices.size(); i++) {
				if(choices.get(i).getKey().equals(value)) {
					return choices.get(i);
				}
			}
		}
		return new ZCChoice(value, value);
	}

	private static List<ZCChoice> parseMultiSelectValues(String value) {
		List<ZCChoice> multSelVals = new ArrayList<ZCChoice>();
		if(value.startsWith("[{"))
		{
			try {
				JSONArray jArray = new JSONArray(value);
				for(int q=0;q<jArray.length();q++)
				{
					JSONObject jsonObject = jArray.getJSONObject(q);
					String key = "";
					String displayValue = "";
					if(jsonObject.has("referFieldValue"))
					{
						key = jsonObject.getString("referFieldValue");
					}
					if(jsonObject.has("displayValue"))
					{
						displayValue = jsonObject.getString("displayValue");
					}
					multSelVals.add(new ZCChoice(key, displayValue));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			if(value.startsWith("[") && value.endsWith("]")) {
				value = value.substring(1, value.length()-1);
			}
			String[] tokens = value.split(", ");
			//			for(int i=1;i<tokens.length;i++)
			//			{
			//				
			//				//tokens[i]=tokens[i].substring(1);
			//			}
			for(int m = 0;m<tokens.length;m++)
			{
				if(!(tokens[m].equals("")))
				{
					String initialValue = tokens[m];
					multSelVals.add(new ZCChoice(initialValue, initialValue));
				}
			}		
		}
		return multSelVals;
	}

	static void parseAndAddRecords(Document rootDocument, ZCView zcView) {
		NodeList nl = rootDocument.getChildNodes();
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("response")) {
				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0; j<responseNodes.getLength(); j++) {
					Node responseChildNode = responseNodes.item(j);
					if(responseChildNode.getNodeName().equals("records")) {
						parseAndSetRecords(zcView, responseChildNode);
					} else if(responseChildNode.getNodeName().equals("calendar")) {
						parseAndSetCalendarRecords(zcView, responseChildNode);
					}					
				}
			}
		}
	}

	private static void parseAndSetCalendarRecords(ZCView zcView, Node calendarNode) {

		zcView.setGrouped(true);
		NodeList eventsList = calendarNode.getChildNodes();


		for(int i=0; i<eventsList.getLength(); i++) {

			Node eventNode = eventsList.item(i);
			NamedNodeMap eventAttrMap = eventNode.getAttributes();
			long recordid = Long.parseLong(eventAttrMap.getNamedItem("id").getNodeValue()); //No I18N
			String title = getChildNodeValue(eventNode, "title"); //eventAttrMap.getNamedItem("title").getNodeValue(); //No I18N
			boolean isAllDay = Boolean.parseBoolean(eventAttrMap.getNamedItem("allDay").getNodeValue()); //No I18N
			// 07/31/2013 08:00:00
			String dateFormat = "MM/dd/yyyy HH:mm:ss"; //No I18N
			if(isAllDay) {
				dateFormat = "MM/dd/yyyy"; //No I18N
			}

			Date startTime = getDateValue(eventAttrMap.getNamedItem("start").getNodeValue(), dateFormat); //No I18N

			ZCRecord record = zcView.getRecord(recordid);

			record.setEventTitle(title);
			if(isAllDay) {
				zcView.setIsAllDay(isAllDay);
				zcView.setEvent(record, startTime);

				record.setEventDate(startTime);
			} else {
				// 07/31/2013 08:00:00
				Date endTime = getDateValue(eventAttrMap.getNamedItem("end").getNodeValue(), dateFormat); //No I18N
				record.setStartTime(startTime);
				record.setEndTime(endTime);

				Calendar startCalendar = new GregorianCalendar();
				startCalendar.setTime(startTime);
				startCalendar.set(Calendar.HOUR_OF_DAY, 0);
				startCalendar.set(Calendar.MINUTE, 0);
				startCalendar.set(Calendar.SECOND, 0);
				startCalendar.set(Calendar.MILLISECOND, 0);

				Calendar endCalendar = new GregorianCalendar();
				endCalendar.setTime(endTime);
				endCalendar.set(Calendar.HOUR_OF_DAY, 0);
				endCalendar.set(Calendar.MINUTE, 0);
				endCalendar.set(Calendar.SECOND, 0);
				endCalendar.set(Calendar.MILLISECOND, 0);

				Date eventDate = new Date(startCalendar.getTimeInMillis());
				zcView.setEvent(record, eventDate);
				while ((startCalendar.get(Calendar.YEAR) != endCalendar.get(Calendar.YEAR)) || (startCalendar.get(Calendar.MONTH) != endCalendar.get(Calendar.MONTH)) || (startCalendar.get(Calendar.DATE) != endCalendar.get(Calendar.DATE))) {
					startCalendar.add(Calendar.DATE, 1);
					eventDate = new Date(startCalendar.getTimeInMillis());
					zcView.setEvent(record, eventDate);
				}
			}
		}

		List<ZCGroup> zcGroups = zcView.getGroups();

		HashMap<Date, List<ZCRecord>> eventsMap = zcView.getEventRecordsMap();

		SortedSet<Date> keys = new TreeSet<Date>(eventsMap.keySet());
		for (Date eventDate : keys) { 
			List<ZCRecord> eventRecords = eventsMap.get(eventDate);
			List<String> groupHeaderValues  = new ArrayList<String>();
			SimpleDateFormat dateFormat = new SimpleDateFormat(zcView.getDateFormat());	//No I18N
			groupHeaderValues.add(dateFormat.format(eventDate));
			ZCGroup zcGroup = new ZCGroup(groupHeaderValues);
			zcGroups.add(zcGroup);
			for(int i=0; i<eventRecords.size(); i++) {
				ZCRecord eventRecord = eventRecords.get(i);
				zcGroup.addRecord(eventRecord);
			}
		}


		zcView.sortRecordsForCalendar();


	}

	private static void parseAndSetRecords(ZCView zcView, Node recordsNode) {
		NodeList recordsList = recordsNode.getChildNodes();
		List<ZCRecord> records = new ArrayList<ZCRecord>();
		List<ZCGroup> zcGroups = zcView.getGroups();
		ZCGroup zcGroup = null;
		if(zcGroups.size()>0) { // setting the previous set's last group
			zcGroup = zcGroups.get(zcGroups.size()-1);
		}
		boolean isViewGrouped = false;
		boolean isFirstRecordInGroup = false;
		List<String> groupHeaderValues  = null;
		for(int k=0; k<recordsList.getLength(); k++) {
			Node recordNode = recordsList.item(k);
			if(recordNode.getNodeName().equals("group")) {
				isFirstRecordInGroup = true;
				if(!isViewGrouped) {
					isViewGrouped = true;
				}
				groupHeaderValues = new ArrayList<String>();
				NodeList columnList = recordNode.getChildNodes();
				for(int l=0; l<columnList.getLength(); l++) {
					Node columnNode = columnList.item(l);
					Node valueNode = columnNode.getFirstChild();
					groupHeaderValues.add(getStringValue(valueNode, ""));
				}
			} else if(recordNode.getNodeName().equals("record")) {
				ZCRecord record = parseAndSetRecord(zcView, recordNode, null);
				records.add(record);
				if(isViewGrouped) {
					if(isFirstRecordInGroup && (zcGroup == null || !zcGroup.getGroupHeaderValues().equals(groupHeaderValues))) {// To check if it's not the same group of the previous set's last group
						zcGroup = new ZCGroup(groupHeaderValues);
						zcGroups.add(zcGroup);
						isFirstRecordInGroup = false;
						groupHeaderValues = null;
					}
					zcGroup.addRecord(record);
				}
			}
		}

		zcView.addRecords(records);
		zcView.setGrouped(isViewGrouped);
	}

	private static ZCRecord parseAndSetRecord(ZCView zcView, Node recordNode, List<ZCField> subFormFields) {
		NamedNodeMap recordAttrMap = recordNode.getAttributes();
		long recordid = 0L;
		if(recordAttrMap.getNamedItem("id")!=null)
		{
			recordid = Long.parseLong(recordAttrMap.getNamedItem("id").getNodeValue()); //No I18N
		}
		else
		{
			recordid = Long.parseLong(recordAttrMap.getNamedItem("ID").getNodeValue()); //No I18N
		}
		List<ZCRecordValue> valueList  = new ArrayList<ZCRecordValue>();
		NodeList columnList = recordNode.getChildNodes();
		for(int l=0; l<columnList.getLength(); l++) {
			Node columnNode = columnList.item(l);
			NamedNodeMap columAttrMap = columnNode.getAttributes();
			String fieldName = columAttrMap.getNamedItem("name").getNodeValue(); //No I18N

			ZCField zcField = null;
			if(zcView != null) {
				zcField = zcView.getColumn(fieldName);
			} else {
				for(int m=0; m<subFormFields.size(); m++) {
					ZCField fieldToCheck = subFormFields.get(m);
					if(fieldToCheck.getFieldName().equals(fieldName)) {
						zcField = fieldToCheck;
						break;
					}
				}
			}
			if(zcField == null) {
				break;
			}

			Node valueNode = columnNode.getFirstChild();
			String value = getStringValue(valueNode, "");
			NodeList columnvalueList = columnNode.getChildNodes();
			List<ZCChoice> selectedChoices = new ArrayList<ZCChoice>();
			ZCChoice selectedChoice = null;
			List<ZCChoice> choices = null;
			if(zcView == null)
			{
				choices = zcField.getRecordValue().getChoices();
				if(FieldType.isChoiceField(zcField.getType())) 
				{
					for(int i=0;i<columnvalueList.getLength();i++)
					{
						Node columnvaluelistChildnode =  columnvalueList.item(i);
						Node columnvaluelistvaluenode = columnvaluelistChildnode.getAttributes().getNamedItem("value");
						if(columnvaluelistvaluenode!=null)
						{
							String key = columnvaluelistvaluenode.getNodeValue();
							for(int j=0; j<choices.size(); j++) {
								if(key.equals(choices.get(j).getKey())) {
									selectedChoices.add(new ZCChoice(key,getStringValue(columnvaluelistChildnode, "")));
									break;
								}
							}
							if(zcField.isLookup()){
								selectedChoices.add(new ZCChoice(key,getStringValue(columnvaluelistChildnode, "")));
							}
						}
					}
				}
				if(selectedChoices.size() >0) {
					selectedChoice = selectedChoices.get(0);

				}
			}
			else 
			{
				if(value.startsWith("[") && value.endsWith("]")) {
					value = value.substring(1, value.length()-1);
				}
				String[] tokens = value.split(", ");
				for(int m = 0;m<tokens.length;m++)
				{
					if(!(tokens[m].equals("")))
					{
						String initialValue = tokens[m];
						selectedChoices.add(new ZCChoice(initialValue, initialValue));
					}
				}		
				selectedChoice = new ZCChoice(value, value);
			}

			ZCRecordValue zcValue = null;
			if(FieldType.isMultiChoiceField(zcField.getType())) {

				zcValue = new ZCRecordValue(zcField, selectedChoices);
				zcValue.addChoices(choices);
			} else if(FieldType.isSingleChoiceField(zcField.getType())) {
				zcValue = new ZCRecordValue(zcField, selectedChoice);
				zcValue.addChoices(choices);
			} else if(zcField.getType()==FieldType.URL) {
				String urlLinkNameValue = "";
				String urlTitleValue = "";
				NodeList urlTagNodes = valueNode.getChildNodes();
				for(int m=0;m<urlTagNodes.getLength();m++) {
					Node urlNode = urlTagNodes.item(m);
					if(urlNode.getNodeName().equals("linkname")) {
						urlLinkNameValue = getStringValue(urlNode, urlLinkNameValue);
					} else if(urlNode.getNodeName().equals("url")) {
						value = getStringValue(urlNode, "");
					} else if(urlNode.getNodeName().equals("title")) {
						urlTitleValue = getStringValue(urlNode, urlTitleValue);
					}
				}
				zcValue = new ZCRecordValue(zcField, value,urlTitleValue,urlLinkNameValue);
			} else {
				zcValue = new ZCRecordValue(zcField, value);
			}
			if(zcView!=null)
			{
				zcField.setRecordValue(zcValue);
			}

			valueList.add(zcValue);
		}
		ZCRecord record = new ZCRecord(recordid, valueList);
		return record;
	}

	static ZCView parseForView(Document rootDocument, String appLinkName, String appOwner, String componentType, int month, int year) throws ZCException{		
		
		NodeList nl = rootDocument.getChildNodes();
		
		ZCView toReturn = null;
		for(int i=0; i<nl.getLength(); i++) {
		
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("response")) {
				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0; j<responseNodes.getLength(); j++) {
					Node responseChildNode = responseNodes.item(j);

					if(responseChildNode.getNodeName().equals("errorlist"))
					{
						NodeList errorListNodes = responseChildNode.getChildNodes();
						for(int o=0; o<errorListNodes.getLength(); o++) {
							Node errorlistNode = errorListNodes.item(o);
							if(errorlistNode.getNodeName().equals("error"))
							{
								NodeList errorlistchildNodes = errorlistNode.getChildNodes();
								int code = 0;
								boolean hasErrorOcured = false;
								String errorMessage = "";
								for(int p=0;p<errorlistchildNodes.getLength();p++)
								{
									Node errorlistchildNode = errorlistchildNodes.item(p);
									if(errorlistchildNode.getNodeName().equals("code"))
									{
										code = getIntValue(errorlistchildNode, code);
									}else if(errorlistchildNode.getNodeName().equals("message"))
									{
										hasErrorOcured = true;
										errorMessage = getStringValue(errorlistchildNode,"");
										
									}
								}
								
								if(hasErrorOcured)
								{
									ZCException exception = new ZCException(errorMessage, ZCException.ERROR_OCCURED,"" );
									exception.setCode(code);
									throw exception;
								}
							}
						}
					}

					if(responseChildNode.getNodeName().equals("metadata")) {
						NodeList viewNodes = responseChildNode.getChildNodes();
						for(int k=0; k<viewNodes.getLength(); k++) {
							Node viewNode = viewNodes.item(k);
							if(viewNode.getNodeName().equals("View")) {
								NamedNodeMap attrMap = viewNode.getAttributes();
								String componentLinkName = attrMap.getNamedItem("LinkName").getNodeValue();//No I18N

								String componentName = getChildNodeValue(viewNode, "DisplayName"); //No I18N
								String dateFormat = attrMap.getNamedItem("DateFormat").getNodeValue();//No I18N
								//String appOwner, String appLinkName, String componentName, String componentLinkName
								toReturn = new ZCView(appOwner, appLinkName, componentType, componentName, componentLinkName);
								toReturn.setDateFormat(dateFormat);
								NodeList viewChildNodes = viewNode.getChildNodes();
								for(int l=0; l<viewChildNodes.getLength(); l++) {
									Node viewChildNode = viewChildNodes.item(l);
									if(viewChildNode.getNodeName().equals("BaseForm")) {
										NamedNodeMap baseFormAttrMap = viewChildNode.getAttributes();
										String baseFormLinkName = baseFormAttrMap.getNamedItem("linkname").getNodeValue(); //No I18N
										toReturn.setBaseFormLinkName(baseFormLinkName);
									} else if(viewChildNode.getNodeName().equals("customFilters")) {
										NodeList customFilterNodeList = viewChildNode.getChildNodes();
										List<ZCCustomFilter> customFilters = new ArrayList<ZCCustomFilter>();
										for(int m=0; m<customFilterNodeList.getLength(); m++) {
											Node customFilterNode = customFilterNodeList.item(m);
											NamedNodeMap customFilterAttrMap = customFilterNode.getAttributes();
											long id = Long.parseLong(customFilterAttrMap.getNamedItem("Id").getNodeValue()); //No I18N											
											String customFilterName = getChildNodeValue(customFilterNode, "DisplayName");//No I18N
											ZCCustomFilter customFilter = new ZCCustomFilter(customFilterName, id);
											customFilters.add(customFilter);
										}
										toReturn.addCustomFilters(customFilters);
									} else if(viewChildNode.getNodeName().equals("filters")) {
										NodeList filterNodeList = viewChildNode.getChildNodes();
										List<ZCFilter> filters = new ArrayList<ZCFilter>();
										for(int m=0; m<filterNodeList.getLength(); m++) {
											Node filterNode = filterNodeList.item(m);
											NamedNodeMap filterAttrMap = filterNode.getAttributes();
											String FilterLinkName = filterAttrMap.getNamedItem("name").getNodeValue(); //No I18N
											String filterName = getChildNodeValue(filterNode, "displayname"); //filterAttrMap.getNamedItem("displayname").getNodeValue(); //No I18N
											ZCFilter filter = new ZCFilter(FilterLinkName, filterName);
											NodeList filterValuesList = filterNode.getChildNodes();
											List<ZCFilterValue> filterValues = new ArrayList<ZCFilterValue>();
											for(int n=0; n<filterValuesList.getLength(); n++) {
												Node filterValueNode = filterValuesList.item(n);
												if(filterValueNode.getNodeName().equals("value")) {

													String filterValue = getStringValue(filterValueNode, ""); //No I18N
													filterValue = filterValue.substring(filterValue.indexOf(":") + 1);
													String displayValue = filterValue;
													NamedNodeMap filterValAttrMap = filterValueNode.getAttributes();
													if(filterValAttrMap.getLength()>0){
														displayValue = filterValAttrMap.getNamedItem("display").getNodeValue();
													}
													filterValues.add(new ZCFilterValue(filterValue,displayValue));
												}
											}
											filter.addValues(filterValues);
											filters.add(filter);
										}
										toReturn.addFilters(filters);
										//									} else if(viewChildNode.getNodeName().equals("rec_filter")) {

										//									} else if(viewChildNode.getNodeName().equals("groupby")) {

										//									} else if(viewChildNode.getNodeName().equals("opensearch")) {

									} else if(viewChildNode.getNodeName().equals("permissions")) {
										NamedNodeMap permissionAttrMap = viewChildNode.getAttributes();
										boolean isAddAllowed = Boolean.parseBoolean(permissionAttrMap.getNamedItem("add").getNodeValue()); //No I18N
										boolean isEditAllowed = Boolean.parseBoolean(permissionAttrMap.getNamedItem("edit").getNodeValue()); //No I18N
										boolean isDeleteAllowed = Boolean.parseBoolean(permissionAttrMap.getNamedItem("delete").getNodeValue()); //No I18N
										boolean isDuplicateAllowed = Boolean.parseBoolean(permissionAttrMap.getNamedItem("duplicate").getNodeValue()); //No I18N
										boolean isBulkEditAllowed = Boolean.parseBoolean(permissionAttrMap.getNamedItem("bulkedit").getNodeValue()); //No I18N
										toReturn.setAddAllowed(isAddAllowed);
										toReturn.setEditAllowed(isEditAllowed);
										toReturn.setDeleteAllowed(isDeleteAllowed);
										toReturn.setDuplicateAllowed(isDuplicateAllowed);
										toReturn.setBulkEditAllowed(isBulkEditAllowed);
									} else if(viewChildNode.getNodeName().equals("Actions")) {
										NodeList actionList = viewChildNode.getChildNodes();
										List<ZCCustomAction> headerCustomActions = new ArrayList<ZCCustomAction>();
										List<ZCCustomAction> recordCustomActions = new ArrayList<ZCCustomAction>();
										for(int m=0; m<actionList.getLength(); m++) {
											Node actionNode = actionList.item(m);
											NamedNodeMap actionAttrMap = actionNode.getAttributes();
											String actionType = actionAttrMap.getNamedItem("type").getNodeValue(); //No I18N
											String actionName = getChildNodeValue(actionNode, "name"); //actionAttrMap.getNamedItem("name").getNodeValue(); //No I18N
											String headerAction = getChildNodeValue(actionNode, "isHeaderAction");
											Long actionId = Long.parseLong(actionAttrMap.getNamedItem("id").getNodeValue()); //No I18N
											ZCCustomAction action = new ZCCustomAction(actionType, actionName, actionId);
											if(actionType.equals("row")) {
												recordCustomActions.add(action);
											} if(headerAction.equals("true")) {
												headerCustomActions.add(action);
											}
										}
										toReturn.addHeaderCustomActions(headerCustomActions);
										toReturn.addRecordCustomActions(recordCustomActions);
									} else if(viewChildNode.getNodeName().equals("Fields")) {
										// String fieldName, int type, String displayName, String delugeType, long compID
										List<ZCColumn> columns = new ArrayList<ZCColumn>();
										NodeList fieldList = viewChildNode.getChildNodes();
										for(int m=0; m<fieldList.getLength(); m++) {
											Node fieldNode = fieldList.item(m);
											NamedNodeMap fieldAttrMap = fieldNode.getAttributes();
											String displayName = getChildNodeValue(fieldNode, "DisplayName"); //fieldAttrMap.getNamedItem("DisplayName").getNodeValue(); //No I18N
											String fieldName = fieldAttrMap.getNamedItem("BaseLabelName").getNodeValue(); //No I18N
											String compType = fieldAttrMap.getNamedItem("ComponentType").getNodeValue(); //No I18N
											FieldType fieldType = FieldType.getFieldType(compType);
											int seqNo  = Integer.parseInt(fieldAttrMap.getNamedItem("SequenceNumber").getNodeValue()); //No I18N
											ZCColumn column = new ZCColumn(fieldName, fieldType, displayName);
											column.setSequenceNumber(seqNo);
											columns.add(column);
										}
										Collections.sort(columns);
										toReturn.addColumns(columns);
									}
								}
							}
						}
					} else if(responseChildNode.getNodeName().equals("records")) {
						parseAndSetRecords(toReturn, responseChildNode);
					} else if(responseChildNode.getNodeName().equals("calendar")) {
						toReturn.setRecordsMonthYear(new ZCPair<Integer, Integer>(month, year));
						parseAndSetCalendarRecords(toReturn, responseChildNode);
					}
				}
			}
		}
		//printDocument(rootDocument);		
		return toReturn;		
	}

	private static String parseForTokenForExternalField(Document rootDocument) {
		// TODO Auto-generated method stub
		String accessToken = "";
		NodeList nl = rootDocument.getChildNodes();
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("OAuth")) {
				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0; j<responseNodes.getLength(); j++) {
					Node responseChildNode = responseNodes.item(j);
					if(responseChildNode.getNodeName().equals("access_token")) {
						accessToken = getChildNodeValue(responseChildNode, "access_token");
					}
				}
			}
		}
		return accessToken;
	}

	private static String getChildNodeValue(Node parentNode, String nodeName) {
		NodeList parentNodeChildNodes = parentNode.getChildNodes();
		for(int i=0; i<parentNodeChildNodes.getLength(); i++) {
			Node parentNodeChildNode = parentNodeChildNodes.item(i);
			if(parentNodeChildNode.getNodeName().equals(nodeName)) {
				Node firstItem = parentNodeChildNode.getChildNodes().item(0);
				if(firstItem != null) {
					return firstItem.getNodeValue();
				}
			}
		}
		return "";
	}

	private static void printDocument(Document doc) {
		try
		{
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		}
		catch(TransformerException ex)
		{
			ex.printStackTrace();
		}

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
}