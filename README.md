Zoho Creator Java API 
======

**[Javadoc Index] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/index.html)**

**Javadoc of objects used below**

* [ZCApplication] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCApplication.html) 
* [ZCSection] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCSection.html) 
* [ZCComponent] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCComponent.html) 
* [ZCForm] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCForm.html) 
* [ZCField] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCField.html) 
* [FieldType] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/FieldType.html) 
* [ZCButton] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCButton.html) 
* [ZCView] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCView.html) 
* [ZCFilter] (http://zohocreator.github.io/java/docs/index.html?com/zoho/creator/jframework/metadata/ZCFilter.html) 


**Login**

	ZOHOUser user = ZOHOCreator.getZohoUser();
	if(user == null) {
		String username = "Get Username Here";
		String password = "Get Password Here";
		user = ZOHOCreator.login(username, password);
	}
		

**Personal Applications List**

	List<ZCApplication> zcapps = ZOHOCreator.getPersonalApplicationList()); 


**List of Sections, Forms, Reports and Pages in an Application**

	List<ZCSection> zcSections = ZOHOCreator.getSectionList(ZCApplication zcApp);
	// Fetches the list of Sections in an application
	
	ZCSection firstSection = zcSections.get(0);
	List<ZCComponent> comps = firstSection.getComponents();
	// lists the Forms, Reports and Pages in the first Section
	
	ZCComponent firstComponent = comps.get(0);
	String type = firstComponent.getType();	
	// One of ZCComponent.FORM , ZCComponent.REPORT , ZCComponent.CALENDAR or ZCComponent.PAGE



**Form**

	ZCForm form = ZOHOCreator.getForm(comps.get(0));
	List<ZCField> fields = form.getFields();

	FieldType ftype = zcField.getType();
	// Check field type

	ZCField firstField = fields.get(0);
	firstField.getRecordValue().setValue("Input Value");
	// Setting Input value
	
	List<ZCButton> buttons = zcForm.getButtons();
	// Fetches all the buttons of form
	for(int i=0; i<buttons.size(); i++) {
		ZCButton button = buttons.get(i);
		if(button.getButtonType().equals(ZCButtonType.SUBMIT)) {
			ZCResponse response = button.click();
			// Submits the input value...
			Hashtable<ZCField, String> errorTable = response.getErrorMessagesTable();
			// Error, if any will be set in this Hashtable
		}
	}


**Report or Calendar**

	ZCView zcView = ZOHOCreator.getView(comps.get(0));
	List<ZCFilter> filters = zcView.getFilters();
	// Gets all the filters 
	List<ZCGroup> groups = zcView.getGroups();
	// Gets all the groups
	
	List<ZCRecord> records = zcView.getRecords();
	// Gets first 50 records in Report. If it's a calendar, it will fetch all the records of current month
	

	ZCGroup firstGroup = groups.get(0);
	List<ZCRecord> records = firstGroup.getGroupRecords();
	// Gets first 50 records in the first group
	
	List<ZCRecord> nextSetOfRecords = zcView.loadMore();				
	// Gets the next 50 records

	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MONTH, 1);
	zcView.loadCalendarRecords(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));				
	// Fetches the next month records

	zcView.deleteRecords(List<Long> recordIDs);
	zcView.duplicateRecords(List<Long> recordIDs);
	List<ZCCustomAction> customActions = zcView.getHeaderCustomActions();
	zcView.customAction(customActions.get(0).getId(), getRecordIdsInput());




**Page**

	ZCHtmlView htmlPage = ZOHOCreator.getHtmlView(comps.get(0));
	String htmlContent = htmlPage.getHtmlContent();

**Shared Applications List**

	List<ZCApplication> sharedAppsWithMe = ZOHOCreator.getSharedApplicationList(); 



**Shared Applications with a Group**

	ZCNavList navList = ZOHOCreator.getNavigationListForApps(); 
	// Fetches the Navigation for Shared Apps with Groups and Workspace Apps.
	
	List<ZCSharedGroup> sharedWithGroupList = navList.getSharedWithGroupList(); 
	// Fetches the List of Groups in which user is present
	
	List<ZCApplication> sharedAppsWithGroup = ZOHOCreator.getSharedApplicationList(ZCSharedGroup sharedGroup); 
	// Fetches the list of Applications in that particular Group

**Workspace Applications**

	List<String> sharedWithWorkSpaceList = navList.getSharedWithWorkSpaceList(); 
	// Fetches the List of Workspaces in which user is a developer

		
