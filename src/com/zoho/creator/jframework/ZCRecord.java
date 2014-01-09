// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ZCRecord implements Comparable<ZCRecord> {
	private List<ZCRecordValue> values = null;	
	private long recordId = -1l;
	private Date eventDate = null;
	private Date startTime = null;
	private Date endTime = null;
	private String eventTitle = null;
	private boolean isRecordError = false;

	public ZCRecord(List<ZCRecordValue> values) {
		this.values = values;
	}
	
	ZCRecord(long recordId, List<ZCRecordValue> values) {
		this.recordId = recordId;
		this.values = values;
	}
	
	public String toString() {
		return "recordID: " + recordId +  " - values: " + values;  //No I18N
	}
	
	public List<ZCRecordValue> getValues() {
		final List<ZCRecordValue> toReturn = new ArrayList<ZCRecordValue>(values);
		return toReturn ;
	}
	
	private String getDisplay(List<ZCRecordValue> recValues) {
		StringBuffer buff = new StringBuffer();
		for(int i=0; i<recValues.size(); i++) {
			if(i != 0) {
				buff.append(", ");
			}
			ZCRecordValue recValue = recValues.get(i);
			buff.append(recValue.getDisplayValue());
		}
		return buff.toString();
	}
	
	public String getPrimaryDisplay() {
		return getDisplay(getPrimaryValues());
	}
	
	public String getSecondaryDisplay() {
		return getDisplay(getSecondaryValues());
	}

	
	public List<ZCRecordValue> getPrimaryValues() {
		return FieldType.getDisplayValues(true, values);
	}
	
	public List<ZCRecordValue> getSecondaryValues() {
		return FieldType.getDisplayValues(false, values);
	}

	public long getRecordId() {
		return recordId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	@Override
	public int compareTo(ZCRecord arg0) {
		// TODO Auto-generated method stub
		ZCRecord toCompare = (ZCRecord)arg0;
		if(getStartTime() != null && toCompare.getStartTime() != null) {
			return getStartTime().getTime() > toCompare.getStartTime().getTime() ? 1 
					: toCompare.getStartTime().getTime() > getStartTime().getTime() ? -1 
					: 0;
		} else if(getEventDate() != null && toCompare.getEventDate() != null) {
			return getEventDate().getTime() > toCompare.getEventDate().getTime() ? 1 
					: toCompare.getEventDate().getTime() > getEventDate().getTime() ? -1 
					: 0;
		}
 		return 0;
	}
	public boolean isRecordError()
	{
		return isRecordError;
	}
	public void setRecordError(boolean isRecordError)
	{
		this.isRecordError = isRecordError;
	}
}
