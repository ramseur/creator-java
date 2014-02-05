package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONParser {

	static ZCResponse parseAndCallFormEvents(String response, ZCForm form,List<ZCRecordValue> subFormTempRecordValues,ZCForm currentShownForm) throws ZCException
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
				ZCField editSubFormField = null;
				ZCRecordValue recordValue=null;
				ZCField subFormField = null;
				String formName=null;
				String alertMessage=null;
				String value = null;
				List<String> keys = new ArrayList<String>();
				ZCChoice zcChoice = null;
				int rowNo = -1;
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
					System.out.println("number...row..."+rowNo);
//					if(rowNum.contains("t::row"))
//					{
//						rowNo = Integer.parseInt(jsonObj.getString("rowNo").substring(7));
//					}
//					else
//					{
//						
//					}	
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
					form.setOpenUrl(openUrlString);
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
					form.setErrorMessage(jsonObj.getString("message"));
				}
				
				
				if(subFormName!=null)
				{
					
					
				} else 
				{
					
				}
				if(fieldName != null) 
				{
					if(subFormName ==null)
					{
						field=form.getField(fieldName);
						field.setRebuildRequired(true);
						recordValue = field.getRecordValue();
					}
					else
					{
						subFormField = form.getField(subFormName);
						ZCForm subForm = subFormField.getSubForm();
						if(currentShownForm!=null)
						{
							subForm = currentShownForm;
						}
						field = subForm.getField(fieldName);
						recordValue = field.getRecordValue();

						
						if(subFormTempRecordValues != null)
						{
							for(int j=0; j<subFormTempRecordValues.size(); j++) 
							{
								ZCRecordValue subFormRecValue = subFormTempRecordValues.get(j);
								if(subFormRecValue.getField().getFieldName().equals(fieldName))
								{
									recordValue = subFormRecValue;
									break;
								}
							}
						}
						
						
						field.setRebuildRequired(true);
						List<ZCField> subformFields = subForm.getFields();
						List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
						for(int l=0;l<subformFields.size();l++)
						{
							recordValues.add(subformFields.get(l).getRecordValue());	
						}
						ZCForm editSubform = subFormField.getEditSubForm(new ZCRecord(recordValues));
						editSubFormField = editSubform.getField(fieldName);

					}

				}
				if(type==ZCForm.TASK_HIDE) {
					field.setHidden(true);
					if(editSubFormField!=null)
					{
						editSubFormField.setHidden(true);
					}
				} else if(type==ZCForm.TASK_SHOW) {
					field.setHidden(false);
					if(editSubFormField!=null)
					{
						editSubFormField.setHidden(false);
					}
				} else if(type==ZCForm.TASK_ENABLE) {
					field.setDisabled(false);
					if(editSubFormField!=null)
					{
						editSubFormField.setDisabled(false);
					}
				} else if(type==ZCForm.TASK_DISABLE) {
					field.setDisabled(true);
					if(editSubFormField!=null)
					{
						editSubFormField.setDisabled(true);
					}
				} else if(type==ZCForm.TASK_CLEAR) {
					recordValue.clearChoices();
					recordValue.setLastReachedForChoices(true);
				} else if(type==ZCForm.TASK_ADDVALUE) {
//					List<ZCChoice> moreChoices = new ArrayList<ZCChoice>();
//					for(int k=0; k<choiceValues.size(); k++) {
//						ZCChoice choice = new ZCChoice(choiceValues.get(k).getKey(), choiceValues.get(k).getValue());
//						moreChoices.add(choice);
//					}
					System.out.println("choicevalues...."+choiceValues);
					recordValue.appendChoices(choiceValues);
					recordValue.setLastReachedForChoices(true);
				} else if(type==ZCForm.TASK_SELECT) {
					if(FieldType.isMultiChoiceField(field.getType())) {
						if(subFormName==null) {
							recordValue.addToValues(choiceValues);
						}
					} else if(FieldType.isSingleChoiceField(field.getType())) {
						recordValue.setChoiceValue(choiceValues.get(0));
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
				} else if(type==ZCForm.TASK_ALERT) {
					alertMessages.add(alertMessage);
				} else if(type==ZCForm.TASK_RELOADFORM) {
					form.setReLoadForm(true);
					break;
				} else if(type==ZCForm.TASK_SETVALUE) {
					if(rowNo>0&&subFormField.getSubFormEntriesSize()>=rowNo)
					{
						List<ZCRecord> records = subFormField.getUpdatedSubFormEntries();
						records.addAll(subFormField.getAddedSubFormEntries());
						ZCRecord zcRecord =  records.get(rowNo-1);
						List<ZCRecordValue> zcRecordValues = zcRecord.getValues();
						for(int l=0;l<zcRecordValues.size();l++)
						{
							ZCRecordValue subFormRecordValue = zcRecordValues.get(l);
							if(subFormRecordValue.getField().getFieldName().equals(field.getFieldName()))
							{
								if(FieldType.isMultiChoiceField(field.getType())) {
									if(currentShownForm!=null && subFormTempRecordValues == null)
									{
										recordValue.setChoiceValues(choiceValues);
									}
									else
									{
										subFormRecordValue.setChoiceValues(choiceValues);
									}
									break;
								}
								else if(FieldType.isSingleChoiceField(field.getType()))
								{
									if(currentShownForm!=null && subFormTempRecordValues == null)
									{
										if(zcChoice!=null)
										{
											recordValue.setChoiceValue(zcChoice);
										}
										else
										{
											recordValue.setChoiceValue(new ZCChoice(value,value));
										}
									}
									else
									{
										if(zcChoice!=null)
										{
											subFormRecordValue.setChoiceValue(zcChoice);
										}
										else
										{
											subFormRecordValue.setChoiceValue(new ZCChoice(value,value));
										}
									}
									break; 
								}
								else
								{
									if(currentShownForm!=null && subFormTempRecordValues == null)
									{
										recordValue.setValue(value);
									}
									else
									{
										subFormRecordValue.setValue(value);
									}
									break;
								}
							}
						}        
					}
					else if(rowNo>0)
					{
						for(int l=0;l<subFormTempRecordValues.size();l++)
						{
							ZCRecordValue subFormRecordValue =subFormTempRecordValues.get(l);
							if(subFormRecordValue.getField().getFieldName().equals(field.getFieldName()))
							{
								if(FieldType.isMultiChoiceField(field.getType())) {
									subFormRecordValue.setChoiceValues(choiceValues);
									break;
								}
								else if(FieldType.isSingleChoiceField(field.getType()))
								{
									if(zcChoice!=null)
									{
										subFormRecordValue.setChoiceValue(zcChoice);
									}else
									{
										subFormRecordValue.setChoiceValue(new ZCChoice(value,value));
									}
									break;
								}
								else
								{
									System.out.println("setValue...");
									subFormRecordValue.setValue(value);
									break;
								}
							}
						}
					}
					else
					{
						if(FieldType.isMultiChoiceField(field.getType())) {
							recordValue.setChoiceValues(choiceValues);
						}
						else if(FieldType.isSingleChoiceField(field.getType()))
						{
							if(zcChoice!=null)
							{
								recordValue.setChoiceValue(zcChoice);
							}
							else
							{
							recordValue.setChoiceValue(new ZCChoice(value,value));
							}
						}
						else
						{
							if(recordValue.getField().getType().equals(FieldType.NOTES)){
								if(value.contains("<img")){
									int indexOfImgTag = value.indexOf("<img");
									int indexOfImgSrc = value.indexOf("src", indexOfImgTag);
									int indexOfstartVal = value.indexOf("\"", indexOfImgSrc);
									int count = 0;
									for(int l=indexOfstartVal+1;;l++){
										if(value.charAt(l) != '\"'){
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
							recordValue.setValue(value);
						}
					}
				}
				if(subFormName==null)
				{
					if(field != null && type==ZCForm.TASK_SETVALUE|| type==ZCForm.TASK_DESELECTALL ||type==ZCForm.TASK_DESELECT || type==ZCForm.TASK_SELECTALL || type==ZCForm.TASK_SELECT) {
						if(field.isHasOnUserInputForFormula()) {
							field.onUserInputForFormula(subFormTempRecordValues);
						}
						if(field.isHasOnUserInput()) {
							field.onUserInput(subFormTempRecordValues);
						}
					}
				}
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(alertMessages.size() >0) {
			if(currentShownForm!=null)
			{
				currentShownForm.setAlertMessages(alertMessages);
			}
			else
			{
				form.setAlertMessages(alertMessages);
			}
		}
		if(infoValues.size()>0)
		{
			if(currentShownForm!=null)
			{
				currentShownForm.setInfos(infoValues);
			}
			else
			{
				form.setInfos(infoValues);
			}
		}
		if(openUrlString!=null)
		{
			if(currentShownForm!=null)
			{
				currentShownForm.setOpenUrl(openUrlString);
			}
			else
			{
				form.setOpenUrl(openUrlString);
			}

		}

		return toReturn;
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
}