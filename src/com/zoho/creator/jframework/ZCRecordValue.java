// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.List;


public class ZCRecordValue{

	private ZCField field = null;
	private String value = null;
	private ZCChoice choiceValue = null;
	private List<ZCChoice> choiceValues = new ArrayList<ZCChoice>();
	private Object fileValue = null;
	private String url = "";
	private String urlTitleValue = "";
	private String urlLinkNameValue = "";
	private boolean errorOccured = false;
	private String errorMessage = null;

	private boolean isLastReachedForChoices = false;
	private List<ZCChoice> choices  = new ArrayList<ZCChoice>(); 
	private String searchForChoices = null;
	private boolean lookupLoadingStarted = false;
	private boolean isFileReUploaded = false;
	private boolean isWriteToParcel = false;
	private String fileName = null;
	private boolean isAllowotherchoice = false;
	private String otherChoiceValue = null;
	
	public static String allowOtherChoiceKey = "ALLOW_OTHER_CHOICES";



	public ZCRecordValue(ZCField field, String value) {
		this.field = field;		
		setValue(value); 
	}

	public ZCRecordValue(ZCField field, ZCChoice choiceValue) {
		this.field = field;
		setChoiceValue(choiceValue); 
	}

	public ZCRecordValue(ZCField field, List<ZCChoice> choiceValues) {
		this.field = field;
		setChoiceValues(choiceValues);
	}

	public ZCRecordValue(ZCField field, Object bitMapValue) {
		this.field = field;
		setFileValue(bitMapValue);
	}

	void setWriteToParcel(boolean isWriteToParcel)
	{
		this.isWriteToParcel = isWriteToParcel;
	}

	boolean isWriteToParcel()
	{
		return isWriteToParcel;
	}

	public ZCRecordValue(ZCField field,String url,String urlTitleValue,String urlLinkNameValue)
	{
		this.field = field;
		this.url = url;
		this.urlTitleValue = urlTitleValue;
		this.urlLinkNameValue = urlLinkNameValue;
	}

	public String toString() {
		return field.getDisplayName() + " : " + value;  //No I18N
	}

