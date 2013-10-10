// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;




public class ZCView extends ZCComponent {

	private String dateFormat = null;
	private String baseFormLinkName = null;
	private boolean isAddAllowed = false;
	private boolean isEditAllowed = false;
	private boolean isDeleteAllowed = false;
	private boolean isDuplicateAllowed = false;
	private boolean isBulkEditAllowed = false;
	private boolean isAllDay = false;
	

	private List<ZCColumn> columns = new ArrayList<ZCColumn>();
	private boolean columnsAdded = false;

	private List<ZCFilter> filters = new ArrayList<ZCFilter>();
	private boolean filtersAdded = false;

	List<ZCCustomFilter> customFilters = new ArrayList<ZCCustomFilter>();
	private boolean customFiltersAdded = false;
	private ZCCustomFilter selectedCustomFilter = null;
	
	private List<ZCGroup> groups = new ArrayList<ZCGroup>();

	private List<ZCColumn> groupByColumns = new ArrayList<ZCColumn>();
	private List<ZCColumn> sortByColumns = new ArrayList<ZCColumn>();
	
	private List<ZCCustomAction> headerCustomActions = new ArrayList<ZCCustomAction>();
	private boolean headerCustomActionAdded = false;
	
	private List<ZCCustomAction> recordCustomActions = new ArrayList<ZCCustomAction>();
	private boolean recordCustomActionAdded = false;
	
	private List<ZCRecord> records = new ArrayList<ZCRecord>();
	private HashMap<Date, List<ZCRecord>> eventsMap = new HashMap<Date, List<ZCRecord>>();

	private boolean isGrouped = false;
	private boolean lastReached = false;
	public static final int PAGE_SIZE = 50;
	
	
	private ZCField titleField = null;
	private ZCField startField = null;
	private ZCField endField = null;
	

	private ZCPair<Integer, Integer> recordsMonthYear = null;
	private int recordsCount = -1;


	
	ZCView(String appOwner, String appLinkName, String componentType, String componentName, String componentLinkName) {
		super(appOwner, appLinkName, componentType, componentName, componentLinkName, -1);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return super.toString() + " - dateFormat: " + dateFormat + " - baseFormLinkName: " + baseFormLinkName +   //No I18N
				" - isAddAllowed: " + isAddAllowed + " - isEditAllowed: " + isEditAllowed + " - isDeleteAllowed: " + isDeleteAllowed +    //No I18N
				" - isDuplicateAllowed: " + isDuplicateAllowed + " - isBulkEditAllowed: " + isBulkEditAllowed + " - filters: " + filters +   //No I18N
				" - headerCustomActions : " + headerCustomActions + " - recordCustomActions: " + recordCustomActions + " - columns: " + columns +  //No I18N
				" \n\n- records: " + records;   //No I18N
	}

	public String getDateFormat() {
		return dateFormat;
	}

	void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getBaseFormLinkName() {
		return baseFormLinkName;
	}

	void setBaseFormLinkName(String baseFormLinkName) {
		this.baseFormLinkName = baseFormLinkName;
	}

	public boolean isAddAllowed() {
		return isAddAllowed;
	}

	void setAddAllowed(boolean isAddAllowed) {
		this.isAddAllowed = isAddAllowed;
	}

	public boolean isDeleteAllowed() {
		return isDeleteAllowed;
	}

	void setDeleteAllowed(boolean isDeleteAllowed) {
		this.isDeleteAllowed = isDeleteAllowed;
	}

	public boolean isEditAllowed() {
		return isEditAllowed;
	}

	void setEditAllowed(boolean isEditAllowed) {
		this.isEditAllowed = isEditAllowed;
	}

	public boolean isDuplicateAllowed() {
		return isDuplicateAllowed;
	}

	void setDuplicateAllowed(boolean isDuplicateAllowed) {
		this.isDuplicateAllowed = isDuplicateAllowed;
	}

	public boolean isBulkEditAllowed() {
		return isBulkEditAllowed;
	}

	void setBulkEditAllowed(boolean isBulkEditAllowed) {
		this.isBulkEditAllowed = isBulkEditAllowed;
	}

	ZCColumn getColumn(String fieldName) {
		for(int i=0; i<columns.size(); i++) {
			ZCColumn column = columns.get(i);
			if(column.getFieldName().equals(fieldName)) {
				return column;
			}
		}
		return null;
	}
	
	public List<ZCColumn> getColumns() {
		final List<ZCColumn> toReturn = new ArrayList<ZCColumn>(columns);
		return toReturn ;
	}

	void addColumns(List<ZCColumn> columns) {
		if(!columnsAdded) {
			this.columns.addAll(columns);
			columnsAdded = true;
		} else {
			throw new RuntimeException ("You cannot add more than once"); //No I18N
		}
	}
	
	public void setGroupByColumns(List<ZCColumn> groupByColumns) {
		if(groupByColumns == null || groupByColumns.size() == 0) {
			this.groupByColumns = new ArrayList<ZCColumn>();
			return;
		}
		if(!this.columns.containsAll(groupByColumns)) {
			throw new RuntimeException("Should be part of columns");//No I18N
		}
		this.groupByColumns = groupByColumns;
	}
	
