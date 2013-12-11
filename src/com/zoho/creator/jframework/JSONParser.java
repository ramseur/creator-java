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
		int type = -1;
		ZCResponse toReturn = new ZCResponse();
		String openUrlString = null;
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
							choiceValues.add(new ZCChoice(jsonArray.getString(j), jsonArray.getString(j)));
						}
					}
					catch(ClassCastException e) {
						value = (String)jsonObj.get("fieldValue");
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
					rowNo = Integer.parseInt(jsonObj.getString("rowNo").substring(7));
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
				if(jsonObj.has("errors"))
				{
					toReturn.setError(true);
					toReturn.setMainErrorMessage("Invalid entries found. Please correct and submit again.");//No I18N        
					JSONArray jsonArray = (JSONArray) jsonObj.get("errors");
					for (int j=0; j<jsonArray.length(); j++) {
						String[] errorMessageField = jsonArray.getString(j).split(",");
						toReturn.addErrorMessage(form.getField(errorMessageField[0]),errorMessageField[1] );
					}                
				}
				if(fieldName!=null&& subFormName ==null)
				{
					field=form.getField(fieldName);
					field.setRebuildRequired(true);
					recordValue = field.getRecordValue();
				}
				else if(subFormName!=null)
				{
					subFormField = form.getField(subFormName);
					ZCForm subForm = subFormField.getSubForm();
					if(currentShownForm!=null)
					{
						subForm = currentShownForm;
					}
					field = subForm.getField(fieldName);
					recordValue = field.getRecordValue();
					field.setRebuildRequired(true);
				}
				if(type==ZCForm.task_hide) {
					field.setHidden(true);
				} else if(type==ZCForm.task_show) {
					field.setHidden(false);
				} else if(type==ZCForm.task_enable) {
					field.setDisabled(false);
				} else if(type==ZCForm.task_disable) {
					field.setDisabled(true);
				} else if(type==ZCForm.task_clear) {
					field.clearChoices();
					field.setLastReachedForChoices(true);
				} else if(type==ZCForm.task_addValue) {
					List<ZCChoice> moreChoices = new ArrayList<ZCChoice>();
					for(int k=0; k<choiceValues.size(); k++) {
						ZCChoice choice = new ZCChoice(choiceValues.get(k).getKey(), choiceValues.get(k).getValue());
						moreChoices.add(choice);
					}
					field.appendChoices(moreChoices);
					field.setLastReachedForChoices(true);
				} else if(type==ZCForm.task_select) {
					if(FieldType.isMultiChoiceField(field.getType())) {
						if(subFormName==null) {
							recordValue.addToValues(choiceValues);
						}
					} else if(FieldType.isSingleChoiceField(field.getType())) {
						recordValue.setChoiceValue(choiceValues.get(0));
					}
				} 
				else if(type==ZCForm.task_selectAll) {
					if(FieldType.isMultiChoiceField(field.getType())) {
						List<ZCChoice> choices = field.getChoices();
						choiceValues = new ArrayList<ZCChoice>();
						for(int k=0; k<choices.size(); k++) {
							choiceValues.add(choices.get(k));
						}
						recordValue.setChoiceValues(choiceValues);
					} 
				} else if(type==ZCForm.task_deselect) {
					if(FieldType.isMultiChoiceField(field.getType())) {
						recordValue.removeFromValues(choiceValues);
					} else {
						recordValue.setValue(null);
					}
				} else if(type==ZCForm.task_deselectAll) {
					if(FieldType.isMultiChoiceField(field.getType())) {
						recordValue.setChoiceValues(new ArrayList<ZCChoice>());
					}
				} else if(type==ZCForm.task_alert) {
					alertMessages.add(alertMessage);
				} else if(type==ZCForm.task_reloadForm) {
					form.setReLoadForm(true);
					break;
				} else if(type==ZCForm.task_setValue) {

					if(rowNo>0&&subFormField.getAddedSubFormEntries().size()>=rowNo)
					{
						ZCRecord zcRecord =  subFormField.getAddedSubFormEntries().get(rowNo-1);
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
										recordValue.setChoiceValue(new ZCChoice(value,value));
									}
									else
									{
										subFormRecordValue.setChoiceValue(new ZCChoice(value,value));
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
									subFormRecordValue.setChoiceValue(new ZCChoice(value,value));
									break;
								}
								else
								{
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
							recordValue.setChoiceValue(new ZCChoice(value,value));
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
					field.setLastReachedForChoices(true); 
				}
				if(subFormName==null)
				{
					if(field != null && type==ZCForm.task_setValue|| type==ZCForm.task_deselectAll ||type==ZCForm.task_deselect || type==ZCForm.task_selectAll || type==ZCForm.task_select) {
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