	public String getValue() {
		if(FieldType.isChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		return value;
	}


	boolean isFileReUploaded()
	{
		return isFileReUploaded;
	}
	public void setFileUploaded(boolean isFileReUploaded)
	{
		this.isFileReUploaded = isFileReUploaded;
	}

	public void setUrlLinkNameValue(String urlLinkNameValue)
	{
		this.urlLinkNameValue = urlLinkNameValue;
	}

	public String getUrlLinkNameValue()
	{
		return urlLinkNameValue;
	}

	public String getUrlTitleValue()
	{
		return urlTitleValue;
	}
	public void setUrlTitleValue(String urlTitleValue)
	{
		this.urlTitleValue = urlTitleValue;
	}
	public String getUrlValue()
	{
		return url;
	}

	public void setUrlValue(String url)
	{
		this.url = url;
	}

	void setField(ZCField field)
	{
		this.field = field;
	}

	public String getDisplayValue() {
		if(FieldType.isMultiChoiceField(field.getType())) {
			StringBuffer buff = new StringBuffer();
			for(int i=0; i<choiceValues.size(); i++) {
				ZCChoice choice = choiceValues.get(i);
				buff.append(choice.getValue());
				if(i != choiceValues.size() -1) {
					buff.append(", ");
				}
			}
			return buff.toString();
		} else if(FieldType.isSingleChoiceField(field.getType())) {
			if(choiceValue==null)
			{
				return "";
			}
			else{
				
				if(choiceValue.getKey().equals(ZCRecordValue.allowOtherChoiceKey)){
					return otherChoiceValue;
				}
				
				return choiceValue.getValue();
			}
		}else if(FieldType.URL==field.getType())
		{
			return url;
		}
		return value;
	}
	
	

	public ZCChoice getChoiceValue() {
		if(!FieldType.isSingleChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		return choiceValue;
	}

	public List<ZCChoice> getChoiceValues() {
		final List<ZCChoice> toReturn = new ArrayList<ZCChoice>(choiceValues);
		return toReturn;
	}

	public ZCRecordValue getNewRecordValue() {
		ZCRecordValue toReturn = null;
		if(FieldType.isMultiChoiceField(field.getType()) || FieldType.isSingleChoiceField(field.getType())) {
			if(FieldType.isMultiChoiceField(field.getType())) {
				toReturn = new ZCRecordValue(field, getChoiceValues());
			} else if(FieldType.isSingleChoiceField(field.getType())) {
				toReturn = new ZCRecordValue(field, getChoiceValue());
			}
			toReturn.setLastReachedForChoices(isLastReachedForChoices());
			toReturn.addChoices(getChoices());
		} else if(FieldType.isPhotoField(field.getType())) {
			if(field.getImageType()!=ZCField.IMAGE_LINK)
			{
				toReturn = new ZCRecordValue(field, getFileValue());
			}else
			{
				toReturn = new ZCRecordValue(field,getValue());
			}
		} else {
			toReturn = new ZCRecordValue(field, getValue());
		}
		return toReturn;
	}


	void addToValues(List<ZCChoice> valuesToAdd) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		for(int i=0;i<valuesToAdd.size();i++) // 3,2
		{
			ZCChoice valueToAdd = valuesToAdd.get(i);
			if(!choiceValues.contains(valueToAdd))
			{
				choiceValues.add(valueToAdd);
			}
		}
	}

	void removeFromValues(List<ZCChoice> valuesToRemove) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Cannot use this one for this field type");//No I18N
		}
		for(int i=0;i<choiceValues.size();i++)
		{
			for(int j=0;j<valuesToRemove.size();j++)
			{
				if(choiceValues.get(i).equals(valuesToRemove.get(j)))
				{
					choiceValues.remove(i);
					i--;
					break;
				}
			}
		}
	}

	public Object getFileValue() {
		return fileValue;
	}
	
	

	public void setValue(String value) {
		if(FieldType.isChoiceField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.value = value;
	}

	public void setChoiceValue(ZCChoice choiceValue) {
		if(!FieldType.isSingleChoiceField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.choiceValue = choiceValue;
	}

	public void setChoiceValues(List<ZCChoice> zcChoices) {
		if(!FieldType.isMultiChoiceField(field.getType())) {
			throw new RuntimeException("Use the other one");//No I18N
		}
		List<ZCChoice> choiceValues = new ArrayList<ZCChoice>();
		for(int i=0;i<zcChoices.size();i++) 
		{
			ZCChoice valueToAdd = zcChoices.get(i);
			if(!choiceValues.contains(valueToAdd))
			{
				choiceValues.add(valueToAdd);
			}
		}
		this.choiceValues = new ArrayList<ZCChoice>(choiceValues);
	}

	public void setFileValue(Object fileValue) {
		if((!FieldType.isPhotoField(field.getType()))&&(!(field.getType()==FieldType.SIGNATURE)) ){
			throw new RuntimeException("Use the other one");//No I18N
		}
		this.fileValue = fileValue;
	}

	public ZCField getField() {
		return field;
	}

	public boolean isErrorOccured()
	{
		return errorOccured;
	}

	public void setErrorOccured(boolean errorOccured)
	{
		this.errorOccured = errorOccured;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage()
	{

		return errorMessage;
	}

	public List<ZCChoice> getChoices() {
		final List<ZCChoice> toReturn = new ArrayList<ZCChoice>(choices);
		return toReturn;
	}

	void addChoices(List<ZCChoice> choices) {
		this.choices = choices;		
	}

	void clearChoices() {
		choices.clear();
	}

	public void appendChoices(List<ZCChoice> moreChoices) {
		choices.addAll(moreChoices);
	}

	public void addToLookupChoice(ZCChoice choice) {
		choices.add(choice);
		if(FieldType.isMultiChoiceField(field.getType())) {
			ArrayList<ZCChoice> values = new ArrayList<ZCChoice>();
			values.add(choice);
			addToValues(values);
		} else {
			setChoiceValue(choice);
		}
	}

	void setLastReachedForChoices(boolean isLastReachedForChoices) {
		this.isLastReachedForChoices = isLastReachedForChoices;
	}

	public boolean isLastReachedForChoices() {
		return isLastReachedForChoices;
	}

	public List<ZCChoice> loadMoreChoices() throws ZCException {
		if(!isLastReachedForChoices) {
			List<ZCChoice> moreChoices = ZOHOCreator.loadMoreChoices(this);
			choices.addAll(moreChoices);
			if(moreChoices.size()<50 && searchForChoices == null) {
				isLastReachedForChoices = true;
			}
			return moreChoices;
		}
		return new ArrayList<ZCChoice>();
	}


	public void reloadChoices() throws ZCException {
		choices.clear();
		isLastReachedForChoices = false;
		loadMoreChoices();
	}

	public void setSearchForChoices(String searchForChoices) {
		this.searchForChoices  = searchForChoices;
	}

	public String getSearchForChoices() {
		return searchForChoices;
	}

	public boolean isLookupLoadingStarted() {
		return lookupLoadingStarted;
	}

	public void setLookupLoadingStarted(boolean lookupLoadingStarted) {
		this.lookupLoadingStarted = lookupLoadingStarted;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	String getUrlValueForSubmit()
	{
		String urlValueForSubmit = "";
		if (field.isUrlTitleReq() && field.isUrlLinkNameReq()) {
			urlValueForSubmit = "<a href=" + "\"" + url + "\"" + " title="+ "\""+ urlTitleValue+ "\"" + " target=\"_blank\">" + urlLinkNameValue + "</a>";// No I18N
		} else if (field.isUrlTitleReq()) {
			urlValueForSubmit = "<a href=" + "\"" + url + "\"" + " title="+ "\"" + urlTitleValue + "\"" + " target=\"_blank\">"+ url + "</a>";// No I18N
		} else if (field.isUrlLinkNameReq()) {
			urlValueForSubmit = "<a href=" + "\"" + url + "\""+ " target=\"_blank\">" + urlLinkNameValue + "</a>";// No I18N
		} else {
			urlValueForSubmit = "<a href=" + "\"" + url + "\""+ " target=\"_blank\"></a>";// No I18N
		}
		return urlValueForSubmit;
	}
	
	public void setAllowotherchoice(boolean allowotherchoice){
		this.isAllowotherchoice = allowotherchoice;
	}
	
	public boolean isAllowotherchoice(){
		return isAllowotherchoice;
	}
	
	public void setOtherChoiceValue(String otherChoiceValue){
		this.otherChoiceValue = otherChoiceValue;
	}
	
	public String getOtherChoiceValue() {
		return otherChoiceValue;
	}
	
}
