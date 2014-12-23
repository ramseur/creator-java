// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZCRule{

	private List<ZCTask> zcTasks = new ArrayList<ZCTask>();
	private List<String> fieldNamesWithCriteria = new ArrayList<String>();

	ZCRule(List<ZCTask> zcTasks,List<String> fieldNamesWithCriteria){

		this.zcTasks = zcTasks;
		this.fieldNamesWithCriteria = fieldNamesWithCriteria;
	}

	public List<ZCTask> getZCTasks()
	{
		return zcTasks;
	}

}

