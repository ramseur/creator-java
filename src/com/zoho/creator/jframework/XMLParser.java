// $Id$
package com.zoho.creator.jframework;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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
			throw new ZCException("Please subscribe to Professional Edition and get access", ZCException.LICENCE_ERROR); //No I18N
		}
		return toReturn;
	}


	static List<ZCSection> parseForSectionList(Document rootDocument, String appLinkName, String appOwner) throws ZCException {
		List<ZCSection> toReturn = new ArrayList<ZCSection>();
		int remainingDays = -1;
		NodeList nl = rootDocument.getChildNodes();
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("Response")) {
				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0; j<responseNodes.getLength(); j++) {
					Node responseNodeChild = responseNodes.item(j);
					if(responseNodeChild.getNodeName().equals("Sections")) {

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
												comps.add(new ZCComponent (appOwner, appLinkName, type, componentName, componentLinkName, sequenceNumber));
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
			} else if(responseNode.getNodeName().equals("license_enabled")) { //No I18N
				if(!getBooleanValue(responseNode, false)) {
					throw new ZCException("Please subscribe to Professional Edition and get access", ZCException.LICENCE_ERROR); //No I18N
				}
			} else if(responseNode.getNodeName().equals("evaluationDays")) { //No I18N
				ZOHOCreator.setUserProperty("evaluationDays", getStringValue(responseNode, ""));
			} 
		}
		return toReturn;		
	}

	static ZCForm parseForForm(Document rootDocument, String appLinkName, String appOwner,String queryString) throws ZCException {
		NodeList nl = rootDocument.getChildNodes();
		ZCForm toReturn = null;
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("response")) {
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
							} else if(resultNodeChild.getNodeName().equalsIgnoreCase("successMessage")) { 
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
										ZCField field = parseField(fieldNode,appLinkName,componentLinkName, appOwner, false,queryString);
										if(field != null) {
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

	static String parseErrorMessage(Node errorlistNode) {
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


	private static ZCField parseField(Node resultNodeChild,String applinkName,String formlinkName, String appOwner,boolean isParentSubForm,String queryString) throws ZCException {
		NodeList fieldPropetyNodes = resultNodeChild.getChildNodes();

		String fieldName = null;
		FieldType fieldType = FieldType.SINGLE_LINE;
		ExternalField externalFieldType = ExternalField.ZOHO_CRM;
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
		boolean isAdminOnly = false;

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

		String refFormLinkName = null;
		String refFormDisplayName = null;
		String refAppLinkName = null;
		String refFieldLinkName = null;
		String currencyType = "USD";//No I18N
		String urlTitleValue = "";
		String urlLinkNameValue = "";

		List<ZCChoice> choices  = new ArrayList<ZCChoice>(); 
		List<ZCField> subFormFields = new ArrayList<ZCField>();
		List<ZCField> subFormEditFields = new ArrayList<ZCField>();
		List<ZCRecord> subFormEntries = new ArrayList<ZCRecord>();

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
					if(fieldPropetyNode.getAttributes().getNamedItem("value")!=null)
					{
						key = fieldPropetyNode.getAttributes().getNamedItem("value").getNodeValue();
					}
					initialValue = getStringValue(fieldPropetyNode, initialValue);
					if(!(key.equals("")))
					{
						keys.add(key);
					}
//					else
//					{
					initialChoiceValues.add(initialValue);
					//}
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
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("isadminonly")) {
				isAdminOnly = getBooleanValue(fieldPropetyNode, isAdminOnly);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("linkname")) {

				urlLinkNameValue = getStringValue(fieldPropetyNode, urlLinkNameValue);
			} 
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("title")) {

				urlTitleValue = getStringValue(fieldPropetyNode, urlTitleValue);
			} else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("TitleReq")) {

				urlTitleReq = getBooleanValue(fieldPropetyNode, urlTitleReq);
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
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("defaultrows"))
			{
				defaultRows = getIntValue(fieldPropetyNode, defaultRows);
			}
			else if(fieldPropetyNode.getNodeName().equalsIgnoreCase("maximumrows"))
			{
				maximumRows = getIntValue(fieldPropetyNode, maximumRows);
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
				for(int m=0; m<subFormFieldNodes.getLength(); m++) {
					Node subFormFieldNode = subFormFieldNodes.item(m);
					if(subFormFieldNode.getNodeName().equalsIgnoreCase("Field")) {
						ZCField subFormField = parseField(subFormFieldNode,appLinkName,formLinkName, appOwner, true,null);
						if(subFormField != null)
						{
							subFormFields.add(subFormField);
							subFormEditFields.add(parseField(subFormFieldNode,appLinkName,formLinkName, appOwner, true,null));
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

		ZCField zcField = new ZCField(fieldName, fieldType, displayName);
		//if(fieldType.equals(FieldType.EXTERNAL_FIELD)){
		zcField.setExternalFieldType(externalFieldType);
		//}
		if(queryString!=null)
		{
			if(queryString.contains("&"))
			{
				String[] stringValues = queryString.split("&");
				for(int p=0;p<stringValues.length;p++)
				{
					if(FieldType.isMultiChoiceField(fieldType)){
						initialChoiceValues = setFieldInitialValues(stringValues[p], fieldName,fieldType,initialChoiceValues);
					}
					else
					{
						initialValue = setFieldInitialValue(stringValues[p], fieldName,fieldType,initialValue);
					}
				}
			}
			else
			{
				if(FieldType.isMultiChoiceField(fieldType)){
					initialChoiceValues = setFieldInitialValues(queryString, fieldName,fieldType,initialChoiceValues);
				}
				else
				{
					initialValue = setFieldInitialValue(queryString, fieldName,fieldType,initialValue);
				}
			}
		}

		if(FieldType.isMultiChoiceField(fieldType)) {
			List<ZCChoice> selectedChoices = new ArrayList<ZCChoice>();// THis is used in Edit record for multichoice field. But in form builder there is no way to set initial value
			if(!isLookup)
			{
				if(keys.size()>0)
				{
					initialChoiceValues = keys;
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
				}
				zcField.setRecordValue(new ZCRecordValue(zcField,toAdd));
			}
		} 
		//		 else if(FieldType.isPhotoField(fieldType)) {
		//			File file = null;
		//			zcField.setRecordValue(new ZCRecordValue(zcField, file));}
		else {
			zcField.setRecordValue(new ZCRecordValue(zcField, initialValue));
		}


		zcField.setHidden(isAdminOnly);
		zcField.addChoices(choices);
		zcField.setDefaultRows(defaultRows);
		zcField.setMaximumRows(maximumRows);
		zcField.setDecimalLength(decimalLength);

		if(!isLookup) {
			zcField.addChoices(choices);
			zcField.setLastReachedForChoices(true);
		}

		zcField.setOnAddRowExists(onAddRowExists);
		zcField.setOnDeleteRowExists(onDeleteRowExists);
		zcField.setLookup(isLookup);
		zcField.setRequired(isRequired);
		zcField.setHasOnUserInput(hasOnUserInput);
		zcField.setHasOnUserInputForFormula(hasOnUserInputForFormula);
		zcField.setMaxChar(maxChar);
		zcField.setCurrencyType(currencyType);
		zcField.setUrlLinkNameReq(urlLinkNameReq);
		zcField.setUrlTitleReq(urlTitleReq);
		zcField.setUrlTitleValue(urlTitleValue);
		zcField.setUrlLinkNameValue(urlLinkNameValue);
		if(isNewEntriesAllowed)
		{
			zcField.setNewEntriesAllowed(true);
		}
		if(refFormLinkName != null && refAppLinkName != null ) {
			zcField.setRefFormComponent(new ZCComponent(appOwner, refAppLinkName, ZCComponent.FORM, "", refFormLinkName, -1));
			zcField.setRefFieldLinkName(refFieldLinkName);
			if(fieldType != FieldType.SUB_FORM) {
				zcField.setNewEntriesAllowed(true);
			}
		}
		// String appOwner, String appLinkName, String type, String componentName, String componentLinkName, int sequenceNumber, Long componentID, boolean hasAddOnLoad, boolean hasEditOnLoad, String successMessage, String tableName, int formLinkId
		if(hasSubForm) { // type/sequenceNumber/componentID/formLinkId -1, hasAddOnLoad/hasEditOnLoad false,  successMessage/tableName empty string, 
			ZCForm subForm = new ZCForm(appOwner, appLinkName,displayName, null, -1, false, false, "", "", false,"","");
			subForm.addFields(subFormFields);
			zcField.setSubForm(subForm);
			for(int i=0; i<subFormEntries.size(); i++) {
				zcField.addSubFormEntry(subFormEntries.get(i));
			}
			ZCForm editSubForm = new ZCForm(appOwner, appLinkName, displayName, null, -1, false, false, "", "", false,"","");
			editSubForm.addFields(subFormEditFields);
			zcField.setEditSubForm(editSubForm);
		}
		return zcField;
	}
	private static String setFieldInitialValue(String queryString,String fieldName,FieldType fieldType,String initialValue)
	{
		String[] fieldValues = queryString.split("=");
		if(fieldName.equals(fieldValues[0]))
		{
			initialValue = fieldValues[1];
		}
		return initialValue;
	}
	private static List<String> setFieldInitialValues(String queryString,String fieldName,FieldType fieldType,List<String> initialValues)
	{
		String[] fieldValues = queryString.split("=");
		String[] choicevalues = null;
		if(fieldName.equals(fieldValues[0]))
		{
			if(fieldValues[1].contains(","))
			{
				choicevalues = fieldValues[1].split(",");
				for(int i=0;i<choicevalues.length;i++)
				{
					initialValues.add(choicevalues[i]);
				}
			}
			else
			{
				initialValues.add(fieldValues[1]);
			}
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

	static List<ZCChoice> getLookUpChoices(Document rootDocument)
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

	private static List<ZCChoice> parseMultiSelectValues(String value, List<ZCChoice> choices) {
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
		else if(value.startsWith("[") && value.endsWith("]")) {
			value = value.substring(1, value.length()-1);
			String[] tokens = value.split(",");
			for(int i=1;i<tokens.length;i++)
			{
				tokens[i]=tokens[i].substring(1);
			}
			for(int m = 0;m<tokens.length;m++)
			{
				if(!(tokens[m].equals("")))
				{
					String initialValue = tokens[m];
					boolean added = false;
					if(choices != null) {
						for(int i=0; i<choices.size(); i++) {
							if(choices.get(i).getKey().equals(initialValue)) {
								multSelVals.add(choices.get(i));
								added = true;
								break;
							}
						}
					}
					if(!added) {
						multSelVals.add(new ZCChoice(initialValue, initialValue));
					}
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
					if(isFirstRecordInGroup && (zcGroup == null || !zcGroup.getGroupHeaderValues().equals(groupHeaderValues))) { // To check if it's not the same group of the previous set's last group
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
			Node valueNode = columnNode.getFirstChild();
			String value = getStringValue(valueNode, "");
			ZCRecordValue zcValue = null;
			List<ZCChoice> choices = null;
			if(zcView == null) {
				choices = zcField.getChoices();
			}
			if(zcField!=null)
			{
				if(FieldType.isMultiChoiceField(zcField.getType())) {
					zcValue = new ZCRecordValue(zcField, parseMultiSelectValues(value, choices));
				} else if(FieldType.isSingleChoiceField(zcField.getType())) {
					zcValue = new ZCRecordValue(zcField, parseSingleSelectValue(value, choices));
				} else {
					zcValue = new ZCRecordValue(zcField, value);
				}
				zcField.setRecordValue(zcValue);
				valueList.add(zcValue);
			}
		}
		ZCRecord record = new ZCRecord(recordid, valueList);
		return record;
	}

	static ZCView parseForView(Document rootDocument, String appLinkName, String appOwner, String componentType, int month, int year) {		
		NodeList nl = rootDocument.getChildNodes();
		ZCView toReturn = null;
		for(int i=0; i<nl.getLength(); i++) {
			Node responseNode = nl.item(i);
			if(responseNode.getNodeName().equals("response")) {
				NodeList responseNodes = responseNode.getChildNodes();
				for(int j=0; j<responseNodes.getLength(); j++) {
					Node responseChildNode = responseNodes.item(j);
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
											Long actionId = Long.parseLong(actionAttrMap.getNamedItem("id").getNodeValue()); //No I18N
											ZCCustomAction action = new ZCCustomAction(actionType, actionName, actionId);
											if(actionType.equals("row")) {
												recordCustomActions.add(action);
											} else if(actionType.equals("view")) {
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

											FieldType fieldType = FieldType.SINGLE_LINE;

											fieldType = FieldType.getFieldType(compType);
											//											if(compType.equals("MULTI_SELECT")) {
											//												fieldType = FieldType.MULTISELECT;
											//											} else if(compType.equals("FILE_UPLOAD")) {
											//												fieldType = FieldType.FILE_UPLOAD;
											//											} else if(compType.equals("IMAGE")) {
											//												fieldType = FieldType.IMAGE;
											//											} else if(compType.equals("URL")){
											//												fieldType = FieldType.URL;
											//											} else if(compType.equals("TEXT_AREA")){
											//												fieldType = FieldType.MULTI_LINE;
											//											} else if(compType.equals("EMAIL_ADDRESS")){
											//												fieldType = FieldType.EMAIL;
											//											} else if(compType.equals("RICH_TEXT_AREA")){
											//												fieldType = FieldType.RICH_TEXT;
											//											} else if(compType.equals("DATE")){
											//												fieldType = FieldType.DATE;
											//											} else if(compType.equals("DATE_TIME")){
											//												fieldType = FieldType.DATE_TIME;
											//											} else if(compType.equals("INLINE_SINGLE_SELECT")){
											//												fieldType = FieldType.DROPDOWN;
											//											} else if(compType.equals("INLINE_SINGLE_SELECT")){
											//												fieldType = FieldType.RADIO;
											//											} else if(compType.equals("NUMBER")){
											//												fieldType = FieldType.NUMBER;
											//											} else if(compType.equals("PERCENTAGE")){
											//												fieldType = FieldType.PERCENTAGE;
											//											} else if(compType.equals("CURRENCY")){
											//												fieldType = FieldType.CURRENCY;
											//											} else if(compType.equals("CHECK_BOX")){
											//												fieldType = FieldType.DECISION_CHECK_BOX;
											//											} else if(compType.equals("SCRIPT")){
											//												fieldType = FieldType.FORMULA;
											//											}

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

	public static String parseForTokenForExternalField(Document rootDocument) {
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