	public List<ZCColumn> getGroupByColumns() {
		return groupByColumns;
	}

	public void setSortByColumns(List<ZCColumn> sortByColumns) {
		if(sortByColumns == null || sortByColumns.size() == 0) {
			this.sortByColumns = new ArrayList<ZCColumn>();
			return;
		}
		if(!this.columns.containsAll(sortByColumns)) {
			throw new RuntimeException("Should be part of columns");//No I18N
		}
		this.sortByColumns = sortByColumns;
	}
	
	List<ZCColumn> getSortByColumns() {
		return sortByColumns;
	}


	public List<ZCCustomFilter> getCustomFilters() {
		final List<ZCCustomFilter> toReturn = new ArrayList<ZCCustomFilter>(customFilters);
		return toReturn ;
	}

	
	void addCustomFilters(List<ZCCustomFilter> customFilters) {
		if(!customFiltersAdded) {
			this.customFilters.addAll(customFilters);
			customFiltersAdded = true;
		} else {
			throw new RuntimeException ("You cannot add more than once"); //No I18N
		}
		
	}

	
	public List<ZCFilter> getFilters() {
		final List<ZCFilter> toReturn = new ArrayList<ZCFilter>(filters);
		return toReturn ;
	}


	void addFilters(List<ZCFilter> filters) {
		if(!filtersAdded) {
			this.filters.addAll(filters);
			filtersAdded = true;
		} else {
			throw new RuntimeException ("You cannot add more than once"); //No I18N
		}
	}

	public List<ZCCustomAction> getHeaderCustomActions() {
		final List<ZCCustomAction> toReturn = new ArrayList<ZCCustomAction>(headerCustomActions);
		return toReturn;
	}

	void addHeaderCustomActions(List<ZCCustomAction> headerCustomActions) {
		if(!headerCustomActionAdded) {
			this.headerCustomActions.addAll(headerCustomActions);
			headerCustomActionAdded = true;
		} else {
			throw new RuntimeException ("You cannot add more than once"); //No I18N
		}
	}

	public List<ZCCustomAction> getRecordCustomActions() {
		final List<ZCCustomAction> toReturn = new ArrayList<ZCCustomAction>(recordCustomActions);
		return toReturn;
	}

	void addRecordCustomActions(List<ZCCustomAction> recordCustomActions) {
		if(!recordCustomActionAdded) {
			this.recordCustomActions.addAll(recordCustomActions);	
			recordCustomActionAdded = true;
		} else {
			throw new RuntimeException ("You cannot add more than once"); //No I18N
		}
	}

	ZCRecord getRecord(long recordid) {
		for(int i=0; i<records.size(); i++) {
			ZCRecord record = records.get(i);
			if(record.getRecordId() == recordid) {
				return record;
			}
		}
		return null;
	}
	public List<ZCRecord> getRecords() {
		return records;
	}
	
	public void reloadRecords()throws ZCException {
		records.clear();
		groups.clear();
		eventsMap.clear();
		lastReached = false;
		recordsCount = -1;
		ZOHOCreator.loadRecords(this);
	}
	

	public HashMap<Date, List<ZCRecord>> loadCalendarRecords(int month, int year) throws ZCException{
		this.recordsMonthYear = new ZCPair<Integer, Integer>(month, year);
		reloadRecords();
		//recordsCount = this.records.size();
		return getEventRecordsMap();
	}

	
    
	void addRecords(List<ZCRecord> records) {
		if(records.size() < ZCView.PAGE_SIZE) {
			//System.out.println("records.size(): " + records.size());
			lastReached = true;
		}
		this.records.addAll(records);
		if(lastReached || getType().equals(ZCComponent.CALENDAR)) {
			recordsCount = this.records.size();
		}
	}
	
	void sortRecordsForCalendar() {
		if(getType().equals(ZCComponent.CALENDAR)) {
			
			
			Collections.sort(this.records);
			

			Iterator<Date> iterator = eventsMap.keySet().iterator();
			while(iterator.hasNext()) {
				Date date = iterator.next();
				List<ZCRecord> eventRecords = eventsMap.get(date);
				Collections.sort(eventRecords);
			}
			
			
		}
	}

	public List<ZCGroup> getGroups() {
		return groups;
	}
	

	public int getRecordPositionForGroup(int overallPosition) {
		if(isGrouped) {
			for(int i=0; i<groups.size(); i++) {
				overallPosition --;
				ZCGroup zcGroup = groups.get(i);
				List<ZCRecord> groupRecords = zcGroup.getGroupRecords();
				int groupRecordsSize = groupRecords.size();
				if((groupRecordsSize) > overallPosition) {
					ZCRecord record = groupRecords.get(overallPosition);
					return records.indexOf(record);
				} else {
					overallPosition = overallPosition - groupRecordsSize;
				}
			}
		}
		return -1;
	}
	

