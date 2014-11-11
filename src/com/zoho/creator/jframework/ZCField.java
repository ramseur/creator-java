// $Id$
package com.zoho.creator.jframework;

import java.util.ArrayList;
import java.util.List;

public class ZCField implements Comparable<ZCField>{
	static final long serialVersionUID = 2l;
	private String fieldName = null;
	private FieldType type;
	private String displayName = null;
	private int sequenceNumber = -1;

	//private String initialValue = "";
	private boolean isUnique = false;
	private boolean isRequired = false;

	private int maxChar = 255;
	private int decimalLength = 0;
	private int defaultRows = 0;
	private int maximumRows = 0;
	private boolean urlTitleReq = false;
	private boolean urlLinkNameReq = true;

	private boolean fromZohoDoc = false;
	private boolean fromGoogleDoc = false;
	private boolean fromLocalComputer = true;

	private boolean imgLinkReq = false;
	private boolean imgTitleReq = false;
	private boolean altTxtReq = false;

	private boolean hasOnUserInput = false;
	private boolean hasOnUserInputForFormula = false;

	private ZCForm baseForm = null;
	private ZCForm subForm = null;
	private ZCForm lookupForm = null;

	private ZCComponent refFormComponent = null;
	private String refFieldLinkName = null;
	private String currencyType = null;
	private List<Long> subFormRecordIds = new ArrayList<Long>();

	private ZCRecordValue recordValue = null;
	private boolean isLookup = false; // This is purely for display checks....
	private boolean newEntriesAllowed = false; // This is purely for display checks....
	private boolean isOnAddRowExists = false;
	private boolean isOnDeleteRowExists = false;

	private List<ZCRecord> subFormEntries = new ArrayList<ZCRecord>();
	private List<ZCRecord> removedSubFormEntries = new ArrayList<ZCRecord>();


	private boolean isHidden = false;
	private boolean isDisabled = false;
	private boolean isRebuildRequired = false;

	private boolean hasFilterApplied = false;

	private int subFormEntryPosition = -1;
	private int imageType = IMAGE_BOTH;
	private ExternalField externalFieldType = ExternalField.UNKNOWN;

	public static int IMAGE_LINK = 1;
	public static int IMAGE_LOCAL = 2;
	public static int IMAGE_BOTH = 3;

	int baseFormFieldSize = 0;



	ZCField(String fieldName, FieldType type, String displayName) {
		this.fieldName = fieldName;
		this.type = type;
		this.displayName = displayName;
	}

	public String toString() {

		String toReturn  = "fieldName:" + fieldName + " - type:" + type + " - displayName:" + displayName +  //No I18N
				" - isUnique:" + isUnique + " - isRequired:" + isRequired + " - maxChar:" + maxChar +  //No I18N
				" - urlTitleReq:" + urlTitleReq + " - urlLinkNameReq:" + urlLinkNameReq + " - fromZohoDoc:" + fromZohoDoc + " - fromGoogleDoc:" + fromGoogleDoc +  //No I18N
				" - fromLocalComputer:" + fromLocalComputer + " - imgLinkReq:" + imgLinkReq + " - altTxtReq:" + altTxtReq +   //No I18N
				" - refFieldLinkName: " + refFieldLinkName ; //No I18N
		if(refFormComponent != null) {
			toReturn = toReturn + " - refFormLinkName:" + refFormComponent.getComponentLinkName() + " - refFormDisplayName:" +  refFormComponent.getComponentName() + " - refAppLinkName:" + refFormComponent.getAppLinkName(); //No I18N
		}
		return toReturn;
	}


	void setExternalFieldType(ExternalField externalFieldType)
	{
		this.externalFieldType = externalFieldType;
	}

	public ExternalField getExternalFieldType()
	{
		return externalFieldType;
	}

	public void setSubFormEntryPosition(int subFormEntryPosition)
	{
		this.subFormEntryPosition = subFormEntryPosition;
	}
	public int getSubFormEntryPosition()
	{
		return subFormEntryPosition;
	}
	public void setFilterApplied(boolean hasFilterApplied)
	{
		this.hasFilterApplied = hasFilterApplied;
	}
	public boolean hasFilterApplied()
	{
		return hasFilterApplied;
	}

