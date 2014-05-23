package com.zoho.creator.jframework;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONParser {

	static ZCResponse parseAndCallFormEvents(String response, ZCForm currentShownForm) throws ZCException
	{
		List<String> alertMessages = new ArrayList<String>();
		List<String> infoValues = new ArrayList<String>();
		String openUrlString = null;
		int type = -1;
		ZCResponse toReturn = new ZCResponse();
		try {
			JSONArray jArray = new JSONArray(response);
			for (int i=0; i < jArray.length(); i++) {
				List<ZCChoice> choiceValues = new ArrayList<ZCChoice>();
				String subFormName = null;
				String fieldName=null;
				ZCField field=null;
				ZCRecordValue recordValue=null;
				ZCField subFormField = null;
				String formName=null;
				String alertMessage=null;
				String value = null;
				List<String> keys = new ArrayList<String>();
				ZCChoice zcChoice = null;
				int rowNo = -1;
				//List<ZCRecordValue> subFormTempRecordValues  = null;
				JSONObject jsonObj = jArray.getJSONObject(i); // Pulling items from the array 
				if(jsonObj.has("task"))
				{
					type = jsonObj.getInt("task");
				}
				if(jsonObj.has("formName"))
				{
					formName = jsonObj.getString("formName");
				}
				if(jsonObj.has("fieldName"))
				{
					fieldName = jsonObj.getString("fieldName");
				}
				if(jsonObj.has("fieldValue"))
				{
					try {
						JSONArray jsonArray = (JSONArray) jsonObj.get("fieldValue");
						for (int j=0; j<jsonArray.length(); j++) {
							keys.add(jsonArray.getString(j));
							choiceValues.add(new ZCChoice(jsonArray.getString(j), jsonArray.getString(j)));
						}
					}
					catch(ClassCastException e) {
						value = (String)jsonObj.get("fieldValue");
					}
				}
				if(jsonObj.has("combinedValue"))
				{
					try {
						JSONArray jsonArray = (JSONArray) jsonObj.get("combinedValue");
						choiceValues = new ArrayList<ZCChoice>();
						for (int j=0; j<jsonArray.length(); j++) {
							choiceValues.add(new ZCChoice(keys.get(j), jsonArray.getString(j)));
						}
					}
					catch(ClassCastException e) {
						zcChoice = new ZCChoice(value,(String)jsonObj.get("combinedValue"));
					}
				}
				if(jsonObj.has("alertValue"))
				{
					alertMessage = jsonObj.getString("alertValue");
				}
				if(jsonObj.has("subFormName"))
				{
					subFormName = jsonObj.getString("subFormName");
				}
				if(jsonObj.has("rowNo"))
				{
					String rowNum = jsonObj.getString("rowNo");
					rowNo = Integer.parseInt(rowNum.substring(rowNum.indexOf("_")+1));
				}
				if(jsonObj.has("infoValue"))
				{
					JSONArray jsonArray = (JSONArray) jsonObj.get("infoValue");
					for (int j=0; j<jsonArray.length(); j++) {
						infoValues.add(jsonArray.getString(j));
					}
				}
				if(jsonObj.has("urlString"))
				{
					openUrlString = jsonObj.getString("urlString");
					currentShownForm.setOpenUrl(openUrlString);
				}
				//				if(jsonObj.has("errors"))
				//				{
				//					toReturn.setError(true);
				//					toReturn.setMainErrorMessage("Invalid entries found. Please correct and submit again.");//No I18N        
				//					JSONArray jsonArray = (JSONArray) jsonObj.get("errors");
				//					for (int j=0; j<jsonArray.length(); j++) {
				//						String[] errorMessageField = jsonArray.getString(j).split(",");
				//						toReturn.addErrorMessage(form.getField(errorMessageField[0]),errorMessageField[1] );
				//					}                
				//				}
				if(jsonObj.has("message"))
				{
					currentShownForm.setErrorMessage(jsonObj.getString("message"));
				}
				if(fieldName != null) 
				{
					ZCField baseSubFormField = currentShownForm.getBaseSubFormField();
					ZCForm baseForm = null;
					if(baseSubFormField == null) 
					{
						baseForm = currentShownForm;
					} 
					else 
					{
						baseForm = baseSubFormField.getBaseForm();
					}

					if(subFormName == null)
					{
						field = baseForm.getField(fieldName);
						field.setRebuildRequired(true);
						recordValue = field.getRecordValue();
					}
					else
					{
						subFormField = baseForm.getField(subFormName);
						ZCForm subForm = subFormField.getSubForm();
						field = subForm.getField(fieldName);
						if(field!=null)
						{
							field.setRebuildRequired(true);
							recordValue = field.getRecordValue();
						}
					}

				}
				if(field!=null)
				{
					if(type==ZCForm.TASK_HIDE) {
						field.setHidden(true);
					} else if(type==ZCForm.TASK_SHOW) {
						field.setHidden(false);
					} else if(type==ZCForm.TASK_ENABLE) {
						field.setDisabled(false);
					} else if(type==ZCForm.TASK_DISABLE) {
						field.setDisabled(true);
					} else if(type==ZCForm.TASK_CLEAR) {
						recordValue.clearChoices();
						recordValue.setLastReachedForChoices(true);
					} else if(type==ZCForm.TASK_ADDVALUE) {
						recordValue.appendChoices(choiceValues);
						recordValue.setLastReachedForChoices(true);
					} else if(type==ZCForm.TASK_SELECT) {
						if(FieldType.isMultiChoiceField(field.getType())) {
							if(subFormName==null) {
								recordValue.addToValues(getAvailableChoices(choiceValues, recordValue));
							}
						} else if(FieldType.isSingleChoiceField(field.getType())) {
							if(choiceValues.size()>0)
							{
								recordValue.setChoiceValue(choiceValues.get(0));
							}
						}
					} 
					else if(type==ZCForm.TASK_SELECTALL) {
						if(FieldType.isMultiChoiceField(field.getType())) {
							List<ZCChoice> choices = recordValue.getChoices();
							choiceValues = new ArrayList<ZCChoice>();
							for(int k=0; k<choices.size(); k++) {
								choiceValues.add(choices.get(k));
							}
							recordValue.setChoiceValues(choiceValues);
						} 
					} else if(type==ZCForm.TASK_DESELECT) {
						if(FieldType.isMultiChoiceField(field.getType())) {
							recordValue.removeFromValues(choiceValues);
						} else {
							recordValue.setValue(null);
						}
					} else if(type==ZCForm.TASK_DESELECTALL) {
						if(FieldType.isMultiChoiceField(field.getType())) {
							recordValue.setChoiceValues(new ArrayList<ZCChoice>());
						}
					} else if(type==ZCForm.TASK_SETVALUE) {
						List<ZCRecordValue> zcRecordValues = new ArrayList<ZCRecordValue>();
						List<ZCRecordValue> currentShownSubFormValues = null;
						if(rowNo>0) 
						{
							List<ZCRecord> records = subFormField.getUpdatedSubFormEntries();
							records.addAll(subFormField.getAddedSubFormEntries());
							ZCRecord zcRecord = records.get(rowNo-1);
							zcRecordValues = zcRecord.getValues();
							ZCField subFormBaseField = currentShownForm.getBaseSubFormField();
							if(subFormBaseField != null )
							{
								if(rowNo == subFormBaseField.getSubFormEntryPosition()+1)
								{
									currentShownSubFormValues = currentShownForm.getRecordValues();
									setValueInRecordValues(currentShownSubFormValues,field,choiceValues,zcChoice,value);
								}
							}
						}
						else
						{
							zcRecordValues.add(recordValue);
						}
						setValueInRecordValues(zcRecordValues,field,choiceValues,zcChoice,value);
					}
				}if(type==ZCForm.TASK_ALERT) {
					alertMessages.add(alertMessage);
				}else if(type==ZCForm.TASK_RELOADFORM) {
					currentShownForm.setReLoadForm(true);
					break;
				} 
				if(subFormName==null)
				{
					if(field != null && type==ZCForm.TASK_SETVALUE|| type==ZCForm.TASK_DESELECTALL ||type==ZCForm.TASK_DESELECT || type==ZCForm.TASK_SELECTALL || type==ZCForm.TASK_SELECT) {
						if(field.isHasOnUserInputForFormula()) {
							field.onUserInputForFormula(currentShownForm);
						}
						if(field.isHasOnUserInput()) {
							field.onUserInput(currentShownForm);
						}
					}
				}
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(alertMessages.size() >0) {
			currentShownForm.setAlertMessages(alertMessages);
		}
		if(infoValues.size()>0)
		{
			currentShownForm.setInfos(infoValues);
		}
		if(openUrlString!=null)
		{
			currentShownForm.setOpenUrl(openUrlString);

		}

		return toReturn;
	}

	static void setValueInRecordValues(List<ZCRecordValue> zcRecordValues,ZCField field,List<ZCChoice> choiceValues,ZCChoice zcChoice,String value)
	{
		for(int l=0;l<zcRecordValues.size();l++)
		{
			ZCRecordValue zcRecordValue = zcRecordValues.get(l);

			if(zcRecordValue.getField().getFieldName().equals(field.getFieldName()))
			{
				if(FieldType.isMultiChoiceField(field.getType())) {
					if(field.isLookup())
					{    
						zcRecordValue.appendChoices(choiceValues);
						zcRecordValue.setChoiceValues(choiceValues);
					}else
					{
						zcRecordValue.setChoiceValues(getAvailableChoices(choiceValues, zcRecordValue));
					}
					break;
				}
				else if(FieldType.isSingleChoiceField(field.getType()))
				{
					List<ZCChoice> zcChoices = new ArrayList<ZCChoice>();
					if(zcChoice!=null)
					{
						if(field.isLookup())
						{    
							zcChoices.add(zcChoice);
							zcRecordValue.appendChoices(zcChoices);
							zcRecordValue.setChoiceValue(zcChoice);
						}
						else
						{
							zcRecordValue.setChoiceValue(getAvailableChoice(zcChoice,zcRecordValue));
						}	
					}
					else
					{
						if(value != null)
						{
							ZCChoice choice = new ZCChoice(value, value);
							if(field.isLookup())
							{    
								zcChoices.add(choice);
								zcRecordValue.appendChoices(zcChoices);
								zcRecordValue.setChoiceValue(choice);
							}else
							{
								zcRecordValue.setChoiceValue(getAvailableChoice(choice,zcRecordValue));
							}
						}
						else
						{
							zcRecordValue.setChoiceValue(null);
						}
					}
					break; 

				}
				else
				{
					if(zcRecordValue.getField().getType().equals(FieldType.NOTES)){
						if(value.contains("<img")){
							int indexOfImgTag = value.indexOf("<img");
							int indexOfImgSrc = value.indexOf("src", indexOfImgTag);
							int indexOfstartVal = value.indexOf("\"", indexOfImgSrc);
							int count = 0;
							for(int m=indexOfstartVal+1;;m++){
								if(value.charAt(m) != '\"'){
									count++;
								}else{
									break;
								}
							}
							String substring = value.substring(indexOfstartVal+1, indexOfstartVal+count+1);
							String[] tokens = substring.split("/");
							String urlForImg = ZOHOCreator.getFileUploadURL(tokens[tokens.length-1], tokens[1], tokens[2], tokens[3]);
							value = value.replace(substring, urlForImg);
						}
					}
					if(!(FieldType.isChoiceField(zcRecordValue.getField().getType())))
					{
						zcRecordValue.setValue(value);
					}
					break;
				}
			}
		} 
	}

	private static ZCChoice getAvailableChoice(ZCChoice zcChoice, ZCRecordValue recValue) {
		List<ZCChoice> fieldChoices = recValue.getChoices();
		for(int i=0; i<fieldChoices.size(); i++) {
			ZCChoice choice = fieldChoices.get(i);
			if(choice.getKey().equals(zcChoice.getKey())) {
				return choice;
			}
		}
		return null;
	}

	private static List<ZCChoice> getAvailableChoices(List<ZCChoice> zcChoices,ZCRecordValue recValue){
		List<ZCChoice> fieldChoices = recValue.getChoices();
		List<ZCChoice> availableChoices = new ArrayList<ZCChoice>();
		for(int i=0; i<fieldChoices.size(); i++) {
			ZCChoice choice = fieldChoices.get(i);
			for(int j=0;j<zcChoices.size();j++){
				ZCChoice zcChoice = zcChoices.get(j); 
				if(choice.getKey().equals(zcChoice.getKey())) {
					availableChoices.add(zcChoice);
					break;
				}
			}	
		}
		return availableChoices;	
	}
	static String parseForTokenForExternalField(String response){
		String toReturn = "";
		try {
			JSONObject jsonObject = new JSONObject(response);
			if(jsonObject.has("access_token"))
			{
				toReturn = jsonObject.getString("access_token");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toReturn;
	}

	static List<ZCApplication> parseForApplicationList(String response) throws ZCException {
		List<ZCApplication> toReturn = new ArrayList<ZCApplication>();
		String appOwner = "";
		String remainingDays = "";
		boolean licenceEnabled = true;
		try {
			JSONObject resultObj =new JSONObject(response);
			if(resultObj.has("result"))
			{
				String resultObjChildResponse = resultObj.getString("result");

				JSONObject resultObjChild = new JSONObject(resultObj.getString("result"));
				if(resultObjChild.has("application_owner"))
				{
					appOwner = resultObjChild.getString("application_owner");
				}
				if(resultObjChild.has("application_list"))
				{
					JSONObject applicationListChildObj = new JSONObject(resultObjChild.getString("application_list"));
					if(applicationListChildObj.has("applications"))
					{
						JSONArray applicationsListJArray = new JSONArray(applicationListChildObj.getString("applications"));
						for(int i=0;i<applicationsListJArray.length();i++)
						{
							JSONObject applicationsObj = applicationsListJArray.getJSONObject(i);
							if(applicationsObj.has("application"))
							{
								JSONArray applicationJArray = new JSONArray(applicationsObj.getString("application"));
								for(int j=0;j<applicationJArray.length();j++)
								{
									String appName = null;
									String linkName = null;
									Date createdTime = null;
									boolean isPrivate = true;
									JSONObject applicationObj = applicationJArray.getJSONObject(j);
									if(applicationObj.has("application_name"))
									{
										appName = applicationObj.getString("application_name");
									}
									if(applicationObj.has("link_name"))
									{
										linkName = applicationObj.getString("link_name");
									}
									if(applicationObj.has("access"))
									{
										isPrivate = applicationObj.getString("access").equals("private");
									}
									if(applicationObj.has("created_time"))
									{
										String s =applicationObj.getString("created_time");
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
										try {
											createdTime = simpleDateFormat.parse(s);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									if(applicationObj.has("sharedBy"))
									{
										appOwner = applicationObj.getString("sharedBy");
									}
									ZCApplication zcApp = new ZCApplication(appOwner, appName, linkName, isPrivate, createdTime);
									toReturn.add(zcApp);
								}	
							}
						}
					}
				}
				if(resultObjChild.has("license_enabled"))
				{
					licenceEnabled = resultObjChild.getBoolean("license_enabled");			
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
}