package com.zoho.creator.jframework;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {

	public static final int PERSONAL_APPS = 1;
	public static final int SHARED_APPS = 2;
	public static final int WORKSPACE_APPS = 3;
	static List<JSONObject> onUserInputJsonObj = new ArrayList<JSONObject>();

	private static ResourceBundle resourceString = ResourceBundle.getBundle("ResourceString", Locale.getDefault());

	static ZCResponse parseAndCallFormEvents(String response, ZCForm currentShownForm,boolean doesOnUserInputRetriggered) throws ZCException
	{
		List<String> alertMessages = new ArrayList<String>();
		List<String> infoValues = new ArrayList<String>();
		String openUrlString = null;
		if(!doesOnUserInputRetriggered)
		{
			onUserInputJsonObj = new ArrayList<JSONObject>();
		}
		ZCResponse toReturn = new ZCResponse();
		try {
			JSONArray jArray = new JSONArray(response);
			for (int i=0; i < jArray.length(); i++) 
			{
				JSONObject jsonObj = jArray.getJSONObject(i); // Pulling items from the array 
				if(!doesOnUserInputRetriggered)
				{
					if(parseJsonObject(jsonObj,currentShownForm,alertMessages,infoValues,openUrlString))
					{
						break;
					}
				}else
				{
					onUserInputJsonObj.add(jsonObj);
				}

			}
			if(!doesOnUserInputRetriggered)
			{
				for(int i=0;i<onUserInputJsonObj.size();i++)
				{
					JSONObject jsonObj = onUserInputJsonObj.get(i);
					if(parseJsonObject(jsonObj,currentShownForm,alertMessages,infoValues,openUrlString))
					{
						break;
					}
					onUserInputJsonObj.remove(i);
					i--;
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



	//	public static void evaluateRuleActions(ZCField zcField,HashMap<String,Object> valuesHashMap)
	//	{
	//		{
	//			CriteriaExecutor cExec = new CriteriaExecutor();
	//
	//			boolean isConditionTrue = false;
	//			List<ZCRule> fieldRules =  zcField.getFieldRules();
	//			
	//			for(int i=0;i<fieldRules.size();i++)
	//			{
	//				ZCRule rule = fieldRules.get(i);
	//				String ruleCondition = rule.getCondition();
	//
	//				
	//				Set set = valuesHashMap.keySet();
	//				Iterator itrtr = set.iterator();
	//				while(itrtr.hasNext())
	//				{
	//					String key = (String) itrtr.next();
	//					
	//				}
	//
	//				if(ruleCondition!=null&&ruleCondition.length()>0)
	//				{
	//					try {
	//						isConditionTrue = cExec.evaluateCriteria(rule.getCondition(), valuesHashMap);
	//					} catch (Exception e) {
	//						// TODO Auto-generated catch block
	//						e.printStackTrace();
	//					}
	//					
	//					executeRuleActions(rule.getZCTasks(), valuesHashMap,isConditionTrue,zcField.getBaseForm());
	//				}
	//			}
	//		} 
	//	}




	public static void executeRuleActions(List<ZCTask> tasks,HashMap<String,Object> valuesHashMap,boolean isConditionTrue,ZCForm loadedForm)
	{
		for(int i=0;i<tasks.size();i++)
		{
			ZCTask task = tasks.get(i);
			int taskType = task.getZCTaskType();
			List<String> fieldNames = task.getFieldNames();
			HashMap<String,String> setValuesHashMap = task.getSetValuesHashMap();
			Set set = setValuesHashMap.keySet();
			Iterator itrtr = set.iterator();
			while(itrtr.hasNext())
			{
				String key = (String) itrtr.next();

			}

			for(int j=0;j<fieldNames.size();j++)
			{
				String fieldName = fieldNames.get(j);
				ZCField zcfield = loadedForm.getField(fieldName);
				zcfield.setRebuildRequired(true);

				if(taskType==ZCTask.SET_FIELD_VALUE)
				{

					FieldType fType = zcfield.getType();
					ZCRecordValue recValue = zcfield.getRecordValue();
					ZCRecordValue previousValue = zcfield.getPreviousRecordValue();
					if(FieldType.isChoiceField(fType))
					{

						List<ZCChoice> zcChoices = recValue.getChoices();
						for(int k=0;k<zcChoices.size();k++)
						{
							ZCChoice zcChoice = zcChoices.get(j);
							if(setValuesHashMap.get(fieldName).equals(zcChoice.getKey()))
							{
								if(FieldType.isSingleChoiceField(fType))
								{
									if(isConditionTrue)
									{
										recValue.setChoiceValue(zcChoice);
									}
									else
									{
										recValue.setChoiceValue(previousValue.getChoiceValue());
									}
								}
								else if(FieldType.isMultiChoiceField(fType))
								{
									List<ZCChoice> setValueZCChoices = new ArrayList<ZCChoice>();
									setValueZCChoices.add(zcChoice);
									if(isConditionTrue)
									{
										recValue.setChoiceValues(setValueZCChoices);
									}
									else
									{
										recValue.setChoiceValues(previousValue.getChoiceValues());
									}
								}
								break;
							}
						}
					}
					else
					{

						if(isConditionTrue)
						{

							recValue.setValue(setValuesHashMap.get(fieldName));
						}
						else
						{

							recValue.setValue(previousValue.getValue());
						}
					}
				}
				else if(taskType==ZCTask.HIDE_FIELDS)
				{
					if(isConditionTrue)
					{
						zcfield.setHidden(true);
					}else
					{
						zcfield.setHidden(false);
					}
				}else if(taskType==ZCTask.DISABLE_FIELDS)
				{
					if(isConditionTrue)
					{
						zcfield.setDisabled(true);
					}else
					{
						zcfield.setDisabled(false);
					}
				}
				else if(taskType==ZCTask.ENABLE_FIELDS)
				{
					if(isConditionTrue)
					{
						zcfield.setDisabled(false);
					}else
					{
						zcfield.setDisabled(true);
					}
				}
				else if(taskType==ZCTask.SHOW_FIELDS)
				{
					if(isConditionTrue)
					{
						zcfield.setHidden(false);
					}else
					{
						zcfield.setHidden(true); 
					}
				}
				else if(taskType==ZCTask.HIDE_SUBFROM_ADD_ENTRY)
				{
					if(isConditionTrue)
					{
						zcfield.setSubFormAddEntryHidden(true);
					}else
					{
						zcfield.setSubFormAddEntryHidden(false);
					}
				}
				else if(taskType==ZCTask.SHOW_SUBFORM_ADD_ENTRY)
				{
					if(isConditionTrue)
					{
						zcfield.setSubFormAddEntryHidden(false);
					}else
					{
						zcfield.setSubFormAddEntryHidden(true);
					}
				}
				else if(taskType==ZCTask.HIDE_SUBFORM_DELETE_ENTRY)
				{
					if(isConditionTrue)
					{
						zcfield.setSubFormDeleteEntryHidden(true);
					}else
					{
						zcfield.setSubFormDeleteEntryHidden(false);
					}
				}
				else if(taskType==ZCTask.SHOW_SUBFORM_DELETE_ENTRY)
				{
					if(isConditionTrue)
					{
						zcfield.setSubFormDeleteEntryHidden(false);
					}else
					{
						zcfield.setSubFormDeleteEntryHidden(true);
					}
				}
			}
		}
	}

	static List<ZCRule> parseForRules(JSONArray rulesArray,List<ZCField> zcFields) throws ZCException
	{
		List<ZCRule> zcRules = new ArrayList<ZCRule>();
		if(rulesArray.length()>0)
		{
			throw new ZCException(resourceString.getString("this_form_contains_rules_which_is_currently_not_supported"), ZCException.ERROR_OCCURED,"" );
		}
		try 
		{
			for(int count=0;count<rulesArray.length();count++)
			{

				JSONObject rulesObj = rulesArray.getJSONObject(count);
				String condition = "";
				List<String> fieldNamesWithCriteria = new ArrayList<String>();
				List<ZCTask> zcTasks = new ArrayList<ZCTask>();
				List<ZCField> zcFieldswithRules = new ArrayList<ZCField>();

				if(rulesObj.has("CONDITION"))
				{
					condition = rulesObj.getString("CONDITION");

				}
				if(rulesObj.has("CONDITION_FIELDS"))
				{
					JSONArray fieldNamesArray = (JSONArray)rulesObj.get("CONDITION_FIELDS");
					for(int i=0;i<fieldNamesArray.length();i++)
					{
						for(int fieldSize = 0;fieldSize<zcFields.size();fieldSize++)
						{
							ZCField zcField = zcFields.get(fieldSize);
							if(zcField.getFieldName().equals((String)fieldNamesArray.get(i)))
							{
								zcFieldswithRules.add(zcField);
							}
						}
						fieldNamesWithCriteria.add((String)fieldNamesArray.get(i));
					}
				}
				if(rulesObj.has("TASKS"))
				{
					JSONArray tasksArray = (JSONArray)rulesObj.get("TASKS");

					for(int i=0;i<tasksArray.length();i++)
					{
						int taskType = 0;
						List<String> fieldNames = new ArrayList<String>();
						HashMap<String,String> setValuesHashMap = new HashMap<String,String>();
						JSONObject taskObj = (JSONObject) tasksArray.get(i);
						if(taskObj.has("TASK_TYPE"))
						{
							taskType = taskObj.getInt("TASK_TYPE");
						}
						if(taskObj.has("TASK_CONFIG"))
						{
							JSONObject taskConfigObj = taskObj.getJSONObject("TASK_CONFIG");
							if(taskConfigObj.has("FIELDS"))
							{
								JSONArray fieldsArrayObj = taskConfigObj.getJSONArray("FIELDS"); 

								for(int j=0;j<fieldsArrayObj.length();j++)
								{
									fieldNames.add(fieldsArrayObj.getString(j));
								}

							}if(taskConfigObj.has("VALUES"))
							{
								JSONObject valuesJsonObj = taskConfigObj.getJSONObject("VALUES");

								for(int j=0;j<fieldNames.size();j++)
								{
									String fieldName = fieldNames.get(j);
									if(valuesJsonObj.has(fieldName));
									{
										setValuesHashMap.put(fieldName,valuesJsonObj.getString(fieldName));
									}
								}
							}

						}
						ZCTask zcTask = null;
						if(taskType!=ZCTask.SET_FIELD_VALUE)
						{
							zcTask = new ZCTask(taskType, fieldNames);
						}else
						{

							zcTask = new ZCTask(taskType,setValuesHashMap,fieldNames);
						}
						zcTasks.add(zcTask);

					}
				}
				ZCRule rule = new ZCRule(zcTasks,fieldNamesWithCriteria,condition);
				for(int j=0;j<zcFieldswithRules.size();j++){
					zcFieldswithRules.get(j).getFieldRules().add(rule);
				}
				zcRules.add(rule);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zcRules;
	}

	static List<ZCChoice> parseCrmLookupChoices(String response,ZCRecordValue zcRecordValue)
	{

		String moduleType = zcRecordValue.getField().getModuleType();
		List<ZCChoice> choices = new ArrayList<ZCChoice>();
		try {
			JSONObject resObj = new JSONObject(response);
			if(moduleType.equalsIgnoreCase("users"))
			{
				if(resObj.has("users"))
				{
					JSONObject usersObj = (JSONObject)resObj.get("users");
					if(usersObj.has("user"))
					{
						try
						{
							JSONArray jarray = (JSONArray)usersObj.get("user");
							for(int i=0;i<jarray.length();i++)
							{
								JSONObject jObj = jarray.getJSONObject(i);
								parseUserModuleInternalObj(choices, jObj);
							}
						}
						catch(ClassCastException e)
						{
							JSONObject jObj = (JSONObject)usersObj.get("user");
							parseUserModuleInternalObj(choices, jObj);
						}
					}
				}
			}
			else
			{
				if(resObj.has("response"))
				{
					JSONObject resultObj =(JSONObject)resObj.get("response");
					if(resultObj.has("result")){
						JSONObject modObj =(JSONObject)resultObj.get("result");
						if(modObj.has(moduleType))
						{
							JSONObject rowObj =(JSONObject)modObj.get(moduleType);
							if(rowObj.has("row"))
							{
								if(moduleType.equals("Leads"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										System.out.println("leads size.."+rowArray.length());
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmLeadModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmLeadModule(choices, jObj);
									}
								}else if(moduleType.equals("Accounts"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmAccountsModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmAccountsModule(choices, jObj);
									}

								}
								else if(moduleType.equals("Potentials"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmPotentialsModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmPotentialsModule(choices, jObj);
									}

								}else if(moduleType.equals("Contacts"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmContactsModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmContactsModule(choices, jObj);
									}

								}
								else if(moduleType.equals("Campaigns"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmCampaignsModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmCampaignsModule(choices, jObj);
									}

								}
								else if(moduleType.equals("Cases"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmCasesModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmCasesModule(choices, jObj);
									}

								}
								else if(moduleType.equals("Solutions"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmSolutionsModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmSolutionsModule(choices, jObj);
									}

								}
								else if(moduleType.equals("Products"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmProductsModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmProductsModule(choices, jObj);
									}

								}
								else if(moduleType.equals("PriceBooks"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmPriceBooksModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmPriceBooksModule(choices, jObj);
									}

								}
								else if(moduleType.equals("Quotes"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmQuotesModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmQuotesModule(choices, jObj);
									}

								}
								else if(moduleType.equals("Vendors"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmVendorsModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmVendorsModule(choices, jObj);
									}

								}
								else if(moduleType.equals("PurchaseOrders"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmPurchaseOrdersModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmPurchaseOrdersModule(choices, jObj);
									}

								}
								else if(moduleType.equals("SalesOrders"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmSalesOrdersModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmSalesOrdersModule(choices, jObj);
									}

								}
								else if(moduleType.equals("Invoices"))
								{
									try
									{
										JSONArray rowArray = (JSONArray)rowObj.get("row");
										for(int i=0;i<rowArray.length();i++)
										{
											JSONObject rowArrayObj = rowArray.getJSONObject(i);
											parseCrmInvoicesModule(choices, rowArrayObj);
										}
									}
									catch(ClassCastException e)
									{
										JSONObject jObj = (JSONObject)rowObj.get("row");
										parseCrmInvoicesModule(choices, jObj);
									}

								}
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return choices;
	}


	static void parseUserModuleInternalObj(List<ZCChoice> choices,JSONObject jObj) throws JSONException
	{
		String name = "";
		String email = "";
		String id = "";
		if(jObj.has("content"))
		{
			name =jObj.getString("content");
		}if(jObj.has("email"))
		{
			email = jObj.getString("email");
		}if(jObj.has("id"))
		{
			id = jObj.getString("id");
		}
		choices.add(new ZCChoice(id,name+"-"+email));
	}
	static List<ZCChoice> parseCrmLeadModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {

			String leadID = "";
			String frstName  = "";
			String lastName = "";
			String smCreatorId = "";
			String modifiedById = "";
			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				try
				{
					JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
					for(int j=0;j<flArray.length();j++)
					{
						JSONObject flArrayObj = (JSONObject)flArray.get(j);
						ZCRecordValue recordValue = null;
						if(flArrayObj.has("content"))
						{
							if(flArrayObj.has("val"))
							{
								if(flArrayObj.getString("val").equals("LEADID"))
								{
									leadID = flArrayObj.getString("content");
								}
								else if(flArrayObj.getString("val").equals("First Name"))
								{
									frstName = flArrayObj.getString("content");
								}
								else if(flArrayObj.getString("val").equals("Last Name"))
								{
									lastName = flArrayObj.getString("content");
								}
								else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY"))))
								{
									recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
								}
							}
						}
						if(recordValue!=null)
						{
							recordValues.add(recordValue);
						}
					}
				}
				catch(ClassCastException e)
				{

				}
			}
			if(frstName!=null)
			{
				choices.add(new ZCChoice(recordValues,Long.valueOf(leadID),leadID, frstName+" "+ lastName));
			}else 
			{
				choices.add(new ZCChoice(recordValues,Long.valueOf(leadID),leadID,lastName));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}

	static List<ZCChoice> parseCrmAccountsModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{
		try {

			String accountId = "";
			String accountName = "";
			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("ACCOUNTID"))
							{
								accountId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Account Name"))
							{
								accountName = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Account Name",FieldType.SINGLE_LINE,"Account Name"),accountName );
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			if(accountName!=null)
			{
				choices.add(new ZCChoice(recordValues,Long.valueOf(accountId),accountId, accountName));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}


	static List<ZCChoice> parseCrmPotentialsModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {

			String potentialId = "";
			String potentialName = "";
			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("POTENTIALID"))
							{
								potentialId = flArrayObj.getString("content");
							}else if(flArrayObj.getString("val").equals("Potential Name"))
							{
								potentialName = flArrayObj.getString("content");
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY")))&&(!(flArrayObj.getString("val").equals("CONTACTID")))&&(!(flArrayObj.getString("val").equals("ACCOUNTID")))&&(!(flArrayObj.getString("val").equals("CAMPAIGNID"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}


			if(potentialName!=null)
			{
				choices.add(new ZCChoice(recordValues,Long.valueOf(potentialId),potentialId, potentialName));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}



	static List<ZCChoice> parseCrmContactsModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {


			String contactId = "";
			String frstName  = "";
			String lastName = "";


			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("CONTACTID"))
							{
								contactId = flArrayObj.getString("content");
							}

							else if(flArrayObj.getString("val").equals("First Name"))
							{
								frstName = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Last Name"))
							{
								lastName = flArrayObj.getString("content");
							}

							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY")))&&(!(flArrayObj.getString("val").equals("ACCOUNTID")))&&(!(flArrayObj.getString("val").equals("VENDORID"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}

			if(frstName!=null)
			{

				choices.add(new ZCChoice(recordValues,Long.valueOf(contactId),contactId, frstName+" "+ lastName));
			}else 
			{

				choices.add(new ZCChoice(recordValues,Long.valueOf(contactId),contactId,lastName));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}


	static List<ZCChoice> parseCrmCampaignsModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {

			String campaignName = "";
			String campaignId = "";
			String smOwnerId = "";
			String modifiedById = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("CAMPAIGNID"))
							{
								campaignId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Campaign Name"))
							{
								campaignName = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Campaign Name",FieldType.SINGLE_LINE,"Campaign Name"), flArrayObj.getString("content"));
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}

						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(campaignId),campaignId,campaignName));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}

	static List<ZCChoice> parseCrmCasesModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {

			String subject = "";
			String caseId = "";
			String smOwnerId = "";
			String smCreatorId = "";
			String modifiedById = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("CASEID"))
							{
								caseId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Subject"))
							{
								subject = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Subject",FieldType.SINGLE_LINE,"Subject"), flArrayObj.getString("content"));
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(caseId),caseId,subject));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}


	static List<ZCChoice> parseCrmSolutionsModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{
		try {

			String solutionTitle = "";
			String solutionNumber = "";
			String solutionId = "";
			String solutionOwner = "";
			String smOwnerId = "";
			String smCreatorId = "";
			String modifiedById = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("SOLUTIONID"))
							{
								solutionId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Solution Title"))
							{
								solutionTitle = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Solution Title",FieldType.SINGLE_LINE,"Solution Title"), flArrayObj.getString("content"));
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(solutionId),solutionId,solutionTitle));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}

	static List<ZCChoice> parseCrmProductsModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {
			String productId = "";
			String productName = "";
			String vendorId = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("PRODUCTID"))
							{
								productId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Product Name"))
							{
								productName = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Product Name",FieldType.SINGLE_LINE,"Product Name"), flArrayObj.getString("content"));
							}

							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY")))&&(!(flArrayObj.getString("val").equals("VENDORID"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}



						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(productId),productId,productName));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}

	static List<ZCChoice> parseCrmPriceBooksModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {

			String bookId = "";
			String priceBookName = "";
			String smOwnerId = "";
			String smCreatorId = "";
			String modifiedById = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("BOOKID"))
							{
								bookId = flArrayObj.getString("content");
							}

							else if(flArrayObj.getString("val").equals("Price Book Name"))
							{
								priceBookName = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Price Book Name",FieldType.SINGLE_LINE,"Price Book Name"), flArrayObj.getString("content"));
							}

							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}

						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(bookId),bookId,priceBookName));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}

	static List<ZCChoice> parseCrmQuotesModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {

			String quoteId = "";
			String subject = "";
			String quoteNum = "";
			String potentialId = "";
			String contatcsId = "";
			String accountId = "";
			String smOwnerId = "";
			String smCreatorId = "";
			String modifiedById = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("QUOTEID"))
							{
								quoteId = flArrayObj.getString("content");
							}

							else if(flArrayObj.getString("val").equals("Subject"))
							{
								subject = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Subject",FieldType.SINGLE_LINE,"Subject"), flArrayObj.getString("content"));
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY")))&&(!(flArrayObj.getString("val").equals("ACCOUNTID")))&&(!(flArrayObj.getString("val").equals("CONTACTID")))&&(!(flArrayObj.getString("val").equals("POTENTIALID")))) 
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(quoteId),quoteId,subject));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return choices;
	}

	static List<ZCChoice> parseCrmVendorsModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{
		try {

			String vendorId = "";
			String vendorName = "";
			String smOwnerId = "";
			String smCreatorId = "";
			String modifiedById = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("VENDORID"))
							{
								vendorId = flArrayObj.getString("content");
							}

							else if(flArrayObj.getString("val").equals("Vendor Name"))
							{
								vendorName = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Vendor Name",FieldType.SINGLE_LINE,"Vendor Name"), flArrayObj.getString("content"));
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(vendorId),vendorId,vendorName));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return choices;
	}


	static List<ZCChoice> parseCrmPurchaseOrdersModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {

			String purchaseOrderId = "";
			String vendorId = "";
			String contactId = "";
			String subject = "";
			String smOwnerId = "";
			String smCreatorId = "";
			String modifiedById = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("PURCHASEORDERID"))
							{
								purchaseOrderId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Subject"))
							{
								subject = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Subject",FieldType.SINGLE_LINE,"Subject"), flArrayObj.getString("content"));
							}
							else if(flArrayObj.getString("val").equals("VENDORID"))
							{
								vendorId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("CONTACTID"))
							{
								contactId = flArrayObj.getString("content");
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY")))&&(!(flArrayObj.getString("val").equals("VENDORID")))&&(!(flArrayObj.getString("val").equals("CONTACTID"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}

						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(purchaseOrderId),purchaseOrderId,subject));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}

	static List<ZCChoice> parseCrmSalesOrdersModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {
			String salesOrderId = "";
			String quoteId = "";
			String contactId = "";
			String subject = "";
			String potentialId = "";
			String accountId = "";
			String smOwnerId = "";
			String smCreatorId = "";
			String modifiedById = "";

			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("SALESORDERID"))
							{
								salesOrderId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Subject"))
							{
								subject = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Subject",FieldType.SINGLE_LINE,"Subject"), flArrayObj.getString("content"));
							}
							else if(flArrayObj.getString("val").equals("ACCOUNTID"))
							{
								accountId = flArrayObj.getString("content");
							}else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY")))&&(!(flArrayObj.getString("val").equals("ACCOUNTID")))&&(!(flArrayObj.getString("val").equals("CONTACTID")))&&(!(flArrayObj.getString("val").equals("QUOTEID")))&&(!(flArrayObj.getString("val").equals("POTENTIALID"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(salesOrderId),salesOrderId,subject));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}


	static List<ZCChoice> parseCrmInvoicesModule(List<ZCChoice> choices,JSONObject rowArrayObj)
	{

		try {

			String invoiceId = "";
			String subject = "";
			List<ZCRecordValue> recordValues = new ArrayList<ZCRecordValue>();
			if(rowArrayObj.has("no"))
			{
				rowArrayObj.getInt("no");
			}
			if(rowArrayObj.has("FL"))
			{
				JSONArray flArray = (JSONArray)rowArrayObj.get("FL");
				for(int j=0;j<flArray.length();j++)
				{
					JSONObject flArrayObj = (JSONObject)flArray.get(j);
					ZCRecordValue recordValue = null;
					if(flArrayObj.has("content"))
					{
						if(flArrayObj.has("val"))
						{
							if(flArrayObj.getString("val").equals("INVOICEID"))
							{
								invoiceId = flArrayObj.getString("content");
							}
							else if(flArrayObj.getString("val").equals("Subject"))
							{
								subject = flArrayObj.getString("content");
								recordValue = new ZCRecordValue(new ZCField("Subject",FieldType.SINGLE_LINE,"Subject"), flArrayObj.getString("content"));
							}
							else if(!(flArrayObj.getString("val").equals("SMOWNERID"))&&(!(flArrayObj.getString("val").equals("SMCREATORID")))&&(!(flArrayObj.getString("val").equals("MODIFIEDBY")))&&(!(flArrayObj.getString("val").equals("SALESORDERID")))&&(!(flArrayObj.getString("val").equals("CONTACTID")))&&(!(flArrayObj.getString("val").equals("QUOTEID")))&&(!(flArrayObj.getString("val").equals("POTENTIALID"))))
							{
								recordValue = new ZCRecordValue(new ZCField(flArrayObj.getString("val"),FieldType.SINGLE_LINE,flArrayObj.getString("val")), flArrayObj.getString("content"));
							}
						}
					}
					if(recordValue!=null)
					{
						recordValues.add(recordValue);
					}
				}
			}
			choices.add(new ZCChoice(recordValues,Long.valueOf(invoiceId),invoiceId,subject));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return choices;
	}

	static boolean parseJsonObject(JSONObject jsonObj,ZCForm currentShownForm,List<String> alertMessages, List<String> infoValues,String openUrlString) throws JSONException, ZCException
	{
		int type = -1;
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
				if(field!=null)
				{
					field.setRebuildRequired(true);
					recordValue = field.getRecordValue();
				}
			}
			else
			{
				subFormField = baseForm.getField(subFormName);
				if(subFormField!=null)
				{
					ZCForm subForm = subFormField.getSubForm();
					field = subForm.getField(fieldName);
					if(field!=null)
					{
						field.setRebuildRequired(true);
						recordValue = field.getRecordValue();
					}
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
				recordValue.setChoiceValues(new ArrayList<ZCChoice>());
				recordValue.setChoiceValue(null);
				
				if(recordValue.isAllowotherchoice()){
					recordValue.setAllowotherchoice(false);
					recordValue.setOtherChoiceValue(null);
				}

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
						ZCChoice selectedChoice = choiceValues.get(0);
						boolean isOtherChoice = true;

						List<ZCChoice> choices = recordValue.getChoices();

						if(choices != null && choices.size() > 0){
							for(int i = 0; i < choices.size(); i++){
								if(selectedChoice.getKey().equals(choices.get(i).getKey())){
									recordValue.setChoiceValue(selectedChoice);
									isOtherChoice = false;
									break;
								}
							}

							if(isOtherChoice && recordValue.isAllowotherchoice()){
								recordValue.setChoiceValue(new ZCChoice(ZCRecordValue.allowOtherChoiceKey, "Other"));
								recordValue.setOtherChoiceValue(selectedChoice.getValue());
							}

						}


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
				
				List<ZCChoice> choices = recordValue.getChoices();
				choiceValues = new ArrayList<ZCChoice>();
				for(int k=0; k<choices.size(); k++) {
					choiceValues.add(choices.get(k));
				}

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
			return true;
		} 
		if(subFormName==null)
		{
			//					if(field != null && type==ZCForm.TASK_SETVALUE|| type==ZCForm.TASK_DESELECTALL ||type==ZCForm.TASK_DESELECT || type==ZCForm.TASK_SELECTALL || type==ZCForm.TASK_SELECT) {
			//						if(field.isHasOnUserInputForFormula()) {
			//							field.onUserInputForFormula(currentShownForm);
			//						}
			//						if(field.isHasOnUserInput()) {
			//							field.onUserInput(currentShownForm);
			//						}
			//					}
			if(field != null && type==ZCForm.TASK_SETVALUE|| type==ZCForm.TASK_DESELECTALL ||type==ZCForm.TASK_DESELECT || type==ZCForm.TASK_SELECTALL || type==ZCForm.TASK_SELECT||type==ZCForm.TASK_CLEAR) {
				if(field.isHasOnUserInputForFormula()||(field.isHasOnUserInput()) ) {
					if(field.isHasOnUserInputForFormula()) {
						field.onUserInputForFormula(currentShownForm,true);
					}
					if(field.isHasOnUserInput()) {
						field.onUserInput(currentShownForm,true);
					}
				}
			}
		}
		return false;
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
						if(value!=null && value.contains("<img")){
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

		if(recValue.isAllowotherchoice()){
			ZCChoice choice = new ZCChoice(ZCRecordValue.allowOtherChoiceKey, "Other");
			recValue.setOtherChoiceValue(zcChoice.getValue());
			return choice;
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

	static List<ZCApplication> parseForApplicationList(String response, int type) throws ZCException {


		List<ZCApplication> toReturn = new ArrayList<ZCApplication>();
		try {
			JSONObject resultObj =new JSONObject(response);

			if(type == JSONParser.PERSONAL_APPS){
				if(resultObj.has("result"))
				{	
					JSONObject resultObjChild = new JSONObject(resultObj.getString("result"));
					toReturn =  parseForApplicationList(resultObjChild);
				}
			}
			else{
				toReturn = parseForApplicationList(resultObj);
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toReturn;
	}

	private static List<ZCApplication> parseForApplicationList(JSONObject jsonObject) throws ZCException{

		List<ZCApplication> toReturn = new ArrayList<ZCApplication>();
		String appOwner = "";
		boolean licenceEnabled = true;

		try {

			if(jsonObject.has("application_owner"))
			{
				appOwner = jsonObject.getString("application_owner");
			}
			if(jsonObject.has("application_list"))
			{
				JSONObject applicationListChildObj = new JSONObject(jsonObject.getString("application_list"));
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
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
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

			if(jsonObject.has("license_enabled"))
			{
				licenceEnabled = jsonObject.getBoolean("license_enabled");			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!licenceEnabled) {
			throw new ZCException(resourceString.getString("please_subscribe_to_professional_edition_and_get_access"), ZCException.LICENCE_ERROR); //No I18N
		}
		return toReturn;
	}

	static ZCForm parseForForm(String response, String appLinkName, String appOwner,String queryString,boolean isEditForm) throws ZCException{
		boolean hasAddOnLoad = false;
		boolean hasEditOnLoad = false;
		String successMessage = "";
		String dateFormat = "";
		boolean isStateLess = false;
		String componentName = "";
		String componentLinkName = null; 
		String openurlType = "";
		String openurlValue = "";
		List<ZCButton> buttons = new ArrayList<ZCButton>();
		List<ZCField> fields = new ArrayList<ZCField>();
		List<ZCRule> rules = new ArrayList<ZCRule>();
		int code = 0;

		ZCForm toReturn = null;
		try {
			JSONObject resultObject = new JSONObject(response);
			if(resultObject.has("response")){
				JSONObject responseObject = new JSONObject(resultObject.getString("response"));
				int sequenceNumber = -1;
				if(responseObject.has("hasaddonload")){
					hasAddOnLoad = responseObject.getBoolean("hasaddonload");
				}
				if(responseObject.has("haseditonload")){
					hasEditOnLoad = responseObject.getBoolean("haseditonload");
				}
				if(responseObject.has("captcha")) { 
					throw new ZCException(resourceString.getString("captcha_enabled_forms_are_currently_not_supported"), ZCException.ERROR_OCCURED, "");
				}
				if(responseObject.has("dateformat")){
					dateFormat = responseObject.getString("dateformat");
				}
				if(responseObject.has("successmessage")){
					successMessage = responseObject.getString("successmessage");
				}
				if(responseObject.has("type")){
					int type = responseObject.getInt("type");
					if(type == 2){
						isStateLess = true;
					}
				}
				if(responseObject.has("displayname")){
					componentName = responseObject.getString("displayname");
				}
				if(responseObject.has("labelname")){
					componentLinkName = responseObject.getString("labelname");
				}

				if(responseObject.has("nexturl")){
					JSONObject nextUrlObject = new JSONObject(responseObject.getString("nexturl"));
					if(nextUrlObject.has("value")){
						openurlValue = nextUrlObject.getString("value");
					}
					if(nextUrlObject.has("type")){
						openurlType = nextUrlObject.getString("type");
					}
				}

				//				if(responseObject.has("buttons")){
				//					JSONArray buttonsArray = new JSONArray(responseObject.getString("buttons"));
				//					for(int i =0; i< buttonsArray.length(); i++){
				//						String buttonDisplayName = null;
				//						String buttonLinkName = null;
				//						ZCButtonType buttonType = null;
				//						int buttonSequenceNumber = -1;
				//						int actiontype = 1;
				//						boolean isOnClickExists = false;
				//						
				//						JSONObject buttonObject = buttonsArray.getJSONObject(i);
				//						
				//						if(buttonObject.has("sequencenumber")){
				//							buttonSequenceNumber = buttonObject.getInt("sequencenumber");
				//						}
				//						if(buttonObject.has("labelname")){
				//							buttonLinkName = buttonObject.getString("labelname");
				//						}
				//						if(buttonObject.has("actiontype")){
				//							actiontype = buttonObject.getInt("actiontype");
				//						}
				//						if(buttonObject.has("type")){
				//							buttonType = ZCButtonType.getButtonType(buttonObject.getInt("type"));
				//						}
				//						if(buttonObject.has("displayname")){
				//							buttonDisplayName = buttonObject.getString("displayname");
				//						}
				//						if(buttonObject.has("onclickexists")){
				//							isOnClickExists = buttonObject.getBoolean("onclickexists");
				//						}
				//						
				//						ZCButton button = new ZCButton(buttonDisplayName, buttonLinkName, buttonType);
				//						if(isStateLess)
				//						{
				//							button.setOnClickExists(isOnClickExists);
				//						}
				//						buttons.add(button);
				//					}
				//				}
				//				
				if(responseObject.has("fields")){
					JSONArray fieldsArray = new JSONArray(responseObject.getString("fields"));
					for(int i = 0; i < fieldsArray.length(); i++){
						JSONObject fieldObject = fieldsArray.getJSONObject(i);
						ZCField field = parseField(fieldObject,appLinkName,componentLinkName, appOwner, false);
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

				if(responseObject.has("rules"))
				{

					JSONArray rulesArray = new JSONArray(responseObject.getString("rules"));
					rules = parseForRules(rulesArray,fields);
				}

				if(responseObject.has("buttons")){
					JSONArray buttonsArray = new JSONArray(responseObject.getString("buttons"));
					for(int i =0; i< buttonsArray.length(); i++){
						JSONObject buttonObject = buttonsArray.getJSONObject(i);
						String buttonDisplayName = null;
						String buttonLinkName = null;
						ZCButtonType buttonType = null;
						int buttonSequenceNumber = -1;
						int actiontype = 1;
						boolean isOnClickExists = false;
						if(buttonObject.has("labelname")){
							buttonLinkName = buttonObject.getString("labelname");
						}
						if(buttonObject.has("sequencenumber")){
							buttonSequenceNumber = buttonObject.getInt("sequencenumber");
						}
						if(buttonObject.has("actiontype")){
							actiontype = buttonObject.getInt("actiontype");
						}
						if(buttonObject.has("type")){
							buttonType = ZCButtonType.getButtonType(buttonObject.getInt("type"));
						}
						if(buttonObject.has("displayname")){
							buttonDisplayName = buttonObject.getString("displayname");
						}
						if(buttonObject.has("onclickexists")){
							isOnClickExists = buttonObject.getBoolean("onclickexists");
						}

						ZCButton button = new ZCButton(buttonDisplayName, buttonLinkName, buttonType);
						if(isStateLess)
						{
							button.setOnClickExists(isOnClickExists);
						}
						buttons.add(button);
					}
				}

				Collections.sort(buttons);

				// String appOwner, String appLinkName, String type, String componentName, String componentLinkName, int sequenceNumber, Long componentID, boolean hasAddOnLoad, boolean hasEditOnLoad, String successMessage, String tableName, int formLinkId
				toReturn = new ZCForm(appOwner, appLinkName, componentName, componentLinkName, sequenceNumber,  hasAddOnLoad, hasEditOnLoad, successMessage, dateFormat, isStateLess,openurlType,openurlValue);
				toReturn.addFields(fields);
				toReturn.setRules(rules);
				for(int i=0;i<fields.size();i++)
				{
					ZCField field = fields.get(i);
					FieldType fieldType = field.getType();
					if((fieldType.equals(FieldType.EXTERNAL_FIELD)&&!(field.getModuleType().equalsIgnoreCase("users")))) {
						ZCField idField = toReturn.getField(field.getFieldName()+"_ID");
						String crmIdValue = idField.getRecordValue().getValue();
						if(crmIdValue!=null&&crmIdValue.length()>0)
						{
							ZCChoice crmChoice = field.getRecordValue().getChoiceValue();
							if(crmChoice!=null)
							{

								String crmValue = crmChoice.getValue();

								if(crmValue!=null&&crmValue.length()>0)
								{

									field.setRecordValue(new ZCRecordValue(field,new ZCChoice(crmIdValue, crmValue)));
								}
							}
						}
					}
				}
				if(buttons.size()>0) {
					toReturn.addButtons(buttons);	
				}
			}

			else if(resultObject.has("message")){
				ZCException exception = null;
				if(resultObject.has("code"))
				{
					code = resultObject.getInt("code");
				}
				if(code == ZCException.LINK_NAME_CODE)
				{
					exception = new ZCException(resultObject.getString("message"), ZCException.LINK_NAME_ERROR,"" );
				}
				else if(code == ZCException.APP_LINK_NAME_CODE)
				{

					exception = new ZCException(resultObject.getString("message"), ZCException.APP_LINK_NAME_ERROR,"" );
				}
				else
				{
					exception = new ZCException(resultObject.getString("message"), ZCException.ERROR_OCCURED,"" );
				}
				throw exception;
			}



		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toReturn;
	}

	private static ZCField parseField(JSONObject fieldObject,String applinkName,String formlinkName, String appOwner,boolean isParentSubForm) throws ZCException{

		String fieldName = null;
		String displayName = "";
		String refFormLinkName = null;
		String refAppLinkName = null;
		String currencyType = "USD";//No I18N
		String initialValue = "";
		String text = "";

		String urlTitleValue = "";
		String urlLinkNameValue = "";
		String urlValue = "";

		String appLinkName = applinkName;
		String formLinkName = formlinkName;

		int maxChar = 255;
		int decimalLength = 0;
		int imageType = 3;
		int defaultRows = 0;
		int maximumRows = 0;

		boolean isUnique = false;
		boolean isRequired = false;
		boolean allowOtherChoice = false;
		boolean isNewEntriesAllowed = false;
		boolean isLookup = false;
		boolean isHidden = false;
		boolean urlLinkNameReq = false;
		boolean hasOnUserInput = false;
		boolean hasOnUserInputForFormula = false;
		boolean hasSubForm = false;
		boolean onAddRowExists = false;
		boolean onDeleteRowExists = false;
		boolean isFilterApplied = false;
		boolean urlTitleReq = false;
		String moduleType = "";
		ExternalField externalFieldType = ExternalField.UNKNOWN;

		FieldType fieldType = FieldType.SINGLE_LINE;
		ZCRecord defaultSubFormEntry = null;
		List<ZCField> subFormFields = new ArrayList<ZCField>();
		List<ZCRecord> subFormEntries = new ArrayList<ZCRecord>();

		List<ZCChoice> choices  = new ArrayList<ZCChoice>(); 

		List<String> initialChoiceValues = new ArrayList<String>(); 		
		List<String> keys = new ArrayList<String>();	

		ZCField zcField = null;


		try {
			if(fieldObject.has("ishidden")){
				isHidden = fieldObject.getBoolean("ishidden");
			}
			if(fieldObject.has("fieldname")){
				fieldName = fieldObject.getString("fieldname");
			}
			if(fieldObject.has("displayname")){
				displayName = fieldObject.getString("displayname");
			}
			if(fieldObject.has("moduletype")){
				moduleType = fieldObject.getString("moduletype");
			}
			if(fieldObject.has("servicetype")){
				String serviceType = fieldObject.getString("servicetype");
				externalFieldType = ExternalField.getExternalFieldType(serviceType);


			}
			if(fieldObject.has("maxchar")){
				maxChar = fieldObject.getInt("maxchar");
			}
			if(fieldObject.has("text")){
				text = fieldObject.getString("text");
			}
			if(fieldObject.has("decimallength")){
				decimalLength = fieldObject.getInt("decimallength");
			}
			if(fieldObject.has("refform")){
				refFormLinkName = fieldObject.getString("refform");
			}
			if(fieldObject.has("refapplication")){
				refAppLinkName = fieldObject.getString("refapplication");
			}
			if(fieldObject.has("required")){
				isRequired = fieldObject.getBoolean("required");
			}
			if(fieldObject.has("allowotherchoice")){
				allowOtherChoice = fieldObject.getBoolean("allowotherchoice");
			}
			if(fieldObject.has("inputtype")){
				imageType = fieldObject.getInt("inputtype");
			}
			if(fieldObject.has("allownewentries")){
				isNewEntriesAllowed = fieldObject.getBoolean("allownewentries");
			}
			if(fieldObject.has("type")){
				int type = fieldObject.getInt("type");
				fieldType = FieldType.getFieldType(type);
			}
			if(fieldObject.has("islookupfield")){
				isLookup = fieldObject.getBoolean("islookupfield");
			}
			if(fieldObject.has("filter")){
				isFilterApplied = fieldObject.getBoolean("filter");
			}
			if(fieldObject.has("unique")){
				isUnique = fieldObject.getBoolean("unique");
			}
			if(fieldObject.has("titlereq")){
				urlTitleReq = fieldObject.getBoolean("titlereq");
			}
			if(fieldObject.has("currencydisp")){
				currencyType = fieldObject.getString("currencydisp");
			}
			if(fieldObject.has("linknamereq")){
				urlLinkNameReq = fieldObject.getBoolean("linknamereq");
			}
			if(fieldObject.has("onchangeexists")){
				hasOnUserInput = fieldObject.getBoolean("onchangeexists");
			}
			if(fieldObject.has("formulaexists")){
				hasOnUserInputForFormula = fieldObject.getBoolean("formulaexists");
			}
			if(fieldObject.has("dynamicpicklistexists")){
				hasOnUserInputForFormula = fieldObject.getBoolean("dynamicpicklistexists");
			}
			if(fieldObject.has("defaultrows")){
				defaultRows = fieldObject.getInt("defaultrows");
			}
			if(fieldObject.has("maximumrows")){
				maximumRows = fieldObject.getInt("maximumrows");
				if(maximumRows < 0){
					maximumRows = 1000;
				}
			}
			if(fieldObject.has("onaddrowexists")){
				onAddRowExists = fieldObject.getBoolean("onaddrowexists");
			}
			if(fieldObject.has("ondeleterowexists")){
				onDeleteRowExists = fieldObject.getBoolean("ondeleterowexists");
			}
			if(fieldObject.has("initial")){
				initialValue = fieldObject.getString("initial");
				initialChoiceValues.add(initialValue);
			}
			if(fieldObject.has("value")){
				boolean isImage = false;
				if(fieldObject.get("value") instanceof JSONArray){
					JSONArray fieldValueArray = new JSONArray(fieldObject.getString("value"));
					for(int i = 0; i< fieldValueArray.length(); i++){
						JSONObject fieldValueObject = fieldValueArray.getJSONObject(i);
						if(fieldValueObject.has("key")){
							keys.add(fieldValueObject.getString("key"));
						}
						if(fieldValueObject.has("value")){
							initialChoiceValues.add(fieldValueObject.getString("value"));
						}
					}

				}
				else if(fieldObject.get("value") instanceof JSONObject){
					JSONObject fieldValueObject = new JSONObject(fieldObject.getString("value"));
					if(fieldValueObject.has("key")){
						keys.add(fieldValueObject.getString("key"));
					}
					if(fieldValueObject.has("value")){
						initialChoiceValues.add(fieldValueObject.getString("value"));
					}
					if(fieldValueObject.has("title")){
						urlTitleValue = fieldValueObject.getString("title");
					}
					if(fieldValueObject.has("linkname")){
						urlLinkNameValue = fieldValueObject.getString("linkname");
					}
					if(fieldValueObject.has("url")){
						urlValue = fieldValueObject.getString("url");
					}
					if(fieldValueObject.has("src")){
						isImage = true;
						initialValue = fieldValueObject.getString("src");
					}
				}
				else if(!isImage){
					initialValue = fieldObject.getString("value");
					initialChoiceValues.add(initialValue);
				}
			}
			if(fieldObject.has("choices")){
				JSONArray choicesArray = new JSONArray(fieldObject.getString("choices"));
				choices = parseLookUpChoices(choicesArray);
			}
			if(fieldObject.has("subformfields")){
				JSONArray subFormFieldsArray = new JSONArray(fieldObject.getString("subformfields"));
				hasSubForm = true;
				List<ZCRecordValue> defaultSubFormEntryValues = new ArrayList<ZCRecordValue>();
				defaultSubFormEntry = new ZCRecord(defaultSubFormEntryValues);
				for(int i = 0; i < subFormFieldsArray.length(); i++){
					JSONObject subFormFieldObject = subFormFieldsArray.getJSONObject(i);
					ZCField subFormField = parseField(subFormFieldObject,appLinkName,formLinkName, appOwner, true);
					if(subFormField != null)
					{
						subFormFields.add(subFormField);
						defaultSubFormEntryValues.add(subFormField.getRecordValue().getNewRecordValue());
					}
				}
			}
			if(fieldObject.has("subformrecords")){

				JSONArray subFormRecordsArray = new JSONArray(fieldObject.getString("subformrecords"));

				for(int i =0; i < subFormRecordsArray.length(); i++){
					JSONObject subFormRecordObject = subFormRecordsArray.getJSONObject(i);

					ZCRecord record = parseAndSetRecord(null, subFormRecordObject, subFormFields);
					subFormEntries.add(record);
				}
			}
			if(isParentSubForm) {
				if(fieldType == FieldType.SUB_FORM || fieldType == FieldType.UNKNOWN  || fieldType ==FieldType.NOTES ) {
					return null;
				}
			}

			//			if(isParentSubForm && (FieldType.isPhotoField(fieldType)||FieldType.SIGNATURE==fieldType))
			//			{
			//				if(fieldType.equals(FieldType.IMAGE))
			//				{
			//					throw new ZCException(resourceString.getString("subform_field_with_image_field_iscurrently_not_supported"), ZCException.ERROR_OCCURED,"" );
			//				}else if(fieldType.equals(FieldType.FILE_UPLOAD))
			//				{
			//					throw new ZCException(resourceString.getString("subform_field_with_fileupload_field_iscurrently_not_supported"), ZCException.ERROR_OCCURED,"" );
			//				}else if(fieldType.equals(FieldType.SIGNATURE))
			//				{
			//					throw new ZCException(resourceString.getString("subform_field_with_signature_field_iscurrently_not_supported"), ZCException.ERROR_OCCURED,"" );
			//				}	
			//			}

			if(externalFieldType==ExternalField.ZOHO_CRM)
			{
				fieldType = FieldType.EXTERNAL_FIELD;
			}

			zcField = new ZCField(fieldName, fieldType, displayName);

			if(FieldType.isMultiChoiceField(fieldType)){
				List<ZCChoice> selectedChoices = new ArrayList<ZCChoice>();
				if(!isLookup){
					if(initialChoiceValues.size()==1) 
					{ 
						String keyValue = initialChoiceValues.get(0); 
						if(keyValue.contains(",")) 
						{ 
							initialChoiceValues = new ArrayList<String>();
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
				else{
					if(initialValue!=null&&initialValue.length()>0)
					{ 
						String keyValue = initialValue;
						if(keyValue.contains(",")) 
						{ 
							String[] choiceValues =keyValue.split(","); 
							for(int i=0;i<choiceValues.length;i++) 
							{ 
								selectedChoices.add(new ZCChoice(choiceValues[i], choiceValues[i])) ;
							}
						} 
						else
						{
							selectedChoices.add(new ZCChoice(initialValue,initialValue));
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

				}
			}else if(FieldType.isSingleChoiceField(fieldType))
			{
				ZCChoice toAdd = null;
				String initValue = null;
				if(!isLookup && (!(fieldType.equals(FieldType.EXTERNAL_FIELD)))){
					if(initialChoiceValues.size() > 0){
						initValue = initialChoiceValues.get(0);
					}
					for(int i=0; i<choices.size(); i++) {
						ZCChoice choice = choices.get(i);
						if(choice.getKey().equals(initValue)) {
							toAdd = choice;
							break;
						}
					}

					zcField.setRecordValue(new ZCRecordValue(zcField, toAdd));

					if(allowOtherChoice && toAdd == null){
						if(initValue!=null && initValue.length() > 0){
							zcField.getRecordValue().setOtherChoiceValue(initValue);
							zcField.getRecordValue().setChoiceValue(new ZCChoice(ZCRecordValue.allowOtherChoiceKey, "Other")); 
						}
					}

				}
				else
				{

					if(keys.size() > 0 && initialChoiceValues.size() > 0){
						toAdd = new ZCChoice(keys.get(0), initialChoiceValues.get(0));
					}
					else if(initialValue!=null&&initialValue.length()>0)
					{
						toAdd = new ZCChoice(initialValue, initialValue);
					}
					zcField.setRecordValue(new ZCRecordValue(zcField,toAdd));
				}
				
			}

			else
			{
				ZCRecordValue recordValue = null;
				if(zcField.getType()==FieldType.URL)
				{
					recordValue = new ZCRecordValue(zcField, urlValue,urlTitleValue,urlLinkNameValue);
				}
				else if(zcField.getType()==FieldType.NOTES)
				{
					recordValue = new ZCRecordValue(zcField, text);
				}
				else{
					recordValue = new ZCRecordValue(zcField, initialValue);
				}

				zcField.setRecordValue(recordValue);
			}

			if(allowOtherChoice){
				ZCChoice zcOtherChoice = new ZCChoice(ZCRecordValue.allowOtherChoiceKey, "Other"); 
				choices.add(zcOtherChoice);
			}

			//			zcField.setExternalFieldType(externalFieldType);
			if(fieldType.equals(FieldType.EXTERNAL_LINK))
			{
				isHidden = true;
			}
			zcField.setHidden(isHidden);
			zcField.setDefaultRows(defaultRows);
			zcField.setMaximumRows(maximumRows);
			zcField.setFilterApplied(isFilterApplied);
			zcField.setDecimalLength(decimalLength);
			if((isFilterApplied || (!isLookup)) &&(externalFieldType==ExternalField.UNKNOWN))
			{
				zcField.getRecordValue().addChoices(choices);
				zcField.getRecordValue().setLastReachedForChoices(true);
			}
			zcField.setModuleType(moduleType);
			zcField.setExternalFieldType(externalFieldType);
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
			zcField.getRecordValue().setAllowotherchoice(allowOtherChoice);

			if(refFormLinkName != null && refAppLinkName != null ) {
				zcField.setRefFormComponent(new ZCComponent(appOwner, refAppLinkName, ZCComponent.FORM, "", refFormLinkName, -1));
				//				zcField.setRefFieldLinkName(refFieldLinkName);
				if(fieldType != FieldType.SUB_FORM) {
					zcField.setNewEntriesAllowed(true);
				}
			}

			if(hasSubForm) { 
				zcField.setDefaultSubFormEntry(defaultSubFormEntry);
				for(int i=0; i<subFormEntries.size(); i++) {

					zcField.addSubFormEntry(subFormEntries.get(i));
				}
				ZCForm subForm = new ZCForm(appOwner, refAppLinkName, displayName, refFormLinkName, -1, false, false, "", "", false,"","");
				subForm.addFields(subFormFields);
				zcField.setSubForm(subForm);
			}



		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return zcField;
	}

	private static List<ZCChoice> parseLookUpChoices(JSONArray choicesArray){
		List<ZCChoice> toReturn = new ArrayList<ZCChoice>();
		try {
			for(int i =0; i< choicesArray.length(); i++){
				JSONObject choiceObject = choicesArray.getJSONObject(i);
				String value = null;
				String key = null;
				if(choiceObject.has("value")){
					value = choiceObject.getString("value");
				}
				if(choiceObject.has("key")){
					key = choiceObject.getString("key");
				}
				toReturn.add(new ZCChoice(key, value));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}

	private static ZCRecord parseAndSetRecord(ZCView zcView, JSONObject recordObject, List<ZCField> subFormFields){
		long recordId = 0L;
		List<ZCRecordValue> valueList  = new ArrayList<ZCRecordValue>();
		int p=1;
		try {
			//			if(recordObject.has("ID")){
			//				recordId = recordObject.getLong("ID");
			//			}
			Iterator<?> recordObjectkeys = recordObject.keys();

			while(recordObjectkeys.hasNext()){

				String recordObjectKey = (String) recordObjectkeys.next();
				String fieldName = "";
				if(recordObjectKey.equals("ID")){
					recordId = recordObject.getLong("ID");
				}
				else{
					fieldName = recordObjectKey;
					String urlLinkNameValue = "";
					String urlTitleValue = "";
					String value = "";
					ZCField zcField = null;
					if(zcView != null) {
						zcField = zcView.getColumn(fieldName);
					} else {
						for(int i=0; i<subFormFields.size(); i++) {
							ZCField fieldToCheck = subFormFields.get(i);

							if(fieldToCheck.getFieldName().equals(fieldName)) {
								zcField = fieldToCheck;
								break;
							}
						}
					}

					if(zcField == null) {
						break;
					}
					List<ZCChoice> choices = null;
					List<ZCChoice> selectedChoices = new ArrayList<ZCChoice>();
					ZCChoice selectedChoice = null;
					ZCRecordValue zcValue = null;

					if(zcView == null){
						choices = zcField.getRecordValue().getChoices();

						if(recordObject.get(fieldName) instanceof JSONArray){
							JSONArray recordValuesArray = new JSONArray(recordObject.getString(fieldName));

							if(FieldType.isChoiceField(zcField.getType())) {
								for(int i = 0; i < recordValuesArray.length(); i++){
									JSONObject recordValueObject = recordValuesArray.getJSONObject(i);
									String key = null;
									String recordValue  = null;
									//boolean isOtherChocie = true;
									boolean isOtherChocie = false;
									if(recordValueObject.has("key")){
										key = recordValueObject.getString("key");
									}
									if(recordValueObject.has("value")){
										recordValue = recordValueObject.getString("value");
									}

									for(int j=0; j<choices.size(); j++) {
										if(key.equals(choices.get(j).getKey())) {
											selectedChoices.add(new ZCChoice(key,recordValue));
											isOtherChocie = false;
											break;
										}
									}
//									if(isOtherChocie || zcField.getRecordValue().isAllowotherchoice()){
									if(isOtherChocie){
//										zcField.getRecordValue().setAllowotherchoice(true);
										ZCChoice otherChoice = new ZCChoice(ZCRecordValue.allowOtherChoiceKey,"Other");
										if(!zcField.getRecordValue().isAllowotherchoice()){
											choices.add(otherChoice);
											zcField.getRecordValue().addChoices(choices);
										}
										selectedChoices.add(otherChoice);
										zcField.getRecordValue().setOtherChoiceValue(recordValue);
									}

									if(zcField.isLookup()){
										selectedChoices.add(new ZCChoice(key,recordValue));
									}
								}
							}
						}else if(recordObject.get(fieldName) instanceof JSONObject){
							JSONObject recordValueObject = new JSONObject(recordObject.getString(fieldName));
							if(zcField.getType() == FieldType.URL){
								if(recordValueObject.has("title")){
									urlTitleValue = recordValueObject.getString("title");
								}
								if(recordValueObject.has("linkname")){
									urlLinkNameValue = recordValueObject.getString("linkname");
								}
								if(recordValueObject.has("url")){
									value = recordValueObject.getString("url");
								}
							}

							if(FieldType.isChoiceField(zcField.getType())) {
								String key = null;

								String choiceValue  = null;
								boolean isOtherChoice = true;


								if(recordValueObject.has("key")){
									key = recordValueObject.getString("key");
								}
								if(recordValueObject.has("value")){
									choiceValue = recordValueObject.getString("value");
								}

								for(int j=0; j<choices.size(); j++) {
									if(key.equals(choices.get(j).getKey())) {
										selectedChoices.add(new ZCChoice(key,choiceValue));
										isOtherChoice = false;
										break;
									}
								}
//								if(isOtherChoice || zcField.getRecordValue().isAllowotherchoice()){
								if(isOtherChoice){
//									zcField.getRecordValue().setAllowotherchoice(true);
									
									ZCChoice otherChoice = new ZCChoice(ZCRecordValue.allowOtherChoiceKey,"Other");
									
									if(! zcField.getRecordValue().isAllowotherchoice()){
										choices.add(otherChoice);
										zcField.getRecordValue().addChoices(choices);
									}
									selectedChoices.add(otherChoice);
									zcField.getRecordValue().setOtherChoiceValue(choiceValue);
								}

								if(zcField.isLookup()){
									selectedChoices.add(new ZCChoice(key,choiceValue));
								}

							}
						}
						else{
							value = recordObject.getString(fieldName);

						}

						if(selectedChoices.size() >0) {
							selectedChoice = selectedChoices.get(0);
						}
					}


					if(FieldType.isMultiChoiceField(zcField.getType())) {
						zcValue = new ZCRecordValue(zcField, selectedChoices);
						zcValue.addChoices(choices);
					} else if(FieldType.isSingleChoiceField(zcField.getType())) {
						zcValue = new ZCRecordValue(zcField, selectedChoice);
						zcValue.addChoices(choices);

						if(selectedChoice != null && selectedChoice.getKey().equals(ZCRecordValue.allowOtherChoiceKey)){
							zcValue.setOtherChoiceValue(zcField.getRecordValue().getOtherChoiceValue());
						}
					}else {
						if(zcField.getType() == FieldType.URL){
							zcValue = new ZCRecordValue(zcField, value,urlTitleValue,urlLinkNameValue);
						}else{

							zcValue = new ZCRecordValue(zcField, value);
						}
					}
					if(zcView!=null)
					{
						zcField.setRecordValue(zcValue);
					}
					valueList.add(zcValue);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ZCRecord record = null;
		if(zcView==null)
		{
			List<ZCRecordValue> recValues = new ArrayList<ZCRecordValue>();
			for(int j=0;j<subFormFields.size();j++)
			{
				for(int i=0;i<valueList.size();i++)
				{
					ZCRecordValue recValue = valueList.get(i);
					String fieldName = recValue.getField().getFieldName();
					if(fieldName.equals(subFormFields.get(j).getFieldName()))
					{
						recValues.add(recValue);
						break;
					}
				}
			}
			record = new ZCRecord(recordId, recValues);
		}else
		{
			record = new ZCRecord(recordId, valueList);
		}
		return record;
	}
}