	public int getMaxChar() {
		return maxChar;
	}
	void setMaxChar(int maxChar) {
		this.maxChar = maxChar;
	}
	public boolean isUnique() {
		return isUnique;
	}
	void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;	
	}

	public String getFieldName() {
		return fieldName;
	}

	public FieldType getType() {
		return type;
	}


	public String getDisplayName() {
		return displayName;
	}

	public boolean isRequired() {
		return isRequired;
	}
	void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean isUrlLinkNameReq() {
		return urlLinkNameReq;
	}

	void setUrlLinkNameReq(boolean urlLinkNameReq) {
		this.urlLinkNameReq = urlLinkNameReq;
	}

	public boolean isUrlTitleReq() {
		return urlTitleReq;
	}

	void setUrlTitleReq(boolean urlTitleReq) {
		this.urlTitleReq = urlTitleReq;
	}

	public boolean isFromZohoDoc() {
		return fromZohoDoc;
	}

	void setFromZohoDoc(boolean fromZohoDoc) {
		this.fromZohoDoc = fromZohoDoc;
	}

	public boolean isFromGoogleDoc() {
		return fromGoogleDoc;
	}

	void setFromGoogleDoc(boolean fromGoogleDoc) {
		this.fromGoogleDoc = fromGoogleDoc;
	}

	public boolean isFromLocalComputer() {
		return fromLocalComputer;
	}

	void setFromLocalComputer(boolean fromLocalComputer) {
		this.fromLocalComputer = fromLocalComputer;
	}

	public boolean isImgLinkReq() {
		return imgLinkReq;
	}

	void setImgLinkReq(boolean imgLinkReq) {
		this.imgLinkReq = imgLinkReq;
	}

	public boolean isImgTitleReq() {
		return imgTitleReq;
	}

	void setImgTitleReq(boolean imgTitleReq) {
		this.imgTitleReq = imgTitleReq;
	}

	public boolean isAltTxtReq() {
		return altTxtReq;
	}

	void setAltTxtReq(boolean altTxtReq) {
		this.altTxtReq = altTxtReq;
	}



	public int getSequenceNumber() {
		return sequenceNumber;
	}

	void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public int compareTo(ZCField arg0) {
		if(arg0 instanceof ZCField) {
			ZCField toCompare = (ZCField)arg0;
			return getSequenceNumber()>toCompare.getSequenceNumber() ? 1 
					: toCompare.getSequenceNumber() > getSequenceNumber() ? -1 
							: 0;
		}
		return -1;
	}

	public ZCRecordValue getRecordValue() {
		return recordValue;
	}

	public void setRecordValue(ZCRecordValue recordValue) {
		this.recordValue = recordValue;
	}

	public boolean isLookup() {
		return isLookup;
	}

	void setLookup(boolean isLookup) {
		this.isLookup = isLookup;
	}

	public boolean isHasOnUserInput() {
		return hasOnUserInput;
	}

	void setHasOnUserInput(boolean hasOnUserInput) {
		this.hasOnUserInput = hasOnUserInput;
	}


	public String getRefFieldLinkName() {
		return refFieldLinkName;
	}

	void setRefFieldLinkName(String refFieldLinkName) {
		this.refFieldLinkName = refFieldLinkName;
	}


	public ZCForm getSubForm(ZCRecord record) {
		List<ZCField> subFormFields = subForm.getFields();
		List<ZCRecordValue> fromRecordValues = record.getValues();
		List<ZCRecordValue> toRecordValues = new ArrayList<ZCRecordValue>();
		for(int i=0; i<subFormFields.size(); i++) {
			ZCField subFormField = subFormFields.get(i);
			toRecordValues.add(subFormField.getRecordValue());
		}
		copyRecordValues(fromRecordValues, toRecordValues);
		return subForm;
	}

	public void updateSubFormEntry() {
		ZCRecord record = getSubFormEntry(getSubFormEntryPosition());
		List<ZCField> subFormFields = subForm.getFields();
		List<ZCRecordValue> fromRecordValues = new ArrayList<ZCRecordValue>();
		List<ZCRecordValue> toRecordValues = record.getValues();
		for(int i=0; i<subFormFields.size(); i++) {
			ZCField subFormField = subFormFields.get(i);
			fromRecordValues.add(subFormField.getRecordValue());
		}
		copyRecordValues(fromRecordValues, toRecordValues);
	}

	public ZCRecord addAndGetNewSubFormEntry() {
		List<ZCRecordValue> toRecordValues = new ArrayList<ZCRecordValue>();
		List<ZCRecordValue> fromRecordValues = defaultSubFormEntry.getValues();
		for(int i=0; i<fromRecordValues.size(); i++) {
			toRecordValues.add(fromRecordValues.get(i).getNewRecordValue());
		}
		ZCRecord toReturn = new ZCRecord(toRecordValues);
		subFormEntries.add(toReturn);
		return toReturn;
	}

	public static void copyRecordValues(List<ZCRecordValue> fromRecordValues, List<ZCRecordValue> toRecordValues) {
		for(int i=0; i<toRecordValues.size(); i++) {
			ZCRecordValue toRecordValue = toRecordValues.get(i);
			for(int j=0; j<fromRecordValues.size(); j++) {
				ZCRecordValue fromRecordValue = fromRecordValues.get(j);
				ZCField fromRecordValueField = fromRecordValue.getField();
				if(fromRecordValueField.getFieldName().equals(toRecordValue.getField().getFieldName())) {
					toRecordValue.setErrorOccured(fromRecordValue.isErrorOccured());
					toRecordValue.setErrorMessage(fromRecordValue.getErrorMessage());
					if(FieldType.isMultiChoiceField(fromRecordValueField.getType()) || FieldType.isSingleChoiceField(fromRecordValueField.getType())) {
						toRecordValue.setLastReachedForChoices(fromRecordValue.isLastReachedForChoices());
						toRecordValue.setLookupLoadingStarted(fromRecordValue.isLookupLoadingStarted());
						toRecordValue.addChoices(fromRecordValue.getChoices());
						if(FieldType.isMultiChoiceField(fromRecordValueField.getType())) {
							toRecordValue.setChoiceValues(fromRecordValue.getChoiceValues());
						} else if(FieldType.isSingleChoiceField(fromRecordValueField.getType())) {
							toRecordValue.setChoiceValue(fromRecordValue.getChoiceValue());
						}
					} 
					else if(fromRecordValueField.getType()==FieldType.URL)
					{
						toRecordValue.setUrlValue(fromRecordValue.getUrlValue());
						toRecordValue.setUrlTitleValue(fromRecordValue.getUrlTitleValue());
						toRecordValue.setUrlLinkNameValue(fromRecordValue.getUrlLinkNameValue());
					}else if(FieldType.isPhotoField(fromRecordValueField.getType()))
					{
						if(fromRecordValueField.getImageType()!=ZCField.IMAGE_LINK)
						{
							toRecordValue.setFileValue(fromRecordValue.getFileValue());
						}
						else
						{
							toRecordValue.setValue(fromRecordValue.getValue());
						}

					}
					else {
						toRecordValue.setValue(fromRecordValue.getValue());
					}
				}
			}
		}
	}



	void setSubForm(ZCForm subForm) {
		this.subForm = subForm;
		subForm.setBaseSubFormField(this);
	}

	private ZCRecord defaultSubFormEntry = null;

	void setDefaultSubFormEntry(ZCRecord defaultSubFormEntry) {
		this.defaultSubFormEntry = defaultSubFormEntry;
	}

	public ZCRecord getDefaultSubFormEntry() {
		return defaultSubFormEntry;
	}

	ZCForm getSubForm() {
		return subForm;
	}



	void addSubFormEntry(ZCRecord record) {
		subFormEntries.add(record);
	}

	public ZCRecord getSubFormEntry(int position) {
		return subFormEntries.get(position);
	}

	public int getSubFormEntriesSize() {
		return subFormEntries.size();
	}

	public void removeSubFormEntry(int position) {
		removedSubFormEntries.add(subFormEntries.remove(position));
	}

	List<ZCRecord> getUpdatedSubFormEntries() {
		List<ZCRecord> updatedEntries = new ArrayList<ZCRecord>();
		for(int i=0; i<subFormEntries.size(); i++) {
			ZCRecord rec = subFormEntries.get(i);
			if(rec.getRecordId() != -1L) {
				updatedEntries.add(rec);
			}
		}
		final List<ZCRecord> toReturn = new ArrayList<ZCRecord>(updatedEntries);
		return toReturn;
	}

	List<ZCRecord> getRemovedSubFormEntries() {
		final List<ZCRecord> toReturn = new ArrayList<ZCRecord>(removedSubFormEntries);
		return toReturn;
	}

	List<ZCRecord> getAddedSubFormEntries() {
		List<ZCRecord> addedEntries = new ArrayList<ZCRecord>();
		for(int i=0; i<subFormEntries.size(); i++) {
			ZCRecord rec = subFormEntries.get(i);
			if(rec.getRecordId() == -1L) {
				addedEntries.add(rec);
			}
		}
		final List<ZCRecord> toReturn = new ArrayList<ZCRecord>(addedEntries);
		return toReturn;
	}

	public ZCComponent getRefFormComponent() {
		return refFormComponent;
	}

	void setRefFormComponent(ZCComponent refFormComponent) {
		this.refFormComponent = refFormComponent;
	}

	public boolean isNewEntriesAllowed() {
		return newEntriesAllowed;
	}

	void setNewEntriesAllowed(boolean newEntriesAllowed) {
		this.newEntriesAllowed = newEntriesAllowed;
	}

	public ZCForm getLookupForm() {
		return lookupForm;
	}

	void setLookupForm(ZCForm lookupForm) {
		this.lookupForm = lookupForm;
		lookupForm.setBaseLookupField(this);
	}

	public ZCForm getBaseForm() {
		return baseForm;
	}

	void setBaseForm(ZCForm baseForm) {
		this.baseForm = baseForm;
	}

	public boolean isHasOnUserInputForFormula() {
		return hasOnUserInputForFormula;
	}

	void setHasOnUserInputForFormula(boolean hasOnUserInputForFormula) {
		this.hasOnUserInputForFormula = hasOnUserInputForFormula;

	}

	public boolean isRebuildRequired() {
		return isRebuildRequired;
	}

	public void setRebuildRequired(boolean isRebuildRequired) {
		this.isRebuildRequired = isRebuildRequired;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public void onUserInput(ZCForm currentShownForm,boolean doesOnUserInputRetriggered) throws ZCException{
		ZOHOCreator.callFieldOnUser(baseForm, fieldName, false, currentShownForm,doesOnUserInputRetriggered);
	}

	public void onUserInputForFormula(ZCForm currentShownForm,boolean doesOnUserInputRetriggered) throws ZCException{
		ZOHOCreator.callFieldOnUser(baseForm, fieldName, true, currentShownForm,doesOnUserInputRetriggered);
	}

	public boolean isOnAddRowExists()
	{
		return isOnAddRowExists;	
	}

	public void setOnAddRowExists(boolean isOnAddRowExists) {
		this.isOnAddRowExists = isOnAddRowExists;
	}

	public boolean isOnDeleteRowExists() {
		return isOnDeleteRowExists;
	}

	public void setOnDeleteRowExists(boolean isOnDeleteRowExists) {
		this.isOnDeleteRowExists = isOnDeleteRowExists;
	}


	public int getMaximumRows() {
		return maximumRows;
	}

	void setMaximumRows(int maximumRows) {
		this.maximumRows = maximumRows;
	}
	public int getDefaultRows()
	{
		return defaultRows;
	}

	void setDefaultRows(int defaultRows){
		this.defaultRows = defaultRows;
	}

	public int getDecimalLength() {
		return decimalLength;
	}
	void setDecimalLength(int decimalLength) {
		this.decimalLength = decimalLength;
	}

	//		public void setExternalFieldType(ExternalField externalFieldType) {
	//			// TODO Auto-generated method stub
	//			this.externalFieldType  = externalFieldType;
	//		}
	//		
	//		public ExternalField getExternalFieldType(){
	//			return externalFieldType;
	//		}


	public int getImageType() {
		return imageType;
	}

	void setImageType(int imageType) {
		this.imageType = imageType;
	}

	void setSubFormRecordIds(List<Long> subFormRecordIds)
	{
		this.subFormRecordIds = subFormRecordIds;
	}
	List<Long> getSubFormRecordIds()
	{
		return subFormRecordIds;
	}
}