	public boolean isLastReached() {
		return lastReached;
	}
	

	public boolean isGrouped() {
		return isGrouped;
	}
	
	

	void setGrouped(boolean isGrouped) {
		this.isGrouped = isGrouped;
	}
	
	public List<ZCRecord> loadMore() throws ZCException{
		if(!lastReached) {
			int lastIndex = records.size();
			ZOHOCreator.loadRecords(this);			
			List<ZCRecord> toReturn = records.subList(lastIndex, records.size());			
			return toReturn;
		}
		return new ArrayList<ZCRecord>();
	}
	
	public ZCResponse  deleteRecords(List<Long> recordIDs) throws ZCException{
		if(!isDeleteAllowed) {
			throw new RuntimeException("Delete not allowed");//No I18N
		}

		return ZOHOCreator.postXMLString(getAppOwner(), getRecordIDXMLString(recordIDs, "delete"), "delete", null, null);//No I18N
	}
	
	public ZCResponse duplicateRecords(List<Long> recordIDs) throws ZCException{
		if(!isDuplicateAllowed) {
			throw new RuntimeException("Duplicate not allowed");//No I18N
		}
		return ZOHOCreator.postXMLString(getAppOwner(), getRecordIDXMLString(recordIDs, "duplicate"), "duplicate", null, null);//No I18N
	}

	private String getRecordIDXMLString(List<Long> recordIDs, String tagName) {
		StringBuffer buff = new StringBuffer();
	    buff.append("<ZohoCreator>");//No I18N
	    	buff.append("<applicationlist>");//No I18N
	    		buff.append("<application name='" + getAppLinkName() + "'>");//No I18N
	    			buff.append("<viewlist>");//No I18N
	    				buff.append("<view name='" + getComponentLinkName() + "'>");//No I18N
	    					buff.append("<" + tagName + ">");//No I18N
	    						buff.append("<criteria>");//No I18N
	    							
	    							// <![CDATA[(Name == "Jean" && DOB == "20-Jul-1981")]]>
							    buff.append("<![CDATA[(");//No I18N
								    for (int i=0; i<recordIDs.size(); i++) {
								    	Long recordID = recordIDs.get(i);
								    	buff.append("ID");//No I18N
								    	buff.append(" == ");//No I18N
								    	buff.append(recordID);
								    	if(i != recordIDs.size()-1) {
								    		buff.append(" || ");//No I18N
								    	}
								    }
								buff.append(")]]>");//No I18N

								buff.append("</criteria>");//No I18N
							buff.append("</" + tagName + ">");//No I18N
						buff.append("</view>");//No I18N
					buff.append("</viewlist>");//No I18N
				buff.append("</application>");//No I18N
			buff.append("</applicationlist>");//No I18N
		buff.append("</ZohoCreator>");	//No I18N
		return buff.toString();
	}
	
	void setEvent(ZCRecord eventRecord, Date date) {
		List<ZCRecord> eventRecords = eventsMap.get(date);
		if(eventRecords == null) {
			eventRecords = new ArrayList<ZCRecord>();
			eventsMap.put(date, eventRecords);
		}
		eventRecords.add(eventRecord);
		//Collections.sort(eventRecords);
	}
	
	public HashMap<Date, List<ZCRecord>> getEventRecordsMap() {
		return eventsMap;
	}
	

	
	public List<ZCRecord> getEventRecords(Date date) {
		return eventsMap.get(date);
	}
	
	
	
	public ZCResponse customAction(long customActionID, List<Long> recordIDs) throws ZCException{
		return ZOHOCreator.postCustomAction(getAppLinkName(), getComponentLinkName(), getAppOwner(), customActionID, recordIDs);
	}

	public ZCField getTitleField() {
		return titleField;
	}

	void setTitleField(ZCField titleField) {
		this.titleField = titleField;
	}

	public ZCField getStartField() {
		return startField;
	}

	void setStartField(ZCField startField) {
		this.startField = startField;
	}

	public ZCField getEndField() {
		return endField;
	}

	void setEndField(ZCField endField) {
		this.endField = endField;
	}

	public ZCPair<Integer, Integer> getRecordsMonthYear() {
		return recordsMonthYear;
	}

	void setRecordsMonthYear(ZCPair<Integer, Integer> recordsMonthYear) {
		this.recordsMonthYear = recordsMonthYear;
	}
	
	void setIsAllDay(boolean isAllDay)
	{
		this.isAllDay = isAllDay;		
	}
	
	public boolean getIsAllDay(){
		return this.isAllDay;
	}

	public ZCCustomFilter getSelectedCustomFilter() {
		return selectedCustomFilter;
	}

	public void setSelectedCustomFilter(ZCCustomFilter selectedCustomFilter) {
		this.selectedCustomFilter = selectedCustomFilter;
	}
	
	public void setRecordsCount(int recordsCount){
		this.recordsCount = recordsCount;
	}
	public int getRecordsCount(){
		return recordsCount;
	}

}
