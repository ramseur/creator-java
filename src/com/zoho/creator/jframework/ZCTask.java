// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ZCTask{

	private int taskType = 0;
	private List<String> fieldNames = new ArrayList<String>();
	private HashMap<String, String> setValuesHashMap = new HashMap<String, String>();

	
	public static final int HIDE_FIELDS = 1;
	public static final int DISABLE_FIELDS = 2;
	public static final int ENABLE_FIELDS = 3;
	public static final int SHOW_FIELDS = 4;
	public static final int HIDE_SUBFROM_ADD_ENTRY = 9;
	public static final int SHOW_SUBFORM_ADD_ENTRY = 10;
	public static final int HIDE_SUBFORM_DELETE_ENTRY = 11;
	public static final int SHOW_SUBFORM_DELETE_ENTRY = 12;
	public static final int SET_FIELD_VALUE = 14;
	


	ZCTask(int taskType, List<String> fieldNames)
	{
		this.taskType = taskType;
		this.fieldNames = fieldNames;	
	}

	public ZCTask(int taskType, HashMap<String,String> setValuesHashMap,List<String> fieldNames) {
		this.taskType = taskType;
		this.setValuesHashMap = setValuesHashMap;
		this.fieldNames = fieldNames;
	}
	
	public int getZCTaskType()
	{
		return taskType;
	}

	public List<String> getFieldNames()
	{
		return fieldNames;
	}

	public HashMap<String,String> getSetValuesHashMap()
	{
		return setValuesHashMap;
	}
}

