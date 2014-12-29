// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.text.GetChars;

public class ZCRule{

	private List<ZCTask> zcTasks = new ArrayList<ZCTask>();
	private List<String> fieldNamesWithCriteria = new ArrayList<String>();
	private String condition = "";

	ZCRule(List<ZCTask> zcTasks,List<String> fieldNamesWithCriteria,String condition){

		this.zcTasks = zcTasks;
		this.fieldNamesWithCriteria = fieldNamesWithCriteria;
		this.condition = condition;
	}

	public List<ZCTask> getZCTasks()
	{
		return zcTasks;
	}

	public List<String> getFieldNamesWithCriteria()
	{
		return fieldNamesWithCriteria;
	}

	public String getCondition()
	{
		return condition;
	}

}

